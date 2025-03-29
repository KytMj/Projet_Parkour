package com.example.projet_parkour.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.api.RetrofitInstance
import com.example.projet_parkour.model.CompetitorModel
import com.example.projet_parkour.model.CreationCompetitionModelItem
import com.example.projet_parkour.model.CreationCompetitorModelItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

class CompetitorsViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val _competitorResult = MutableLiveData<NetworkResponse<CompetitorModel>>()
    val competitorResult : LiveData<NetworkResponse<CompetitorModel>> = _competitorResult

    private val _createCompetitorResult = MutableLiveData<NetworkResponse<CreationCompetitorModelItem>>()
    val createCompetitorResult : LiveData<NetworkResponse<CreationCompetitorModelItem>> = _createCompetitorResult

    var firstName = MutableLiveData("")
    var lastName = MutableLiveData("")
    var email = MutableLiveData("")
    var phone = MutableLiveData("")
    var bornAt = MutableLiveData("")

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

    fun getPotentialCompetitors(){
        _competitorResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getCompetitors()
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

    fun createCompetitor(competitor : CreationCompetitorModelItem){
        viewModelScope.launch {
            try {
                val response = api.createCompetitor(competitor)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _createCompetitorResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _createCompetitorResult.value = NetworkResponse.Error("" +
                            "Les données n'ont pas réussi à être chargées. (error " +response.code().toString() + ")" +
                            " "+response.errorBody().toString())
                }
            }
            catch (ex : Exception){
                _createCompetitorResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }
}