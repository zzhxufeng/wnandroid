package name.zzhxufeng.wanandroid.ui.screens.drawer

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import name.zzhxufeng.wanandroid.ui.composables.WanWebView
import name.zzhxufeng.wanandroid.ui.screens.NavControllerNav
import name.zzhxufeng.wanandroid.ui.screens.WanScreen
import name.zzhxufeng.wanandroid.ui.model.DrawerItem
import name.zzhxufeng.wanandroid.ui.screens.drawer.items.Coins
import name.zzhxufeng.wanandroid.viewmodel.drawer.CoinViewModel
import name.zzhxufeng.wanandroid.viewmodel.drawer.DrawerViewModel

@Composable
fun DrawerNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavControllerNav.DRAWER_NAV
    ) {
        drawerGraph(navController)
    }
}

private fun navigate(navController: NavHostController, route: String) {
    navController.navigate(route)
}

fun NavGraphBuilder.drawerGraph(navController: NavHostController) {
    navigation(
        route = NavControllerNav.DRAWER_NAV,
        startDestination = DrawerItem.MAIN.route
    ) {
        composable(route = DrawerItem.MAIN.route) {
            val drawerViewModel: DrawerViewModel = viewModel()
            WanDrawer(
                uiState = drawerViewModel.uiState.collectAsState().value,
                handleEvent = drawerViewModel::handleEvent,
                onDrawerItemNavigate = { drawerItem ->
                    when (drawerItem) {
                        DrawerItem.DARK_MODE -> { /*TODO handleEvent */ }
                        DrawerItem.LOGOUT -> { /*TODO handleEvent */ }
                        DrawerItem.MAIN -> { /*This won't be clicked, MAIN not shown.*/ }
                        else -> { navigate(navController, drawerItem.route) }
                    }
                }
            )
        }

        composable(route = DrawerItem.COINS.route) {
            val coinViewModel: CoinViewModel = viewModel()
            Coins(
                uiState = coinViewModel.uiState.collectAsState().value,
                handleEvent = coinViewModel::handleEvent,
                navigateBack = { navController.popBackStack() },
                navigateToCheckInRecord = { navController.navigate("check_in_records") },
                navigateToHelp = { navController.navigate(WanScreen.Web.createRoute(
                    "https://www.wanandroid.com/blog/show/2653"
                )) },
            )
        }

        composable(route = DrawerItem.BOOKMARKS.route) {
            Text(text = "COINS TODO")
        }

        composable(route = DrawerItem.SHARE.route) {
            Text(text = "COINS TODO")
        }

        composable(route = DrawerItem.TODO.route) {
            Text(text = "COINS TODO")
        }

        composable(route = DrawerItem.DARK_MODE.route) {
            Text(text = "COINS TODO")
        }

        composable(route = DrawerItem.SETTINGS.route) {
            Text(text = "COINS TODO")
        }

        composable(route = DrawerItem.LOGOUT.route) {
            Text(text = "COINS TODO")
        }

        composable(route = "check_in_records") {
            Text(text = "check_in_records")
        }

        composable(
            route = WanScreen.Web.route,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val encodedUrl = navBackStackEntry.arguments?.getString("url")
            if (encodedUrl != null) {
                WanWebView(url = WanScreen.Web.parseUrl(encodedUrl))
            } else {
                Text(text = "不该发生的情况")
            }
        }
    }
}
