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
import com.example.projet_parkour.model.MessageModel
import com.example.projet_parkour.model.ObstacleModel
import com.example.projet_parkour.model.ObstacleModelItem
import com.example.projet_parkour.model.PerformanceCreateModelItem
import com.example.projet_parkour.model.PerformanceModel
import kotlinx.coroutines.launch

class PerformancesViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val _performancesResult = MutableLiveData<NetworkResponse<PerformanceModel>>()
    val performancesResult: LiveData<NetworkResponse<PerformanceModel>> = _performancesResult

    private val _performancesByIdResult = MutableLiveData<NetworkResponse<PerformanceModel>>()
    val performancesByIdResult: LiveData<NetworkResponse<PerformanceModel>> = _performancesByIdResult

    private val _performancesByCompetitorIdResult = MutableLiveData<NetworkResponse<PerformanceModel>>()
    val performancesByCompetitorIdResult: LiveData<NetworkResponse<PerformanceModel>> = _performancesByCompetitorIdResult

    private val _createPerformanceResult = MutableLiveData<NetworkResponse<MessageModel>>()
    val createPerformanceResult: LiveData<NetworkResponse<MessageModel>> = _createPerformanceResult

    fun getPerformances() {
        _performancesResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getPerformances()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _performancesResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _performancesResult.value = NetworkResponse.Error(
                        "Les données n'ont pas réussi à être chargées. (error " + response.code()
                            .toString() + ")"
                    );
                }
            } catch (ex: Exception) {
                _performancesResult.value =
                    NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n" + ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun getPerformancesById(performanceId: Int) {
        _performancesByIdResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getPerformancesById(performanceId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _performancesByIdResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _performancesByIdResult.value = NetworkResponse.Error(
                        "Les données n'ont pas réussi à être chargées. (error " + response.code()
                            .toString() + ")"
                    );
                }
            } catch (ex: Exception) {
                _performancesByIdResult.value =
                    NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n" + ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun getPerformancesByCompetitorId(competitorId: Int) {
        _performancesByCompetitorIdResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getPerformancesByCompetitorId(competitorId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _performancesByCompetitorIdResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _performancesByCompetitorIdResult.value = NetworkResponse.Error(
                        "Les données n'ont pas réussi à être chargées. (error " + response.code()
                            .toString() + ")"
                    );
                }
            } catch (ex: Exception) {
                _performancesByCompetitorIdResult.value =
                    NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n" + ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun createPerformance(performance : PerformanceCreateModelItem) {
        viewModelScope.launch {
            try {
                val response = api.createPerformance(performance)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _createPerformanceResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _createPerformanceResult.value = NetworkResponse.Error(
                        "" +
                                "Les données n'ont pas réussi à être chargées. (error " + response.code()
                            .toString() + ")" +
                                " " + response.errorBody().toString()
                    )
                }
            } catch (ex: Exception) {
                _createPerformanceResult.value =
                    NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n" + ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }
}