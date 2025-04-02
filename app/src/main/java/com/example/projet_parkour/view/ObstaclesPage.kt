package com.example.projet_parkour.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.projet_parkour.model.CompetitorIdModelItem
import com.example.projet_parkour.model.CompetitorModel
import com.example.projet_parkour.model.CourseObstacleModel
import com.example.projet_parkour.model.MessageModel
import com.example.projet_parkour.model.ObstacleModel
import com.example.projet_parkour.view.utils.DropdownMenuComposable
import com.example.projet_parkour.view.utils.FloatingButtonAdd
import com.example.projet_parkour.viewmodel.CompetitorsViewModel
import com.example.projet_parkour.viewmodel.ObstaclesViewModel

@Composable
fun ObstaclesPage(
    modifier: Modifier,
    viewModel: ObstaclesViewModel,
    courseId: Int,
    navController: NavController,
    context: Context,
    isEnableConstructMode: MutableState<Boolean>,
    ) {
    val selectedText = remember { mutableStateOf("") }
    var idAddObstacle = remember { mutableIntStateOf(-1) }
    val obstaclesList = CourseObstacleModel()

    LaunchedEffect(Unit) {
        viewModel.getObstacles()
        viewModel.getObstaclesByCourseId(courseId)
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)){
        Column {
            AddToListRegisteredObstacles(viewModel, obstaclesList)
            ObstaclesInParkourPage(modifier, viewModel, courseId, obstaclesList)
            if(isEnableConstructMode.value){
                AvailableObstaclesPage(modifier, viewModel, selectedText, obstaclesList)
            }
        }
        if (isEnableConstructMode.value){
            FloatingButtonAdd(
                modifier = Modifier.padding(bottom = 50.dp, end = 10.dp).align(Alignment.BottomEnd),
                route = "create_obstacle_page/${courseId}",
                navController = navController,
            )
        }
    }
}

@Composable
fun AvailableObstaclesPage(
    modifier: Modifier,
    viewModel: ObstaclesViewModel,
    selectedText: MutableState<String>,
    obstaclesList: CourseObstacleModel
) {
    val competitorResult = viewModel.obstaclesResult.observeAsState()

    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight(),
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
                val data = result.data
                val removeObstacles = ObstacleModel()

                for(obstacle in obstaclesList){
                    data.forEach { element ->
                        if(element.name == obstacle.obstacle_name){
                            removeObstacles.add(element)
                        }
                    }
                }
                data.removeAll(removeObstacles)
                val obstacles = ArrayList<String>()

                data.forEach { element ->
                    obstacles.add("ID"+element.id+". "+element.name)
                }

                DropdownMenuComposable(obstacles, "Obstacles disponibles", selectedText)
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
    obstaclesList: CourseObstacleModel
) {
    val obstacleResult = viewModel.obstaclesByCourseResult.observeAsState()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Obstacles de la course",
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 5.dp, bottom = 15.dp),
            fontWeight = FontWeight.Bold)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(top=20.dp,start = 10.dp, end = 10.dp, bottom = 20.dp)
        ) {
            when (val result = obstacleResult.value) {
                is NetworkResponse.Error -> {
                    Text(text = result.message)
                }

                is NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }
                is NetworkResponse.Success -> {
                    LazyColumn {
                        items(result.data.size) { index ->
                            val data = result.data[index]
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
                            ) {
                                Text(modifier = Modifier.padding(10.dp), text = data.obstacle_name)
                            }
                        }
                    }
                }
                null -> {}
            }
        }
    }
}

private fun AddToListRegisteredObstacles(viewModel: ObstaclesViewModel, registeredObstaclesList: CourseObstacleModel){
    val competitorByCompetitionResult = viewModel.obstaclesByCourseResult

    when(val result = competitorByCompetitionResult.value){
        is NetworkResponse.Success -> {
            registeredObstaclesList.addAll(result.data)
        }
        null -> {}
        is NetworkResponse.Error -> {}
        is NetworkResponse.Loading -> {}
    }
}