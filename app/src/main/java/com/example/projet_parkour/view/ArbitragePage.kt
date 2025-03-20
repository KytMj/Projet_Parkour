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
import com.example.projet_parkour.bdd.CompetitionStatus
import com.example.projet_parkour.model.CompetitionModelItem
import com.example.projet_parkour.model.CompetitorModelItem
import com.example.projet_parkour.model.CoursesModelItem
import com.example.projet_parkour.viewmodel.CompetitorsViewModel
import com.example.projet_parkour.viewmodel.CoursesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ArbitragePage(competitionViewModel: CompetitionsViewModel, coursesViewModel: CoursesViewModel, competitorsViewModel: CompetitorsViewModel){
    Column {
        selectParams(competitionViewModel, coursesViewModel, competitorsViewModel)

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
fun CompetitionModelItem.toCompetition(): Competition? {
    try {
        return Competition(
            name = this.name,
            ageMin = this.age_min,
            ageMax = this.age_max,
            gender = this.gender.firstOrNull() ?: 'U',
            hasTry = this.has_retry == 1,
            status = this.status,
        )
    }catch (e: Exception){
        println("ERROR: "+ e.message)
    }
    return null
}

@Composable
fun selectParams(competitionViewModel: CompetitionsViewModel, coursesViewModel: CoursesViewModel, competitorsViewModel: CompetitorsViewModel) {
    val bdd = AppDatabase.getInstance(LocalContext.current)
    val selectedCompetition = remember { mutableStateOf<CompetitionModelItem?>(null) }
    val selectedCourse = remember { mutableStateOf<CoursesModelItem?>(null) }
    val selectedCompetitor = remember { mutableStateOf<CompetitorModelItem?>(null) }

    selectCompetition(competitionViewModel, selectedCompetition)
    selectCourse(coursesViewModel, selectedCompetition.value?.id, selectedCourse)
    selectCompetitor(competitorsViewModel, selectedCompetition.value?.id, selectedCompetitor)

    var showButton by remember { mutableStateOf(false) }
    var comp by remember { mutableStateOf<Competition?>(null) }
    LaunchedEffect(selectedCompetition.value) {
        comp = selectedCompetition.value?.toCompetition()
        println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        if (comp != null){
            try {

                withContext(Dispatchers.IO) {
                    bdd.competitionDao().insertCompetition(comp!!)
                }
            }catch (e: Exception){
                println("ERROR" + e.message)
            }
        }
    }
//        if (selectedCompetition.value != null) {
//            comp = selectedCompetition.value?.toCompetition()
//            showButton = true
//            println("aaaaaaa")
////            bdd.competitionDao().insertCompetition(comp!!)
//        }
//    }

//    Column {
//        if (showButton && comp != null) {
//            Button(onClick = {
//                comp?.let {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        bdd.competitionDao().insertCompetition(it)
//                    }
//                }
//            }) {
//                Text("Arbitrer" + selectedCompetition.value?.name)
//            }
//        }
    //}

}


@Composable
fun selectCompetition(viewModel: CompetitionsViewModel, selectedCompetition: MutableState<CompetitionModelItem?>) {
    val competitionResult = viewModel.competitionResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getCompetitions()
    }

    when (val result = competitionResult.value) {
        is NetworkResponse.Error -> Text(text = result.message)
        is NetworkResponse.Loading -> CircularProgressIndicator()
        is NetworkResponse.Success -> {
            DropdownSelector(
                title = "Sélectionnez une compétition",
                items = result.data,
                selectedItem = selectedCompetition,
                labelExtractor = { it.name.toString() }
            )
        }
        null -> {}
    }
}

@Composable
fun selectCourse(viewModel: CoursesViewModel, compId: Int?, selectedCourse: MutableState<CoursesModelItem?>) {
    val coursesResult = viewModel.coursesResult.observeAsState()

    LaunchedEffect(compId) {
        compId?.let {
            viewModel.getCoursesByCompetitionId(it)
        }
    }

    when (val result = coursesResult.value) {
        is NetworkResponse.Error -> Text(text = result.message)
        is NetworkResponse.Loading -> CircularProgressIndicator()
        is NetworkResponse.Success -> {
            DropdownSelector(
                title = "Sélectionnez une course",
                items = result.data,
                selectedItem = selectedCourse,
                labelExtractor = { it.name.toString() }
            )
        }
        null -> {}
    }
}

@Composable
fun selectCompetitor(viewModel: CompetitorsViewModel, compId: Int?, selectedCompetitor: MutableState<CompetitorModelItem?>){
    val competitorResult = viewModel.competitorResult.observeAsState()
    LaunchedEffect(compId) {
        compId?.let {
            viewModel.getCompetitorsByCompetitionId(it)
        }
    }

    when (val result = competitorResult.value) {
        is NetworkResponse.Error -> Text(text = result.message)
        is NetworkResponse.Loading -> CircularProgressIndicator()
        is NetworkResponse.Success -> {
            DropdownSelector(
                title = "Sélectionnez un participant",
                items = result.data,
                selectedItem = selectedCompetitor,
                labelExtractor = { it.first_name.toString() }
            )
        }
        null -> {}
    }
}



/*
@Composable
fun selectParams(competitionViewModel: CompetitionsViewModel,  coursesViewModel: CoursesViewModel){
    var selectedCompetition = remember { mutableStateOf<CompetitionModelItem?>(null)}
    var selectedCourse = remember { mutableStateOf<CoursesModelItem?>(null)}
    selectCompetition(competitionViewModel, selectedCompetition)
    selectCourse(coursesViewModel, selectedCompetition.value?.id, selectedCourse)
}


@Composable
fun selectCompetition(viewModel: CompetitionsViewModel, selectedCompetition: MutableState<CompetitionModelItem?>){


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
                        DropdownMenuItem(text = {Text(comp.name.toString())}, onClick = {expanded = false; selectedOption = comp.name.toString(); selectedCompetition.value = comp})
                    }
                    }
                }
            }
            null -> {}
    }
}

@Composable
fun selectCourse(viewModel: CoursesViewModel, comp: Int?, selectedCourse: MutableState<CoursesModelItem?>){
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("selectionnez une course") }
    val competitionResult = viewModel.coursesResult.observeAsState()
    LaunchedEffect(comp) {
        comp?.let {
            viewModel.getCoursesByCompetitionId(it)
        };
        selectedOption = "selectionnez une course"
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
                    result.data.forEach { course ->
                        DropdownMenuItem(text = {Text(course.name.toString())}, onClick = {expanded = false; selectedOption = course.name.toString(); selectedCourse.value = course})
                    }
                }
            }
        }
        null -> {}
    }


}
*/
