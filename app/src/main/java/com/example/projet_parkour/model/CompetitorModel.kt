package com.example.projet_parkour.model

class CompetitorModel : ArrayList<CompetitorModelItem>()

data class CompetitorModelItem(
    val born_at: String,
    val created_at: String,
    val email: String,
    val first_name: String,
    val gender: String,
    val id: Int,
    val last_name: String,
    val phone: String,
    val updated_at: String
)