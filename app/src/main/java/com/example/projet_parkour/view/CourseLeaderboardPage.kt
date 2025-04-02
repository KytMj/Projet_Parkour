package com.example.projet_parkour.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.model.CompetitorModelItem
import com.example.projet_parkour.model.PerformanceModel
import com.example.projet_parkour.model.PerformanceModelItem
import com.example.projet_parkour.viewmodel.CompetitorsViewModel
import com.example.projet_parkour.viewmodel.PerformanceObstaclesViewModel
import com.example.projet_parkour.viewmodel.PerformancesViewModel

@Composable
fun CourseLeaderboardPage(
    modifier: Modifier,
    viewModelPerformances: PerformancesViewModel,
    viewModelPerformanceObstacles: PerformanceObstaclesViewModel,
    competitorsViewModel: CompetitorsViewModel,
    navController: NavController,
    courseId: Int
) {
    val competitionResult = viewModelPerformances.performancesResult.observeAsState()
    val competitorsResult = competitorsViewModel.competitorResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModelPerformances.getPerformances()
        competitorsViewModel.getCompetitors()
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Classement Course n°${courseId}",
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 5.dp, bottom = 15.dp),
            fontWeight = FontWeight.Bold)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .padding(10.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
        ) {
            when (val result = competitionResult.value) {
                is NetworkResponse.Error -> {
                    Text(text = result.message)
                }

                is NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }

                is NetworkResponse.Success -> {
                    val filteredPerformances = result.data.filter { it.course_id == courseId }
                    var sortedTab = filteredPerformances.sortedWith (compareBy (
                        {it.status =="defection"},// Trie selon l'abandon (faux => début, vrai => fin)
                        {it.total_time} // Sinon selon le temps
                    ));

                        LazyColumn {
                        items(sortedTab.size) { index ->
                            val data = sortedTab[index]
                            Card(
                                colors = CardDefaults.cardColors(containerColor =
                                    when(val status = data.status) {
                                        "defection" -> Color.Red
                                        "to_verify" -> Color.Gray
                                        "over" -> MaterialTheme.colorScheme.primary
                                        else -> {
                                            MaterialTheme.colorScheme.secondary
                                        }
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
                            ) {
                                Row (modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Text(modifier = Modifier.padding(10.dp),text = "${index+1}")
                                    when (val competitorResult = competitorsResult.value) {
                                        is NetworkResponse.Error -> {
                                            Text(text = competitorResult.message)
                                        }

                                        is NetworkResponse.Loading -> {
                                            CircularProgressIndicator()
                                        }

                                        is NetworkResponse.Success -> {
                                            val competitor = competitorResult.data.find { it.id == data.competitor_id }
                                            Text(modifier = Modifier.padding(10.dp),text = ""+ competitor?.first_name+" "+competitor?.last_name)
                                        }
                                        null -> {}
                                    }
                                    Text(modifier = Modifier.padding(10.dp),text = "${data.total_time}ms")
                                }
                                Row (modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Text(modifier = Modifier.padding(10.dp),text = data.status)
                                    Text(modifier = Modifier.padding(10.dp),text = ""+data.course_id)
                                }
                            }
                        }


                    }
                }

                null -> {}
            }
        }
        Button(onClick = {navController.navigate("leaderboard_page")}) {
            Text("Classement général")
        }
    }
}