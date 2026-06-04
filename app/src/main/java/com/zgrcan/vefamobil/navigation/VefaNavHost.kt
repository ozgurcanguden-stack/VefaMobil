package com.zgrcan.vefamobil.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zgrcan.vefamobil.data.preferences.LastLoginType
import com.zgrcan.vefamobil.data.preferences.LoginPreferencesManager
import com.zgrcan.vefamobil.model.UserRole
import com.zgrcan.vefamobil.presentation.AnnouncementViewModel
import com.zgrcan.vefamobil.presentation.ExcelImportViewModel
import com.zgrcan.vefamobil.presentation.ForcePasswordChangeViewModel
import com.zgrcan.vefamobil.presentation.HouseholdViewModel
import com.zgrcan.vefamobil.presentation.LoginViewModel
import com.zgrcan.vefamobil.presentation.ManagerLoginTarget
import com.zgrcan.vefamobil.presentation.ManagerLoginViewModel
import com.zgrcan.vefamobil.presentation.PersonnelLoginTarget
import com.zgrcan.vefamobil.presentation.PersonnelLoginViewModel
import com.zgrcan.vefamobil.presentation.PersonnelViewModel
import com.zgrcan.vefamobil.presentation.ReportsViewModel
import com.zgrcan.vefamobil.presentation.SettingsViewModel
import com.zgrcan.vefamobil.presentation.TaskViewModel
import com.zgrcan.vefamobil.presentation.TrashAuditViewModel
import com.zgrcan.vefamobil.presentation.screen.AnnouncementsScreen
import com.zgrcan.vefamobil.presentation.screen.AuditLogsScreen
import com.zgrcan.vefamobil.presentation.screen.ExcelImportScreen
import com.zgrcan.vefamobil.presentation.screen.ForcePasswordChangeScreen
import com.zgrcan.vefamobil.presentation.screen.HouseholdDetailScreen
import com.zgrcan.vefamobil.presentation.screen.HouseholdFormScreen
import com.zgrcan.vefamobil.presentation.screen.HouseholdsScreen
import com.zgrcan.vefamobil.presentation.screen.LoginSelectionScreen
import com.zgrcan.vefamobil.presentation.screen.ManagerHomeScreen
import com.zgrcan.vefamobil.presentation.screen.ManagerLoginScreen
import com.zgrcan.vefamobil.presentation.screen.PersonnelHomeScreen
import com.zgrcan.vefamobil.presentation.screen.PersonnelDetailScreen
import com.zgrcan.vefamobil.presentation.screen.PersonnelFormScreen
import com.zgrcan.vefamobil.presentation.screen.PersonnelListScreen
import com.zgrcan.vefamobil.presentation.screen.PersonnelLoginScreen
import com.zgrcan.vefamobil.presentation.screen.ReportsScreen
import com.zgrcan.vefamobil.presentation.screen.SettingsScreen
import com.zgrcan.vefamobil.presentation.screen.SplashScreen
import com.zgrcan.vefamobil.presentation.screen.TaskDetailScreen
import com.zgrcan.vefamobil.presentation.screen.TaskFormScreen
import com.zgrcan.vefamobil.presentation.screen.TasksScreen
import com.zgrcan.vefamobil.presentation.screen.TrashScreen
import kotlinx.coroutines.launch

