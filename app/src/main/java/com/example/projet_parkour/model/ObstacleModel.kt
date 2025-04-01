package com.example.projet_parkour.model

import androidx.recyclerview.widget.SortedList
import java.util.SortedSet

class ObstacleModel : ArrayList<ObstacleModelItem>()

data class ObstacleModelItem(
    val created_at: String,
    val id: Int,
    val name: String,
    val picture: Any,
    val updated_at: String
)

class CourseObstacleModel : ArrayList<CourseObstacleModelItem>()

data class CourseObstacleModelItem(
    val id : Int,
    val obstacle_name : String,
    val position : Int
)