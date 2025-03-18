package com.example.projet_parkour.bdd

import androidx.room.*

@Entity
data class PerformanceObstacle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val time: Int,
    val hasFell: Boolean,
    val toVerify: Boolean
)