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

        when {
            primaryPhone.isBlank() && secondaryPhone.isBlank() -> {
                Toast
                    .makeText(context, "Bu hane için kayıtlı telefon numarası yok.", Toast.LENGTH_SHORT)
                    .show()
            }

            secondaryPhone.isNotBlank() -> {
                showPhoneSelectionDialog(
                    context = context,
                    phone1 = primaryPhone,
                    phone2 = secondaryPhone,
                )
            }

            else -> {
                openSelectedPhone(context = context, phoneNumber = primaryPhone)
            }
        }
    }

    private fun showPhoneSelectionDialog(
        context: Context,
        phone1: String,
        phone2: String,
    ) {
        AlertDialog.Builder(context)
            .setTitle("Aranacak numarayı seçin")
            .setItems(arrayOf("Cep 1", "Cep 2")) { dialog, which ->
                val selectedPhone = if (which == 0) phone1 else phone2
                dialog.dismiss()
                openSelectedPhone(context = context, phoneNumber = selectedPhone)
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
