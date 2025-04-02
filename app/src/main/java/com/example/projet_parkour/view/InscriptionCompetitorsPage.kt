package com.example.projet_parkour.view

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.model.CompetitorIdModelItem
import com.example.projet_parkour.model.CompetitorModel
import com.example.projet_parkour.model.MessageModel
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
    var idAddCompetitor = remember { mutableIntStateOf(-1) }
    val registeredCompetitorsList = CompetitorModel()
    val addCompetitorResult = viewModel.addCompetitorCompetitionResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getCompetitors()
        viewModel.getCompetitorsByCompetitionId(competitionId)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)){
        Column {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp).border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(10.dp)
                    ).padding(10.dp)
            ) {
                viewModel.AddToListRegisteredCompetitors(registeredCompetitorsList)
                PotentialsCompetitorsPage(
                    modifier,
                    viewModel,
                    ageMin,
                    ageMax,
                    gender,
                    selectedText,
                    registeredCompetitorsList
                )
                if (selectedText.value != "") {
                    idAddCompetitor.value =
                        selectedText.value.split(".").get(0).substring(3).toInt()
                }
                Button(onClick = {
                    val result = viewModel.RegisterCompetitorOnClick(
                        idAddCompetitor.intValue,
                        competitionId,
                        context,
                        addCompetitorResult
                    )
                    if (result) navController.navigate("inscription_competitors_page/${competitionId}:${ageMin},${ageMax},${gender}")
                }) {
                    Text("Inscrire un participant")
                }
            }
            RegisteredCompetitorsPage(modifier, viewModel, competitionId)
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = 40.dp, end = 30.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                navController.navigate("create_competitor_page/${competitionId}:${ageMin},${ageMax},${gender}")
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
                Log.d("deboggage", registeredCompetitorsList.size.toString())
                data.removeAll(registeredCompetitorsList)

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
    competitionId: Int
) {
    val competitorByCompetitionResult = viewModel.competitorByCompetitionResult.observeAsState()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text ="Participants inscrits à la compétition",
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 5.dp, bottom = 15.dp),
            fontWeight = FontWeight.Bold)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(top=20.dp,start = 10.dp, end = 10.dp, bottom = 20.dp)
        ) {

            when (val result = competitorByCompetitionResult.value) {
                is NetworkResponse.Error -> {
                    Text(text = result.message)
                }

                is NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }

                is NetworkResponse.Success -> {
                    LazyColumn {
                        items(result.data.size) { index ->
                            val data = result.data[index]
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
                            ) {
                                Row (modifier = Modifier.fillMaxWidth().padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Text(text = data.last_name.uppercase()+" "+data.first_name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text(data.born_at+"   "+data.gender)
                                }

                            }
                        }
                    }
                }

                null -> {}
            }
        }
    }
}