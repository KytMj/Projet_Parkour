package com.example.projet_parkour.viewmodel

import android.content.Context
import android.util.Log
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
import com.example.projet_parkour.model.CoursesModel
import com.example.projet_parkour.model.CoursesModelItem
import com.example.projet_parkour.model.CreationCompetitionModelItem
import com.example.projet_parkour.model.CreationCompetitorModelItem
import com.example.projet_parkour.model.CreationCourseModelItem
import kotlinx.coroutines.launch

class CoursesViewModel : ViewModel() {

    private val api = RetrofitInstance.api
    private val _coursesResult = MutableLiveData<NetworkResponse<CoursesModel>>()
    val coursesResult : LiveData<NetworkResponse<CoursesModel>> = _coursesResult

    private val _createCourseResult = MutableLiveData<NetworkResponse<CoursesModelItem>>()
    val createCourseResult : LiveData<NetworkResponse<CoursesModelItem>> = _createCourseResult

    var nameCourse = MutableLiveData("")
    var maxDurationCourse = MutableLiveData("")

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

    fun createCourse(course : CreationCourseModelItem){
        viewModelScope.launch {
            try {
                val response = api.createCourse(course)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _createCourseResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _createCourseResult.value = NetworkResponse.Error("" +
                            "Les données n'ont pas réussi à être chargées. (error " +response.code().toString() + ")" +
                            " "+response.errorBody().toString())
                }
            }
            catch (ex : Exception){
                _createCourseResult.value = NetworkResponse.Error("Les données n'ont pas réussi à être chargées. \n"+ ex.toString() + "\nmessage :" + ex.message)
            }
        }
    }

    fun checkAndAddCourse(
        context: Context,
        competitionId: Int
    ): Boolean {
        if (nameCourse.value == ""){
            Toast.makeText(context, "Le champ Nom Course ne contient rien", Toast.LENGTH_LONG).show()
            return false;
        }

        if (maxDurationCourse.value == ""){
            Toast.makeText(context, "Le champ Durée Max ne contient rien", Toast.LENGTH_LONG).show()
            return false;
        }

        val course = CreationCourseModelItem(
            name = nameCourse.value.toString(),
            max_duration = maxDurationCourse.value?.toInt() ?: 0,
            competition_id = competitionId
        );

        createCourse(course);

        when (val result = createCourseResult.value) {
            is NetworkResponse.Error -> Toast.makeText(context, "error: "+ result.message, Toast.LENGTH_LONG).show()
            is NetworkResponse.Loading -> {}
            is NetworkResponse.Success -> {
                Toast.makeText(context, "Course bien ajoutée !", Toast.LENGTH_LONG).show()
            }
            null -> {}
        }
        return true;
    }

    fun isValidName(text: String): Boolean {
        return text.matches(Regex("^[A-Za-zéèëêàç\\s'-]{1,50}\$"))
    }

    fun isValidDuration(text: String): Boolean {
        return text.matches(Regex("^\\d+\$"))
    }
}