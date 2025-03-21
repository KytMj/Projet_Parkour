package com.example.projet_parkour.view

import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Header(modifier: Modifier, navController: NavController, isEnable: MutableState<Boolean>){
    Row(modifier = Modifier.fillMaxWidth()
        .background(MaterialTheme.colorScheme.inversePrimary),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Button(
            onClick = {
            navController.navigate("competitions_page")
        }) {
            Text("Competitions")
        }

        SwitchWithLabel(modifier = Modifier, isEnable)
    }
}

@Composable
fun SwitchWithLabel(modifier: Modifier, state: MutableState<Boolean>) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.inversePrimary)
            .clickable(
                interactionSource = interactionSource,
                // This is for removing ripple when Row is clicked
                indication = null,
                role = Role.Switch,
                onClick = {
                    state.value = !(state.value)
                }
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {

        Text(text = "Mode Construction")
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Switch(
            checked = state.value,
            onCheckedChange = {
                state.value = it
            }
        )
    }
}