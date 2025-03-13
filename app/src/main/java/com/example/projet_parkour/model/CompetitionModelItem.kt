package com.example.projet_parkour.model

data class CompetitionModelItem(
    val age_max: Int,
    val age_min: Int,
    val created_at: String,
    val gender: String,
    val has_retry: Int,
    val id: Int? = null,
    val name: String,
    val status: String,
    val updated_at: String
)