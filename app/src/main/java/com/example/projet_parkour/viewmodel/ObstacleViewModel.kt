package com.example.projet_parkour.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.api.RetrofitInstance
import com.example.projet_parkour.model.CompetitionModel
import com.example.projet_parkour.model.CoursesModel
import com.example.projet_parkour.model.CreationCompetitionModelItem
import com.example.projet_parkour.model.ObstacleModel
import kotlinx.coroutines.launch

class ObstacleViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val _obstaclesResult = MutableLiveData<NetworkResponse<ObstacleModel>>()
    val obstacleResult : LiveData<NetworkResponse<ObstacleModel>> = _obstaclesResult


    fun getObstacleByCourseId(courseID: Int){
        _obstaclesResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getObstaclesByCourseId(courseID)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _obstaclesResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _obstaclesResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. (error)")
                }
            }
            catch (ex : Exception){
                _obstaclesResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }
}