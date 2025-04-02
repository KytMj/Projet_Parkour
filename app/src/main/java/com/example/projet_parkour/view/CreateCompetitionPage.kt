package com.example.projet_parkour.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projet_parkour.viewmodel.CompetitionsViewModel


@Composable
fun CreateCompetitionPage(
    modifier: Modifier,
    viewModel: CompetitionsViewModel,
    navController: NavController,
    context: Context
){
    val createCompetitionResult = viewModel.createCompetitionResult.observeAsState()

    val nameState by viewModel.nameCompetition.observeAsState() ;
    val ageMiniState by viewModel.ageMiniCompet.observeAsState() ;
    val ageMaxiState by viewModel.ageMaxiCompet.observeAsState() ;

    val radioOptionsGender = listOf("Homme","Femme")
    val selectedOptionGender = remember { mutableStateOf(radioOptionsGender[0]) }
    val checkedHasRetry = remember { mutableStateOf(false) }

    var isValidName by remember { mutableStateOf(false) }
    var isValidMaxAge by remember { mutableStateOf(false) }
    var isValidMiniAge by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(10.dp)
            ).padding(10.dp)
    ) {

        //NOM COMPETITION
        OutlinedTextField(
            value = nameState ?: "",
            label = { Text("Nom de la compétition", color= Color.Black) },
            onValueChange = { input ->
                viewModel.nameCompetition.postValue(input)
                isValidName = input.isNotEmpty() && viewModel.isValidName(input)
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (!isValidName){
            Text(text = "Champ invalide", color = Color.Red)
        }
        Spacer(modifier = Modifier.size(4.dp))

        Row {
            Text(
                text = "Genre des participants",
                style = MaterialTheme.typography.bodyMedium
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

        //AGE MINIMUM
        OutlinedTextField(
            value = ageMiniState ?: "",
            onValueChange = {
                viewModel.ageMiniCompet.postValue(it)
                isValidMiniAge = it.isNotEmpty() && viewModel.isValidMinAge(it)
                            },
            label = { Text("Âge minimum", color= Color.Black) },
            modifier = Modifier.fillMaxWidth()
        )
        if (!isValidMiniAge){
            Text(text = "Champ invalide", color = Color.Red)
        }
        Spacer(modifier = Modifier.size(4.dp))

        //AGE MAXIMUM
        OutlinedTextField(
            value = ageMaxiState ?: "",
            onValueChange = {
                viewModel.ageMaxiCompet.postValue(it)
                isValidMaxAge = it.isNotEmpty() && viewModel.isValidMaxAge(it)
            },
            label = { Text("Âge maximum", color= Color.Black) },
            modifier = Modifier.fillMaxWidth()
        )
        if (!isValidMaxAge){
            Text(text = "Champ invalide", color = Color.Red)
        }
        Spacer(modifier = Modifier.size(4.dp))

        Row {
            Text(
                text = "Plusieurs essais possibles ?",
                style = MaterialTheme.typography.bodyMedium
            )
            Checkbox(
                checked = checkedHasRetry.value,
                onCheckedChange = { checkedHasRetry.value = it }
            )

        }
        Spacer(modifier = Modifier.size(4.dp))

        Button(onClick = {
            if(isValidName && isValidMiniAge && isValidMaxAge){
                val response = viewModel.checkAndAddCompetition(selectedOptionGender, checkedHasRetry, context)
                if(response) navController.navigate("competitions_page")
            }
            else{
                Toast.makeText(context, "Des champs ne sont pas valides", Toast.LENGTH_LONG).show()
            }
        }) {
            Text(text = "Enregistrer")
        }

    }
}