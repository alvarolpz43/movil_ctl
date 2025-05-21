package com.example.movil_ctl.compoose

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movil_ctl.HomeViewModel
import com.example.movil_ctl.data.entities.ContratistaEntity
import com.example.movil_ctl.ui.theme.colorDosSw
import com.example.movil_ctl.ui.theme.colorEfa
import com.example.movil_ctl.ui.theme.colorUnoSw
import com.example.movil_ctl.utils.isOnline
import com.example.movil_ctl.view.SyncViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    syncViewModel: SyncViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val syncState by syncViewModel.syncState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(syncState) {
        if (syncState is SyncViewModel.SyncState.Success) {
            viewModel.loadContratistas()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Contratistas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp),
            color = Color.Black
        )


        Button(
            onClick = {
                if (isOnline(context)) {
                    scope.launch {
                        syncViewModel.triggerSync()
                    }
                } else {
                    Toast.makeText(context, "Se requiere conexión a internet", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            enabled = syncState !is SyncViewModel.SyncState.InProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorUnoSw,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            if (syncState is SyncViewModel.SyncState.InProgress) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = colorDosSw,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    color = Color.Black,
                    text = "Sincronizando..."
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = "Sincronizar",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    color = Color.Black,
                    text = "Sincronizar Datos"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = syncState) {
            is SyncViewModel.SyncState.Success -> {
                Text(
                    text = state.message,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            is SyncViewModel.SyncState.Error -> {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            else -> {}
        }

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Error al cargar contratistas: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            else -> {
                if (uiState.contratistas.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "No hay contratistas disponibles",
                            color = Color.Black
                        )
                    }
                } else {
                    ContratistasList(
                        contratistas = uiState.contratistas,
                        onContratistaClick = { id ->
                            navController.navigate("equipos/$id")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ContratistasList(
    contratistas: List<ContratistaEntity>,
    onContratistaClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(contratistas) { contratista ->
            ContratistaCard(
                contratista = contratista,
                onClick = { onContratistaClick(contratista.id) }
            )
        }
    }
}

@Composable
fun ContratistaCard(
    contratista: ContratistaEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = colorEfa,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = contratista.nombre ?: "Sin nombre",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Ver detalles",
                    tint = Color.White
                )
            }

//            Text(
//                text = "ID: ${contratista.id}",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//            )
        }
    }
}