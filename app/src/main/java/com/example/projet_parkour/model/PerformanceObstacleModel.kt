package com.example.projet_parkour.model

class PerformanceObstacleModel : ArrayList<PerformanceObstacleModelItem>()

data class PerformanceObstacleModelItem(
    val created_at: String,
    val has_fell: Int,
    val id: Int,
    val obstacle_id: Int,
    val performance_id: Int,
    val time: Int,
    val to_verify: Int,
    val updated_at: String
)