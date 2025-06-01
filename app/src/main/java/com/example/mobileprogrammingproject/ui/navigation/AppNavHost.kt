package com.example.mobileprogrammingproject.ui.navigation

import AddTransactionScreen
import com.example.mobileprogrammingproject.ui.screens.components.BottomNavigationBar
import LoginScreen
import HomeScreen
import TransactionScreen
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController

import com.example.mobileprogrammingproject.ui.screens.RegisterScreen
import com.example.mobileprogrammingproject.ui.viewmodel.UserViewModel
import kotlinx.serialization.Serializable
import com.example.mobileprogrammingproject.ui.screens.components.shouldShowBottomBar
import com.example.mobileprogrammingproject.ui.viewmodel.TransactionViewModel

@Serializable
object Auth

@Serializable
object Login

@Serializable
object Register

@Serializable
object Main

@Serializable
object Home

@Serializable
object Transaction

@Serializable
object AddTransaction

@Serializable
object Analytics

@Serializable
object Profile

@Composable
fun AppNavHost(userViewModel: UserViewModel = hiltViewModel(), transactionViewModel: TransactionViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isLoading by userViewModel.isLoading.collectAsState()
    val loggedUser by userViewModel.loggedUser.collectAsState()
    val startDestination: Any = Auth

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }


    LaunchedEffect(loggedUser) {
        Log.d("Navigation", "Current route: $currentRoute")
        if (loggedUser == null) {
            navController.navigate(Login) {
                popUpTo(Main) { inclusive = true }
                launchSingleTop = true
            }
        } else {
            userViewModel.setLoadingFalse()
            navController.navigate(Home) {
                popUpTo(Auth) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(currentRoute)) {
                BottomNavigationBar(
                    currentRoute = when (currentRoute) {
                        "com.example.mobileprogrammingproject.ui.navigation.Home" -> "home"
                        "com.example.mobileprogrammingproject.ui.navigation.Transaction" -> "transactions"
                        else -> null
                    },
                    onItemClick = { route ->
                        when (route) {
                            "home" -> navController.navigate(Home) {
                                popUpTo(Home) { inclusive = true }
                                launchSingleTop = true
                            }
                            "transactions" -> navController.navigate(Transaction) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ) {
            navigation<Auth>(startDestination = Login) {
                composable<Login> {
                    LoginScreen(
                        userViewModel = userViewModel,
                        onSuccessLogin = {
                            navController.navigate(Home) {
                                popUpTo(Auth) { inclusive = true }
                            }
                        },
                        onRegisterNav = {
                            navController.navigate(Register)
                        }
                    )
                }

                composable<Register> {
                    RegisterScreen(
                        userViewModel = userViewModel,
                        onLoginNav = {
                            navController.navigate(Login)
                        }
                    )
                }
            }

            navigation<Main>(startDestination = Home) {
                composable<Home> {
                    HomeScreen(
                        transactionViewModel = transactionViewModel,
                        userViewModel = userViewModel,
                        onNavigateToTransactions = {
                            navController.navigate(Transaction)
                        },
                        onAddTransaction = {
                            navController.navigate(AddTransaction)
                        },
                        onLogout = {
                            userViewModel.logout()
                        }
                    )
                }

                composable<Transaction> {
                    TransactionScreen(
                        userViewModel = userViewModel,
                        transactionViewModel,
                        onAddTransaction = {
                            navController.navigate(AddTransaction)
                        }
                    )
                }

                composable<AddTransaction> {
                    AddTransactionScreen(
                        userViewModel = userViewModel,
                        transactionViewModel = transactionViewModel,
                        onSave = {
                            navController.navigate(Transaction) {
                                popUpTo(Transaction) { inclusive = true }
                            }
                        },
                        onCancel = {
                            navController.popBackStack()
                        }
                    )
                }

            }
        }
    }
}