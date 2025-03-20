package com.example.projet_parkour.bdd
import androidx.room.*
import java.util.Date


@Entity
data class Competitor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val bornAt: Date,
    val gender: Gender
)

enum class Gender { H, F }