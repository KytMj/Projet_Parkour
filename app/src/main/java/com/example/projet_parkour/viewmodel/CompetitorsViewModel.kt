package com.example.projet_parkour.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.api.RetrofitInstance
import com.example.projet_parkour.model.CompetitorIdModelItem
import com.example.projet_parkour.model.CompetitorModel
import com.example.projet_parkour.model.CompetitorModelItem
import com.example.projet_parkour.model.CreationCompetitorModelItem
import com.example.projet_parkour.model.MessageModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CompetitorsViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val _competitorResult = MutableLiveData<NetworkResponse<CompetitorModel>>()
    val competitorResult : LiveData<NetworkResponse<CompetitorModel>> = _competitorResult

    private val _competitorByCompetitionResult = MutableLiveData<NetworkResponse<CompetitorModel>>()
    val competitorByCompetitionResult : LiveData<NetworkResponse<CompetitorModel>> = _competitorByCompetitionResult

    private val _createCompetitorResult = MutableLiveData<NetworkResponse<CompetitorModelItem>>()
    val createCompetitorResult : LiveData<NetworkResponse<CompetitorModelItem>> = _createCompetitorResult

    private val _addCompetitorCompetitionResult = MutableLiveData<NetworkResponse<MessageModel>>()
    val addCompetitorCompetitionResult : LiveData<NetworkResponse<MessageModel>> = _addCompetitorCompetitionResult

    private val _deleteCompetitorResult = MutableLiveData<NetworkResponse<MessageModel>>()
    val deleteCompetitorResult : LiveData<NetworkResponse<MessageModel>> = _deleteCompetitorResult

    var firstName = MutableLiveData("")
    var lastName = MutableLiveData("")
    var email = MutableLiveData("")
    var phone = MutableLiveData("")
    var bornAt = MutableLiveData("")

    fun getCompetitorsByCompetitionId(competitionId : Int){
        _competitorByCompetitionResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getCompetitorsByCompetitionId(competitionId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _competitorByCompetitionResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _competitorByCompetitionResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. (error "+response.code().toString() + ")");
                }
            }
            catch (ex : Exception){
                _competitorByCompetitionResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun getCompetitors(){
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

    fun deleteCompetitor(competitorId : CompetitorIdModelItem){
        viewModelScope.launch {
            try {
                val response = api.deleteCompetitor(competitorId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _deleteCompetitorResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _deleteCompetitorResult.value = NetworkResponse.Error("" +
                            "Les données n'ont pas réussi à être chargées. (error " +response.code().toString() + ")" +
                            " "+response.errorBody().toString())
                }
            }
            catch (ex : Exception){
                _deleteCompetitorResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun addCompetitorCompetition(competitorId : CompetitorIdModelItem, competitionId : Int){
        viewModelScope.launch {
            try {
                val response = api.addCompetitorCompetition(competitionId, competitorId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _addCompetitorCompetitionResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _addCompetitorCompetitionResult.value = NetworkResponse.Error("" +
                            "Les données n'ont pas réussi à être chargées. (error " +response.code().toString() + ")" +
                            " "+response.errorBody().toString())
                    Log.d("deboggage", response.code().toString() + " - " + response.errorBody()
                            + "\n" + response.headers().toString() + "" + response.raw().request.url)
                }
            }
            catch (ex : Exception){
                _addCompetitorCompetitionResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun checkAndAddCompetitorToDB(
        selectedOptionGender: MutableState<String>,
        context: Context,
        mDate: MutableState<String>,
        competitionId: Int
    ): Boolean {
        val gender = when (selectedOptionGender.value) {
            "Homme" -> "H"; "Femme" -> "F"
            else -> {
                ""
            }
        }

        if (firstName.value == "" || lastName.value == "" || email.value == "" || phone.value == "") {
            Toast.makeText(context, "Champ(s) vide(s)", Toast.LENGTH_LONG).show()
            return false;
        }

        val competitor = CreationCompetitorModelItem(
            first_name = firstName.value.toString(),
            last_name = lastName.value.toString(),
            gender = gender,
            email = email.value.toString(),
            phone = phone.value.toString(),
            born_at = mDate.value
        );

        createCompetitor(competitor);

        when (val result = createCompetitorResult.value) {
            is NetworkResponse.Error -> {
                Toast.makeText(context, "error: " + result.message, Toast.LENGTH_LONG).show()
                return false
            }
            is NetworkResponse.Loading -> {}
            is NetworkResponse.Success -> {
                Toast.makeText(context, "Participant ajouté à la base", Toast.LENGTH_LONG).show()
            }

            null -> {}
        }
        return true
    }

    fun isValidName(text: String): Boolean {
        return text.matches(Regex("^[a-zA-Zéèëêàç]{1,50}\$"))
    }

    fun isValidEmail(text: String): Boolean {
        return text.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"))
    }

    fun isValidPhone(text: String): Boolean {
        return text.matches(Regex("^(\\+?\\d{1,3}[-.\\s]?)?(\\(?\\d{1,4}\\)?[-.\\s]?)?(\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4})\$"))
    }

    fun isValidDate(text: String): Boolean {
        return text.matches(Regex("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])\$"))
    }

    fun RegisterCompetitorOnClick (
        idAddCompetitor: Int,
        competitionId: Int,
        context: Context,
        addCompetitorResult: State<NetworkResponse<MessageModel>?>
    ): Boolean {
        if(idAddCompetitor != -1) {
            addCompetitorCompetition(CompetitorIdModelItem(idAddCompetitor), competitionId)
            if (addCompetitorResult.value is NetworkResponse.Error) {
                val result = (addCompetitorResult.value as NetworkResponse.Error)
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                return false
            }
            return true
        }
        else{
            Toast.makeText(context, "Vous n'avez pas sélectionné de participants à inscrire", Toast.LENGTH_LONG).show()
            return false
        }
    }

    fun AddToListRegisteredCompetitors(registeredCompetitorsList: CompetitorModel){
        when(val result = competitorByCompetitionResult.value){
            is NetworkResponse.Success -> {
                registeredCompetitorsList.addAll(result.data)
            }
            null -> {}
            is NetworkResponse.Error -> {}
            is NetworkResponse.Loading -> {}
        }
    }
}