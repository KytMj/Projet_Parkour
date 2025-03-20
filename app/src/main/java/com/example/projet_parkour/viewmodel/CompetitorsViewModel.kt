package com.example.projet_parkour.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.api.RetrofitInstance
import com.example.projet_parkour.model.CompetitorModel
import kotlinx.coroutines.launch

class CompetitorsViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val _competitorResult = MutableLiveData<NetworkResponse<CompetitorModel>>()
    val competitorResult : LiveData<NetworkResponse<CompetitorModel>> = _competitorResult

    fun getCompetitorsByCompetitionId(competitionId : Int){
        _competitorResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getCompetitorsByCompetitionId(competitionId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _competitorResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _competitorResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. (error "+response.code().toString() + ")");
                }
            }
            catch (ex : Exception){
                _competitorResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }
}