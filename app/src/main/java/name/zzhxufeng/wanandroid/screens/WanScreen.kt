package name.zzhxufeng.wanandroid.screens

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class WanScreen (
    val route: String,
    val type: ScreenType,
    val icon: ImageVector
) {

    /*main*/
    object Home: WanScreen (
        route = "Home",
        type = ScreenType.direct,
        icon = Icons.Filled.Home
    )
    object Public: WanScreen (
        route = "Public",
        type = ScreenType.direct,
        icon = Icons.Filled.Place
    )
    object Posts: WanScreen (
        route = "Posts",
        type = ScreenType.direct,
        icon = Icons.Filled.Notifications
    )
    object Path: WanScreen (
        route = "Path",
        type = ScreenType.direct,
        icon = Icons.Filled.Person
    )
    object Projects: WanScreen (
        route = "Projects",
        type = ScreenType.direct,
        icon = Icons.Filled.Face
    )

    /*deep*/
    object Search: WanScreen(
        route = "Search",
        type = ScreenType.deep,
        icon = Icons.Filled.Search
    )

    object Web: WanScreen(
        route = "Web/{url}",
        type = ScreenType.deep,
        icon = Icons.Filled.Web
    ){
        fun createRoute(url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            Log.d("Web", "Web/$encodedUrl")
            return "Web/${encodedUrl}"
        }
        fun parseUrl(encodedUrl: String): String {
            Log.d("Web", URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString()))
            return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
        }
    }

    /*静态方法*/
    companion object {
        fun fromRouteToScreen(route: String?): WanScreen =
            when (route?.substringBefore("/")) {
                /*direct compose*/
                Home.route -> Home
                Public.route -> Public
                Posts.route -> Posts
                Path.route -> Path
                Projects.route -> Projects
                /*deep compose*/
                Search.route -> Search
                Web.route -> Web
                null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}

enum class ScreenType {
    direct,
    deep
}