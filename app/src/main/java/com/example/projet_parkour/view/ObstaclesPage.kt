package com.example.projet_parkour.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.model.ObstacleModel
import com.example.projet_parkour.ui.theme.Pink40
import com.example.projet_parkour.view.utils.DropdownMenuComposable
import com.example.projet_parkour.viewmodel.ObstaclesViewModel

@Composable
fun ObstaclesPage(
    modifier: Modifier,
    viewModel: ObstaclesViewModel,
    courseId: Int,
    navController: NavController,
    context: Context
) {
    val selectedText = remember { mutableStateOf("") }
    var idAddCompetitor = remember { mutableIntStateOf(-1) }
    val obstaclesList = ObstacleModel()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)){
        Column {
            AvailableObstaclesPage(modifier, viewModel, selectedText, obstaclesList)
            if (selectedText.value != "") {
                idAddCompetitor.intValue = selectedText.value.split(".").get(0).substring(3).toInt()
            }
            ObstaclesInParkourPage(modifier, viewModel, courseId, obstaclesList)
        }
    }
}

@Composable
fun AvailableObstaclesPage(
    modifier: Modifier,
    viewModel: ObstaclesViewModel,
    selectedText: MutableState<String>,
    obstaclesList: ObstacleModel
) {
    val competitorResult = viewModel.obstaclesResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getObstacles()
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(val result = competitorResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                val data = result.data //TOUS LES OBSTACLES
                    //TODO enlever ceux déjà enregistrer... pas de vérif non plus
                val competitors = ArrayList<String>()

                data.forEach { element ->
                    competitors.add(element.name)
                }

                DropdownMenuComposable(competitors, "Obstacles disponibles", selectedText)
            }
            null -> {}
        }
    }
}

@Composable
fun ObstaclesInParkourPage(
    modifier: Modifier,
    viewModel: ObstaclesViewModel,
    courseId: Int,
    obstaclesList: ObstacleModel
) {
    val obstacleResult = viewModel.obstaclesByCourseResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getObstaclesByCourseId(courseId)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier.padding(bottom = 10.dp), text = "Obstacles de la course")
        when(val result = obstacleResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                result.data.forEach{element -> obstaclesList.add(element)}
                LazyColumn {
                    items(result.data.size){ index ->
                        val data = result.data[index]
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
                        ) {
                            Text(modifier = Modifier.padding(10.dp), text = data.name)
                        }
                    }
                }
            }
            null -> {}
        }
    }
}