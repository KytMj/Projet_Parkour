package com.example.projet_parkour.api

import com.example.projet_parkour.model.CompetitionModel
import com.example.projet_parkour.model.CompetitionModelItem
import com.example.projet_parkour.model.CoursesModel
import com.example.projet_parkour.model.CreationCompetitionModelItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface API {

    @GET("/competitions")
    suspend fun getCompetitions() : Response<CompetitionModel>

    @POST("/competitions")
    suspend fun createCompetitions(@Body compet : CreationCompetitionModelItem) : Response<CreationCompetitionModelItem>

    @GET("/courses")
    suspend fun getCourses() : Response<CoursesModel>
}