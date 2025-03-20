package com.example.projet_parkour.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
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
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
                        ) {
                            Column (modifier = Modifier.padding(25.dp)){
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
                                Row{
                                    Button(
                                        onClick = {
                                            navController.navigate("courses_page/${data.id}")
                                        }
                                    ) {
                                        Text("Accès aux parkours")
                                    }
                                    Button(
                                        onClick = {
                                            navController.navigate("inscription_concurrents_page/${data.id}")
                                        }
                                    ) {
                                        Text("Accès aux concurrents de la compétition")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            null -> {}
        }
    }
}