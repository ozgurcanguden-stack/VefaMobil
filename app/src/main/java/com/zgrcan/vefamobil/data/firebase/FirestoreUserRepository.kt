package com.zgrcan.vefamobil.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.zgrcan.vefamobil.model.AppUser
import com.zgrcan.vefamobil.model.Organization
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirestoreUserRepository {
    suspend fun getUserProfile(uid: String): AppUser? {
        val firestore = firestoreOrNull() ?: return null

        return suspendCancellableCoroutine { continuation ->
            try {
                firestore.collection(FirestoreCollections.USERS)
                    .document(uid)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (!continuation.isActive) return@addOnSuccessListener

                        val user = if (snapshot.exists()) {
                            snapshot.toObject(AppUser::class.java)?.let { appUser ->
                                if (appUser.uid.isBlank()) appUser.copy(uid = snapshot.id) else appUser
                            }
                        } else {
                            null
                        }
                        continuation.resume(user)
                    }
                    .addOnFailureListener {
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }
            } catch (_: Exception) {
                if (continuation.isActive) {
                    continuation.resume(null)
                }
            }
        }
    }

    suspend fun getOrganization(organizationId: String): Organization? {
        val firestore = firestoreOrNull() ?: return null

        return suspendCancellableCoroutine { continuation ->
            try {
                firestore.collection(FirestoreCollections.ORGANIZATIONS)
                    .document(organizationId)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (!continuation.isActive) return@addOnSuccessListener

                        val organization = if (snapshot.exists()) {
                            snapshot.toObject(Organization::class.java)?.let { organization ->
                                if (organization.organizationId.isBlank()) {
                                    organization.copy(organizationId = snapshot.id)
                                } else {
                                    organization
                                }
                            }
                        } else {
                            null
                        }
                        continuation.resume(organization)
                    }
                    .addOnFailureListener {
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }
            } catch (_: Exception) {
                if (continuation.isActive) {
                    continuation.resume(null)
                }
            }
        }
    }

    private fun firestoreOrNull(): FirebaseFirestore? {
        return try {
            FirebaseFirestore.getInstance()
        } catch (_: Exception) {
            null
        }
    }
}
