package com.example.composebasic.data.Respository

import android.content.Context
import com.google.ai.edge.aicore.GenerativeModel
import com.google.ai.edge.aicore.generationConfig

class SummaryRepository(private val context: Context) {

    private val model by lazy {
        GenerativeModel(
            generationConfig = generationConfig {
                temperature = 0.2f
                topK = 16
                maxOutputTokens = 256
            }
        )
    }

    suspend fun summarize(articleText: String): Result<String> {
        return try {
            val response = model.generateContent("Summarize this article:\n\n$articleText")
            val text = response.text
            if (text.isNullOrBlank()) Result.failure(Exception("Empty response from model"))
            else Result.success(text)
        } catch (e: Exception) {
            // Thrown if AICore / Gemini Nano is not available on this device
            Result.failure(e)
        }
    }
}