package com.example.projet_parkour.view

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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
import com.example.projet_parkour.view.utils.DropdownMenuComposable
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
    navController: NavController,
    context: Context
) {
    val selectedText = remember { mutableStateOf("") }
    var idAddCompetitor = remember { mutableStateOf(-1) }
    val registeredCompetitorsList = CompetitorModel()
    val addCompetitorResult = viewModel.addCompetitorCompetitionResult.observeAsState()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)){
        Column {
            PotentialsCompetitorsPage(modifier, viewModel, ageMin, ageMax, gender, selectedText, registeredCompetitorsList)
            if (selectedText.value != "") {
                idAddCompetitor.value = selectedText.value.split(".").get(0).substring(3).toInt()
            }
            Button(onClick = {
                RegisterCompetitorOnClick(idAddCompetitor.value, competitionId, context, viewModel, addCompetitorResult)
            }) {
                Text("Inscrire un participant")
            }
            RegisteredCompetitorsPage(modifier, viewModel, competitionId, registeredCompetitorsList)
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
        viewModel.getCompetitors()
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
                        competitors.add("ID-" + element.id.toString() + ". " + element.last_name.uppercase() + " " + element.first_name)
                    }
                }

                DropdownMenuComposable(competitors, "Participants potentiels", selectedText)
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
        Text(modifier = Modifier.padding(bottom = 10.dp), text ="Participants inscrits à la compétition")
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
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
                        ) {
                            Text(modifier = Modifier.padding(10.dp), text = data.last_name.uppercase() + " " + data.first_name + data.gender + data.born_at)
                        }
                    }
                }
            }
            null -> {}
        }
    }
}

fun RegisterCompetitorOnClick(
    idAddCompetitor: Int,
    competitionId: Int,
    context: Context,
    viewModel: CompetitorsViewModel,
    addCompetitorResult: State<NetworkResponse<CompetitorModelItem>?>
) {

    if(idAddCompetitor != -1) {
        viewModel.addCompetitorCompetition(idAddCompetitor, competitionId)
        if (addCompetitorResult.value is NetworkResponse.Success) {
            Toast.makeText(context, "Insertion réussie !", Toast.LENGTH_LONG).show()
        }
        if (addCompetitorResult.value is NetworkResponse.Error) {
            Toast.makeText(
                context,
                (addCompetitorResult.value as NetworkResponse.Error).message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    else{
        Toast.makeText(context, "Vous n'avez pas sélectionner de participants à inscrire", Toast.LENGTH_LONG).show()
    }
}