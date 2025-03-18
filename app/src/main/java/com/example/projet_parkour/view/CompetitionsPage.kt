package com.example.projet_parkour.view

import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.model.CompetitionModelItem
import com.example.projet_parkour.model.CreationCompetitionModelItem
import com.example.projet_parkour.viewmodel.CompetitionsViewModel

@Composable
fun CompetitionsPage(
    modifier: Modifier,
    viewModel: CompetitionsViewModel,
    navController: NavController
){
    val competitionResult = viewModel.competitionResult.observeAsState()

     LaunchedEffect(Unit) {
        viewModel.getCompetitions()
     }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Compétitions disponibles", fontSize = 20.sp, modifier = Modifier.padding(bottom = 20.dp))

        when(val result = competitionResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                LazyColumn {
                    items(result.data.size){ index ->
                        val data = result.data[index]
                        Card(
                            colors = CardDefaults.cardColors( containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.size(width = 350.dp, height = 170.dp).padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
                            onClick = {
                                navController.navigate("courses_page/${data.id}")
                            }
                        ) {
                            Column (modifier = Modifier.padding(5.dp)){
                                Text("Identifiant de la compétition : " + data.id)
                                Text("Nom de la compétition : " + data.name)
                                Text("Status : " +
                                        when(val status = data.status) {
                                            "not_ready" -> "Pas encore prête"
                                            "not_started" -> "Pas commencée"
                                            "started" -> "Commencée"
                                            "finished" -> "Terminée"
                                            else -> {
                                                "Pas de status"
                                            }
                                        }
                                )
                                Text("Genre de la compétition : " +
                                        when(val gender = data.gender){
                                            "H" -> "Homme"
                                            "F" -> "Femme"
                                            else -> {
                                                "Pas de catégorie"
                                            }
                                        }
                                )
                                Text("Age minimum : " + data.age_min.toString())
                                Text("Age maximum : " + data.age_max.toString())
                                Text("Plusieurs essais possible ? " + if(data.has_retry == 1) "Oui" else "Non")
                            }
                        }
                    }
                }
            }
            null -> {}
        }
    }
}

@Composable
fun CreateCompetitionPage(
    modifier: Modifier,
    viewModel: CompetitionsViewModel,
    navController: NavController
){
    val createCompetitionResult = viewModel.createCompetitionResult.observeAsState()

    val nameState by viewModel.nameCompetition.observeAsState() ;
    val ageMiniState by viewModel.ageMiniCompet.observeAsState() ;
    val ageMaxiState by viewModel.ageMaxiCompet.observeAsState() ;

    val radioOptionsGender = listOf("Homme","Femme")
    val selectedOptionGender = remember { mutableStateOf(radioOptionsGender[0]) }
    var checkedHasRetry = remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = nameState ?: "",
            label = { Text("Nom de la compétition") },
            onValueChange = { viewModel.nameCompetition.postValue(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(4.dp))

        Row {
            Text(
                text = "Genre des participants",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
            radioOptionsGender.forEach { genreText ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (genreText == selectedOptionGender.value),
                        onClick = { selectedOptionGender.value = genreText }
                    )
                    Text(
                        text = genreText,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(4.dp))

        OutlinedTextField(
            value = ageMiniState ?: "",
            label = { Text("Âge minimum") },
            onValueChange = { viewModel.ageMiniCompet.postValue(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(4.dp))

        OutlinedTextField(
            value = ageMaxiState ?: "",
            label = { Text("Âge maximum") },
            onValueChange = { viewModel.ageMaxiCompet.postValue(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(4.dp))

        Row {
            Text(
                text = "Plusieurs essais possibles ?",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
            Checkbox(
                checked = checkedHasRetry.value,
                onCheckedChange = { checkedHasRetry.value = it }
            )

        }
        Spacer(modifier = Modifier.size(4.dp))

        Button(onClick = {
            val gender = when (selectedOptionGender.value) {
                "Homme" -> "H"; "Femme" -> "F"
                else -> {
                    ""
                }
            }
            val competition = CreationCompetitionModelItem(
                age_max = ageMaxiState?.toInt() ?: 0,
                age_min = ageMiniState?.toInt() ?: 0,
                gender = gender,
                has_retry = checkedHasRetry.value,
                name = nameState.toString()
            );

            viewModel.createCompetition(competition);
        }) {
            Text(text = "Enregistrer")
        }
        when (val result = createCompetitionResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> navController.navigate("competitions_page")
            null -> {}
        }
    }
}