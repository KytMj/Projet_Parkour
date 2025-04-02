package com.example.projet_parkour.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projet_parkour.api.NetworkResponse
import com.example.projet_parkour.viewmodel.PerformanceObstaclesViewModel
import com.example.projet_parkour.viewmodel.PerformancesViewModel

@Composable
fun LeaderboardPage(
    modifier: Modifier,
    viewModelPerformances: PerformancesViewModel,
    viewModelPerformanceObstacles: PerformanceObstaclesViewModel,
    navController: NavController
) {
    val competitionResult = viewModelPerformances.performancesResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModelPerformances.getPerformances()
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                        Text("performance : ID"+data.id.toString() + " - "+ data.status + " temps:"+ data.total_time)
                    }
                }
            }
            null -> {}
        }
    }
}