package com.example.projet_parkour.bdd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.*

@Dao
interface PerfDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertPerf(perf: Perf)
    @Query("SELECT * FROM Perf") fun getAllPerf(): List<Perf>
    @Query("SELECT DISTINCT competitorId FROM Perf") fun getAllCompetitorsInPerf(): List<Int>
    @Query("SELECT DISTINCT courseId FROM Perf") fun getAllCoursesInPerf(): List<Int>
    @Query("SELECT * FROM Perf where competitorId = :competitorId and courseId = :courseId") fun getPerfByCompetitorIdAndCourseId(competitorId : Int, courseId : Int): List<Perf>
}