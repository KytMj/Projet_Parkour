package com.example.projet_parkour.view

import android.os.SystemClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.bdd.AppDatabase
import com.example.projet_parkour.bdd.Perf
import com.example.projet_parkour.bdd.Remember
import com.example.projet_parkour.model.CompetitionModelItem
import com.example.projet_parkour.model.CompetitorModel
import com.example.projet_parkour.model.CompetitorModelItem
import com.example.projet_parkour.model.CourseObstacleModel
import com.example.projet_parkour.model.CourseObstacleModelItem
import com.example.projet_parkour.model.CoursesModel
import com.example.projet_parkour.model.CoursesModelItem
import com.example.projet_parkour.model.ObstacleModel
import com.example.projet_parkour.model.PerformanceCreateModelItem
import com.example.projet_parkour.model.PerformanceModelItem
import com.example.projet_parkour.model.PerformanceObstacleCreateModelItem
import com.example.projet_parkour.model.Perfs
import com.example.projet_parkour.viewmodel.CompetitorsViewModel
import com.example.projet_parkour.viewmodel.CoursesViewModel
import com.example.projet_parkour.viewmodel.ObstaclesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class CompetitionState(coursesViewModel: CoursesViewModel, competitorsViewModel: CompetitorsViewModel, obstaclesViewModel: ObstaclesViewModel) {
    private val competition = mutableStateOf<CompetitionModelItem?>(null)
    private val courses = mutableStateOf<CoursesModel?>(null)
    private val obstacles = mutableStateOf<HashMap<Int, CourseObstacleModel>>(HashMap())
    private val competitors = mutableStateOf<CompetitorModel?>(null)
    private val coursesViewModel = coursesViewModel
    private val competitorsViewModel = competitorsViewModel
    private val obstacleViewModel = obstaclesViewModel
    private val compId = mutableStateOf<Int?>(null)


    @Composable
    fun init(comp: CompetitionModelItem){
        val arbitrage = remember { mutableStateOf(false) }

        competition.value = comp
        compId.value = competition.value?.id
        initCompetitiors()
        initCourses()
        initObstacles()
        if (!arbitrage.value) Button(onClick = {arbitrage.value = true}) { Text("arbitrer") }
        if (arbitrage.value) arbitrage()
    }

    @Composable
    fun arbitrage(){
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .padding(top = Dp(50.0f))) {
            val bdd = AppDatabase.getInstance(LocalContext.current)
            var currTime = remember { mutableStateOf(0L) }
            var isRunning by remember { mutableStateOf(false) }
            var startTime by remember { mutableStateOf(0L) }
            var lastObstacleTime by remember { mutableStateOf(0L) }
            val allCompetitors = remember { competitors.value.orEmpty() }
            val allCourses = remember { courses.value.orEmpty().filter { it.is_over == 0 } }
            val allObstacles = remember { allCourses.associateWith { course -> obstacles.value[course.id].orEmpty() } }
            var courseIndex by remember { mutableStateOf(0) }
            var competitorIndex by remember { mutableStateOf(0) }
            var obstacleIndex by remember { mutableStateOf(0) }


            LaunchedEffect(competition.value) {
                CoroutineScope(Dispatchers.IO).launch {
                    val res = bdd.rememberDao().getLastRemember(competition.value?.id!!)
                    if (res != null){
                        courseIndex = res.courseIndex
                        competitorIndex = res.competitorIndex
                        obstacleIndex = res.obstacleIndex
                    }
                }
            }
            LaunchedEffect(isRunning) {
                if (isRunning) {
                    startTime = SystemClock.elapsedRealtime()
                    while (isRunning) {
                        delay(10)
                        currTime.value = SystemClock.elapsedRealtime() - startTime
                    }
                }
            }
            println("debugage: course ${allCourses.size}")
            println("debugage: competitor ${allCompetitors.size}")
            if (allCourses.isNotEmpty() && allCompetitors.isNotEmpty()) {
                val currentCourse = allCourses[courseIndex]
                val currentCompetitor = allCompetitors[competitorIndex]
                val currentObstacleList = allObstacles[currentCourse].orEmpty()

                if (currentObstacleList.isNotEmpty()) {
                    val currentObstacle = currentObstacleList[obstacleIndex]

                    Text("Course:  ${currentCourse.name}")
                    Text("Participant: ${currentCompetitor.first_name} ${currentCompetitor.last_name}")
                    Text("Obstacle: ${currentObstacle.obstacle_name}")

                    Button(onClick = {
                        if (isRunning) {
                            val elapsedTime = currTime.value - lastObstacleTime
                            CoroutineScope(Dispatchers.IO).launch {
                                bdd.perfDao().insertPerf(Perf(
                                    courseId = currentCourse.id,
                                    competitorId = currentCompetitor.id,
                                    obstacleId = currentObstacle.id,
                                    time = elapsedTime.toInt()
                                ))
                                bdd.rememberDao().insertRemember(Remember(
                                    competitionId =  competition.value?.id!!,
                                    courseIndex =  courseIndex,
                                    competitorIndex = competitorIndex,
                                    obstacleIndex = obstacleIndex
                                ))
                            }
                            lastObstacleTime = currTime.value
                            if (obstacleIndex + 1 < currentObstacleList.size) obstacleIndex++
                            else {
                                obstacleIndex = 0
                                if (competitorIndex + 1 < allCompetitors.size) {
                                    competitorIndex++
                                    isRunning = false
                                    currTime.value = lastObstacleTime
                                } else {
                                    competitorIndex = 0
                                    if (courseIndex + 1 < allCourses.size) courseIndex++
                                }
                            }
                        } else {
                            isRunning = true
                            lastObstacleTime = currTime.value
                        }
                    }) {Text(if (isRunning) "Enregistrer" else "Démarrer")}

                    if (isRunning){
                        Button(onClick = {
                            if (competition.value?.has_retry == 1){
                                isRunning = false
                                currTime.value = lastObstacleTime
                            }else{
                                isRunning = false
                                if (competitorIndex + 1 < allCompetitors.size){
                                    competitorIndex++
                                }else
                                    if (courseIndex + 1 < allCourses.size) {
                                        courseIndex++
                                    }
                            }
                        }) { Text("Chute") }
                    }
                }
            }
            Text("Chrono: ${currTime.value.milliseconds}")
        }
    }


    @Composable
    fun initCompetitiors(){
        val competitorResult = competitorsViewModel.competitorByCompetitionResult.observeAsState()
        LaunchedEffect(competition.value) {
            if (competition.value?.id != null){
                competitorsViewModel.getCompetitorsByCompetitionId(competition.value?.id!!)
            }
        }

        when (val result = competitorResult.value) {
            is NetworkResponse.Error -> Text(text = result.message)
            is NetworkResponse.Loading -> CircularProgressIndicator()
            is NetworkResponse.Success -> {

                competitors.value = result.data
            }
            null -> {}
        }
    }

    @Composable
    fun initCourses() {
        val courseResult = coursesViewModel.coursesResult.observeAsState()

        LaunchedEffect(competition.value) {
            if (competition.value?.id != null) {
                coursesViewModel.getCoursesByCompetitionId(competition.value?.id!!)
            }
        }

        when (val result = courseResult.value) {
            is NetworkResponse.Error -> Text(text = result.message)
            is NetworkResponse.Loading -> CircularProgressIndicator()
            is NetworkResponse.Success -> {
                courses.value = result.data
            }
            null -> {}
        }
    }
    @Composable
    fun initObstacles() {
        val obstaclesByCourseResult = obstacleViewModel.obstaclesByCourseResult.observeAsState()
        LaunchedEffect(courses.value) {
            courses.value?.forEach { course ->
                obstacleViewModel.getObstaclesByCourseId(course.id)
            }
        }

        when (val result = obstaclesByCourseResult.value) {
            is NetworkResponse.Error -> Text(text = result.message)
            is NetworkResponse.Loading -> CircularProgressIndicator()
            is NetworkResponse.Success -> {
                result.data.let { obstaclesList ->
                    obstacles.value.set(result.data.first, result.data.second)
                }
            }
            null -> {}
        }
    }
}