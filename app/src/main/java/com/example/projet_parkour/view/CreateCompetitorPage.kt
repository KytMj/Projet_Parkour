package com.example.projet_parkour.view

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import com.example.projet_parkour.view.utils.CalendarComposable
import com.example.projet_parkour.viewmodel.CompetitorsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateCompetitorPage(
    modifier: Modifier,
    viewModel: CompetitorsViewModel,
    navController: NavController,
    context: Context,
    competitionId: Int,
    ageMin: Int,
    ageMax: Int,
    gender: String
) {
    val createCompetitorResult = viewModel.createCompetitorResult.observeAsState()

    val lastNameState by viewModel.lastName.observeAsState() ;
    val firstNameState by viewModel.firstName.observeAsState() ;
    val emailState by viewModel.email.observeAsState() ;
    val phoneState by viewModel.phone.observeAsState() ;
    val bornAtState by viewModel.bornAt.observeAsState() ;
    val mDate = remember { mutableStateOf("") }

    val radioOptionsGender = listOf("Homme", "Femme")
    val selectedOptionGender = remember { mutableStateOf(radioOptionsGender[0]) }

    var isValidLastName by remember { mutableStateOf(false) }
    var isValidFirstName by remember { mutableStateOf(false) }
    var isValidEmail by remember { mutableStateOf(false) }
    var isValidPhone by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //LAST NAME
        OutlinedTextField(
            value = lastNameState ?: "",
            label = { Text("Nom") },
            onValueChange = {
                viewModel.lastName.postValue(it)
                isValidLastName = it.isNotEmpty() && viewModel.isValidName(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (!isValidLastName){
            Text(text = "Champ invalide", color = Color.Red)
        }
        Spacer(modifier = Modifier.size(4.dp))

        //FIRST NAME
        OutlinedTextField(
            value = firstNameState ?: "",
            label = { Text("Prénom") },
            onValueChange = {
                viewModel.firstName.postValue(it)
                isValidFirstName = it.isNotEmpty() && viewModel.isValidName(it)
                            },
            modifier = Modifier.fillMaxWidth()
        )
        if (!isValidFirstName){
            Text(text = "Champ invalide", color = Color.Red)
        }
        Spacer(modifier = Modifier.size(4.dp))

        //GENDER
        Row {
            Text(
                text = "Sexe",
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

        //BORN_AT
        CalendarComposable(context, mDate)
        Spacer(modifier = Modifier.size(4.dp))

        //EMAIL
        OutlinedTextField(
            value = emailState ?: "",
            label = { Text("Email") },
            onValueChange = {
                viewModel.email.postValue(it)
                isValidEmail = it.isNotEmpty() && viewModel.isValidEmail(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (!isValidEmail){
            Text(text = "Champ invalide", color = Color.Red)
        }
        Spacer(modifier = Modifier.size(4.dp))

        //PHONE
        OutlinedTextField(
            value = phoneState ?: "",
            label = { Text("Numéro de téléphone") },
            onValueChange = {
                viewModel.phone.postValue(it)
                isValidPhone = it.isNotEmpty() && viewModel.isValidPhone(it)
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (!isValidPhone){
            Text(text = "Champ invalide", color = Color.Red)
        }
        Spacer(modifier = Modifier.size(4.dp))

        Column {
            Button(onClick = {
                if(isValidLastName && isValidFirstName && isValidEmail && isValidPhone){
                    val response = viewModel.checkAndAddCompetitorToDB(selectedOptionGender, context, mDate, competitionId)
                    if(response) {
                        viewModel.lastName.postValue("")
                        viewModel.firstName.postValue("")
                        viewModel.email.postValue("")
                        viewModel.phone.postValue("")
                        viewModel.bornAt.postValue("")
                        mDate.value = ""
                        navController.navigate("inscription_competitors_page/${competitionId}:${ageMin},${ageMax},${gender}")
                    }
                }
                else{
                    Toast.makeText(context, "Des champs ne sont pas valides", Toast.LENGTH_LONG).show()
                }
            }) {
                Text(text = "Enregistrer")
            }
            Button(onClick = {
                if(isValidLastName && isValidFirstName && isValidEmail && isValidPhone){
                    val response = viewModel.checkAndAddCompetitorToCompetition(selectedOptionGender, context, mDate,
                        competitionId, ageMin, ageMax, gender)
                    if(response) {
                        viewModel.lastName.postValue("")
                        viewModel.firstName.postValue("")
                        viewModel.email.postValue("")
                        viewModel.phone.postValue("")
                        viewModel.bornAt.postValue("")
                        mDate.value = ""
                        navController.navigate("inscription_competitors_page/${competitionId}:${ageMin},${ageMax},${gender}")
                    }
                }
                else{
                    Toast.makeText(context, "Des champs ne sont pas valides", Toast.LENGTH_LONG).show()
                }
            }) {
                Text(text = "Enregistrer et ajouter à la compétition")
            }
        }
    }
}