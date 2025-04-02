package com.example.projet_parkour.bdd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PerfDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertPerf(perf: Perf)
    @Query("SELECT * FROM Perf ") fun getAllPerf(competitorId : Int, courseId : Int): List<Perf>
}