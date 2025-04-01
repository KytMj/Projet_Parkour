package com.example.projet_parkour.bdd

import android.content.Context
import androidx.room.*

@Database(
    entities = [Competition::class, Competitor::class, CompetitionCompetitor::class,
        Course::class, Obstacle::class, CourseObstacle::class, Performance::class,
        PerformanceObstacle::class, Perf::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun perfDao(): PerfDao
    abstract fun competitionDao(): CompetitionDao
    abstract fun courseDao(): CourseDao
    abstract fun courseObstacleDao(): CourseObstacleDao
    abstract fun obstacleDao(): ObstacleDao
    abstract fun competitorDao(): CompetitorDao


    companion object{
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}