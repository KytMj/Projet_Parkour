package com.example.projet_parkour.bdd

import androidx.room.*
import java.util.Date

@Entity(primaryKeys = ["competitorId", "competitionId"])
data class CompetitionCompetitor(
    val competitorId: Int,
    val competitionId: Int,
    val inscriptionDate: Date
)