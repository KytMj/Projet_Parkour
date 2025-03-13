package com.example.projet_parkour.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.api.RetrofitInstance
import com.example.projet_parkour.model.CompetitionModel
import kotlinx.coroutines.launch

class CompetitionsViewModel : ViewModel() {

    private val api = RetrofitInstance.api
    private val _competitionResult = MutableLiveData<NetworkResponse<CompetitionModel>>()
    val competitionResult : LiveData<NetworkResponse<CompetitionModel>> = _competitionResult

    fun getCompetitions(){
        _competitionResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getCompetitions()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _competitionResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _competitionResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. (error)")
                }
            }
            catch (ex : Exception){
                _competitionResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

}