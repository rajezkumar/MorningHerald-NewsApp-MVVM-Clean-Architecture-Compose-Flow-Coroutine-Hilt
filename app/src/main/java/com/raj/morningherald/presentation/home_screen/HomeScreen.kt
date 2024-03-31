package com.raj.morningherald.presentation.home_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.raj.morningherald.R
import com.raj.morningherald.presentation.base.NavigationUtil.navigateSingleTopTo
import com.raj.morningherald.presentation.base.Route
import com.raj.morningherald.presentation.base.bottomBarScreens

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen =
        bottomBarScreens.find { it.route == currentDestination?.route } ?: Route.Home
    Scaffold(
        topBar = {
            NewsTopBar {
                navController.popBackStack()
            }
        },
        bottomBar = {
            NewsBottomNavigation(
                currentScreen = currentScreen
            ) {
                navigateSingleTopTo(it.route, navController)
            }
        }
    ) {
        NewsNavHost(
            modifier = Modifier.padding(it),
            navController = navController
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar(onBackClicked: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        navigationIcon = {
             IconButton(onClick = { onBackClicked() }) {
                 Icon(
                     imageVector = Icons.Filled.ArrowBack,
                     contentDescription = null
                 )
             }
        }
    )
}

@Composable
fun NewsBottomNavigation(
    currentScreen: Route,
    onIconSelected: (Route) -> Unit
) {
    NavigationBar {
        bottomBarScreens.forEach { screen ->
            NavigationBarItem(
                selected = screen == currentScreen,
                label = {
                    Text(text = stringResource(id = screen.resourceId))
                },
                icon = {
                    Icon(painter = painterResource(id = screen.icon), null)
                },
                onClick = {
                    onIconSelected.invoke(screen)
                }
            )
        }
    }
}


@Composable
private fun NewsNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.route,
        modifier = modifier
    ) {
        composable(route = Route.Home.route) {
            NewsScreen()
        }
        composable(route = Route.News.route) {
            SourceScreen()
        }

        composable(route = Route.Browse.route) {
            BrowseScreen()
        }

    }
}
