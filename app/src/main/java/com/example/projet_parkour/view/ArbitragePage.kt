package com.example.projet_parkour.view

import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.viewmodel.CompetitionsViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.projet_parkour.bdd.AppDatabase
import com.example.projet_parkour.bdd.Competition
import com.example.projet_parkour.bdd.Course
import com.example.projet_parkour.model.CompetitionModelItem
import com.example.projet_parkour.model.CoursesModelItem
import com.example.projet_parkour.viewmodel.CompetitorsViewModel
import com.example.projet_parkour.viewmodel.CoursesViewModel
import com.example.projet_parkour.viewmodel.ObstaclesViewModel
import com.example.projet_parkour.viewmodel.PerformanceObstaclesViewModel
import com.example.projet_parkour.viewmodel.PerformancesViewModel


@Composable
fun ArbitragePage(competitionViewModel: CompetitionsViewModel, coursesViewModel: CoursesViewModel,
                  competitorsViewModel: CompetitorsViewModel, obstaclesViewModel: ObstaclesViewModel, competitionId : Int,
                  performancesViewModel: PerformancesViewModel, performanceObstaclesViewModel: PerformanceObstaclesViewModel){
    val selectedCompetition = remember { mutableStateOf<CompetitionModelItem?>(null) }
    selectCompetition(competitionViewModel, selectedCompetition, competitionId)
    val state = remember { CompetitionState(coursesViewModel, competitorsViewModel, obstaclesViewModel, performancesViewModel, performanceObstaclesViewModel) }
    if (selectedCompetition.value != null) state.init(selectedCompetition.value!!)
}

@Composable
fun selectCompetition(viewModel: CompetitionsViewModel, selectedCompetition: MutableState<CompetitionModelItem?>, competitionId : Int) {
    val competitionResult = viewModel.competitionResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getCompetitions()
    }

    when (val result = competitionResult.value) {
        is NetworkResponse.Error -> Text(text = result.message)
        is NetworkResponse.Loading -> CircularProgressIndicator()
        is NetworkResponse.Success -> {
            result.data.forEach {competition->
                if (competition.id == competitionId){
                    selectedCompetition.value = competition
                }
            }
//            DropdownSelector(
//                title = "Sélectionnez une compétition",
//                items = result.data,
//                selectedItem = selectedCompetition,
//                labelExtractor = { it.name.toString() }
//            )
        }
        null -> {}
    }
}


@Composable
fun <T> DropdownSelector(
    title: String,
    items: List<T>,
    selectedItem: MutableState<T?>,
    labelExtractor: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(title) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = selectedOption,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(labelExtractor(item)) },
                    onClick = {
                        expanded = false
                        selectedOption = labelExtractor(item)
                        selectedItem.value = item
                    }
                )
            }
        }
    }
}




