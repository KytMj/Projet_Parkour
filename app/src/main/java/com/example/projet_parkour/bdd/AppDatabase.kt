package com.example.projet_parkour.bdd

import android.content.Context
import androidx.room.*

@Database(
    entities = [Competition::class, Competitor::class, CompetitionCompetitor::class,
        Course::class, Obstacle::class, CourseObstacle::class, Performance::class,
        PerformanceObstacle::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun competitionDao(): CompetitionDao
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