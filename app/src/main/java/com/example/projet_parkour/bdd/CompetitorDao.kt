package com.example.projet_parkour.bdd

import androidx.room.*

@Dao
interface CompetitorDao {
    @Insert fun insertCompetitor(competitor: Competitor)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCompetitors(competitors: List<Competitor>)
    @Query("SELECT * FROM Competitor") fun getAllCompetitor(): List<Competitor>
}
