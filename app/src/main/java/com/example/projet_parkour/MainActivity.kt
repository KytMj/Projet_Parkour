package com.example.projet_parkour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.projet_parkour.bdd.AppDatabase
import com.example.projet_parkour.ui.theme.Pink40
import com.example.projet_parkour.ui.theme.Projet_ParkourTheme
import com.example.projet_parkour.view.ArbitragePage
import com.example.projet_parkour.viewmodel.CompetitionsViewModel
import com.example.projet_parkour.viewmodel.CompetitorsViewModel
import com.example.projet_parkour.viewmodel.CoursesViewModel
import com.example.projet_parkour.viewmodel.CourseObstacleViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppDatabase.getInstance(this)
        val competitionsViewModel = ViewModelProvider(this)[CompetitionsViewModel::class.java]
        val coursesViewModel = ViewModelProvider(this)[CoursesViewModel::class.java]
        val competitorsViewModel = ViewModelProvider(this)[CompetitorsViewModel::class.java]
        val courseObstacleViewModel = ViewModelProvider(this)[CourseObstacleViewModel::class.java]
        this.deleteDatabase("database");
        setContent {
            Projet_ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        //Header(modifier = Modifier)
                        ArbitragePage(competitionsViewModel, coursesViewModel, competitorsViewModel, courseObstacleViewModel)
                    }
                }
            }
        }
    }
            /*
                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    enableEdgeToEdge()

                    val competitionsViewModel = ViewModelProvider(this)[CompetitionsViewModel::class.java]
                    val coursesViewModel = ViewModelProvider(this)[CoursesViewModel::class.java]
                    val competitorsViewModel = ViewModelProvider(this)[CompetitorsViewModel::class.java]

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
                                            if (competitionId != null) {
                                                CoursesPage(
                                                    modifier = Modifier.padding(16.dp),
                                                    coursesViewModel,
                                                    competitionId
                                                )
                                            }
                                        }
                                        composable("inscription_concurrents_page/{competitionId}", arguments = listOf(
                                            navArgument("competitionId"){
                                                type = NavType.IntType
                                            }
                                        )) { backStackEntry ->
                                            val competitionId = backStackEntry.arguments?.getInt("competitionId")
                                            if (competitionId != null) {
                                                InscriptionConcurrentsPage(
                                                    modifier = Modifier.padding(16.dp),
                                                    competitorsViewModel,
                                                    competitionId
                                                )
                                            }
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

        }
*/

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

