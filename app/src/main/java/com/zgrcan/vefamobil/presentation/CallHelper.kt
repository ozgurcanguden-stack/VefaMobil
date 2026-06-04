package com.zgrcan.vefamobil.presentation

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast

object CallHelper {
    private const val NETSIPP_PACKAGE_NAME = "com.netsippplus.android"

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

        if (!openWithNetsipp(
            context = context,
            phoneNumber = normalizedPhone,
        )) {
            Toast
                .makeText(context, "Kurumsal arama açılamadı. Telefon arama ekranı açılıyor.", Toast.LENGTH_SHORT)
                .show()
            openWithDialer(context = context, phoneNumber = normalizedPhone)
        }
    }

    private fun openWithNetsipp(
        context: Context,
        phoneNumber: String,
    ): Boolean {
        if (!context.isPackageInstalled(NETSIPP_PACKAGE_NAME)) {
            return false
        }

        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.parse("tel:$phoneNumber"),
        ).apply {
            setPackage(NETSIPP_PACKAGE_NAME)
        }

        return context.tryStartActivity(intent)
    }

    private fun Context.isPackageInstalled(packageName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(0),
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(packageName, 0)
            }
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        } catch (_: RuntimeException) {
            false
        }
    }

    private fun openWithDialer(
        context: Context,
        phoneNumber: String,
    ) {
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.parse("tel:$phoneNumber"),
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
        } catch (_: RuntimeException) {
            false
        }
    }
}
