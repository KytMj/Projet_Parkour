package com.example.projet_parkour.bdd

import androidx.room.*

@Dao
interface CompetitionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCompetition(competition: Competition)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCompetitions(courses: List<Competition>)
    @Query("SELECT * FROM Competition") fun getAllCompetitions(): List<Competition>
    @Query("SELECT * FROM Competition WHERE id = :p_id") fun getCompetitionByID(p_id:Int): Competition?
}
