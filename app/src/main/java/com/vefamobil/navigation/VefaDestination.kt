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
    data object HouseholdForm : VefaDestination("household_form")
}
