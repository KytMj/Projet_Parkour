package com.example.projet_parkour.api

import com.example.projet_parkour.model.CompetitionModel
import com.example.projet_parkour.model.CoursesModel
import retrofit2.Response
import retrofit2.http.GET

interface API {

    @GET("/competitions")
    suspend fun getCompetitions() : Response<CompetitionModel>

    @GET("/courses")
    suspend fun getCourses() : Response<CoursesModel>
}