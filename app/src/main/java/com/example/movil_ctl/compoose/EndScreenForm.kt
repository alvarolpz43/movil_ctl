package com.example.movil_ctl.compoose

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.excludeFromSystemGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.movil_ctl.data.dataui.TipoPregunta
import com.example.movil_ctl.ui.theme.colorEfa
import com.example.movil_ctl.ui.theme.colorUnoSw
import com.example.movil_ctl.view.EndScreenFormViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndScreenForm(
    tipoEquipo: String,
    navController: NavController,
    viewModel: EndScreenFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val shareLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        navController.navigate("contratistas") {
            popUpTo(0)
        }
    }

    LaunchedEffect(tipoEquipo) {
        viewModel.setTipoEquipo(tipoEquipo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Finalizar Formulario",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorUnoSw,
                    titleContentColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                // Sección de preguntas
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black, shape = MaterialTheme.shapes.medium),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Información Adicional",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )

                        Spacer(Modifier.height(16.dp))

                        uiState.preguntas.forEach { pregunta ->
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(
                                    pregunta.texto,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    color = Color.Black
                                )

                                when (pregunta.tipo) {
                                    TipoPregunta.TEXTO -> {
                                        OutlinedTextField(
                                            value = uiState.respuestas[pregunta.id] ?: "",
                                            onValueChange = {
                                                viewModel.actualizarRespuesta(
                                                    pregunta.id,
                                                    it
                                                )
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            singleLine = false,
                                            maxLines = 3,
                                            shape = MaterialTheme.shapes.small,
                                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                                focusedBorderColor = colorEfa,
                                                unfocusedBorderColor = Color.Black,
                                                focusedTextColor = Color.Black,
                                                unfocusedTextColor = Color.Black,
                                                cursorColor = Color.Black
                                            )
                                        )
                                    }

                                    TipoPregunta.NUMERO -> {
                                        OutlinedTextField(
                                            value = uiState.respuestas[pregunta.id] ?: "",
                                            onValueChange = {
                                                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                                    viewModel.actualizarRespuesta(pregunta.id, it)
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            shape = MaterialTheme.shapes.small,
                                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                                focusedBorderColor = colorEfa,
                                                unfocusedBorderColor = Color.Black,
                                                focusedTextColor = Color.Black,
                                                unfocusedTextColor = Color.Black,
                                                cursorColor = Color.Black
                                            )
                                        )
                                    }

                                    TipoPregunta.BOOLEANO -> {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            FilterChip(
                                                selected = uiState.respuestas[pregunta.id] == "true",
                                                onClick = {
                                                    viewModel.actualizarRespuesta(
                                                        pregunta.id,
                                                        "true"
                                                    )
                                                },
                                                label = { Text("Sí") },
                                                modifier = Modifier.weight(1f),
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                                                )
                                            )
                                            FilterChip(
                                                selected = uiState.respuestas[pregunta.id] == "false",
                                                onClick = {
                                                    viewModel.actualizarRespuesta(
                                                        pregunta.id,
                                                        "false"
                                                    )
                                                },
                                                label = { Text("No") },
                                                modifier = Modifier.weight(1f),
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                                                )
                                            )
                                        }
                                    }

                                    TipoPregunta.SELECCION -> {
                                        var expanded by remember { mutableStateOf(false) }
                                        val selected = uiState.respuestas[pregunta.id] ?: ""

                                        ExposedDropdownMenuBox(
                                            expanded = expanded,
                                            onExpandedChange = { expanded = !expanded }
                                        ) {
                                            OutlinedTextField(
                                                value = selected,
                                                onValueChange = {},
                                                readOnly = true,
                                                label = {
                                                    Text(
                                                        "Seleccione una opción",
                                                        color = Color.Black
                                                    )
                                                },
                                                trailingIcon = {
                                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                                        expanded = expanded
                                                    )
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .menuAnchor(),
                                                shape = MaterialTheme.shapes.small,
                                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                                    focusedBorderColor = colorEfa,
                                                    unfocusedBorderColor = Color.Black,
                                                    focusedTextColor = Color.Black,
                                                    unfocusedTextColor = Color.Black,
                                                    cursorColor = Color.Black
                                                )
                                            )

                                            ExposedDropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = { expanded = false },
                                                modifier = Modifier
                                                    .excludeFromSystemGesture()
                                                    .background(Color.White)
                                            ) {
                                                pregunta.opciones?.forEach { opcion ->
                                                    DropdownMenuItem(
                                                        text = { Text(opcion, color = Color.Black) },
                                                        onClick = {
                                                            viewModel.actualizarRespuesta(
                                                                pregunta.id,
                                                                opcion
                                                            )
                                                            expanded = false
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    TipoPregunta.HORA -> {
                                        Button(
                                            onClick = { /* Implementar TimePickerDialog */ },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = MaterialTheme.shapes.small,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        ) {
                                            Text(
                                                if (uiState.respuestas[pregunta.id].isNullOrEmpty())
                                                    "Seleccionar hora"
                                                else
                                                    "Hora: ${uiState.respuestas[pregunta.id]}"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Botones de acción
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text("Cancelar", color = Color.Black)
                    }

                    Button(
                        onClick = {
                            val file = viewModel.generateTxt(context)
                            val uri = viewModel.getFileUri(context, file)
                            val intent = viewModel.buildShareIntent(uri, context)
                            shareLauncher.launch(intent)
                        },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorEfa,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Enviar Formulario", color = Color.White)
                    }
                }
            }
        }
    }
}
