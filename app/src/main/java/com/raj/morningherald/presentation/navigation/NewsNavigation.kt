package com.raj.morningherald.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.raj.morningherald.data.model.Article
import com.raj.morningherald.presentation.base.ArticleScreen
import com.raj.morningherald.presentation.browse.BrowseScreen
import com.raj.morningherald.presentation.newslist.NewsListPagingScreen
import com.raj.morningherald.presentation.newssource.NewsSourceScreen
import java.net.URLEncoder

@Composable
fun NewsNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen =
        bottomBarScreens.find { it.route == currentDestination?.route } ?: Routes.Home
    Scaffold(
        topBar = { NewsTopBar() },
        bottomBar = {
            NewsBottomNavigation(currentScreen = currentScreen) {
                navigateSingleTopTo(it.route, navController)
            }
        }
    ) {
        NewsNavHost(modifier = Modifier.padding(it), navController = navController)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
        ),
        title = {
            Text("Morning Herald")
        },
    )
}

@Composable
fun NewsBottomNavigation(
    currentScreen: Routes,
    onIconSelected: (Routes) -> Unit
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
                },
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
        composable(
            route = Routes.Home.route,
            arguments = listOf(navArgument("source") { type = NavType.StringType })
        ) {
            NewsListPagingScreen { article ->
                navigateToArticleScreen(article, navController)
            }
        }
        composable(route = Routes.News.route) {
            NewsSourceScreen {
                if (it.id != null) {
                    navigateToSourceScreen(it.id, navController)
                }
            }
        }
        composable(route = Routes.Browse.route) {
            BrowseScreen(
                backPressed = {
                    navigateSingleTopTo(Routes.Home.route, navController)
                }
            ) { article ->
                navigateToArticleScreen(article, navController)
            }
        }
        composable(
            route = Routes.NewsArticle.route,
            arguments = listOf(navArgument("article") { type = NavType.StringType })
        ) {
            val articleJson = it.arguments?.getString("article")
            val gson = Gson()
            val article = gson.fromJson(articleJson, Article::class.java)
            ArticleScreen(article)
        }
    }
}

private fun navigateToArticleScreen(
    it: Article,
    navController: NavHostController
) {
    val articleJsonString = Gson().toJson(it)
    val encodedArticle = URLEncoder.encode(articleJsonString, Charsets.UTF_8.name())
    navController.navigate(Routes.NewsArticle.routeWithoutArgs + "/${encodedArticle}") {
        launchSingleTop = true
    }
}

private fun navigateSingleTopTo(
    route: String,
    navController: NavHostController
) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id)
        launchSingleTop = true
    }
}

private fun navigateToSourceScreen(
    sourceId: String,
    navController: NavHostController
) {
    navController.navigate(Routes.Home.routeWithoutArgs + "/${sourceId}") {
        launchSingleTop = true
    }
}