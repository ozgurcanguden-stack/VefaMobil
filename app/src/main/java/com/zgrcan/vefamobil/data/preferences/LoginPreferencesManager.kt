package com.zgrcan.vefamobil.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.loginPreferencesDataStore by preferencesDataStore(name = "login_preferences")

data class ManagerLoginPreferences(
    val rememberMe: Boolean = false,
    val organizationCode: String = "",
    val email: String = "",
)

data class PersonnelLoginPreferences(
    val rememberMe: Boolean = false,
    val organizationCode: String = "",
    val email: String = "",
)

class LoginPreferencesManager(
    private val context: Context,
) {
    val managerLoginPreferences: Flow<ManagerLoginPreferences> =
        context.loginPreferencesDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                ManagerLoginPreferences(
                    rememberMe = preferences[REMEMBER_MANAGER_LOGIN] ?: false,
                    organizationCode = preferences[SAVED_MANAGER_ORG_CODE].orEmpty(),
                    email = preferences[SAVED_MANAGER_EMAIL].orEmpty(),
                )
            }

    val personnelLoginPreferences: Flow<PersonnelLoginPreferences> =
        context.loginPreferencesDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                PersonnelLoginPreferences(
                    rememberMe = preferences[REMEMBER_PERSONNEL_LOGIN] ?: false,
                    organizationCode = preferences[SAVED_PERSONNEL_ORG_CODE].orEmpty(),
                    email = preferences[SAVED_PERSONNEL_EMAIL].orEmpty(),
                )
            }

    suspend fun saveManagerLogin(
        organizationCode: String,
        email: String,
    ) {
        context.loginPreferencesDataStore.edit { preferences ->
            preferences[REMEMBER_MANAGER_LOGIN] = true
            preferences[SAVED_MANAGER_ORG_CODE] = organizationCode
            preferences[SAVED_MANAGER_EMAIL] = email
        }
    }

    suspend fun clearManagerLogin() {
        context.loginPreferencesDataStore.edit { preferences ->
            preferences.remove(REMEMBER_MANAGER_LOGIN)
            preferences.remove(SAVED_MANAGER_ORG_CODE)
            preferences.remove(SAVED_MANAGER_EMAIL)
        }
    }

    suspend fun savePersonnelLogin(
        organizationCode: String,
        email: String,
    ) {
        context.loginPreferencesDataStore.edit { preferences ->
            preferences[REMEMBER_PERSONNEL_LOGIN] = true
            preferences[SAVED_PERSONNEL_ORG_CODE] = organizationCode
            preferences[SAVED_PERSONNEL_EMAIL] = email
        }
    }

    suspend fun clearPersonnelLogin() {
        context.loginPreferencesDataStore.edit { preferences ->
            preferences.remove(REMEMBER_PERSONNEL_LOGIN)
            preferences.remove(SAVED_PERSONNEL_ORG_CODE)
            preferences.remove(SAVED_PERSONNEL_EMAIL)
        }
    }

    private companion object {
        val REMEMBER_MANAGER_LOGIN = booleanPreferencesKey("remember_manager_login")
        val SAVED_MANAGER_ORG_CODE = stringPreferencesKey("saved_manager_org_code")
        val SAVED_MANAGER_EMAIL = stringPreferencesKey("saved_manager_email")
        val REMEMBER_PERSONNEL_LOGIN = booleanPreferencesKey("remember_personnel_login")
        val SAVED_PERSONNEL_ORG_CODE = stringPreferencesKey("saved_personnel_org_code")
        val SAVED_PERSONNEL_EMAIL = stringPreferencesKey("saved_personnel_email")
    }
}
