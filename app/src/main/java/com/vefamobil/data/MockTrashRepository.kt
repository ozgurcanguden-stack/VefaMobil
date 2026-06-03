package com.vefamobil.data

import com.vefamobil.data.mock.MockDataStore
import com.vefamobil.model.TrashItem

class MockTrashRepository {
    fun getTrashItems(): List<TrashItem> = MockDataStore.trashItems.sortedByDescending { it.deletedAt }

    fun addToTrash(item: TrashItem) {
        MockDataStore.trashItems.add(item)
    }

    fun restoreItem(id: String) {
        MockDataStore.trashItems.removeAll { it.id == id }
    }

    fun permanentlyDelete(id: String) {
        MockDataStore.trashItems.removeAll { it.id == id }
    }
}
