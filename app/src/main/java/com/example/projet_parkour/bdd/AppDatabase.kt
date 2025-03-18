package com.example.projet_parkour.bdd

import androidx.room.*

@Database(
    entities = [Competition::class, Competitor::class, CompetitionCompetitor::class,
        Course::class, Obstacle::class, CourseObstacle::class, Performance::class,
        PerformanceObstacle::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun competitionDao(): CompetitionDao
}