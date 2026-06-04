package com.zgrcan.vefamobil.data.firebase

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.zgrcan.vefamobil.model.Household
import kotlinx.coroutines.tasks.await

class FirestoreHouseholdRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {
    suspend fun getHouseholds(organizationId: String): List<Household> {
        val snapshot = firestore.collection(FirestoreCollections.HOUSEHOLDS)
            .whereEqualTo(FirestoreFields.ORGANIZATION_ID, organizationId)
            .get()
            .await()

        return snapshot.documents
            .mapNotNull { document ->
                document.toObject(Household::class.java)?.let { household ->
                    if (household.id.isBlank()) {
                        household.copy(id = document.id)
                    } else {
                        household
                    }
                }
            }
            .filter { household -> !household.isDeleted }
    }

    suspend fun addHousehold(
        organizationId: String,
        household: Household,
    ) {
        val document = if (household.id.isBlank()) {
            firestore.collection(FirestoreCollections.HOUSEHOLDS).document()
        } else {
            firestore.collection(FirestoreCollections.HOUSEHOLDS).document(household.id)
        }
        val householdId = document.id

        document.set(
            household.toFirestoreMap(
                id = householdId,
                organizationId = organizationId,
                includeCreatedFields = true,
            ),
        ).await()
    }

    suspend fun updateHousehold(
        organizationId: String,
        household: Household,
    ) {
        val document = householdDocument(organizationId, household.id)
        document.reference.set(
            household.toFirestoreMap(
                id = document.id,
                organizationId = organizationId,
                includeCreatedFields = false,
            ),
            SetOptions.merge(),
        ).await()
    }

    suspend fun setHouseholdActive(
        organizationId: String,
        householdId: String,
        isActive: Boolean,
    ) {
        val document = householdDocument(organizationId, householdId)
        document.reference.update(
            mapOf(
                FirestoreFields.IS_ACTIVE to isActive,
                FirestoreFields.UPDATED_AT to FieldValue.serverTimestamp(),
            ),
        ).await()
    }

    suspend fun deleteHousehold(
        organizationId: String,
        householdId: String,
    ) {
        val document = householdDocument(organizationId, householdId)
        document.reference.update(
            mapOf(
                FirestoreFields.IS_DELETED to true,
                FirestoreFields.UPDATED_AT to FieldValue.serverTimestamp(),
                FirestoreFields.DELETED_AT to FieldValue.serverTimestamp(),
            ),
        ).await()
    }

    private suspend fun householdDocument(
        organizationId: String,
        householdId: String,
    ) = firestore.collection(FirestoreCollections.HOUSEHOLDS)
        .document(householdId)
        .get()
        .await()
        .also { document ->
            val documentOrganizationId = document.getString(FirestoreFields.ORGANIZATION_ID)
            if (!document.exists() || documentOrganizationId != organizationId) {
                throw IllegalStateException("Household does not belong to this organization.")
            }
        }

    private fun Household.toFirestoreMap(
        id: String,
        organizationId: String,
        includeCreatedFields: Boolean,
    ): Map<String, Any?> {
        return buildMap {
            put(FirestoreFields.ID, id)
            put(FirestoreFields.ORGANIZATION_ID, organizationId)
            put("refCode", refCode)
            put("neighborhood", neighborhood)
            put("fullName", fullName)
            put("tcNo", tcNo)
            put("phone1", phone1)
            put("phone2", phone2)
            put("address", address)
            put(FirestoreFields.IS_ACTIVE, if (includeCreatedFields) true else isActive)
            put("isNewHousehold", if (includeCreatedFields) true else isNewHousehold)
            put("isUrgent", isUrgent)
            put("firstVisitCompleted", if (includeCreatedFields) false else firstVisitCompleted)
            put(FirestoreFields.IS_DELETED, false)
            put(FirestoreFields.UPDATED_AT, FieldValue.serverTimestamp())

            if (includeCreatedFields) {
                put(FirestoreFields.CREATED_AT, FieldValue.serverTimestamp())
                put(FirestoreFields.CREATED_BY, createdBy)
            }
        }
    }
}
