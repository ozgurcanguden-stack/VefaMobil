package com.vefamobil.presentation

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object CallHelper {
    private val netsippPackageCandidates = listOf(
        "com.netsipp",
        "com.netsipp.plus",
        "net.netsipp",
        "tr.com.netsipp",
    )

    fun openCallScreen(
        context: Context,
        phone1: String,
        phone2: String?,
    ) {
        val primaryPhone = phone1.trim()
        val secondaryPhone = phone2?.trim().orEmpty()
        val availablePhones = listOf(primaryPhone, secondaryPhone)
            .filter { it.isNotBlank() }

        when {
            availablePhones.isEmpty() -> {
                Toast
                    .makeText(context, "Bu hane için kayıtlı telefon numarası yok.", Toast.LENGTH_SHORT)
                    .show()
            }

            availablePhones.size > 1 -> {
                showPhoneSelectionDialog(
                    context = context,
                    phoneNumbers = availablePhones,
                )
            }

            else -> {
                openSelectedPhone(context = context, phoneNumber = availablePhones.first())
            }
        }
    }

    private fun showPhoneSelectionDialog(
        context: Context,
        phoneNumbers: List<String>,
    ) {
        AlertDialog.Builder(context)
            .setTitle("Aranacak numarayı seçin")
            .setItems(phoneNumbers.toTypedArray()) { dialog, which ->
                dialog.dismiss()
                openSelectedPhone(context = context, phoneNumber = phoneNumbers[which])
            }
            .show()
    }

    private fun openSelectedPhone(
        context: Context,
        phoneNumber: String,
    ) {
        val normalizedPhone = phoneNumber.trim()
        if (normalizedPhone.isBlank()) {
            Toast
                .makeText(context, "Bu hane için kayıtlı telefon numarası yok.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (!openWithNetsipp(context = context, phoneNumber = normalizedPhone)) {
            openWithDialer(context = context, phoneNumber = normalizedPhone)
        }
    }

    private fun openWithNetsipp(
        context: Context,
        phoneNumber: String,
    ): Boolean {
        val encodedPhone = Uri.encode(phoneNumber)
        val dialUri = Uri.parse("tel:$encodedPhone")
        val specialIntents = buildList {
            add(Intent(Intent.ACTION_VIEW, Uri.parse("netsipp://call?number=$encodedPhone")))
            add(Intent(Intent.ACTION_VIEW, Uri.parse("netsippplus://call?number=$encodedPhone")))
            netsippPackageCandidates.forEach { packageName ->
                add(
                    Intent(Intent.ACTION_DIAL, dialUri).apply {
                        setPackage(packageName)
                    },
                )
                add(
                    Intent(Intent.ACTION_VIEW, dialUri).apply {
                        setPackage(packageName)
                    },
                )
            }
        }

        return specialIntents.any { intent ->
            context.tryStartActivity(intent)
        }
    }

    private fun openWithDialer(
        context: Context,
        phoneNumber: String,
    ) {
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.parse("tel:${Uri.encode(phoneNumber)}"),
        )

        if (!context.tryStartActivity(intent)) {
            Toast
                .makeText(context, "Arama ekranı açılamadı.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun Context.tryStartActivity(intent: Intent): Boolean {
        return try {
            if (this !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            true
        } catch (_: ActivityNotFoundException) {
            false
        } catch (_: SecurityException) {
            false
        } catch (_: IllegalArgumentException) {
            false
        }
    }
}
