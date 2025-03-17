package com.example.projet_parkour.model

class CoursesModel : ArrayList<CoursesModelItem>()

data class CoursesModelItem(
    val competition_id: Int,
    val created_at: String,
    val id: Int,
    val is_over: Int,
    val max_duration: Int,
    val name: String,
    val position: Int,
    val updated_at: String
)