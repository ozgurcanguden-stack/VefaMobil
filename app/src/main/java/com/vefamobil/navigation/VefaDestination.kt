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
}
