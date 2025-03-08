package com.example.projet_parkour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.projet_parkour.ui.theme.Projet_ParkourTheme
import com.example.projet_parkour.view.CompetitionsPage
import com.example.projet_parkour.viewmodel.CompetitionsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val competitionsViewModel = ViewModelProvider(this)[CompetitionsViewModel::class.java]

        setContent {
            Projet_ParkourTheme {
                Scaffold( modifier = Modifier.fillMaxSize().padding(28.dp)) { innerPadding ->
                    CompetitionsPage(modifier = Modifier.padding(innerPadding),
                        competitionsViewModel
                    )
                }
            }
        }
    }
}
