package com.example.composebasic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.composebasic.R
import com.example.composebasic.ui.theme.ComposeBasicTheme

@Composable
fun AnimatedStickyHeader(title: String,
                         isHeaderVisible: Boolean) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isHeaderVisible
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(32.dp).wrapContentSize()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, style = MaterialTheme.typography.titleMedium)
    }
}


@Preview(showBackground = true)
@Composable
fun AnimatedStickyHeaderPreview(
    @PreviewParameter(StickyHeaderPreviewProvider::class) isVisible: Boolean
) {
    ComposeBasicTheme(dynamicColor = false) {
        AnimatedStickyHeader(
            title = if (isVisible) "Playing" else "Paused",
            isHeaderVisible = isVisible
        )
    }
}

class StickyHeaderPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}