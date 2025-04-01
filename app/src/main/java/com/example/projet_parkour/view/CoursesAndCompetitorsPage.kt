package com.example.projet_parkour.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.view.utils.FloatingButtonAdd
import com.example.projet_parkour.viewmodel.CompetitorsViewModel
import com.example.projet_parkour.viewmodel.CoursesViewModel

@Composable
fun DisplayCoursesCompetitors(
    modifier: Modifier,
    coursesViewModel: CoursesViewModel,
    competitorsViewModel: CompetitorsViewModel,
    competitionId: Int,
    navController: NavController,
    isEnableConstructMode: MutableState<Boolean>
){
    var currentPage by remember { mutableStateOf<String?>(null) }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize() .fillMaxHeight()) {
            Button(modifier = Modifier.padding(5.dp).fillMaxWidth(),
                shape = RoundedCornerShape(15),
                colors = ButtonDefaults.buttonColors(contentColor = Color.Black, containerColor = Color(0xFFCCA43B)),
                onClick = {
                    currentPage = if (currentPage == "Courses") null else "Courses"
                }
            ) {
                Row( modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Courses de la compétition",
                        modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.List,
                        contentDescription = "List Icon"
                    )
                }
            }
            AnimatedVisibility(visible = currentPage == "Courses") {
                CoursesPage(modifier, coursesViewModel,navController, competitionId)
            }

            Button(modifier = Modifier.padding(5.dp).fillMaxWidth(),
                shape = RoundedCornerShape(15),
                colors = ButtonDefaults.buttonColors(contentColor = Color.Black, containerColor = Color(0xFFCCA43B)),
                onClick = {
                    currentPage = if (currentPage == "Competitors") null else "Competitors" }
            ) {
                Row( modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Concurrents de la compétition",
                        modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.List,
                        contentDescription = "List Icon"
                    )
                }
            }
            AnimatedVisibility(visible = currentPage == "Competitors") {
                CompetitorsPage(modifier, competitorsViewModel, competitionId)
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                //TODO chrono
                //navController.navigate("")
            },
            containerColor = Color(0xFFCCA43B),
            contentColor = Color.Black,
            elevation = FloatingActionButtonDefaults.elevation()
        ) {
            Text(text = "Accéder au chrono", modifier = Modifier.padding(start = 10.dp, end = 10.dp))
        }
        if (isEnableConstructMode.value){
            FloatingButtonAdd(
                modifier = Modifier.padding(bottom = 50.dp, end = 10.dp).align(Alignment.BottomEnd),
                route = "create_course_page/${competitionId}",
                navController = navController,
            )
        }
    }
}

@Composable
fun CoursesPage(
    modifier: Modifier,
    viewModel: CoursesViewModel,
    navController: NavController,
    competitionId: Int
){
    val coursesResult = viewModel.coursesResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getCoursesByCompetitionId(competitionId)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .padding(10.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(Color(0xFF242F40))
            .padding(top=20.dp,start = 10.dp, end = 10.dp, bottom = 20.dp)
    ) {
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
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF748cab)),
                            modifier = Modifier.padding(top=20.dp,start = 10.dp, end = 10.dp, bottom = 20.dp),
                            onClick = {
                                /*if(!(data.is_over)){
                                    //TODO accès au chrono ?
                                }*/
                            }
                        ) {
                            Column (modifier = Modifier.padding(25.dp)){
                                Row {
                                    Text(
                                        "N°" + data.position,
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .align(Alignment.CenterVertically),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Column {
                                        Text(
                                            "Nom de la course : " + data.name,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text("Durée maximum : " + data.max_duration)
                                        Text(
                                            text = if (data.is_over == 1) "Course terminée" else "Course pas terminée",
                                            color = if (data.is_over == 1) Color.Green else Color.Red
                                        )
                                        Button(
                                            modifier = Modifier.fillMaxWidth(),
                                            onClick = {
                                                navController.navigate("obstacles_page/${data.id}")
                                            }, colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFCCA43B),
                                                contentColor = Color.Black
                                            ),
                                            shape = RoundedCornerShape(30)
                                        ) {
                                            Text("Obstacles")
                                        }
                                    }
                                }
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .padding(10.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(Color(0xFF242F40))
            .padding(top=20.dp,start = 10.dp, end = 10.dp, bottom = 20.dp)
    ) {
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
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF748cab)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top=20.dp,start = 10.dp, end = 10.dp, bottom = 20.dp)
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