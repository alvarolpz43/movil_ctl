package com.example.movil_ctl.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movil_ctl.compoose.EndScreenForm
import com.example.movil_ctl.compoose.EquipoScreen
import com.example.movil_ctl.compoose.FirstScreenForm
import com.example.movil_ctl.compoose.HomeScreen
import com.example.movil_ctl.compoose.components.DynamicFormScreen

// AppNavigation.kt
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "contratistas"
    ) {
        composable("contratistas") {
            HomeScreen(navController = navController)
        }

        composable(
            "equipos/{contratistaId}",
            arguments = listOf(navArgument("contratistaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contratistaId = backStackEntry.arguments?.getString("contratistaId")!!
            EquipoScreen(
                contratistaId = contratistaId,
                navController = navController
            )

        }
        composable(
            "formSDos/{tipoEquipo}/{contratistaId}/{equipoId}",
            arguments = listOf(
                navArgument("tipoEquipo") { type = NavType.StringType },
                navArgument("contratistaId") { type = NavType.StringType },
                navArgument("equipoId") { type = NavType.StringType }
            )
        ){
            backStackEntry ->
            val tipoEquipo = backStackEntry.arguments?.getString("tipoEquipo")!!
            val contratistaId = backStackEntry.arguments?.getString("contratistaId")!!
            val equipoId = backStackEntry.arguments?.getString("equipoId")!!

            DynamicFormScreen(
                tipoEquipo = tipoEquipo,
                contratistaId = contratistaId,
                equipoId = equipoId,
                navController = navController
            )
        }

        composable(
            "formSTres/{tipoEquipo}",
            arguments = listOf(
                navArgument("tipoEquipo") { type = NavType.StringType },

            )
        ){
            backStackEntry ->
            val tipoEquipo = backStackEntry.arguments?.getString("tipoEquipo")!!


            EndScreenForm(
                tipoEquipo = tipoEquipo,
                navController = navController
            )
        }

        composable(
            "formSUno/{tipoEquipo}/{contratistaId}/{equipoId}",
            arguments = listOf(
                navArgument("tipoEquipo") { type = NavType.StringType },
                navArgument("contratistaId") { type = NavType.StringType },
                navArgument("equipoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val tipoEquipo = backStackEntry.arguments?.getString("tipoEquipo")!!
            val contratistaId = backStackEntry.arguments?.getString("contratistaId")!!
            val equipoId = backStackEntry.arguments?.getString("equipoId")!!

            FirstScreenForm(
                tipoEquipo = tipoEquipo,
                contratistaId = contratistaId,
                equipoId = equipoId,
                navController = navController
            )
        }

    }
}