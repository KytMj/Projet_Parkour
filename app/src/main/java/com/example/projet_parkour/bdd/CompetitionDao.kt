package com.example.projet_parkour.bdd

import androidx.room.*

@Dao
interface CompetitionDao {
    @Insert fun insertCompetition(competition: Competition)
    @Query("SELECT * FROM Competition") fun getAllCompetitions(): List<Competition>
}
