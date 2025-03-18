package com.example.projet_parkour.bdd

import androidx.room.*

@Entity
data class Performance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courseId: Int,
    val competitorId: Int,
    val status: PerformanceStatus,
    val totalTime: Int
)

enum class PerformanceStatus { DEFECTION, TO_FINISH, TO_VERIFY, OVER }