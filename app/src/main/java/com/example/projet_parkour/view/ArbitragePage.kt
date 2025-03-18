package com.example.projet_parkour.view

import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.model.CompetitionModel
import com.example.projet_parkour.viewmodel.CompetitionsViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.runtime.livedata.observeAsState
import com.example.projet_parkour.model.CompetitionModelItem

@Composable
fun ArbitragePage(competitionViewModel: CompetitionsViewModel){
    Column {
        selectParams(competitionViewModel)
        Timer()
    }
}

@Composable
fun Timer(){
    var currTime by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var baseTime by remember { mutableStateOf(0L) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = SystemClock.elapsedRealtime() - baseTime
            while (isRunning) {
                delay(10)
                currTime = SystemClock.elapsedRealtime() - startTime
            }
        } else {
            baseTime = currTime
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = currTime.milliseconds.toString(), fontFamily = FontFamily.Monospace)
        Button(onClick = {isRunning = !isRunning}) {Text(text = "start/stop")}

    }


}

@Composable
fun selectParams(competitionViewModel: CompetitionsViewModel){
    selectCompetition(competitionViewModel)
}


@Composable
fun selectCompetition(viewModel: CompetitionsViewModel){

    var selectedCompetition : CompetitionModelItem? = null;
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("selectionnez une competition") }
    val competitionResult = viewModel.competitionResult.observeAsState()
    LaunchedEffect(Unit) {
        viewModel.getCompetitions()
    }

    when (val result = competitionResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = selectedOption,
                        modifier = Modifier
                            .clickable { expanded = true }
                            .padding(8.dp)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                    result.data.forEach { comp ->
                        DropdownMenuItem(text = {Text(comp.name.toString())}, onClick = {expanded = false; selectedOption = comp.name.toString(); selectedCompetition = comp})
                    }
                    }
                }
            }
            null -> {}
    }
}

