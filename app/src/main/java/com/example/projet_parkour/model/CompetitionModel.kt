package com.example.projet_parkour.model

data class CompetitionModel(
    val age_max: Int,
    val age_min: Int,
    val created_at: String,
    val gender: String,
    val has_retry: Boolean,
    val id: Int,
    val name: String,
    val status: String,
    val updated_at: String
)