package com.raj.morningherald.presentation.base

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.raj.morningherald.R


sealed class Route(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val icon: Int
) {
    data object Home : Route("home", R.string.home, R.drawable.home)
    data object News : Route("news", R.string.news, R.drawable.ic_news)
    data object Browse : Route("browse", R.string.browse, R.drawable.browse)
}

val bottomBarScreens = listOf(
    Route.Home,
    Route.News,
    Route.Browse
)