package com.example.projet_parkour.model

class PerformanceModel : ArrayList<PerformanceModelItem>()

data class PerformanceModelItem(
    val id: Int,
    val competitor_id: Int,
    val course_id: Int,
    val created_at: String,
    val status: String,
    val total_time: Int,
    val updated_at: String
)

data class PerformanceCreateModelItem(
    val competitor_id : Int,
    val course_id : Int,
    val status : String,
    val total_time: Int
)

data class Perfs(
    val obstacleId : Int,
    val time : Long,
    val hasfell : Boolean
)