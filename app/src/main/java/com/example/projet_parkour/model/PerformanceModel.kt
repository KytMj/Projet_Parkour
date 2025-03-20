package com.example.projet_parkour.model

class PerformanceModel : ArrayList<PerformanceModelItem>()

data class PerformanceModelItem(
    val competitor_id: Int,
    val course_id: Int,
    val created_at: String,
    val id: Int,
    val status: String,
    val total_time: Int,
    val updated_at: String
)