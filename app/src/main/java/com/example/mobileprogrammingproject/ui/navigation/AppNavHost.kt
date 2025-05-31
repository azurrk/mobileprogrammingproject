package com.example.mobileprogrammingproject.ui.navigation

import AddTransactionScreen
import LoginScreen
import TransactionScreen
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
import com.example.mobileprogrammingproject.ui.viewmodel.UserViewModel
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mobileprogrammingproject.ui.screens.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object Auth

@Serializable
object Login

@Serializable
object Main
@Serializable
object Home
@Serializable
object Transaction
@Serializable
object AddTransaction


@Composable
fun AppNavHost(userViewModel: UserViewModel = hiltViewModel()) {
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

    Scaffold { padding ->
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
                        }
                    )
                }
            }

            navigation<Main>(startDestination = Home) {
                composable<Home> {
                    HomeScreen(
                        onNavigateToTransactions = { navController.navigate(Transaction) },
                        onNavigateToAddTransaction = { navController.navigate(AddTransaction) }
                    )
                }

                composable<Transaction> {
                    TransactionScreen()
                }

                composable<AddTransaction> {
                    AddTransactionScreen()
                }
            }
        }
    }
}