@Composable
fun VefaNavHost(
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val loginPreferencesManager = remember(context) { LoginPreferencesManager(context) }
    val lastLoginType by loginPreferencesManager.lastLoginType.collectAsState(initial = null)
    val loginViewModel: LoginViewModel = viewModel()
    val managerLoginViewModel: ManagerLoginViewModel = viewModel()
    val personnelLoginViewModel: PersonnelLoginViewModel = viewModel()
    val forcePasswordChangeViewModel: ForcePasswordChangeViewModel = viewModel()
    val householdViewModel: HouseholdViewModel = viewModel()
    val personnelViewModel: PersonnelViewModel = viewModel()
    val taskViewModel: TaskViewModel = viewModel()
    val announcementViewModel: AnnouncementViewModel = viewModel()
    val reportsViewModel: ReportsViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val trashAuditViewModel: TrashAuditViewModel = viewModel()
    val excelImportViewModel: ExcelImportViewModel = viewModel()
    var passwordChangeDestination by remember { mutableStateOf<String?>(null) }
    var splashDestination by remember { mutableStateOf<String?>(null) }
    var showManagerLogoutDialog by remember { mutableStateOf(false) }
    var showPersonnelLogoutDialog by remember { mutableStateOf(false) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    fun safePopBackStack() {
        navController.popBackStack()
    }

    fun clearBackStackAndNavigate(route: String) {
        while (navController.popBackStack()) {
            // Clear every previous screen so login/home screens cannot remain underneath.
        }

        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateToLoginSelection(clearLastLoginType: Boolean) {
        if (clearLastLoginType) {
            coroutineScope.launch {
                loginPreferencesManager.setLastLoginType(LastLoginType.NONE)
            }
        }

        clearBackStackAndNavigate(VefaDestination.LoginSelection.route)
    }

    fun navigateToLoginAfterLogout(lastUsedLoginType: LastLoginType) {
        coroutineScope.launch {
            loginPreferencesManager.setLastLoginType(lastUsedLoginType)
        }

        val route = when (lastUsedLoginType) {
            LastLoginType.MANAGER -> VefaDestination.ManagerLogin.route
            LastLoginType.PERSONNEL -> VefaDestination.PersonnelLogin.route
            LastLoginType.NONE -> VefaDestination.LoginSelection.route
        }
        clearBackStackAndNavigate(route)
    }

    fun navigateAfterSuccessfulLogin(
        destination: String,
        lastUsedLoginType: LastLoginType,
    ) {
        coroutineScope.launch {
            loginPreferencesManager.setLastLoginType(lastUsedLoginType)
        }

        clearBackStackAndNavigate(destination)
    }

    BackHandler(enabled = true) {
        when (currentRoute) {
            VefaDestination.Splash.route,
            VefaDestination.LoginSelection.route,
            VefaDestination.ManagerLogin.route,
            VefaDestination.PersonnelLogin.route,
            VefaDestination.ForcePasswordChange.route
            -> Unit

            VefaDestination.ManagerHome.route,
            VefaDestination.PersonnelHome.route,
            -> Unit

            else -> {
                safePopBackStack()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = VefaDestination.Splash.route,
    ) {
        composable(VefaDestination.Splash.route) {
            LaunchedEffect(lastLoginType) {
                if (lastLoginType == null) return@LaunchedEffect

                val managerRestoreTarget = managerLoginViewModel.restoreSession()
                val personnelRestoreTarget = if (managerRestoreTarget == null) {
                    personnelLoginViewModel.restoreSession()
                } else {
                    null
                }

                splashDestination = when {
                    managerRestoreTarget == ManagerLoginTarget.FORCE_PASSWORD_CHANGE -> {
                        passwordChangeDestination = VefaDestination.ManagerHome.route
                        VefaDestination.ForcePasswordChange.route
                    }
                    managerRestoreTarget == ManagerLoginTarget.MANAGER_HOME -> {
                        VefaDestination.ManagerHome.route
                    }
                    personnelRestoreTarget == PersonnelLoginTarget.PERSONNEL_HOME -> {
                        VefaDestination.PersonnelHome.route
                    }
                    lastLoginType == LastLoginType.MANAGER -> VefaDestination.ManagerLogin.route
                    lastLoginType == LastLoginType.PERSONNEL -> VefaDestination.PersonnelLogin.route
                    else -> VefaDestination.LoginSelection.route
                }
            }

            SplashScreen(
                isReady = lastLoginType != null && splashDestination != null,
                onFinished = {
                    val destination = splashDestination ?: VefaDestination.LoginSelection.route
                    navController.navigate(destination) {
                        launchSingleTop = true
                        popUpTo(VefaDestination.Splash.route) { inclusive = true }
                    }
                },
            )
        }

        composable(VefaDestination.LoginSelection.route) {
            LoginSelectionScreen(
                onManagerLoginClick = {
                    coroutineScope.launch {
                        loginPreferencesManager.setLastLoginType(LastLoginType.MANAGER)
                    }
                    navController.navigate(VefaDestination.ManagerLogin.route) {
                        launchSingleTop = true
                    }
                },
                onPersonnelLoginClick = {
                    coroutineScope.launch {
                        loginPreferencesManager.setLastLoginType(LastLoginType.PERSONNEL)
                    }
                    navController.navigate(VefaDestination.PersonnelLogin.route) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(VefaDestination.ManagerLogin.route) {
            ManagerLoginScreen(
                state = managerLoginViewModel.state,
                onBackClick = { navigateToLoginSelection(clearLastLoginType = false) },
                onOrganizationCodeChange = managerLoginViewModel::onOrganizationCodeChange,
                onEmailChange = managerLoginViewModel::onEmailChange,
                onPasswordChange = managerLoginViewModel::onPasswordChange,
                onRememberMeChange = managerLoginViewModel::onRememberMeChange,
                onChangeOrganizationClick = managerLoginViewModel::clearSavedManagerLogin,
                onChangeLoginTypeClick = { navigateToLoginSelection(clearLastLoginType = true) },
                onLoginClick = managerLoginViewModel::login,
                onLoginSuccess = { target ->
                    val destination = when (target) {
                        ManagerLoginTarget.FORCE_PASSWORD_CHANGE -> {
                            passwordChangeDestination = VefaDestination.ManagerHome.route
                            VefaDestination.ForcePasswordChange.route
                        }
                        ManagerLoginTarget.MANAGER_HOME -> VefaDestination.ManagerHome.route
                    }

                    navigateAfterSuccessfulLogin(
                        destination = destination,
                        lastUsedLoginType = LastLoginType.MANAGER,
                    )
                },
                onErrorShown = managerLoginViewModel::clearError,
                onSuccessShown = managerLoginViewModel::clearSuccess,
            )
        }

        composable(VefaDestination.PersonnelLogin.route) {
            PersonnelLoginScreen(
                state = personnelLoginViewModel.state,
                onBackClick = { navigateToLoginSelection(clearLastLoginType = false) },
                onOrganizationCodeChange = personnelLoginViewModel::onOrganizationCodeChange,
                onEmailChange = personnelLoginViewModel::onEmailChange,
                onPasswordChange = personnelLoginViewModel::onPasswordChange,
                onRememberMeChange = personnelLoginViewModel::onRememberMeChange,
                onChangeOrganizationClick = personnelLoginViewModel::clearSavedPersonnelLogin,
                onChangeLoginTypeClick = { navigateToLoginSelection(clearLastLoginType = true) },
                onLoginClick = personnelLoginViewModel::login,
                onLoginSuccess = { target ->
                    val destination = when (target) {
                        PersonnelLoginTarget.PERSONNEL_HOME -> VefaDestination.PersonnelHome.route
                    }

                    navigateAfterSuccessfulLogin(
                        destination = destination,
                        lastUsedLoginType = LastLoginType.PERSONNEL,
                    )
                },
                onErrorShown = personnelLoginViewModel::clearError,
                onSuccessShown = personnelLoginViewModel::clearSuccess,
            )
        }

        composable(VefaDestination.ForcePasswordChange.route) {
            ForcePasswordChangeScreen(
                state = forcePasswordChangeViewModel.state,
                useFirebasePasswordChange = true,
                onNewPasswordChange = forcePasswordChangeViewModel::onNewPasswordChange,
                onConfirmPasswordChange = forcePasswordChangeViewModel::onConfirmPasswordChange,
                onChangePasswordClick = forcePasswordChangeViewModel::changePassword,
                onMockPasswordChangeClick = forcePasswordChangeViewModel::completeMockPasswordChange,
                onErrorShown = forcePasswordChangeViewModel::clearError,
                onSuccessShown = forcePasswordChangeViewModel::clearSuccess,
                onPasswordChanged = {
                    val destination = passwordChangeDestination
                        ?: when (loginViewModel.currentUser?.role) {
                            UserRole.MANAGER -> VefaDestination.ManagerHome.route
                            UserRole.PERSONNEL -> VefaDestination.PersonnelHome.route
                            null -> VefaDestination.LoginSelection.route
                        }
                    passwordChangeDestination = null
                    navController.navigate(destination) {
                        launchSingleTop = true
                        popUpTo(VefaDestination.ForcePasswordChange.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(VefaDestination.ManagerHome.route) {
            BackHandler(enabled = true) {}

            ManagerHomeScreen(
                displayName = managerLoginViewModel.currentUser?.fullName
                    ?: loginViewModel.currentUser?.displayName.orEmpty(),
                onHouseholdsClick = { navController.navigate(VefaDestination.Households.route) },
                onPersonnelClick = { navController.navigate(VefaDestination.PersonnelList.route) },
                onTasksClick = { navController.navigate(VefaDestination.Tasks.route) },
                onAnnouncementsClick = { navController.navigate(VefaDestination.Announcements.route) },
                onReportsClick = { navController.navigate(VefaDestination.Reports.route) },
                onSettingsClick = { navController.navigate(VefaDestination.Settings.route) },
                onTrashClick = { navController.navigate(VefaDestination.Trash.route) },
                onAuditLogsClick = { navController.navigate(VefaDestination.AuditLogs.route) },
            )

            LogoutConfirmDialog(
                visible = showManagerLogoutDialog,
                onDismiss = { showManagerLogoutDialog = false },
                onConfirm = {
                    showManagerLogoutDialog = false
                    managerLoginViewModel.logout()
                    navigateToLoginAfterLogout(LastLoginType.MANAGER)
                },
            )
        }

        composable(VefaDestination.PersonnelHome.route) {
            BackHandler(enabled = true) {}

            PersonnelHomeScreen(
                displayName = personnelLoginViewModel.currentUser?.fullName
                    ?: loginViewModel.currentUser?.displayName.orEmpty(),
                announcementState = announcementViewModel.state,
                onAnnouncementRead = announcementViewModel::markAsRead,
                onLogoutClick = { showPersonnelLogoutDialog = true },
            )

            LogoutConfirmDialog(
                visible = showPersonnelLogoutDialog,
                onDismiss = { showPersonnelLogoutDialog = false },
                onConfirm = {
                    showPersonnelLogoutDialog = false
                    personnelLoginViewModel.logout()
                    navigateToLoginAfterLogout(LastLoginType.PERSONNEL)
                },
            )
        }

        composable(VefaDestination.Households.route) {
            val householdOrganizationId = managerLoginViewModel.currentUser?.organizationId.orEmpty()
            val householdUserId = managerLoginViewModel.currentUser?.uid.orEmpty()
            LaunchedEffect(householdOrganizationId, householdUserId) {
                householdViewModel.setOrganizationContext(
                    organizationId = householdOrganizationId,
                    currentUserId = householdUserId,
                )
            }

            HouseholdsScreen(
                state = householdViewModel.state,
                onBackClick = navController::popBackStack,
                onNewHouseholdClick = { navController.navigate(VefaDestination.HouseholdForm.createRoute()) },
                onExcelImportClick = { navController.navigate(VefaDestination.ExcelImport.route) },
                onHouseholdClick = { householdId ->
                    navController.navigate(VefaDestination.HouseholdDetail.createRoute(householdId))
                },
                onEditClick = { householdId ->
                    navController.navigate(VefaDestination.HouseholdForm.createRoute(householdId))
                },
                onSearchQueryChange = householdViewModel::onSearchQueryChange,
                onDeleteClick = householdViewModel::deleteHousehold,
                onToggleActiveClick = householdViewModel::toggleActive,
                onMessageShown = householdViewModel::clearMessages,
            )
        }

        composable(VefaDestination.HouseholdDetail.route) { backStackEntry ->
            val householdOrganizationId = managerLoginViewModel.currentUser?.organizationId.orEmpty()
            val householdUserId = managerLoginViewModel.currentUser?.uid.orEmpty()
            LaunchedEffect(householdOrganizationId, householdUserId) {
                householdViewModel.setOrganizationContext(
                    organizationId = householdOrganizationId,
                    currentUserId = householdUserId,
                )
            }
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
            val householdOrganizationId = managerLoginViewModel.currentUser?.organizationId.orEmpty()
            val householdUserId = managerLoginViewModel.currentUser?.uid.orEmpty()
            LaunchedEffect(householdOrganizationId, householdUserId) {
                householdViewModel.setOrganizationContext(
                    organizationId = householdOrganizationId,
                    currentUserId = householdUserId,
                )
            }
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

        composable(VefaDestination.ExcelImport.route) {
            val context = LocalContext.current
            ExcelImportScreen(
                state = excelImportViewModel.state,
                onBackClick = {
                    householdViewModel.loadHouseholds()
                    navController.popBackStack()
                },
                onFileSelected = { uri ->
                    excelImportViewModel.onFileSelected(context = context, uri = uri)
                },
                onImportClick = excelImportViewModel::importPreviewItems,
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
                onLoad = reportsViewModel::loadReports,
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
                    passwordChangeDestination = VefaDestination.ManagerHome.route
                    navController.navigate(VefaDestination.ForcePasswordChange.route)
                },
                onLogoutClick = {
                    managerLoginViewModel.logout()
                    navigateToLoginAfterLogout(LastLoginType.MANAGER)
                },
            )
        }

        composable(VefaDestination.Trash.route) {
            TrashScreen(
                state = trashAuditViewModel.state,
                onBackClick = navController::popBackStack,
                onLoad = trashAuditViewModel::loadData,
                onRestoreClick = trashAuditViewModel::restoreItem,
                onPermanentDeleteClick = trashAuditViewModel::permanentlyDelete,
            )
        }

        composable(VefaDestination.AuditLogs.route) {
            AuditLogsScreen(
                state = trashAuditViewModel.state,
                onBackClick = navController::popBackStack,
                onLoad = trashAuditViewModel::loadData,
            )
        }
    }
}

@Composable
private fun LogoutConfirmDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (!visible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Çıkış yapılsın mı?")
        },
        text = {
            Text(text = "Oturumdan çıkmak istiyor musunuz?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Çıkış Yap")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "İptal")
            }
        },
    )
}
