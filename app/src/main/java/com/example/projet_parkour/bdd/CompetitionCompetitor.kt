package com.example.projet_parkour.bdd

import androidx.room.*

@Entity(primaryKeys = ["competitorId", "competitionId"])
data class CompetitionCompetitor(
    val competitorId: Int,
    val competitionId: Int,
    val inscriptionDate: String
)