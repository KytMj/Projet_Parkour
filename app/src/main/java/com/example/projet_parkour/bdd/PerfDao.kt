package com.example.projet_parkour.bdd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PerfDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertPerf(perf: Perf)
}