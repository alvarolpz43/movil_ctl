package com.example.movil_ctl.compoose

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.movil_ctl.view.SyncViewModel
import kotlinx.coroutines.launch

@Composable
fun SyncScreen(viewModel: SyncViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val syncState by viewModel.syncState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (isOnline(context)) {
                    scope.launch { // Lanzar en una corrutina
                        viewModel.triggerSync()
                    }
                } else {
                    Toast.makeText(context, "Se requiere conexión a internet", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            enabled = syncState !is SyncViewModel.SyncState.InProgress,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (syncState is SyncViewModel.SyncState.InProgress) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.Blue,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sincronizando...")
            } else {
                Text("Sincronizar Datos")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = syncState) {
            is SyncViewModel.SyncState.Success -> {
                Text(
                    text = state.message,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            is SyncViewModel.SyncState.Error -> {
                Text(
                    text = state.error,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            else -> {}
        }
    }
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val activeNetwork =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}