package com.example.projet_parkour.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.ui.theme.Pink40
import com.example.projet_parkour.viewmodel.CompetitorsViewModel
import com.example.projet_parkour.viewmodel.CoursesViewModel

@Composable
fun DisplayCoursesCompetitors(
    modifier: Modifier,
    coursesViewModel: CoursesViewModel,
    competitorsViewModel: CompetitorsViewModel,
    competitionId: Int,
    navController: NavController
){
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(modifier = Modifier.fillMaxSize()) {
            CoursesPage(modifier, coursesViewModel, competitionId)
            CompetitorsPage(modifier, competitorsViewModel, competitionId)
        }
        FloatingActionButton(
            modifier = Modifier.padding(bottom = 50.dp).align(Alignment.BottomCenter),
            onClick = {
                //TODO chrono
                //navController.navigate("")
            },
            containerColor = Pink40,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation()
        ) {
            Text(text = "Accéder au chrono", modifier = Modifier.padding(start = 20.dp, end = 20.dp))
        }
    }
}

@Composable
fun CoursesPage(
    modifier: Modifier,
    viewModel: CoursesViewModel,
    competitionId: Int
){
    val coursesResult = viewModel.coursesResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getCoursesByCompetitionId(competitionId)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Courses de la compétition n°${competitionId}", fontSize = 20.sp, modifier = Modifier.padding(bottom = 20.dp))

        when(val result = coursesResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                LazyColumn {
                    items(result.data.size){ index ->
                        val data = result.data[index]
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
                            onClick = {
                                //TODO accès au chrono ?
                            }
                        ) {
                            Column (modifier = Modifier.padding(25.dp)){
                                Text("Nom de la course : " + data.name)
                                Text("Durée maximum de la course : " + data.max_duration)
                                Text("Course terminée ? " + if(data.is_over == 1) "Oui" else "Non")
                                Text("Position dans la compétition : " + data.position)
                            }
                        }
                    }
                }
            }
            null -> {}
        }
    }
}

@Composable
fun CompetitorsPage(
    modifier: Modifier,
    viewModel: CompetitorsViewModel,
    competitionId: Int
) {
    val competitorResult = viewModel.competitorResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getCompetitorsByCompetitionId(competitionId)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Concurrents de la compétition n°${competitionId} ", fontSize = 20.sp, modifier = Modifier.padding(bottom = 20.dp))

        when(val result = competitorResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                LazyColumn {
                    items(result.data.size){ index ->
                        val data = result.data[index]
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
                        ) {
                            Column (modifier = Modifier.padding(25.dp)){
                                Text("Nom : " + data.last_name)
                                Text("Prénom : " + data.first_name)
                                Text("Genre du concurrent : " +
                                        when(val gender = data.gender){
                                            "H" -> "Homme"
                                            "F" -> "Femme"
                                            else -> {
                                                "Pas de catégorie"
                                            }
                                        }
                                )
                                Text("Date de naissance : " + data.born_at)
                                Text("Email : " + data.email)
                                Text("N° de téléphone : " + data.phone)
                            }
                        }
                    }
                }
            }
            null -> {}
        }
    }
}