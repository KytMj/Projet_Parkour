package com.example.projet_parkour.bdd

import androidx.room.*

@Dao
interface CourseObstacleDao {
    @Insert fun insertObstacle(obstacle: Obstacle)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertObstacles(obstacles: List<Obstacle>)
    @Query("SELECT * FROM Obstacle") fun getAllObstacle(): List<Obstacle>
}
