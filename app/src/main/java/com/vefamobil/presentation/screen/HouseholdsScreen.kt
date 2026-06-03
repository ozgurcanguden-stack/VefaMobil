package com.vefamobil.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vefamobil.model.Household
import com.vefamobil.presentation.HouseholdUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseholdsScreen(
    state: HouseholdUiState,
    onBackClick: () -> Unit,
    onNewHouseholdClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onToggleActiveClick: (String) -> Unit,
) {
    val context = LocalContext.current
    val actionMessage = "Bu ekran sonraki aşamada eklenecek."

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Haneler") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Geri",
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = onNewHouseholdClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                        )
                        Text(text = "Yeni Hane")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = state.searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text(text = "Hane ara") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                        )
                    },
                    singleLine = true,
                )

                OutlinedButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.FilterList,
                        contentDescription = "Filtre",
                    )
                }
            }

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            state.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(
                    items = state.households,
                    key = { household -> household.id },
                ) { household ->
                    HouseholdRow(
                        household = household,
                        onEditClick = {
                            Toast
                                .makeText(context, actionMessage, Toast.LENGTH_SHORT)
                                .show()
                        },
                        onToggleActiveClick = { onToggleActiveClick(household.id) },
                        onDeleteClick = { onDeleteClick(household.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HouseholdRow(
    household: Household,
    onEditClick: () -> Unit,
    onToggleActiveClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val textAlpha = if (household.isActive) 1f else 0.48f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (household.isActive) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = "${household.refCode} - ${household.neighborhood}",
                    color = MaterialTheme.colorScheme.primary.copy(alpha = textAlpha),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text = household.fullName,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (household.isActive) 0.72f else 0.48f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Düzenle",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = textAlpha),
                )
            }

            IconButton(onClick = onToggleActiveClick) {
                Icon(
                    imageVector = Icons.Outlined.Block,
                    contentDescription = "Pasife Al",
                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = textAlpha),
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Sil",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = textAlpha),
                )
            }
        }
    }
}
