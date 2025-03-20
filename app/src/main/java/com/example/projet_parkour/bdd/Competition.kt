package com.example.projet_parkour.bdd

import androidx.room.*

@Entity
data class Competition(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val ageMin: Int,
    val ageMax: Int,
    val gender: Char,
    val hasTry: Boolean,
    val status: CompetitionStatus
)

enum class CompetitionStatus { NOT_READY, NOT_STARTED, STARTED, FINISHED }