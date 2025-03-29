package com.example.projet_parkour.bdd

import androidx.room.*

@Entity
data class Competition(
    @PrimaryKey val id: Int?,
    val name: String,
    val ageMin: Int,
    val ageMax: Int,
    val gender: Char,
    val hasTry: Boolean,
    val status: String
)


enum class CompetitionStatus { NOT_READY, NOT_STARTED, STARTED, FINISHED }