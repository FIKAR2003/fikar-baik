package com.example.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.ChildFormScreen
import com.example.ui.screens.ChildListScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EducationDetailScreen
import com.example.ui.screens.EducationScreen
import com.example.ui.screens.KmsChartScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WeightFormScreen
import com.example.ui.screens.WeightHistoryScreen
import com.example.ui.theme.KmsPink
import com.example.viewmodel.KmsViewModel

@Composable
fun KmsApp(viewModel: KmsViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Verify parent's authorization session status
    val parentSession by viewModel.currentParent.collectAsState()

    // Determine if we should show bottom navigation
    val bottomNavTabList = listOf("dashboard", "anak", "riwayat", "edukasi", "tentang")
    val showBottomBar = currentRoute in bottomNavTabList

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    modifier = Modifier.testTag("app_navigation_bar")
                ) {
                    val tabs = listOf(
                        Triple("dashboard", "Home", Icons.Default.Home),
                        Triple("anak", "Anak", Icons.Default.Face),
                        Triple("riwayat", "Riwayat", Icons.Default.Assignment),
                        Triple("edukasi", "Edukasi", Icons.Default.MenuBook),
                        Triple("tentang", "Tentang", Icons.Default.Info)
                    )

                    tabs.forEach { (route, label, icon) ->
                        val isSelected = currentRoute == route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo("dashboard") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) KmsPink else Color.Gray
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    color = if (isSelected) KmsPink else Color.Gray
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color(0xFFFFEBEE)
                            ),
                            modifier = Modifier.testTag("nav_item_$route")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Splash Screen Route
            composable("splash") {
                SplashScreen(
                    onGetStarted = {
                        if (parentSession != null) {
                            navController.navigate("dashboard") {
                                popUpTo("splash") { inclusive = true }
                            }
                        } else {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }
                )
            }

            // Login Route
            composable("login") {
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToRegister = {
                        navController.navigate("register")
                    },
                    onLoginSuccess = {
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            // Registration Route
            composable("register") {
                RegisterScreen(
                    viewModel = viewModel,
                    onNavigateToLogin = {
                        navController.navigate("login")
                    },
                    onRegisterSuccess = {
                        navController.navigate("dashboard") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }

            // Dashboard Home Route
            composable("dashboard") {
                DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToTab = { route ->
                        navController.navigate(route) {
                            popUpTo("dashboard") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToWeightForm = {
                        navController.navigate("weight_form")
                    },
                    onNavigateToKmsChart = {
                        navController.navigate("kms_chart")
                    },
                    onNavigateToChildForm = {
                        navController.navigate("child_form")
                    },
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo("dashboard") { inclusive = true }
                        }
                    }
                )
            }

            // Child List Route
            composable("anak") {
                ChildListScreen(
                    viewModel = viewModel,
                    onNavigateToChildForm = {
                        navController.navigate("child_form")
                    }
                )
            }

            // Weight History Route
            composable("riwayat") {
                WeightHistoryScreen(
                    viewModel = viewModel
                )
            }

            // Education List Route
            composable("edukasi") {
                EducationScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = {
                        navController.navigate("education_detail")
                    }
                )
            }

            // About App Route
            composable("tentang") {
                AboutScreen()
            }

            // Secondary Form: Add Child profile
            composable("child_form") {
                ChildFormScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Secondary Form: Input Weight entry
            composable("weight_form") {
                WeightFormScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Secondary View: Interactive KMS Graph Canvas
            composable("kms_chart") {
                KmsChartScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Secondary View: Education Article Detail readout
            composable("education_detail") {
                EducationDetailScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
