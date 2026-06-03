package com.vefamobil.data

import com.vefamobil.model.TrashItem

class MockTrashRepository {
    fun getTrashItems(): List<TrashItem> = trashItems.sortedByDescending { it.deletedAt }

    fun addToTrash(item: TrashItem) {
        trashItems.add(item)
    }

    fun restoreItem(id: String) {
        trashItems.removeAll { it.id == id }
    }

    fun permanentlyDelete(id: String) {
        trashItems.removeAll { it.id == id }
    }

    companion object {
        private val trashItems = mutableListOf<TrashItem>()
    }
}
