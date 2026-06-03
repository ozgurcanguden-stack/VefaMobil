package com.vefamobil.navigation

sealed class VefaDestination(val route: String) {
    data object Splash : VefaDestination("splash")
    data object LoginSelection : VefaDestination("login_selection")
    data object ManagerLogin : VefaDestination("manager_login")
    data object PersonnelLogin : VefaDestination("personnel_login")
    data object ForcePasswordChange : VefaDestination("force_password_change")
    data object ManagerHome : VefaDestination("manager_home")
    data object PersonnelHome : VefaDestination("personnel_home")
    data object Households : VefaDestination("households")
    data object HouseholdDetail : VefaDestination("household_detail/{householdId}") {
        fun createRoute(householdId: String): String = "household_detail/$householdId"
    }
    data object HouseholdForm : VefaDestination("household_form?householdId={householdId}") {
        const val baseRoute: String = "household_form"

        fun createRoute(householdId: String? = null): String {
            return householdId?.let { "$baseRoute?householdId=$it" } ?: baseRoute
        }
    }
    data object PersonnelList : VefaDestination("personnel_list")
    data object PersonnelDetail : VefaDestination("personnel_detail/{personnelId}") {
        fun createRoute(personnelId: String): String = "personnel_detail/$personnelId"
    }
    data object PersonnelForm : VefaDestination("personnel_form?personnelId={personnelId}") {
        const val baseRoute: String = "personnel_form"

        fun createRoute(personnelId: String? = null): String {
            return personnelId?.let { "$baseRoute?personnelId=$it" } ?: baseRoute
        }
    }
    data object Tasks : VefaDestination("tasks")
    data object TaskDetail : VefaDestination("task_detail/{taskId}") {
        fun createRoute(taskId: String): String = "task_detail/$taskId"
    }
    data object TaskForm : VefaDestination("task_form")
    data object Announcements : VefaDestination("announcements")
    data object Reports : VefaDestination("reports")
    data object Settings : VefaDestination("settings")
    data object Trash : VefaDestination("trash")
    data object AuditLogs : VefaDestination("audit_logs")
}
