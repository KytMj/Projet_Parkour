package com.example.projet_parkour.bdd

import androidx.room.*

@Entity
data class Obstacle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val picture: ByteArray
)