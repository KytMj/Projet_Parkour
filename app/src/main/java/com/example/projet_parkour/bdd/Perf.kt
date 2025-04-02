package com.example.projet_parkour.bdd

import androidx.room.*

@Entity(primaryKeys = ["courseId", "competitorId", "obstacleId"])
data class Perf (
    val courseId : Int,
    val competitorId : Int,
    val obstacleId : Int,
    val has_fell : Int,
    val time : Int
)