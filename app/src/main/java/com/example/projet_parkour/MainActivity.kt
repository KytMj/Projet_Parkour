package com.example.projet_parkour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projet_parkour.ui.theme.Pink40
import com.example.projet_parkour.ui.theme.Projet_ParkourTheme
import com.example.projet_parkour.view.CompetitionsPage
import com.example.projet_parkour.view.CreateCompetitionPage
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
            val isEnable = remember { mutableStateOf(false) }
            val backStackEntry by navController.currentBackStackEntryAsState();

            Projet_ParkourTheme {
                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                    Column {
                        Header(modifier = Modifier, navController, isEnable)
                        NavHost(navController = navController, startDestination = "competitions_page", builder = {
                            composable("competitions_page") {
                                CompetitionsPage(
                                    modifier = Modifier.padding(16.dp),
                                    competitionsViewModel,
                                    navController
                                )
                            }
                            composable("create_competitions_page") {
                                CreateCompetitionPage(
                                    modifier = Modifier.padding(16.dp),
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
                                    modifier = Modifier.padding(16.dp),
                                    coursesViewModel,
                                    competitionId
                                )
                            }
                        })
                    }
                    if(isEnable.value){
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

@Composable
fun FloatingButtonAdd(modifier: Modifier, route : String, navController: NavController){
    val context = LocalContext.current
    FloatingActionButton(
        modifier = modifier,
        onClick = {
            navController.navigate(route)
        },
        containerColor = Pink40,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation()
    ) {
        // adding icon for button.
        Icon(Icons.Filled.Add, "Ajouter")
    }
}

