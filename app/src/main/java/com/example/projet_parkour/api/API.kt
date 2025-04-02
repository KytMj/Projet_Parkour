package com.example.projet_parkour.api

import com.example.projet_parkour.model.CompetitionModel
import com.example.projet_parkour.model.CompetitionModelItem
import com.example.projet_parkour.model.CompetitorIdModelItem
import com.example.projet_parkour.model.CompetitorModel
import com.example.projet_parkour.model.CompetitorModelItem
import com.example.projet_parkour.model.CourseObstacleModel
import com.example.projet_parkour.model.CoursesModel
import com.example.projet_parkour.model.CoursesModelItem
import com.example.projet_parkour.model.CreationCompetitionModelItem
import com.example.projet_parkour.model.CreationCompetitorModelItem
import com.example.projet_parkour.model.CreationCourseModelItem
import com.example.projet_parkour.model.CreationObstacleModelItem
import com.example.projet_parkour.model.MessageModel
import com.example.projet_parkour.model.ObstacleIdModelItem
import com.example.projet_parkour.model.ObstacleModel
import com.example.projet_parkour.model.ObstacleModelItem
import com.example.projet_parkour.model.PerformanceCreateModelItem
import com.example.projet_parkour.model.PerformanceModel
import com.example.projet_parkour.model.PerformanceObstacleCreateModelItem
import com.example.projet_parkour.model.PerformanceObstacleModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface API {

    //COMPETITIONS
    @GET("/api/competitions")
    suspend fun getCompetitions() : Response<CompetitionModel>

    @GET("/api/competitions/{id}")
    suspend fun getCompetitionsById(@Path("id") id : Int) : Response<CompetitionModel>

    @POST("/api/competitions")
    suspend fun createCompetitions(@Body competition : CreationCompetitionModelItem) : Response<CompetitionModelItem>

    @POST("/api/competitions/{id}/add_competitor")
    suspend fun addCompetitorCompetition(@Path("id") id: Int, @Body competitorId : CompetitorIdModelItem) : Response<MessageModel>


    //COMPETITORS
    @GET("/api/competitions/{id}/inscriptions")
    suspend fun getCompetitorsByCompetitionId(@Path("id") id : Int) : Response<CompetitorModel>

    @GET("/api/competitors")
    suspend fun getCompetitors() : Response<CompetitorModel>

    @GET("/api/competitors/{id}")
    suspend fun getCompetitorsById(@Path("id") id : Int) : Response<CompetitorModelItem>

    @POST("/api/competitors")
    suspend fun createCompetitor(@Body competitor : CreationCompetitorModelItem) : Response<CompetitorModelItem>

    @DELETE("/api/competitors/{id}")
    suspend fun deleteCompetitor(@Path("id") competitorId : CompetitorIdModelItem) : Response<MessageModel>


    //COURSES
    @GET("/api/competitions/{id}/courses")
    suspend fun getCoursesByCompetitionId(@Path("id") id : Int) : Response<CoursesModel>

    @GET("/api/competitors/{id}/courses")
    suspend fun getCoursesByCompetitorId(@Path("id") id : Int) : Response<CoursesModel>

    @GET("/api/courses")
    suspend fun getCourses() : Response<CoursesModel>

    @GET("/api/courses/{id}")
    suspend fun getCoursesById(@Path("id") id : Int) : Response<CoursesModel>

    @POST("/api/courses")
    suspend fun createCourse(@Body course : CreationCourseModelItem) : Response<CoursesModelItem>

    @POST("/api/courses/{id}/add_obstacle")
    suspend fun addObstacleToCourse(@Path("id") courseId : Int, @Body obstacleId : ObstacleIdModelItem) : Response<MessageModel>


    //OBSTACLES
    @GET("/api/courses/{id}/obstacles")
    suspend fun getObstaclesByCourseId(@Path("id") id : Int) : Response<CourseObstacleModel>

    @GET("/api/obstacles")
    suspend fun getObstacles() : Response<ObstacleModel>

    @GET("/api/obstacles/{id}")
    suspend fun getObstaclesById(@Path("id") id : Int) : Response<ObstacleModel>

    @POST("/api/obstacles")
    suspend fun createObstacle(@Body obstacle : CreationObstacleModelItem) : Response<ObstacleModelItem>


    //PERFORMANCE OBSTACLES
    @GET("/api/competitors/{id}/{id_course}/details_performances")
    suspend fun getPerformanceObstaclesByCompetitorId(@Path("id") id : Int, @Path("id_course") idCourse : Int) : Response<PerformanceObstacleModel>

    @GET("/api/performance_obstacles")
    suspend fun getPerformanceObstacles() : Response<PerformanceObstacleModel>

    @GET("/api/performance_obstacles/{id}")
    suspend fun getPerformanceObstaclesById(@Path("id") id : Int) : Response<PerformanceObstacleModel>

    @GET("/api/performances/{id}/details")
    suspend fun getPerformanceObstaclesByPerformanceId(@Path("id") id : Int) : Response<PerformanceObstacleModel>

    @POST("/api/performance_obstacles")
    suspend fun createPerformanceObstacles(@Body performanceObstacles : PerformanceObstacleCreateModelItem) : Response<MessageModel>


    //PERFORMANCES
    @GET("/api/competitors/{id}/performances")
    suspend fun getPerformancesByCompetitorId(@Path("id") id : Int) : Response<PerformanceModel>

    @GET("/api/performances")
    suspend fun getPerformances() : Response<PerformanceModel>

    @GET("/api/performances/{id}")
    suspend fun getPerformancesById(@Path("id") id : Int) : Response<PerformanceModel>

    @GET("/api/courses/{id}/performances")
    suspend fun getCoursePerformancesByCourseId(@Path("id") id : Int) : Response<PerformanceModel>

    @POST("/api/performances")
    suspend fun createPerformance(@Body performance : PerformanceCreateModelItem) : Response<MessageModel>
}