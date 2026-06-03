package com.vefamobil.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vefamobil.model.Personnel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonnelFormScreen(
    onBackClick: () -> Unit,
    initialPersonnel: Personnel? = null,
    onSavePersonnel: (Personnel) -> Unit,
) {
    val personnelId = initialPersonnel?.id
    var fullName by rememberSaveable(personnelId) { mutableStateOf(initialPersonnel?.fullName.orEmpty()) }
    var username by rememberSaveable(personnelId) { mutableStateOf(initialPersonnel?.username.orEmpty()) }
    var temporaryPassword by rememberSaveable(personnelId) {
        mutableStateOf(initialPersonnel?.temporaryPassword.orEmpty())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (initialPersonnel == null) "Personel Ekle" else "Personeli Düzenle") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text(text = "Ad Soyad") },
                singleLine = true,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Kullanıcı Adı") },
                singleLine = true,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = temporaryPassword,
                onValueChange = { temporaryPassword = it },
                label = { Text(text = "Geçici Şifre") },
                singleLine = true,
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onSavePersonnel(
                        Personnel(
                            id = initialPersonnel?.id ?: UUID.randomUUID().toString(),
                            fullName = fullName.trim(),
                            username = username.trim(),
                            temporaryPassword = temporaryPassword.trim(),
                            isActive = initialPersonnel?.isActive ?: true,
                            mustChangePassword = true,
                            createdAt = initialPersonnel?.createdAt ?: System.currentTimeMillis(),
                        ),
                    )
                },
            ) {
                Text(text = "Kaydet")
            }
        }
    }
}
