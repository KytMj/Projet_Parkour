package com.example.projet_parkour.bdd

import androidx.room.*

@Dao
interface RememberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRemember(remember: Remember)
    @Query("SELECT * FROM REMEMBER WHERE competitionId = :p_cId") fun getLastRemember(p_cId : Int) : Remember?
}