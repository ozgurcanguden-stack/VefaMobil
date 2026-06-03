package com.vefamobil.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vefamobil.model.UserRole
import com.vefamobil.presentation.AnnouncementViewModel
import com.vefamobil.presentation.HouseholdViewModel
import com.vefamobil.presentation.LoginViewModel
import com.vefamobil.presentation.PersonnelViewModel
import com.vefamobil.presentation.ReportsViewModel
import com.vefamobil.presentation.SettingsViewModel
import com.vefamobil.presentation.TaskViewModel
import com.vefamobil.presentation.screen.AnnouncementsScreen
import com.vefamobil.presentation.screen.ForcePasswordChangeScreen
import com.vefamobil.presentation.screen.HouseholdDetailScreen
import com.vefamobil.presentation.screen.HouseholdFormScreen
import com.vefamobil.presentation.screen.HouseholdsScreen
import com.vefamobil.presentation.screen.LoginSelectionScreen
import com.vefamobil.presentation.screen.ManagerHomeScreen
import com.vefamobil.presentation.screen.ManagerLoginScreen
import com.vefamobil.presentation.screen.PersonnelHomeScreen
import com.vefamobil.presentation.screen.PersonnelDetailScreen
import com.vefamobil.presentation.screen.PersonnelFormScreen
import com.vefamobil.presentation.screen.PersonnelListScreen
import com.vefamobil.presentation.screen.PersonnelLoginScreen
import com.vefamobil.presentation.screen.ReportsScreen
import com.vefamobil.presentation.screen.SettingsScreen
import com.vefamobil.presentation.screen.SplashScreen
import com.vefamobil.presentation.screen.TaskDetailScreen
import com.vefamobil.presentation.screen.TaskFormScreen
import com.vefamobil.presentation.screen.TasksScreen

