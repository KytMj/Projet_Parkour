package com.example.projet_parkour.view

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.model.CompetitionModelItem
import com.example.projet_parkour.model.CompetitorModel
import com.example.projet_parkour.model.CoursesModel
import com.example.projet_parkour.model.ObstacleModel
import com.example.projet_parkour.viewmodel.CompetitorsViewModel
import com.example.projet_parkour.viewmodel.CoursesViewModel
import com.example.projet_parkour.viewmodel.ObstacleViewModel

class CompetitionState(coursesViewModel: CoursesViewModel, competitorsViewModel: CompetitorsViewModel, obstacleViewModel: ObstacleViewModel) {
    private val competition = mutableStateOf<CompetitionModelItem?>(null)
    private val courses = mutableStateOf<CoursesModel?>(null)
    private val obstacles= mutableStateOf<ArrayList<ObstacleModel?>>(ArrayList())
    private val competitors = mutableStateOf<CompetitorModel?>(null)
    private val coursesViewModel = coursesViewModel
    private val competitorsViewModel = competitorsViewModel
    private val obstacleViewModel = obstacleViewModel
    private val compId = mutableStateOf<Int?>(null)

    @Composable
    fun init(comp: CompetitionModelItem){
        competition.value = comp
        println("debugage:id: ${competition.value?.id}")
        compId.value = competition.value?.id
        initCompetitiors()
        initCourses()
        courses.value?.forEach { course->
            initObstacles(course.id)
        }
        if (obstacles.value.size > 0){
            println("debugage: S" + obstacles.value.get(0)?.size)
        }
        println("debugage: here")
        courses.value?.forEach { course->
            println("debugage" + course.name)
        }
        println("debugage: ${obstacles.value.size}")
    }


    @Composable
    fun initCompetitiors(){
        val competitorResult = competitorsViewModel.competitorResult.observeAsState()
        LaunchedEffect(competition.value?.id) {
            competition.value?.id?.let {
                competitorsViewModel.getCompetitorsByCompetitionId(it)
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

        // Ensure the network call is triggered when `compIdValue` changes
        LaunchedEffect(competition.value?.id) {
            competition.value?.id?.let {
                coursesViewModel.getCoursesByCompetitionId(it)
            }
        }

        when (val result = courseResult.value) {
            is NetworkResponse.Error -> Text(text = result.message)
            is NetworkResponse.Loading -> CircularProgressIndicator()
            is NetworkResponse.Success -> {
                println("debugage:data:${result.data.size}")
                courses.value = result.data
            }
            null -> {}
        }
    }
    @Composable
    fun initObstacles(id: Int) {
        obstacles.value = ArrayList()
        val obstacleResult = obstacleViewModel.obstacleResult.observeAsState()
            LaunchedEffect(id) {
                id.let {
                    obstacleViewModel.getObstacleByCourseId(it)
                }
            }
            when (val result = obstacleResult.value) {
                is NetworkResponse.Error -> Text(text = result.message)
                is NetworkResponse.Loading -> CircularProgressIndicator()
                is NetworkResponse.Success -> {
                    obstacles.value.add(result.data)
                }
                null -> {}
            }
    }
}