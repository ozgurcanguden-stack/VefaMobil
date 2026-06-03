package com.vefamobil.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseholdFormScreen(
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    var refCode by rememberSaveable { mutableStateOf("") }
    var neighborhood by rememberSaveable { mutableStateOf("") }
    var fullName by rememberSaveable { mutableStateOf("") }
    var identityNumber by rememberSaveable { mutableStateOf("") }
    var phonePrimary by rememberSaveable { mutableStateOf("") }
    var phoneSecondary by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var isEmergencyPriority by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Yeni Hane") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Geri",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HouseholdTextField(
                value = refCode,
                onValueChange = { refCode = it },
                label = "Ref Kodu",
            )
            HouseholdTextField(
                value = neighborhood,
                onValueChange = { neighborhood = it },
                label = "Mahalle",
            )
            HouseholdTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Ad Soyad",
            )
            HouseholdTextField(
                value = identityNumber,
                onValueChange = { identityNumber = it },
                label = "TC",
                keyboardType = KeyboardType.Number,
            )
            HouseholdTextField(
                value = phonePrimary,
                onValueChange = { phonePrimary = it },
                label = "Cep 1",
                keyboardType = KeyboardType.Phone,
            )
            HouseholdTextField(
                value = phoneSecondary,
                onValueChange = { phoneSecondary = it },
                label = "Cep 2",
                keyboardType = KeyboardType.Phone,
            )
            HouseholdTextField(
                value = address,
                onValueChange = { address = it },
                label = "Adres",
                singleLine = false,
                minLines = 3,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = isEmergencyPriority,
                    onCheckedChange = { isEmergencyPriority = it },
                )

                Text(
                    text = "Acil Öncelik",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    Toast
                        .makeText(context, "Kayıt sonraki aşamada eklenecek.", Toast.LENGTH_SHORT)
                        .show()
                },
            ) {
                Text(text = "Kaydet")
            }
        }
    }
}

@Composable
private fun HouseholdTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        minLines = minLines,
    )
}
