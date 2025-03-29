package com.example.projet_parkour.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.model.CreationCompetitionModelItem
import com.example.projet_parkour.model.CreationCompetitorModelItem
import com.example.projet_parkour.viewmodel.CompetitorsViewModel

@Composable
fun CreateCompetitorPage(
    modifier: Modifier,
    viewModel: CompetitorsViewModel,
    navController: NavController
) {
    val createCompetitorResult = viewModel.createCompetitorResult.observeAsState()

    val lastNameState by viewModel.lastName.observeAsState() ;
    val firstNameState by viewModel.firstName.observeAsState() ;
    val emailState by viewModel.email.observeAsState() ;
    val phoneState by viewModel.phone.observeAsState() ;
    val bornAtState by viewModel.bornAt.observeAsState() ;

    val radioOptionsGender = listOf("Homme", "Femme")
    val selectedOptionGender = remember { mutableStateOf(radioOptionsGender[0]) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = lastNameState ?: "",
            label = { Text("Nom") },
            onValueChange = { viewModel.lastName.postValue(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(4.dp))

        OutlinedTextField(
            value = firstNameState ?: "",
            label = { Text("Prénom") },
            onValueChange = { viewModel.firstName.postValue(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(4.dp))

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

        OutlinedTextField(
            value = emailState ?: "",
            label = { Text("Email") },
            onValueChange = { viewModel.email.postValue(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(4.dp))

        OutlinedTextField(
            value = phoneState ?: "",
            label = { Text("Numéro de téléphone") },
            onValueChange = { viewModel.phone.postValue(it) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(4.dp))

        Button(onClick = {
            val gender = when (selectedOptionGender.value) {
                "Homme" -> "H"; "Femme" -> "F"
                else -> {
                    ""
                }
            }

            val competitor = CreationCompetitorModelItem(
                first_name = firstNameState.toString(),
                last_name = lastNameState.toString(),
                gender = gender,
                email = emailState.toString(),
                phone = phoneState.toString(),
                born_at = bornAtState.toString()
            );

            viewModel.createCompetitor(competitor);
            //update competition with new competitor
        }) {
            Text(text = "Enregistrer")
        }
        when (val result = createCompetitorResult.value) {
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