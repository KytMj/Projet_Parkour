package com.example.projet_parkour.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.api.RetrofitInstance
import com.example.projet_parkour.model.CompetitionModel
import com.example.projet_parkour.model.CompetitionModelItem
import com.example.projet_parkour.model.CreationCompetitionModelItem
import kotlinx.coroutines.launch

class CompetitionsViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val _competitionResult = MutableLiveData<NetworkResponse<CompetitionModel>>()
    val competitionResult : LiveData<NetworkResponse<CompetitionModel>> = _competitionResult

    private val _createCompetitionResult = MutableLiveData<NetworkResponse<CompetitionModelItem>>()
    val createCompetitionResult : LiveData<NetworkResponse<CompetitionModelItem>> = _createCompetitionResult

    val nameCompetition = MutableLiveData("")
    val ageMiniCompet = MutableLiveData("")
    val ageMaxiCompet = MutableLiveData("")

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
                    _competitionResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. (error "+response.code().toString() + ")")
                }
            }
            catch (ex : Exception){
                _competitionResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }


    private fun createCompetition(competition : CreationCompetitionModelItem){
        viewModelScope.launch {
            try {
                val response = api.createCompetitions(competition)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _createCompetitionResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _createCompetitionResult.value = NetworkResponse.Error("" +
                            "Les données n'ont pas réussi à être chargées. (error " +response.code().toString() + ")" +
                            " "+response.errorBody().toString())
                }
            }
            catch (ex : Exception){
                _createCompetitionResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun checkAndAddCompetition(
        selectedOptionGender: MutableState<String>,
        checkedHasRetry: MutableState<Boolean>,
        context: Context
    ): Boolean {
        val gender = when (selectedOptionGender.value) {
            "Homme" -> "H"; "Femme" -> "F"
            else -> {
                ""
            }
        }
        val age_max = ageMaxiCompet.value?.toInt() ?:0
        val age_min = ageMiniCompet.value?.toInt() ?: 0
        val name = nameCompetition.value.toString()

        if (age_min > age_max){
            Toast.makeText(context, "L'âge minimum est plus grand que l'âge maximum", Toast.LENGTH_LONG).show()
            return false;
        }

        if (name == ""){
            Toast.makeText(context, "Le champ Nom Compétition ne contient rien", Toast.LENGTH_LONG).show()
            return false;
        }

        val competition = CreationCompetitionModelItem(
            age_max = ageMaxiCompet.value?.toInt() ?:0,
            age_min = ageMiniCompet.value?.toInt() ?: 0,
            gender = gender,
            has_retry = if(checkedHasRetry.value) 1 else 0,
            name = nameCompetition.value.toString()
        )

        createCompetition(competition)
        when (val result = createCompetitionResult.value) {
            is NetworkResponse.Error -> Toast.makeText(context, "error: "+ result.message, Toast.LENGTH_LONG).show()
            is NetworkResponse.Loading -> {}
            is NetworkResponse.Success -> {
                Toast.makeText(context, "Compétition bien ajoutée !", Toast.LENGTH_LONG).show()
            }
            null -> {}
        }
        return true;
    }

    fun isValidMaxAge(text: String): Boolean {
        return text.matches(Regex("^(?:[1-9]|[1-9][0-9])\$"))
    }

    fun isValidMinAge(text: String): Boolean {
        return text.matches(Regex("^(?:[1-9]|[1-9][0-9])\$"))
    }

    fun isValidName(text: String): Boolean {
        return text.matches(Regex("^[A-Za-z]{1,50}\$"))
    }
}