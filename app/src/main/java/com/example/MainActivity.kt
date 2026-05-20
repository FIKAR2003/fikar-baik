package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.data.KmsDatabaseProvider
import com.example.ui.KmsApp
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.KmsViewModel
import com.example.viewmodel.KmsViewModelFactory

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val repository = KmsDatabaseProvider.getRepository(this)
    val viewModel: KmsViewModel by viewModels {
        KmsViewModelFactory(repository)
    }

    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          KmsApp(viewModel = viewModel)
        }
      }
    }
  }
}
