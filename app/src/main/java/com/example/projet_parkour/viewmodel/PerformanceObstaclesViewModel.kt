package com.example.projet_parkour.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.api.RetrofitInstance
import com.example.projet_parkour.model.PerformanceObstacleModel
import kotlinx.coroutines.launch

class PerformanceObstaclesViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val _performanceObstaclesResult = MutableLiveData<NetworkResponse<PerformanceObstacleModel>>()
    val performanceObstaclesResult: LiveData<NetworkResponse<PerformanceObstacleModel>> = _performanceObstaclesResult

    private val _performanceObstaclesByIdResult = MutableLiveData<NetworkResponse<PerformanceObstacleModel>>()
    val performanceObstaclesByIdResult: LiveData<NetworkResponse<PerformanceObstacleModel>> = _performanceObstaclesByIdResult

    private val _performanceObstaclesByPerformanceIdResult = MutableLiveData<NetworkResponse<PerformanceObstacleModel>>()
    val performanceObstaclesByPerformanceIdResult: LiveData<NetworkResponse<PerformanceObstacleModel>> = _performanceObstaclesByPerformanceIdResult

    private val _performanceObstaclesByCompetitorIdResult = MutableLiveData<NetworkResponse<PerformanceObstacleModel>>()
    val performanceObstaclesByCompetitorIdResult: LiveData<NetworkResponse<PerformanceObstacleModel>> = _performanceObstaclesByCompetitorIdResult

    fun getPerformanceObstacles() {
        _performanceObstaclesResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getPerformanceObstacles()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _performanceObstaclesResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _performanceObstaclesResult.value = NetworkResponse.Error(
                        "Les données n'ont pas réussi à être chargées. (error " + response.code()
                            .toString() + ")"
                    );
                }
            } catch (ex: Exception) {
                _performanceObstaclesResult.value =
                    NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n" + ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun getPerformanceObstaclesById(obstacleId :Int) {
        _performanceObstaclesByIdResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getPerformanceObstaclesById(obstacleId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _performanceObstaclesByIdResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _performanceObstaclesByIdResult.value = NetworkResponse.Error(
                        "Les données n'ont pas réussi à être chargées. (error " + response.code()
                            .toString() + ")"
                    );
                }
            } catch (ex: Exception) {
                _performanceObstaclesByIdResult.value =
                    NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n" + ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun getPerformanceObstaclesByPerformanceId(performanceId :Int) {
        _performanceObstaclesByPerformanceIdResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getPerformanceObstaclesByPerformanceId(performanceId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _performanceObstaclesByPerformanceIdResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _performanceObstaclesByPerformanceIdResult.value = NetworkResponse.Error(
                        "Les données n'ont pas réussi à être chargées. (error " + response.code()
                            .toString() + ")"
                    );
                }
            } catch (ex: Exception) {
                _performanceObstaclesByPerformanceIdResult.value =
                    NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n" + ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun getPerformanceObstaclesByCompetitorId(competitorId :Int, courseId: Int) {
        _performanceObstaclesByCompetitorIdResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getPerformanceObstaclesByCompetitorId(competitorId, courseId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _performanceObstaclesByCompetitorIdResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _performanceObstaclesByCompetitorIdResult.value = NetworkResponse.Error(
                        "Les données n'ont pas réussi à être chargées. (error " + response.code()
                            .toString() + ")"
                    );
                }
            } catch (ex: Exception) {
                _performanceObstaclesByCompetitorIdResult.value =
                    NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n" + ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }
}