package com.example.projet_parkour.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.viewmodel.CoursesViewModel

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
        modifier = modifier.fillMaxWidth(),
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
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
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