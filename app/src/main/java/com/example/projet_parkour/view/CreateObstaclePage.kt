package com.example.projet_parkour.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.model.CreationObstacleModelItem
import com.example.projet_parkour.viewmodel.ObstaclesViewModel

@Composable
fun CreateObstaclePage(
    modifier: Modifier,
    viewModel: ObstaclesViewModel,
    navController: NavController,
    courseId: Int,
) {
    val createCourseResult = viewModel.createObstacleResult.observeAsState()

    val nameState by viewModel.nameObstacle.observeAsState();

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(10.dp)
            ).padding(10.dp)
    ) {
        OutlinedTextField(
            value = nameState ?: "",
            label = { Text("Nom de l'obstacle", color=Color.Black) },
            onValueChange = { viewModel.nameObstacle.postValue(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(4.dp))

        Button(onClick = {
            val obstacle = CreationObstacleModelItem(
                name = nameState.toString()
            );

            viewModel.createObstacle(obstacle);
        }) {
            Text(text = "Enregistrer")
        }
        when (val result = createCourseResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> navController.navigate("competitions_page")
            null -> {}
        }
    }
}