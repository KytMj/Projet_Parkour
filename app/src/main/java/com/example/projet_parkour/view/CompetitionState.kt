package com.example.projet_parkour.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    private val obstacles = mutableStateOf<HashMap<Int, ObstacleModel>>(HashMap())
    private val competitors = mutableStateOf<CompetitorModel?>(null)
    private val coursesViewModel = coursesViewModel
    private val competitorsViewModel = competitorsViewModel
    private val obstacleViewModel = obstacleViewModel
    private val compId = mutableStateOf<Int?>(null)

    @Composable
    fun init(comp: CompetitionModelItem){
//        obstacles.value = ArrayList()
        competition.value = comp
        println("--------------------------------- debugage:id: ${competition.value?.id} ----------------------------------")
        compId.value = competition.value?.id
        initCompetitiors()
        initCourses()
        initObstacles()
//        println("debugage competitor " + competitors.value?.forEach { copetitor -> copetitor.first_name  + " "})

        courses.value?.forEach { course->
//            println("debugage course id ${course.id}" + obstacles.value.get(course.id)?.forEach { obstacle -> obstacle.id.toString() + " " })
        }
//
//        println("debugage competitor size" + competitors.value?.size)
//
//        println("debugage: obstacles count " + obstacles.value.size)
//        println("debugage obstacle s ${obstacles.value.size}")
        obstacles.value.forEach { (courseId, obstacleList) ->
            println("debugage: Course $courseId has obstacles")
            obstacleList.forEach { obstacle -> print(obstacle.id.toString() + " ") }
        }
////        println("debugage: ${obstacles.value.size}")
//        println("debugage: here")
        val arbitrage = remember { mutableStateOf(false) }
        if (arbitrage.value) arbitrage()
        Button(onClick = {arbitrage.value = true}) { }

    }

    @Composable
    fun arbitrage(){
        Column {
            Row {
                Text(competition.value?.name.toString())
            }
//            Row {
//                competitors.value?.forEach { competitor ->
//                    Text(competitor.first_name + " ")
//                }
//            }
            Column {
                courses.value?.forEach { course->
                    println("debugage here")
                    Column {
                        Text(course.name)
                        Row {
                            obstacles.value.get(course.id)?.forEach { obstacle ->
                                Text(obstacle.id.toString() + " ")
                            }
                            }

                        }
                }
            }
        }
    }


    @Composable
    fun initCompetitiors(){
        val competitorResult = competitorsViewModel.competitorResult.observeAsState()
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
        // Ensure the network call is triggered when `compIdValue` changes
        LaunchedEffect(competition.value) {
            if (competition.value?.id != null) {
                coursesViewModel.getCoursesByCompetitionId(competition.value?.id!!)
            }
        }
//        if (competition.value?.id != null)
//        coursesViewModel.getCoursesByCompetitionId(competition.value?.id!!)
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
        val obstacleResult = obstacleViewModel.obstaclesResult.observeAsState()
        val id : Int
        LaunchedEffect(courses.value) {
            courses.value?.forEach { course ->
                println("debugage heyyyyyyy ${course.id}")
                obstacleViewModel.getObstacleByCourseId(course.id)
            }
        }

        when (val result = obstacleResult.value) {
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