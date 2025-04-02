package com.example.projet_parkour.bdd

import androidx.room.*

@Dao
interface CourseDao {
    @Insert fun insertCourse(course: Course)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCourses(courses: List<Course>)
    @Query("SELECT * FROM Course") fun getAllCourse(): List<Course>
}
