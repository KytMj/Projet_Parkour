package com.example.projet_parkour.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.api.RetrofitInstance
import com.example.projet_parkour.model.CourseObstacleModel
import com.example.projet_parkour.model.ObstacleModel
import kotlinx.coroutines.launch

class CourseObstacleViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val _obstaclesResult = MutableLiveData<NetworkResponse<Pair<Int, CourseObstacleModel>>>()
    val obstaclesResult: LiveData<NetworkResponse<Pair<Int, CourseObstacleModel>>> = _obstaclesResult

    fun getObstacleByCourseId(courseID: Int) {
        _obstaclesResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getObstaclesByCourseId(courseID)
                if (response.isSuccessful) {
                    response.body()?.let { obstacleList ->
                        val result = Pair(courseID, obstacleList)

                        _obstaclesResult.value = NetworkResponse.Success(result)
                    } ?: run {
                        _obstaclesResult.value = NetworkResponse.Error("Réponse vide.")
                    }
                } else {
                    _obstaclesResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. (error)")
                }
            } catch (ex: Exception) {
                _obstaclesResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n${ex}\nmessage: ${ex.message}")
            }
        }
    }
}