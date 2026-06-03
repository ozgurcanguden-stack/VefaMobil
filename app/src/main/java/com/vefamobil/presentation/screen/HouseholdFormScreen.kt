package com.vefamobil.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vefamobil.model.Household
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseholdFormScreen(
    onBackClick: () -> Unit,
    onSaveHousehold: (Household) -> Unit,
) {
    var refCode by rememberSaveable { mutableStateOf("") }
    var neighborhood by rememberSaveable { mutableStateOf("") }
    var fullName by rememberSaveable { mutableStateOf("") }
    var tcNo by rememberSaveable { mutableStateOf("") }
    var phone1 by rememberSaveable { mutableStateOf("") }
    var phone2 by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var isUrgent by rememberSaveable { mutableStateOf(false) }

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
                value = tcNo,
                onValueChange = { tcNo = it },
                label = "TC",
                keyboardType = KeyboardType.Number,
            )
            HouseholdTextField(
                value = phone1,
                onValueChange = { phone1 = it },
                label = "Cep 1",
                keyboardType = KeyboardType.Phone,
            )
            HouseholdTextField(
                value = phone2,
                onValueChange = { phone2 = it },
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
                    checked = isUrgent,
                    onCheckedChange = { isUrgent = it },
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
                    onSaveHousehold(
                        Household(
                            id = UUID.randomUUID().toString(),
                            refCode = refCode.trim(),
                            neighborhood = neighborhood.trim(),
                            fullName = fullName.trim(),
                            tcNo = tcNo.trim(),
                            phone1 = phone1.trim(),
                            phone2 = phone2.trim().takeIf { it.isNotEmpty() },
                            address = address.trim(),
                            isActive = true,
                            isNewHousehold = true,
                            isUrgent = isUrgent,
                            firstVisitCompleted = false,
                        ),
                    )
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
