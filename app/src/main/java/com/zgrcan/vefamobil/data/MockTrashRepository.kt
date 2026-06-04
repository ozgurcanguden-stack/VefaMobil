package com.zgrcan.vefamobil.data

import com.zgrcan.vefamobil.data.mock.MockDataStore
import com.zgrcan.vefamobil.model.TrashItem

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