@Composable
fun VefaNavHost(
    navController: NavHostController = rememberNavController(),
) {
    val loginViewModel: LoginViewModel = viewModel()
    val householdViewModel: HouseholdViewModel = viewModel()
    val personnelViewModel: PersonnelViewModel = viewModel()
    val taskViewModel: TaskViewModel = viewModel()
    val announcementViewModel: AnnouncementViewModel = viewModel()
    val reportsViewModel: ReportsViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()

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
                onPersonnelClick = { navController.navigate(VefaDestination.PersonnelList.route) },
                onTasksClick = { navController.navigate(VefaDestination.Tasks.route) },
                onAnnouncementsClick = { navController.navigate(VefaDestination.Announcements.route) },
                onReportsClick = { navController.navigate(VefaDestination.Reports.route) },
                onSettingsClick = { navController.navigate(VefaDestination.Settings.route) },
            )
        }

        composable(VefaDestination.PersonnelHome.route) {
            PersonnelHomeScreen(
                displayName = loginViewModel.currentUser?.displayName.orEmpty(),
                announcementState = announcementViewModel.state,
                onAnnouncementRead = announcementViewModel::markAsRead,
            )
        }

        composable(VefaDestination.Households.route) {
            HouseholdsScreen(
                state = householdViewModel.state,
                onBackClick = navController::popBackStack,
                onNewHouseholdClick = { navController.navigate(VefaDestination.HouseholdForm.createRoute()) },
                onHouseholdClick = { householdId ->
                    navController.navigate(VefaDestination.HouseholdDetail.createRoute(householdId))
                },
                onEditClick = { householdId ->
                    navController.navigate(VefaDestination.HouseholdForm.createRoute(householdId))
                },
                onSearchQueryChange = householdViewModel::onSearchQueryChange,
                onDeleteClick = householdViewModel::deleteHousehold,
                onToggleActiveClick = householdViewModel::toggleActive,
            )
        }

        composable(VefaDestination.HouseholdDetail.route) { backStackEntry ->
            val householdId = backStackEntry.arguments?.getString("householdId").orEmpty()

            HouseholdDetailScreen(
                household = householdViewModel.getHousehold(householdId),
                onBackClick = navController::popBackStack,
                onEditClick = { id ->
                    navController.navigate(VefaDestination.HouseholdForm.createRoute(id))
                },
                onDeleteClick = { id ->
                    householdViewModel.deleteHousehold(id)
                    navController.popBackStack()
                },
                onToggleActiveClick = householdViewModel::toggleActive,
            )
        }

        composable(
            route = VefaDestination.HouseholdForm.route,
            arguments = listOf(
                navArgument("householdId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { backStackEntry ->
            val householdId = backStackEntry.arguments?.getString("householdId")
            val initialHousehold = householdId?.let { householdViewModel.getHousehold(it) }

            HouseholdFormScreen(
                onBackClick = navController::popBackStack,
                initialHousehold = initialHousehold,
                onSaveHousehold = { household ->
                    if (householdId == null) {
                        householdViewModel.addHousehold(household)
                    } else {
                        householdViewModel.updateHousehold(household)
                    }
                    navController.popBackStack()
                },
            )
        }

        composable(VefaDestination.PersonnelList.route) {
            PersonnelListScreen(
                state = personnelViewModel.state,
                onBackClick = navController::popBackStack,
                onAddPersonnelClick = { navController.navigate(VefaDestination.PersonnelForm.createRoute()) },
                onPersonnelClick = { personnelId ->
                    navController.navigate(VefaDestination.PersonnelDetail.createRoute(personnelId))
                },
                onEditClick = { personnelId ->
                    navController.navigate(VefaDestination.PersonnelForm.createRoute(personnelId))
                },
                onSearchQueryChange = personnelViewModel::onSearchQueryChange,
                onDeleteClick = personnelViewModel::deletePersonnel,
                onToggleActiveClick = personnelViewModel::toggleActive,
            )
        }

        composable(VefaDestination.PersonnelDetail.route) { backStackEntry ->
            val personnelId = backStackEntry.arguments?.getString("personnelId").orEmpty()

            PersonnelDetailScreen(
                personnel = personnelViewModel.getPersonnel(personnelId),
                onBackClick = navController::popBackStack,
                onEditClick = { id ->
                    navController.navigate(VefaDestination.PersonnelForm.createRoute(id))
                },
                onDeleteClick = { id ->
                    personnelViewModel.deletePersonnel(id)
                    navController.popBackStack()
                },
                onToggleActiveClick = personnelViewModel::toggleActive,
            )
        }

        composable(
            route = VefaDestination.PersonnelForm.route,
            arguments = listOf(
                navArgument("personnelId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { backStackEntry ->
            val personnelId = backStackEntry.arguments?.getString("personnelId")
            val initialPersonnel = personnelId?.let { personnelViewModel.getPersonnel(it) }

            PersonnelFormScreen(
                onBackClick = navController::popBackStack,
                initialPersonnel = initialPersonnel,
                onSavePersonnel = { personnel ->
                    if (personnelId == null) {
                        personnelViewModel.addPersonnel(personnel)
                    } else {
                        personnelViewModel.updatePersonnel(personnel)
                    }
                    navController.popBackStack()
                },
            )
        }

        composable(VefaDestination.Tasks.route) {
            TasksScreen(
                state = taskViewModel.state,
                onBackClick = navController::popBackStack,
                onCreateTaskClick = { navController.navigate(VefaDestination.TaskForm.route) },
                onAutoCreateTaskClick = taskViewModel::createAutomaticTask,
                onTaskClick = { taskId ->
                    navController.navigate(VefaDestination.TaskDetail.createRoute(taskId))
                },
            )
        }

        composable(VefaDestination.TaskDetail.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId").orEmpty()

            TaskDetailScreen(
                task = taskViewModel.getTask(taskId),
                households = taskViewModel.getTaskHouseholds(taskId),
                onBackClick = navController::popBackStack,
            )
        }

        composable(VefaDestination.TaskForm.route) {
            TaskFormScreen(
                onBackClick = navController::popBackStack,
                onSaveTask = { neighborhood, totalHouseholds, createdMode, publishMode ->
                    taskViewModel.addTask(
                        neighborhood = neighborhood,
                        totalHouseholds = totalHouseholds,
                        createdMode = createdMode,
                        publishMode = publishMode,
                    )
                    navController.popBackStack()
                },
            )
        }

        composable(VefaDestination.Announcements.route) {
            AnnouncementsScreen(
                state = announcementViewModel.state,
                onBackClick = navController::popBackStack,
            )
        }

        composable(VefaDestination.Reports.route) {
            ReportsScreen(
                state = reportsViewModel.state,
                onBackClick = navController::popBackStack,
            )
        }

        composable(VefaDestination.Settings.route) {
            SettingsScreen(
                settings = settingsViewModel.settings,
                onBackClick = navController::popBackStack,
                onDailyTargetChange = settingsViewModel::updateDailyTargetCount,
                onWorkStartTimeChange = settingsViewModel::updateWorkStartTime,
                onWorkEndTimeChange = settingsViewModel::updateWorkEndTime,
                onSalaryReminderTimeChange = settingsViewModel::updateSalaryReminderTime,
                onBiometricEnabledChange = settingsViewModel::setBiometricEnabled,
                onAddNeighborhood = settingsViewModel::addNeighborhood,
                onRemoveNeighborhood = settingsViewModel::removeNeighborhood,
                onPasswordChangeClick = {
                    navController.navigate(VefaDestination.ForcePasswordChange.route)
                },
                onLogoutClick = {
                    navController.navigate(VefaDestination.LoginSelection.route) {
                        launchSingleTop = true
                        popUpTo(VefaDestination.LoginSelection.route) {
                            inclusive = false
                        }
                    }
                },
            )
        }
    }
}
