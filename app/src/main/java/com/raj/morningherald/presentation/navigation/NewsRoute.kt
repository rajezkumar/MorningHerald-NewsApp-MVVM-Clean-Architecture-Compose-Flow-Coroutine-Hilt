package com.raj.morningherald.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.raj.morningherald.R


sealed class Routes(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val icon: Int,
    val routeWithoutArgs: String = route
) {
    object Home : Routes("home/{source}", R.string.home, R.drawable.home, "home")
    object News : Routes("news", R.string.news, R.drawable.ic_news)
    object Browse : Routes("browse", R.string.browse, R.drawable.browse)
    object NewsArticle :
        Routes("newsArticle/{article}", R.string.news, R.drawable.ic_news, "newsArticle")
}

val bottomBarScreens = listOf(
    Routes.Home, Routes.News, Routes.Browse
)