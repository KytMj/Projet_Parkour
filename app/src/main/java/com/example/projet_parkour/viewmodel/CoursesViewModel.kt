package com.example.projet_parkour.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.api.RetrofitInstance
import com.example.projet_parkour.model.CompetitionModel
import com.example.projet_parkour.model.CoursesModel
import com.example.projet_parkour.model.CreationCompetitionModelItem
import com.example.projet_parkour.model.CreationCompetitorModelItem
import com.example.projet_parkour.model.CreationCourseModelItem
import kotlinx.coroutines.launch

class CoursesViewModel : ViewModel() {

    private val api = RetrofitInstance.api
    private val _coursesResult = MutableLiveData<NetworkResponse<CoursesModel>>()
    val coursesResult : LiveData<NetworkResponse<CoursesModel>> = _coursesResult

    private val _createCourseResult = MutableLiveData<NetworkResponse<CreationCourseModelItem>>()
    val createCourseResult : LiveData<NetworkResponse<CreationCourseModelItem>> = _createCourseResult

    var nameCourse = MutableLiveData("")
    var maxDurationCourse = MutableLiveData("")

    fun getCoursesByCompetitionId(competitionId : Int){
        _coursesResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getCoursesByCompetitionId(competitionId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _coursesResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _coursesResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. (error)")
                }
            }
            catch (ex : Exception){
                _coursesResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun createCourse(course : CreationCourseModelItem){
        viewModelScope.launch {
            try {
                val response = api.createCourse(course)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _createCourseResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _createCourseResult.value = NetworkResponse.Error("" +
                            "Les données n'ont pas réussi à être chargées. (error " +response.code().toString() + ")" +
                            " "+response.errorBody().toString())
                }
            }
            catch (ex : Exception){
                _createCourseResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

}