package com.example.projet_parkour

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projet_parkour.ui.theme.Projet_ParkourTheme
import com.example.projet_parkour.view.CompetitionsPage
import com.example.projet_parkour.view.CreateCompetitionPage
import com.example.projet_parkour.view.CreateCompetitorPage
import com.example.projet_parkour.view.CreateCoursePage
import com.example.projet_parkour.view.CreateObstaclePage
import com.example.projet_parkour.view.DisplayCoursesCompetitors
import com.example.projet_parkour.view.utils.FloatingButtonAdd
import com.example.projet_parkour.view.utils.Header
import com.example.projet_parkour.view.InscriptionCompetitorsPage
import com.example.projet_parkour.view.ObstaclesPage
import com.example.projet_parkour.viewmodel.CompetitionsViewModel
import com.example.projet_parkour.viewmodel.CompetitorsViewModel
import com.example.projet_parkour.viewmodel.CoursesViewModel
import com.example.projet_parkour.viewmodel.ObstaclesViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val competitionsViewModel = ViewModelProvider(this)[CompetitionsViewModel::class.java]
        val coursesViewModel = ViewModelProvider(this)[CoursesViewModel::class.java]
        val competitorsViewModel = ViewModelProvider(this)[CompetitorsViewModel::class.java]
        val obstaclesViewModel = ViewModelProvider(this)[ObstaclesViewModel::class.java]

        setContent {
            val navController = rememberNavController()
            val isEnableConstructMode = remember { mutableStateOf(false) }
            val backStackEntry by navController.currentBackStackEntryAsState();
            val context = LocalContext.current

            Projet_ParkourTheme {
                Box(modifier = Modifier.padding(top = 40.dp, bottom = 40.dp).fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                    Column{
                        Header(modifier = Modifier, navController, isEnableConstructMode)
                        NavHost(navController = navController, startDestination = "competitions_page", builder = {
                            composable("competitions_page") {
                                CompetitionsPage(
                                    modifier = Modifier.padding(16.dp),
                                    competitionsViewModel,
                                    navController
                                )
                            }
                            composable("courses_competitors_page/{competitionId}", arguments = listOf(
                                navArgument("competitionId"){
                                    type = NavType.IntType
                                }
                            )) { backStackEntry ->
                                val competitionId = backStackEntry.arguments?.getInt("competitionId")
                                if (competitionId != null) {
                                    DisplayCoursesCompetitors(
                                        modifier = Modifier.padding(16.dp),
                                        coursesViewModel,
                                        competitorsViewModel,
                                        competitionId,
                                        navController,
                                        isEnableConstructMode
                                    )
                                }
                            }
                            composable("obstacles_page/{courseId}", arguments = listOf(
                                navArgument("courseId"){
                                    type = NavType.IntType
                                }
                            )) { backStackEntry ->
                                val courseId = backStackEntry.arguments?.getInt("courseId")
                                if (courseId != null) {
                                    ObstaclesPage(
                                        modifier = Modifier.padding(16.dp),
                                        obstaclesViewModel,
                                        courseId,
                                        navController,
                                        context = context,
                                        isEnableConstructMode
                                    )
                                }
                            }
                            composable("inscription_competitors_page/{competitionId}:{competitionAgeMin},{competitionAgeMax},{competitionGender}", arguments = listOf(
                                navArgument("competitionId"){
                                    type = NavType.IntType
                                },
                                navArgument("competitionAgeMin"){
                                    type = NavType.IntType
                                },
                                navArgument("competitionAgeMax"){
                                    type = NavType.IntType
                                },
                                navArgument("competitionGender"){
                                    type = NavType.StringType
                                },
                            )) { backStackEntry ->
                                val competitionId = backStackEntry.arguments?.getInt("competitionId")
                                val ageMin = backStackEntry.arguments?.getInt("competitionAgeMin")
                                val ageMax = backStackEntry.arguments?.getInt("competitionAgeMax")
                                val gender = backStackEntry.arguments?.getString("competitionGender")
                                if (competitionId != null && ageMin != null && ageMax != null && gender != null) {
                                    InscriptionCompetitorsPage(
                                        modifier = Modifier.padding(16.dp),
                                        competitorsViewModel, competitionId, ageMin, ageMax, gender,
                                        navController, context
                                    )
                                }
                            }


                            composable("create_competitions_page") {
                                CreateCompetitionPage(
                                    modifier = Modifier.padding(16.dp),
                                    competitionsViewModel,
                                    navController,
                                    context
                                )
                            }
                            composable("create_competitor_page/{competitionId}:{competitionAgeMin},{competitionAgeMax},{competitionGender}", arguments = listOf(
                                navArgument("competitionId"){
                                    type = NavType.IntType
                                },
                                navArgument("competitionAgeMin"){
                                    type = NavType.IntType
                                },
                                navArgument("competitionAgeMax"){
                                    type = NavType.IntType
                                },
                                navArgument("competitionGender"){
                                    type = NavType.StringType
                                },
                            )) { backStackEntry ->
                                val competitionId = backStackEntry.arguments?.getInt("competitionId")
                                val ageMin = backStackEntry.arguments?.getInt("competitionAgeMin")
                                val ageMax = backStackEntry.arguments?.getInt("competitionAgeMax")
                                val gender = backStackEntry.arguments?.getString("competitionGender")
                                if (competitionId != null && ageMin != null && ageMax != null && gender != null) {
                                    CreateCompetitorPage(
                                        modifier = Modifier.padding(16.dp),
                                        competitorsViewModel,
                                        navController,
                                        context, competitionId, ageMin, ageMax, gender
                                    )
                                }
                            }
                            composable("create_course_page/{competitionId}", arguments = listOf(
                                navArgument("competitionId"){
                                    type = NavType.IntType
                                }
                            )) { backStackEntry ->
                                val competitionId = backStackEntry.arguments?.getInt("competitionId")
                                if (competitionId != null) {
                                    CreateCoursePage(
                                        modifier = Modifier.padding(16.dp),
                                        coursesViewModel,
                                        competitionId,
                                        navController,
                                        context
                                    )
                                }
                            }
                            composable("create_obstacle_page/{courseId}", arguments = listOf(
                                navArgument("courseId"){
                                    type = NavType.IntType
                                }
                            )) { backStackEntry ->
                                val courseId = backStackEntry.arguments?.getInt("courseId")
                                if (courseId != null) {
                                    CreateObstaclePage(
                                        modifier = Modifier.padding(16.dp),
                                        obstaclesViewModel,
                                        navController = navController,
                                        courseId,
                                        context
                                    )
                                }
                            }
                        })
                    }
                    if(isEnableConstructMode.value){
                        when(val currentRoute = backStackEntry?.destination?.route){
                            "competitions_page" -> FloatingButtonAdd(modifier = Modifier.padding(bottom = 40.dp, end = 30.dp).align(Alignment.BottomEnd),
                                                    route = "create_competitions_page", navController);
                            null -> {}
                        }
                    }
                }
            }
        }
    }
}

