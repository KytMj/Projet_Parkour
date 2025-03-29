package com.example.projet_parkour.bdd

import androidx.room.*

@Entity
data class Course(
    @PrimaryKey val id: Int,
    val name: String,
    val maxDuration: Int,
    val position: Int,
    val isOver: Int,
    val competitionId: Int
)