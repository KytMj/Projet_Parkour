package com.example.projet_parkour.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.api.RetrofitInstance
import com.example.projet_parkour.model.CompetitionModel
import com.example.projet_parkour.model.CourseObstacleModel
import com.example.projet_parkour.model.CreationCompetitionModelItem
import com.example.projet_parkour.model.CreationObstacleModelItem
import com.example.projet_parkour.model.ObstacleModel
import com.example.projet_parkour.model.ObstacleModelItem
import kotlinx.coroutines.launch

class ObstaclesViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val _obstaclesResult = MutableLiveData<NetworkResponse<ObstacleModel>>()
    val obstaclesResult : LiveData<NetworkResponse<ObstacleModel>> = _obstaclesResult

    private val _obstaclesByCourseResult = MutableLiveData<NetworkResponse<Pair<Int, CourseObstacleModel>>>()
    val obstaclesByCourseResult : LiveData<NetworkResponse<Pair<Int, CourseObstacleModel>>> = _obstaclesByCourseResult

    private val _createObstacleResult = MutableLiveData<NetworkResponse<ObstacleModelItem>>()
    val createObstacleResult : LiveData<NetworkResponse<ObstacleModelItem>> = _createObstacleResult

    var nameObstacle = MutableLiveData("")

    fun getObstacles(){
        _obstaclesResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getObstacles()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _obstaclesResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _obstaclesResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. (error "+response.code().toString() + ")");
                }
            }
            catch (ex : Exception){
                _obstaclesResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun getObstaclesByCourseId(courseId : Int){
        _obstaclesByCourseResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getObstaclesByCourseId(courseId)
                if (response.isSuccessful) {
                    response.body()?.let { obstacleList ->
                        val result = Pair(courseId, obstacleList)

                        _obstaclesByCourseResult.value = NetworkResponse.Success(result)
                    } ?: run {
                        _obstaclesByCourseResult.value = NetworkResponse.Error("Réponse vide.")
                    }
                } else {
                    _obstaclesByCourseResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. (error)")
                }
            } catch (ex: Exception) {
                _obstaclesByCourseResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n${ex}\nmessage: ${ex.message}")
            }
        }
    }

    fun createObstacle(obstacle : CreationObstacleModelItem) {
        viewModelScope.launch {
            try {
                val response = api.createObstacle(obstacle)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _createObstacleResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _createObstacleResult.value = NetworkResponse.Error(
                        "" +
                                "Les données n'ont pas réussi à être chargées. (error " + response.code()
                            .toString() + ")" +
                                " " + response.errorBody().toString()
                    )
                }
            } catch (ex: Exception) {
                _createObstacleResult.value =
                    NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n" + ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }


}