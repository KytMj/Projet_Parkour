package com.example.projet_parkour.model

class ObstacleModel : ArrayList<ObstacleModelItem>()

data class ObstacleModelItem(
    val created_at: String,
    val id: Int,
    val name: String,
    val picture: Any,
    val updated_at: String
)