package com.vefamobil.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vefamobil.model.UserRole
import com.vefamobil.presentation.HouseholdViewModel
import com.vefamobil.presentation.LoginViewModel
import com.vefamobil.presentation.screen.ForcePasswordChangeScreen
import com.vefamobil.presentation.screen.HouseholdFormScreen
import com.vefamobil.presentation.screen.HouseholdsScreen
import com.vefamobil.presentation.screen.LoginSelectionScreen
import com.vefamobil.presentation.screen.ManagerHomeScreen
import com.vefamobil.presentation.screen.ManagerLoginScreen
import com.vefamobil.presentation.screen.PersonnelHomeScreen
import com.vefamobil.presentation.screen.PersonnelLoginScreen
import com.vefamobil.presentation.screen.SplashScreen

@Composable
fun VefaNavHost(
    navController: NavHostController = rememberNavController(),
) {
    val loginViewModel: LoginViewModel = viewModel()
    val householdViewModel: HouseholdViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = VefaDestination.LoginSelection.route,
    ) {
        composable(VefaDestination.Splash.route) {
            SplashScreen(
                onFinished = {
                    navController.navigate(VefaDestination.LoginSelection.route) {
                        popUpTo(VefaDestination.Splash.route) { inclusive = true }
                    }
                },
            )
        }

        composable(VefaDestination.LoginSelection.route) {
            LoginSelectionScreen(
                onManagerLoginClick = { navController.navigate(VefaDestination.ManagerLogin.route) },
                onPersonnelLoginClick = { navController.navigate(VefaDestination.PersonnelLogin.route) },
            )
        }

        composable(VefaDestination.ManagerLogin.route) {
            ManagerLoginScreen(
                onBackClick = navController::popBackStack,
                onLoginClick = { username, password ->
                    val user = loginViewModel.login(UserRole.MANAGER, username, password)
                    val destination = if (user.mustChangePassword) {
                        VefaDestination.ForcePasswordChange.route
                    } else {
                        VefaDestination.ManagerHome.route
                    }
                    navController.navigate(destination)
                },
            )
        }

        composable(VefaDestination.PersonnelLogin.route) {
            PersonnelLoginScreen(
                onBackClick = navController::popBackStack,
                onLoginClick = { username, password ->
                    val user = loginViewModel.login(UserRole.PERSONNEL, username, password)
                    val destination = if (user.mustChangePassword) {
                        VefaDestination.ForcePasswordChange.route
                    } else {
                        VefaDestination.PersonnelHome.route
                    }
                    navController.navigate(destination)
                },
            )
        }

        composable(VefaDestination.ForcePasswordChange.route) {
            ForcePasswordChangeScreen(
                onPasswordChanged = {
                    val destination = when (loginViewModel.currentUser?.role) {
                        UserRole.MANAGER -> VefaDestination.ManagerHome.route
                        UserRole.PERSONNEL -> VefaDestination.PersonnelHome.route
                        null -> VefaDestination.LoginSelection.route
                    }
                    navController.navigate(destination) {
                        popUpTo(VefaDestination.LoginSelection.route)
                    }
                },
            )
        }

        composable(VefaDestination.ManagerHome.route) {
            ManagerHomeScreen(
                displayName = loginViewModel.currentUser?.displayName.orEmpty(),
                onHouseholdsClick = { navController.navigate(VefaDestination.Households.route) },
            )
        }

        composable(VefaDestination.PersonnelHome.route) {
            PersonnelHomeScreen(
                displayName = loginViewModel.currentUser?.displayName.orEmpty(),
            )
        }

        composable(VefaDestination.Households.route) {
            HouseholdsScreen(
                state = householdViewModel.state,
                onBackClick = navController::popBackStack,
                onNewHouseholdClick = { navController.navigate(VefaDestination.HouseholdForm.route) },
                onSearchQueryChange = householdViewModel::onSearchQueryChange,
                onDeleteClick = householdViewModel::deleteHousehold,
                onToggleActiveClick = householdViewModel::toggleActive,
            )
        }

        composable(VefaDestination.HouseholdForm.route) {
            HouseholdFormScreen(
                onBackClick = navController::popBackStack,
                onSaveHousehold = { household ->
                    householdViewModel.addHousehold(household)
                    navController.popBackStack()
                },
            )
        }
    }
}
