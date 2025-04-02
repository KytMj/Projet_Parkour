package com.example.projet_parkour.bdd

import androidx.room.*

@Entity
data class Remember (
    @PrimaryKey val competitionId : Int,
    val courseIndex : Int,
    val competitorIndex : Int,
    val obstacleIndex : Int
)