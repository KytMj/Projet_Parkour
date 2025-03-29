package com.example.projet_parkour.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.model.CompetitorModel
import com.example.projet_parkour.model.CompetitorModelItem
import com.example.projet_parkour.ui.theme.Pink40
import com.example.projet_parkour.viewmodel.CompetitorsViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InscriptionCompetitorsPage(
    modifier: Modifier,
    viewModel: CompetitorsViewModel,
    competitionId: Int,
    ageMin: Int,
    ageMax: Int,
    gender: String,
    navController: NavController
) {
    var selectedText = remember { mutableStateOf("") }
    val registeredCompetitorsList = CompetitorModel()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)){
        Column {
            PotentialsCompetitorsPage(modifier, viewModel, ageMin, ageMax, gender, selectedText, registeredCompetitorsList)
            RegisteredCompetitorsPage(modifier, viewModel, competitionId, registeredCompetitorsList)
            Text(selectedText.value)
            Button(onClick = {
                //update Competition with new competitors and reload the page (?)
            }) {
                Text("Inscrire un participant")
            }
        }
        FloatingActionButton(
            modifier = Modifier.padding(bottom = 40.dp, end = 30.dp).align(Alignment.BottomEnd),
            onClick = {
                navController.navigate("create_competitor_page")
            },
            containerColor = Pink40,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation()
        ) {
            Text("Créer un nouveau participant", modifier = Modifier.padding(start = 20.dp, end = 20.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PotentialsCompetitorsPage(
    modifier: Modifier,
    viewModel: CompetitorsViewModel,
    ageMin: Int,
    ageMax: Int,
    gender: String,
    selectedText: MutableState<String>,
    registeredCompetitorsList: CompetitorModel
) {
    val competitorResult = viewModel.competitorResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getPotentialCompetitors()
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(val result = competitorResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                val data = result.data
                data.removeAll(registeredCompetitorsList) //don't work.... Why....
                val competitors = ArrayList<String>()

                data.forEach { element ->
                    val age = ChronoUnit.YEARS.between(LocalDate.parse(element.born_at), LocalDate.now())
                    if(age in ageMin..ageMax && element.gender == gender){
                        competitors.add("ID " + element.id.toString() + " - " + element.last_name.uppercase() + " " + element.first_name + element.gender + age.toString())
                    }
                }

                DropMenu(competitors, "Participants potentiels", selectedText)
                Text(text = selectedText.value)
            }
            null -> {}
        }
    }
}

@Composable
fun RegisteredCompetitorsPage(
    modifier: Modifier,
    viewModel: CompetitorsViewModel,
    competitionId: Int,
    competitorsList: CompetitorModel
) {
    val competitorResult = viewModel.competitorResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getCompetitorsByCompetitionId(competitionId)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when(val result = competitorResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                result.data.forEach{element -> competitorsList.add(element)}
                LazyColumn {
                    items(result.data.size){ index ->
                        val data = result.data[index]
                        Text(text = data.last_name.uppercase() + " " + data.first_name + data.gender + data.born_at)
                    }
                }
            }
            null -> {}
        }
    }
}