package com.example.projet_parkour.model

class ObstacleModel : ArrayList<ObstacleModelItem>()
class CourseObstacleModel : ArrayList<CourseObstacleModelItem>()

data class ObstacleModelItem(
    val created_at: String,
    val id: Int,
    val name: String,
    val picture: Any,
    val updated_at: String
)

data class CreationObstacleModelItem(
    val name: String
)

data class CourseObstacleModelItem(
    val id: Int,
    val obstacle_name: String,
    val position : Int
)

data class ObstacleIdModelItem(
    val obstacle_id: Int
)