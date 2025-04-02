package com.example.projet_parkour.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.model.CompetitionModelItem
import com.example.projet_parkour.viewmodel.CompetitionsViewModel

@Composable
fun CompetitionsPage(
    modifier: Modifier,
    viewModel: CompetitionsViewModel,
    navController: NavController,
    isCompetitionOver: MutableState<Boolean>
){
    val competitionResult = viewModel.competitionResult.observeAsState()
    val configuration = LocalConfiguration.current

     LaunchedEffect(Unit) {
        viewModel.getCompetitions()
     }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Compétitions disponibles",
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 5.dp, bottom = 15.dp),
            fontWeight = FontWeight.Bold)

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
                        if (data.status == "finished") isCompetitionOver.value = true
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 20.dp).shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        ) {
                            when (configuration.orientation) {
                                Configuration.ORIENTATION_LANDSCAPE -> {
                                    Row(modifier = Modifier.fillMaxWidth()){
                                        ContentDisplay(modifier = Modifier.padding(25.dp).fillMaxWidth(0.6f),data);
                                        ButtonDisplay(navController, data);
                                    }
                                }else-> {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        ContentDisplay(modifier = Modifier.padding(25.dp).fillMaxWidth(), data);
                                        ButtonDisplay(navController, data);
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

@Composable
fun ButtonDisplay(navController: NavController, data: CompetitionModelItem){
    Column(modifier = Modifier.padding(6.dp).fillMaxHeight(), verticalArrangement = Arrangement.Center) {
        Button(
            modifier = Modifier.fillMaxWidth(),

            onClick = {
                navController.navigate("courses_competitors_page/${data.id}")
            }, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.Black),
            shape = RoundedCornerShape(15)
        ) {
            Text("Courses & Concurrents")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate("inscription_competitors_page/${data.id}:${data.age_min},${data.age_max},${data.gender}")
            }, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.Black),
            shape = RoundedCornerShape(15)
        ) {
            Text("Inscriptions")
        }
    }
}

@Composable
fun ContentDisplay(modifier: Modifier, data: CompetitionModelItem){
    Column (modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,){
        Text("ID : " + data.id, color = Color.White)// Utiliser MaterialScheme pour toutes les couleurs
        Text(text = data.name, fontSize = 30.sp , color = Color.White)
        Text(text = when(val status = data.status) {
            "not_ready" -> "Pas encore prête"
            "not_started" -> "Pas commencée"
            "started" -> "Commencée"
            "finished" -> "Terminée"
            else -> {
                "Pas de status"
            }
        },
            color = when(val status = data.status) {
                "not_ready" -> Color(0xFFEE4A44)
                "not_started" -> Color(0xFFCCA43B)
                "started" -> Color(0xFF3C9DE2)
                "finished" -> Color(0xFF35AC82)
                else -> {
                    Color.Gray
                }
            }
        )
        Text("Genre : " +
                when(val gender = data.gender){
                    "H" -> "Homme"
                    "F" -> "Femme"
                    else -> {
                        "Pas de catégorie"
                    }
                }, color = Color.White
        )
        Text("De " + data.age_min.toString() + " à "+data.age_max.toString()+" ans", color = Color.White)
        Text("Plusieurs essais possible ? " + if(data.has_retry == 1) "Oui" else "Non", color = Color.White)
    }
}