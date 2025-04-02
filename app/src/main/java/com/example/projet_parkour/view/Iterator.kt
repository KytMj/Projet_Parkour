package com.example.projet_parkour.view

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.projet_parkour.model.CompetitorModel
import com.example.projet_parkour.model.CompetitorModelItem
import com.example.projet_parkour.model.CourseObstacleModel
import com.example.projet_parkour.model.CoursesModel

class Iterator(courses : CoursesModel, competitors : CompetitorModel, obstacles : HashMap<Int, CourseObstacleModel>) {

    private val courses = courses
    private val competitors = competitors
    private val obstacles = obstacles



    @Composable
    fun getNext() {
        var indexCourse by remember { mutableStateOf(0) }
        var indexObstacle by remember { mutableStateOf(0) }
        var indexCompetitor by remember { mutableStateOf(0) }
        Button(onClick = {
            if (obstacles.get(courses[indexCourse].id)?.isNotEmpty() == true) {
                val currentObstacle = obstacles.get(courses[indexCourse].id).orEmpty()
                if (indexObstacle + 1 < currentObstacle.size) {
                    indexObstacle++
                } else {
                    indexObstacle = 0
                    if (indexCompetitor + 1 < competitors.size) {
                        indexCompetitor++
                    } else {
                        indexCompetitor = 0
                        if (indexCourse + 1 < courses.size) {
                            indexCourse++
                        } else {

                        }
                    }
                }
            }
        }) { Text("next") }

        Text(courses[indexCourse].name)
        Text(competitors[indexCompetitor].first_name)
        Text(obstacles.get(courses[indexCourse].id)?.get(indexObstacle)?.obstacle_name ?: "error")
    }
}