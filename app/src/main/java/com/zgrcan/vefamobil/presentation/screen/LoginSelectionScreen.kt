package com.zgrcan.vefamobil.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginSelectionScreen(
    onManagerLoginClick: () -> Unit,
    onPersonnelLoginClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onManagerLoginClick,
                ) {
                    Text(text = "Müdür Girişi")
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onPersonnelLoginClick,
                ) {
                    Text(text = "Personel Girişi")
                }
            }
        }
    }
}
