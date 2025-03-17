package com.example.projet_parkour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projet_parkour.ui.theme.Projet_ParkourTheme
import com.example.projet_parkour.view.CompetitionsPage
import com.example.projet_parkour.view.CoursesPage
import com.example.projet_parkour.view.Header
import com.example.projet_parkour.viewmodel.CompetitionsViewModel
import com.example.projet_parkour.viewmodel.CoursesViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val competitionsViewModel = ViewModelProvider(this)[CompetitionsViewModel::class.java]
        val coursesViewModel = ViewModelProvider(this)[CoursesViewModel::class.java]

        setContent {
            val navController = rememberNavController()
            Projet_ParkourTheme {
                Scaffold( modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Header(modifier = Modifier, navController)
                        NavHost(navController = navController, startDestination = "competitions_page", builder = {
                            composable("competitions_page") {
                                CompetitionsPage(
                                    modifier = Modifier.padding(innerPadding),
                                    competitionsViewModel,
                                    navController
                                )
                            }
                            composable("courses_page/{competitionId}", arguments = listOf(
                                navArgument("competitionId"){
                                    type = NavType.IntType
                                }
                            )) { backStackEntry ->
                                val competitionId = backStackEntry.arguments?.getInt("competitionId")
                                CoursesPage(
                                    modifier = Modifier.padding(innerPadding),
                                    coursesViewModel,
                                    competitionId
                                )
                            }
                        })
                    }
                }
            }
        }
    }
}

