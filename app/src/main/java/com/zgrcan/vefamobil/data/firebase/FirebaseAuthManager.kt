package com.zgrcan.vefamobil.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseAuthManager {
    suspend fun login(email: String, password: String): Result<Unit> {
        val auth = authOrNull() ?: return Result.failure(
            IllegalStateException("Firebase Authentication is not configured."),
        )

        return suspendCancellableCoroutine { continuation ->
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (!continuation.isActive) return@addOnCompleteListener

                        if (task.isSuccessful) {
                            continuation.resume(Result.success(Unit))
                        } else {
                            val exception = task.exception ?: IllegalStateException("Firebase login failed.")
                            continuation.resume(Result.failure(exception))
                        }
                    }
            } catch (exception: Exception) {
                if (continuation.isActive) {
                    continuation.resume(Result.failure(exception))
                }
            }
        }
    }

    fun logout() {
        try {
            authOrNull()?.signOut()
        } catch (_: Exception) {
        }
    }

    fun currentUser(): FirebaseUser? {
        return try {
            authOrNull()?.currentUser
        } catch (_: Exception) {
            null
        }
    }

    fun isLoggedIn(): Boolean = currentUser() != null

    private fun authOrNull(): FirebaseAuth? {
        return try {
            FirebaseAuth.getInstance()
        } catch (_: Exception) {
            null
        }
    }
}
