package com.example.projet_parkour.view

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projet_parkour.viewmodel.ObstaclesViewModel

@Composable
fun CreateObstaclePage(
    modifier: Modifier,
    viewModel: ObstaclesViewModel,
    navController: NavController,
    courseId: Int,
    context: Context,
) {
    val nameState by viewModel.nameObstacle.observeAsState();

    var isValidName by remember { mutableStateOf(false) }

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
            onValueChange = {
                viewModel.nameObstacle.postValue(it)
                isValidName = it.isNotEmpty() && viewModel.isValidName(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (!isValidName){
            Text(text = "Champ invalide", color = Color.Red)
        }
        Spacer(modifier = Modifier.size(4.dp))

        Row {
            Button(onClick = {
                val result = viewModel.checkAndAddObstacle(context)
                if(result){
                    viewModel.nameObstacle.postValue("")
                    navController.navigate("obstacles_page/${courseId}")
                }
            }) {
                Text(text = "Enregistrer dans la base")
            }
            Button(onClick = {
                val result = viewModel.checkAndAddObstacleToCourse(context, courseId)
                if(result){
                    viewModel.nameObstacle.postValue("")
                    navController.navigate("obstacles_page/${courseId}")
                }
            }) {
                Text(text = "Enregistrer et ajouter à la course")
            }
        }
    }
}