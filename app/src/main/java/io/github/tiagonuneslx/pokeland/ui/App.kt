package io.github.tiagonuneslx.pokeland.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.github.tiagonuneslx.pokeland.ui.screen.SharedViewModel
import io.github.tiagonuneslx.pokeland.ui.screen.details.DetailsScreen
import io.github.tiagonuneslx.pokeland.ui.screen.list.ListScreen
import io.github.tiagonuneslx.pokeland.ui.theme.PokelandTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {
    val navController = rememberAnimatedNavController()
    PokelandTheme {
        val systemUiController = rememberSystemUiController()
        val isDarkMode = isSystemInDarkTheme()
        val surfaceColor = MaterialTheme.colors.surface
        LaunchedEffect(systemUiController, isDarkMode, surfaceColor) {
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = !isDarkMode
            )
            systemUiController.setNavigationBarColor(
                color = surfaceColor,
                darkIcons = !isDarkMode
            )
        }
        Surface(Modifier.fillMaxSize()) {
            AnimatedNavHost(
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
                                AnimatedContentScope.SlideDirection.Up,
                                animationSpec = tween(400)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Down,
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
