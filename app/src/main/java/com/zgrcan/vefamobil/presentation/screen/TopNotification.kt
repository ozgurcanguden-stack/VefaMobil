package com.zgrcan.vefamobil.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class TopNotificationType {
    SUCCESS,
    ERROR,
    WARNING,
}

@Composable
fun TopNotification(
    message: String?,
    type: TopNotificationType,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = !message.isNullOrBlank(),
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 260),
            initialOffsetY = { -it },
        ) + fadeIn(animationSpec = tween(durationMillis = 220)),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 220),
            targetOffsetY = { -it },
        ) + fadeOut(animationSpec = tween(durationMillis = 180)),
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            Text(
                modifier = Modifier
                    .widthIn(max = 520.dp)
                    .fillMaxWidth()
                    .background(
                        color = type.containerColor(),
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                text = message.orEmpty(),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun TopNotificationType.containerColor(): Color {
    return when (this) {
        TopNotificationType.SUCCESS -> Color(0xFF15803D)
        TopNotificationType.ERROR -> Color(0xFFB91C1C)
        TopNotificationType.WARNING -> Color(0xFFC2410C)
    }
}
