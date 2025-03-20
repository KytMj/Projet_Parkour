package com.example.projet_parkour.bdd

import androidx.room.*

@Entity
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val maxDuration: Int,
    val position: Int,
    val isOver: Boolean,
    val competitionId: Int
)