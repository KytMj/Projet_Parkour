package com.example.projet_parkour.api

import com.example.projet_parkour.model.CompetitionModel
import com.example.projet_parkour.model.CompetitionModelItem
import com.example.projet_parkour.model.CompetitorModel
import com.example.projet_parkour.model.CoursesModel
import com.example.projet_parkour.model.CreationCompetitionModelItem
import com.example.projet_parkour.model.ObstacleModel
import com.example.projet_parkour.model.PerformanceModel
import com.example.projet_parkour.model.PerformanceObstacleModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
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
    suspend fun createCompetitions(@Body competition : CreationCompetitionModelItem) : Response<CreationCompetitionModelItem>


    //COMPETITORS
    @GET("/api/competitions/{id}/inscriptions")
    suspend fun getCompetitorsByCompetitionId(@Path("id") id : Int) : Response<CompetitorModel>

    @GET("/api/competitors")
    suspend fun getCompetitors() : Response<CompetitorModel>

    @GET("/api/competitors/{id}")
    suspend fun getCompetitorsById(@Path("id") id : Int) : Response<CompetitorModel>


    //COURSES
    @GET("/api/competitions/{id}/courses")
    suspend fun getCoursesByCompetitionId(@Path("id") id : Int) : Response<CoursesModel>

    @GET("/api/competitors/{id}/courses")
    suspend fun getCoursesByCompetitorId(@Path("id") id : Int) : Response<CoursesModel>

    @GET("/api/courses")
    suspend fun getCourses() : Response<CoursesModel>

    @GET("/api/courses/{id}")
    suspend fun getCoursesById(@Path("id") id : Int) : Response<CoursesModel>


    //OBSTACLES
    @GET("/api/courses/{id}/obstacles")
    suspend fun getObstaclesByCourseId(@Path("id") id : Int) : Response<ObstacleModel>

    @GET("/api/obstacles")
    suspend fun getObstacles() : Response<ObstacleModel>

    @GET("/api/obstacles/{id}")
    suspend fun getObstaclesById(@Path("id") id : Int) : Response<ObstacleModel>


    //PERFORMANCE OBSTACLES
    @GET("/api/competitors/{id}/{id_course}/details_performances")
    suspend fun getPerformanceObstaclesByCompetitorId(@Path("id") id : Int, @Path("id_course") idCourse : Int) : Response<PerformanceObstacleModel>

    @GET("/api/performance_obstacles")
    suspend fun getPerformanceObstacles() : Response<PerformanceObstacleModel>

    @GET("/api/performance_obstacles/{id}")
    suspend fun getPerformanceObstaclesById(@Path("id") id : Int) : Response<PerformanceObstacleModel>


    //PERFORMANCES
    @GET("/api/competitors/{id}/performances")
    suspend fun getPerformancesByCompetitorId(@Path("id") id : Int) : Response<PerformanceModel>

    @GET("/api/performances")
    suspend fun getPerformances() : Response<PerformanceModel>

    @GET("/api/performances/{id}")
    suspend fun getPerformancesById(@Path("id") id : Int) : Response<PerformanceModel>
}