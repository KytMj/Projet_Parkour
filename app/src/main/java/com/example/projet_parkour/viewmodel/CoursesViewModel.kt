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
import kotlinx.coroutines.launch

class CoursesViewModel : ViewModel() {

    private val api = RetrofitInstance.api
    private val _coursesResult = MutableLiveData<NetworkResponse<CoursesModel>>()
    val coursesResult : LiveData<NetworkResponse<CoursesModel>> = _coursesResult

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

}