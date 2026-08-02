package com.ecotec.floramedica.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ecotec.floramedica.data.repository.ContentRepository
import com.ecotec.floramedica.ui.screens.autores.AutoresScreen
import com.ecotec.floramedica.ui.screens.especies.EspecieDetailScreen
import com.ecotec.floramedica.ui.screens.especies.EspeciesListScreen
import com.ecotec.floramedica.ui.screens.etnobotanica.EtnobotanicaDetailScreen
import com.ecotec.floramedica.ui.screens.etnobotanica.EtnobotanicaScreen
import com.ecotec.floramedica.ui.screens.home.HomeScreen

@Composable
fun EcotecNavGraph(
    navController: NavHostController,
    repository: ContentRepository,
) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                repository = repository,
                onVerEspecies = { navController.navigate(Routes.ESPECIES) },
                onVerEtnobotanica = { navController.navigate(Routes.ETNOBOTANICA) },
                onEspecieClick = { slug -> navController.navigate(Routes.especieDetail(slug)) },
            )
        }
        composable(Routes.ESPECIES) {
            EspeciesListScreen(
                repository = repository,
                onEspecieClick = { slug -> navController.navigate(Routes.especieDetail(slug)) },
            )
        }
        composable(
            route = Routes.ESPECIE_DETAIL,
            arguments = listOf(navArgument("slug") { }),
        ) { backStackEntry ->
            val slug = backStackEntry.arguments?.getString("slug").orEmpty()
            EspecieDetailScreen(
                slug = slug,
                repository = repository,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.ETNOBOTANICA) {
            EtnobotanicaScreen(
                repository = repository,
                onFichaClick = { slug -> navController.navigate(Routes.etnobotanicaDetail(slug)) },
            )
        }
        composable(
            route = Routes.ETNOBOTANICA_DETAIL,
            arguments = listOf(navArgument("slug") { }),
        ) { backStackEntry ->
            val slug = backStackEntry.arguments?.getString("slug").orEmpty()
            EtnobotanicaDetailScreen(
                slug = slug,
                repository = repository,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.AUTORES) {
            AutoresScreen()
        }
    }
}
