package com.example.projet_parkour.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.api.RetrofitInstance
import com.example.projet_parkour.model.CourseObstacleModel
import com.example.projet_parkour.model.CreationObstacleModelItem
import com.example.projet_parkour.model.MessageModel
import com.example.projet_parkour.model.ObstacleIdModelItem
import com.example.projet_parkour.model.ObstacleModel
import com.example.projet_parkour.model.ObstacleModelItem
import kotlinx.coroutines.launch

class ObstaclesViewModel : ViewModel() {
    private val api = RetrofitInstance.api
    private val _obstaclesResult = MutableLiveData<NetworkResponse<ObstacleModel>>()
    val obstaclesResult : LiveData<NetworkResponse<ObstacleModel>> = _obstaclesResult

    private val _obstaclesByCourseResult = MutableLiveData<NetworkResponse<CourseObstacleModel>>()
    val obstaclesByCourseResult : LiveData<NetworkResponse<CourseObstacleModel>> = _obstaclesByCourseResult

    private val _createObstacleResult = MutableLiveData<NetworkResponse<ObstacleModelItem>>()
    val createObstacleResult : LiveData<NetworkResponse<ObstacleModelItem>> = _createObstacleResult

    private val _addObstacleToCourseResult = MutableLiveData<NetworkResponse<MessageModel>>()
    val addObstacleToCourseResult : LiveData<NetworkResponse<MessageModel>> = _addObstacleToCourseResult

    var nameObstacle = MutableLiveData("")

    fun getObstacles(){
        _obstaclesResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getObstacles()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _obstaclesResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _obstaclesResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. (error "+response.code().toString() + ")");
                }
            }
            catch (ex : Exception){
                _obstaclesResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun getObstaclesByCourseId(courseId : Int){
        _obstaclesByCourseResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = api.getObstaclesByCourseId(courseId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _obstaclesByCourseResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _obstaclesByCourseResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. (error)")
                }
            }
            catch (ex : Exception){
                _obstaclesByCourseResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun createObstacle(obstacle : CreationObstacleModelItem) {
        viewModelScope.launch {
            try {
                val response = api.createObstacle(obstacle)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _createObstacleResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _createObstacleResult.value = NetworkResponse.Error(
                        "" +
                                "Les données n'ont pas réussi à être chargées. (error " + response.code()
                            .toString() + ")" +
                                " " + response.errorBody().toString()
                    )
                }
            } catch (ex: Exception) {
                _createObstacleResult.value =
                    NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n" + ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun addObstacleToCourse(courseId: Int, obstacleIdModelItem: ObstacleIdModelItem) {
        viewModelScope.launch {
            try {
                val response = api.addObstacleToCourse(courseId, obstacleIdModelItem)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _addObstacleToCourseResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _addObstacleToCourseResult.value = NetworkResponse.Error(
                        "" +
                                "Les données n'ont pas réussi à être chargées. (error " + response.code()
                            .toString() + ")" +
                                " " + response.errorBody().toString()
                    )
                }
            } catch (ex: Exception) {
                _addObstacleToCourseResult.value =
                    NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n" + ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun checkAndAddObstacle(
        context: Context
    ): Boolean {
        if (nameObstacle.value == ""){
            Toast.makeText(context, "Le champ Nom Compétition ne contient rien", Toast.LENGTH_LONG).show()
            return false;
        }

        val obstacle = CreationObstacleModelItem(
            name = nameObstacle.value.toString()
        );

        createObstacle(obstacle)

        when (val result = createObstacleResult.value) {
            is NetworkResponse.Error -> Toast.makeText(context, "error: "+ result.message, Toast.LENGTH_LONG).show()
            is NetworkResponse.Loading -> {}
            is NetworkResponse.Success -> {
                Toast.makeText(context, "Obstacle bien ajouté !", Toast.LENGTH_LONG).show()
            }
            null -> {}
        }
        return true;
    }

    fun RegisterObstacleOnClick (
        idAddObstacle: Int,
        courseId: Int,
        context: Context,
        addObstacleResult: State<NetworkResponse<MessageModel>?>
    ): Boolean {
        if(idAddObstacle != -1) {
            addObstacleToCourse(courseId, ObstacleIdModelItem(idAddObstacle))
            if (addObstacleResult.value is NetworkResponse.Error) {
                val result = (addObstacleResult.value as NetworkResponse.Error)
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                return false
            }
            return true
        }
        else{
            Toast.makeText(context, "Vous n'avez pas sélectionné d'obstacles à inscrire", Toast.LENGTH_LONG).show()
            return false
        }
    }

    fun AddToListRegisteredObstacles(registeredObstaclesList: CourseObstacleModel){
        when(val result = obstaclesByCourseResult.value){
            is NetworkResponse.Success -> {
                registeredObstaclesList.addAll(result.data)
            }
            null -> {}
            is NetworkResponse.Error -> {}
            is NetworkResponse.Loading -> {}
        }
    }

    fun isValidName(text: String): Boolean {
        return text.matches(Regex("^[A-Za-zéèëêàç\\s'-]{1,50}\$"))
    }
}