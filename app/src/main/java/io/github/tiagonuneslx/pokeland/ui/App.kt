package io.github.tiagonuneslx.pokeland.ui

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import io.github.tiagonuneslx.pokeland.ui.screen.SharedViewModel
import io.github.tiagonuneslx.pokeland.ui.screen.details.DetailsScreen
import io.github.tiagonuneslx.pokeland.ui.screen.list.ListScreen
import io.github.tiagonuneslx.pokeland.ui.theme.PokelandTheme

@Composable
fun ComponentActivity.App() {
    val navController = rememberNavController()
    PokelandTheme {
        val isDarkMode = isSystemInDarkTheme()
        val surfaceColor = MaterialTheme.colors.surface
        LaunchedEffect(isDarkMode, surfaceColor) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT,
                ) { isDarkMode },
                navigationBarStyle = SystemBarStyle.auto(
                    surfaceColor.toArgb(),
                    surfaceColor.toArgb(),
                ) { isDarkMode }
            )
        }
        Surface(Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = "pokeland",
            ) {
                navigation(startDestination = "list", route = "pokeland") {
                    composable(
                        "list",
                    ) { backStackEntry ->
                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry("pokeland")
                        }
                        val sharedViewModel = hiltViewModel<SharedViewModel>(parentEntry)
                        ListScreen(navController, sharedViewModel)
                    }
                    composable(
                        "details",
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Up,
                                animationSpec = tween(400)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                animationSpec = tween(200)
                            )
                        },
                    ) { backStackEntry ->
                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry("pokeland")
                        }
                        val sharedViewModel = hiltViewModel<SharedViewModel>(parentEntry)
                        DetailsScreen(navController, sharedViewModel)
                    }
                }
            }
        }
    }
}
