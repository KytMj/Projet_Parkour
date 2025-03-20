package com.example.projet_parkour.bdd

import androidx.room.*

@Entity(primaryKeys = ["courseId", "obstacleId"])
data class CourseObstacle(
    val courseId: Int,
    val obstacleId: Int,
    val position: Int
)
