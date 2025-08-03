package com.raj.morningherald.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.raj.morningherald.core.util.ValidationUtil
import com.raj.morningherald.domain.model.Article
import com.raj.morningherald.domain.model.Source
import com.raj.morningherald.presentation.base.ArticleScreen
import com.raj.morningherald.presentation.browse.BrowseScreen
import com.raj.morningherald.presentation.newslist.NewsListPagingScreen
import com.raj.morningherald.presentation.newslist.NewsListScreen
import com.raj.morningherald.presentation.newssource.NewsSourceScreen
import com.raj.morningherald.presentation.newssource.SourceViewModel
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
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.LightGray
                )
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
            val sourceCode = it.arguments?.getString("source")
            if (ValidationUtil.checkIfValidArgNews(sourceCode)) {
                NewsListScreen { article ->
                    navigateToArticleScreen(article, navController)
                }
            } else {
                NewsListPagingScreen { article ->
                    navigateToArticleScreen(article, navController)
                }
            }
        }
        composable(route = Routes.News.route) {
            NewsSourceRoute(
                onSourceClick = { source ->
                    source.id?.let { sourceId -> navigateToSourceScreen(sourceId, navController) }
                }
            )
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

@Composable
fun NewsSourceRoute(
    onSourceClick: (Source) -> Unit
) {
    val viewModel: SourceViewModel = hiltViewModel()
    val uiState by viewModel.newsSource.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getNewsSource()
    }

    NewsSourceScreen(
        uiState = uiState,
        onRetry = { viewModel.getNewsSource() },
        onSourceClick = onSourceClick
    )
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
