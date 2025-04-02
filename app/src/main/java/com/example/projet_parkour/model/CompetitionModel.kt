package com.example.projet_parkour.model

import com.example.projet_parkour.bdd.Competition
import com.example.projet_parkour.bdd.CompetitionStatus

class CompetitionModel : ArrayList<CompetitionModelItem>()

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


data class CreationCompetitionModelItem(
    val name: String,
    val age_min: Int,
    val age_max: Int,
    val gender: String,
    val has_retry: Int,
)