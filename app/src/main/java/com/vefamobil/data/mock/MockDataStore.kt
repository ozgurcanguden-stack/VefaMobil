package com.vefamobil.data.mock

import com.vefamobil.model.Announcement
import com.vefamobil.model.AuditLog
import com.vefamobil.model.Household
import com.vefamobil.model.Note
import com.vefamobil.model.Personnel
import com.vefamobil.model.Task
import com.vefamobil.model.TaskCreatedMode
import com.vefamobil.model.TaskItem
import com.vefamobil.model.TaskItemStatus
import com.vefamobil.model.TaskPublishMode
import com.vefamobil.model.TrashItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MockDataStore {
    val households: MutableList<Household> = mutableListOf(
        Household(
            id = "1",
            refCode = "REF001",
            neighborhood = "Hürriyet",
            fullName = "Ahmet Yılmaz",
            tcNo = "11111111111",
            phone1 = "05550000001",
            phone2 = null,
            address = "Hürriyet Mahallesi",
            isActive = true,
            isNewHousehold = true,
            isUrgent = false,
            firstVisitCompleted = false,
        ),
        Household(
            id = "2",
            refCode = "REF002",
            neighborhood = "Cumhuriyet",
            fullName = "Ayşe Kaya",
            tcNo = "22222222222",
            phone1 = "05550000002",
            phone2 = "05550000012",
            address = "Cumhuriyet Mahallesi",
            isActive = true,
            isNewHousehold = false,
            isUrgent = true,
            firstVisitCompleted = true,
        ),
        Household(
            id = "3",
            refCode = "REF003",
            neighborhood = "Acarlar",
            fullName = "Mehmet Demir",
            tcNo = "33333333333",
            phone1 = "05550000003",
            phone2 = null,
            address = "Acarlar Mahallesi",
            isActive = true,
            isNewHousehold = false,
            isUrgent = false,
            firstVisitCompleted = true,
        ),
        Household(
            id = "4",
            refCode = "REF004",
            neighborhood = "Hürriyet",
            fullName = "Fatma Çelik",
            tcNo = "44444444444",
            phone1 = "05550000004",
            phone2 = null,
            address = "Hürriyet Mahallesi",
            isActive = true,
            isNewHousehold = false,
            isUrgent = false,
            firstVisitCompleted = true,
        ),
    )

    val personnel: MutableList<Personnel> = mutableListOf(
        Personnel(
            id = "1",
            fullName = "Ali Kaya",
            username = "ali.kaya",
            temporaryPassword = "123456",
            isActive = true,
            mustChangePassword = true,
            createdAt = System.currentTimeMillis(),
        ),
        Personnel(
            id = "2",
            fullName = "Ayşe Demir",
            username = "ayse.demir",
            temporaryPassword = "123456",
            isActive = true,
            mustChangePassword = true,
            createdAt = System.currentTimeMillis(),
        ),
    )

    val tasks: MutableList<Task> = mutableListOf(
        Task(
            id = "1",
            taskDate = "03.06.2026",
            neighborhood = "Hürriyet Mahallesi",
            totalHouseholds = 6,
            completedCount = 4,
            status = "Devam Ediyor",
            createdMode = TaskCreatedMode.AUTO,
            publishMode = TaskPublishMode.TODAY,
            createdAt = System.currentTimeMillis(),
        ),
        Task(
            id = "2",
            taskDate = "03.06.2026",
            neighborhood = "Cumhuriyet Mahallesi",
            totalHouseholds = 5,
            completedCount = 5,
            status = "Tamamlandı",
            createdMode = TaskCreatedMode.MANUAL,
            publishMode = TaskPublishMode.TODAY,
            createdAt = System.currentTimeMillis(),
        ),
    )

    val taskItems: MutableList<TaskItem> = mutableListOf(
        TaskItem(
            id = "task-item-1",
            taskId = "1",
            householdId = "1",
            neighborhood = "Hürriyet",
            householdName = "Ahmet Yılmaz",
            refCode = "REF001",
            phone1 = "05550000001",
            phone2 = null,
            address = "Hürriyet Mahallesi",
            status = TaskItemStatus.PENDING,
            isUrgent = false,
            isNewHousehold = true,
            note = "",
        ),
        TaskItem(
            id = "task-item-2",
            taskId = "1",
            householdId = "2",
            neighborhood = "Cumhuriyet",
            householdName = "Ayşe Kaya",
            refCode = "REF002",
            phone1 = "05550000002",
            phone2 = "05550000012",
            address = "Cumhuriyet Mahallesi",
            status = TaskItemStatus.PENDING,
            isUrgent = true,
            isNewHousehold = false,
            note = "",
        ),
        TaskItem(
            id = "task-item-3",
            taskId = "1",
            householdId = "3",
            neighborhood = "Acarlar",
            householdName = "Mehmet Demir",
            refCode = "REF003",
            phone1 = "05550000003",
            phone2 = null,
            address = "Acarlar Mahallesi",
            status = TaskItemStatus.PENDING,
            isUrgent = false,
            isNewHousehold = false,
            note = "",
        ),
    )

    val announcements: MutableList<Announcement> = mutableListOf(
        Announcement(
            id = "1",
            title = "Yeni Görev Listesi Oluşturuldu",
            message = "Bugün için saha görev listesi yayınlandı.",
            type = "TASK",
            createdAt = System.currentTimeMillis() - 90 * 60 * 1000,
            isRead = false,
        ),
        Announcement(
            id = "2",
            title = "Maaş İmzası Hatırlatması",
            message = "Personel maaş imza listesi gün içinde tamamlanmalıdır.",
            type = "REMINDER",
            createdAt = System.currentTimeMillis() - 3 * 60 * 60 * 1000,
            isRead = true,
        ),
    )

    val notes: MutableList<Note> = mutableListOf(
        Note(
            id = "note-1",
            householdId = "1",
            householdName = "Ahmet Yılmaz",
            neighborhood = "Hürriyet",
            summary = "İlaç desteği tekrar kontrol edilecek.",
            createdAt = "03.06.2026",
        ),
        Note(
            id = "note-2",
            householdId = "2",
            householdName = "Ayşe Kaya",
            neighborhood = "Cumhuriyet",
            summary = "Evde yoktu, tekrar ziyaret planlanacak.",
            createdAt = "03.06.2026",
        ),
    )

    val auditLogs: MutableList<AuditLog> = mutableListOf()
    val trashItems: MutableList<TrashItem> = mutableListOf()

    fun updateTaskItem(taskItem: TaskItem) {
        val index = taskItems.indexOfFirst { it.id == taskItem.id }
        if (index >= 0) {
            taskItems[index] = taskItem
        } else {
            taskItems.add(taskItem)
        }
    }

    fun addOrUpdateNote(taskItem: TaskItem) {
        if (taskItem.note.isBlank()) return

        val note = Note(
            id = "note-${taskItem.id}",
            householdId = taskItem.householdId,
            householdName = taskItem.householdName,
            neighborhood = taskItem.neighborhood,
            summary = taskItem.note,
            createdAt = SimpleDateFormat("dd.MM.yyyy", Locale("tr", "TR")).format(Date()),
        )
        val index = notes.indexOfFirst { it.id == note.id }
        if (index >= 0) {
            notes[index] = note
        } else {
            notes.add(note)
        }
    }
}
