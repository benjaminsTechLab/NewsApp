package com.example.composebasic.onxx

import ai.onnxruntime.*
import ai.onnxruntime.extensions.OrtxPackage
import android.content.Context
import com.example.composebasic.R
import java.nio.ByteBuffer
import java.nio.LongBuffer
import java.nio.channels.FileChannel

class EmotionClassifier(context: Context) {

    private val env = OrtEnvironment.getEnvironment()
    private val labels = context.resources.getStringArray(R.array.emotions_array)

    private val tokSession: OrtSession
    private val modelSession: OrtSession

    init {
        val opts = OrtSession.SessionOptions()
        opts.registerCustomOpLibrary(OrtxPackage.getLibraryPath())

        tokSession = env.createSession(
            loadModel(context, "tokenizer.onnx"), opts
        )
        modelSession = env.createSession(
            loadModel(context, "emotion_model_base.onnx")
        )
    }

    private fun loadModel(context: Context, assetName: String): ByteBuffer {
        val assetFd = context.assets.openFd(assetName)
        val inputStream = assetFd.createInputStream()
        val fileChannel = inputStream.channel
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            assetFd.startOffset,
            assetFd.declaredLength
        )
    }

    fun classify(text: String): String {
        // Tokenize
        val tokInputs = mapOf("input_text" to OnnxTensor.createTensor(
            env, arrayOf(text)
        ))
        val tokOutput = tokSession.run(tokInputs)
        val inputIds = (tokOutput[0].value as Array<LongArray>)[0]
        val attentionMask = (tokOutput[1].value as Array<LongArray>)[0]

        // Run model
        val shape = longArrayOf(1, inputIds.size.toLong())
        val modelInputs = mapOf(
            "input_ids" to OnnxTensor.createTensor(env, LongBuffer.wrap(inputIds), shape),
            "attention_mask" to OnnxTensor.createTensor(env, LongBuffer.wrap(attentionMask), shape)
        )
        val output = modelSession.run(modelInputs)
        val logits = (output[0].value as Array<FloatArray>)[0]

        return labels[logits.indices.maxByOrNull { logits[it] }!!]
    }
}