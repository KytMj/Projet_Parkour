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
import com.example.projet_parkour.viewmodel.ObstacleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ArbitragePage(competitionViewModel: CompetitionsViewModel, coursesViewModel: CoursesViewModel, competitorsViewModel: CompetitorsViewModel, obstacleViewModel: ObstacleViewModel){
    Column {
        selectParams(competitionViewModel, coursesViewModel, competitorsViewModel, obstacleViewModel)

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
            id = this.id,
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

fun CoursesModelItem.toCourse(): Course? {
    try {
        return Course(
            id = this.id,
            name = this.name,
            maxDuration = this.max_duration,
            position = this.position,
            isOver = this.is_over,
            competitionId = this.competition_id
        )
    }catch (e: Exception){
        println("ERROR: "+ e.message)
    }
    return null
}



@Composable
fun selectParams(competitionViewModel: CompetitionsViewModel, coursesViewModel: CoursesViewModel, competitorsViewModel: CompetitorsViewModel, obstacleViewModel: ObstacleViewModel) {
    val selectedCompetition = remember { mutableStateOf<CompetitionModelItem?>(null) }
    val bdd = AppDatabase.getInstance(LocalContext.current)
    val click = remember { mutableStateOf(0) }
    val lastClick = remember { mutableStateOf(0) }


    selectCompetition(competitionViewModel, selectedCompetition, bdd)
    val state = remember { CompetitionState(coursesViewModel, competitorsViewModel, obstacleViewModel) }
    if (selectedCompetition.value != null) state.init(selectedCompetition.value!!)
    //selectCourse(coursesViewModel, selectedCompetition.value?.id, bdd, state, selectedCompetition.value)


}



@Composable
fun selectCompetition(viewModel: CompetitionsViewModel, selectedCompetition: MutableState<CompetitionModelItem?>, bdd: AppDatabase) {
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

            LaunchedEffect(selectedCompetition.value) {

                selectedCompetition.value?.let { competition ->
                    val competitionEntity = competition.toCompetition()
                    competitionEntity?.let {
                        withContext(Dispatchers.IO) {
                            try {
                                bdd.competitionDao().insertCompetition(it)
                            } catch (e: Exception) {
                                println("Error: " + e.message)
                            }
                        }
                    } ?: run {
                        println("Error: competition entity is null")
                    }
                }
            }
        }
        null -> {}
    }
}

@Composable
fun selectCourse(viewModel: CoursesViewModel, compId: Int?, bdd: AppDatabase, comp: CompetitionModelItem?) {

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

            val courses = result.data.mapNotNull { it.toCourse() }
            LaunchedEffect(courses) {
                withContext(Dispatchers.IO) {
                    try {
                        bdd.courseDao().insertCourses(courses)

                    }catch (e: Exception){
                        println("Error:" + e.message)
                    }
                }
            }
        }
        null -> {}
    }
}

@Composable
fun selectCompetitor(viewModel: CompetitorsViewModel, compId: Int?, bdd: AppDatabase){
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
