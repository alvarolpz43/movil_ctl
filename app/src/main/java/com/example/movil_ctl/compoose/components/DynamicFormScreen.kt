package com.example.movil_ctl.compoose.components

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.excludeFromSystemGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.movil_ctl.data.constants.FormularioHolder
import com.example.movil_ctl.data.dataui.EstadoEquipo
import com.example.movil_ctl.data.dataui.TipoPregunta
import com.example.movil_ctl.ui.theme.colorEfa
import com.example.movil_ctl.ui.theme.colorUnoSw
import com.example.movil_ctl.view.DynamicFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicFormScreen(
    tipoEquipo: String,
    contratistaId: String,
    equipoId: String,
    navController: NavController,
    viewModel: DynamicFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(tipoEquipo) {
        viewModel.setTipoEquipo(tipoEquipo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Formulario Técnico",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.Black
                        )
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
                .padding(paddingValues)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState)
                    .imePadding()
                    .navigationBarsPadding()
            ) {

                Spacer(Modifier.height(24.dp))
                // Sección de estado del equipo
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black, shape = MaterialTheme.shapes.medium),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Estado del equipo",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "¿El equipo está en funcionamiento?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = uiState.equipoEnUso == EstadoEquipo.EN_USO,
                                onClick = { viewModel.setEstadoEquipo(true) },
                                label = { Text("Sí", color = Color.Black) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colorUnoSw
                                )
                            )
                            FilterChip(
                                selected = uiState.equipoEnUso == EstadoEquipo.NO_OPERATIVO,
                                onClick = { viewModel.setEstadoEquipo(false) },
                                label = { Text("No", color = Color.Black) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = colorUnoSw,
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Preguntas dinámicas
                val preguntas = when (uiState.equipoEnUso) {
                    EstadoEquipo.EN_USO -> uiState.preguntasOperativas
                    EstadoEquipo.NO_OPERATIVO -> uiState.preguntasNoOperativas
                    else -> emptyList()
                }

                if (preguntas.isNotEmpty()) {
                    Text(
                        "Cuestionario técnico",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = Color.Black
                    )
                }

                preguntas.forEach { pregunta ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .border(1.dp, Color.Black, shape = MaterialTheme.shapes.medium),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
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
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Done
                                        ),
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
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done
                                        ),
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
                                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
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
                                                    },
                                                )
                                            }
                                        }
                                    }
                                }

                                TipoPregunta.HORA -> {
                                    val context = LocalContext.current
                                    val currentValue = uiState.respuestas[pregunta.id] ?: ""
                                    var showDialog by remember { mutableStateOf(false) }

                                    if (showDialog) {
                                        val timePicker = TimePickerDialog(
                                            context,
                                            { _, hour: Int, minute: Int ->
                                                val horaFormateada =
                                                    String.format("%02d:%02d", hour, minute)
                                                viewModel.actualizarRespuesta(
                                                    pregunta.id,
                                                    horaFormateada
                                                )
                                                showDialog = false
                                            },
                                            8, 0, true
                                        )

                                        timePicker.setOnCancelListener {
                                            showDialog = false
                                        }

                                        timePicker.show()
                                    }


                                    Button(
                                        onClick = { showDialog = true },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = MaterialTheme.shapes.small,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    ) {
                                        Text(
                                            if (currentValue.isEmpty()) "Seleccionar hora" else "Hora: $currentValue"
                                        )
                                    }
                                }

                            }
                        }
                    }
                }

                // Sección de paradas adicionales
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .border(1.dp, Color.Black, shape = MaterialTheme.shapes.medium),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Registro de paradas adicionales",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )

                        Spacer(Modifier.height(8.dp))

                        if (uiState.paradasAdicionales.isEmpty()) {
                            Text(
                                "No hay paradas registradas",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        } else {
                            uiState.paradasAdicionales.forEachIndexed { index, parada ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = parada.tiempo,
                                        onValueChange = {
                                            if (it.isEmpty() || it.matches(Regex("^\\d*\$"))) {
                                                viewModel.actualizarParada(index, it, parada.motivo)
                                            }
                                        },
                                        label = { Text("Tiempo (min)", color = Color.Black) },
                                        modifier = Modifier.weight(1f),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done
                                        ),
                                        shape = MaterialTheme.shapes.small,
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = colorEfa,
                                            unfocusedBorderColor = Color.Black,
                                            focusedTextColor = Color.Black,
                                            unfocusedTextColor = Color.Black,
                                            cursorColor = Color.Black
                                        ),
                                    )
                                    OutlinedTextField(
                                        value = parada.motivo,
                                        onValueChange = {
                                            viewModel.actualizarParada(
                                                index,
                                                parada.tiempo,
                                                it
                                            )
                                        },
                                        label = { Text("Motivo", color = Color.Black) },
                                        modifier = Modifier.weight(1f),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = colorEfa,
                                            unfocusedBorderColor = Color.Black,
                                            focusedTextColor = Color.Black,
                                            unfocusedTextColor = Color.Black,
                                            cursorColor = Color.Black
                                        ),
                                        shape = MaterialTheme.shapes.small,
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                                    )
                                    IconButton(
                                        onClick = { viewModel.deleteParada(index) },
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { viewModel.agregarParada() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorEfa,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text("Agregar parada", color = Color.White)
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
                        Text(
                            "Cancelar",
                            color = Color.Black
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.limpiarCamposNoSeleccionados()
                            val parcial = viewModel.buildFormularioCompletoDynamic()
                            FormularioHolder.formulario = parcial
                            navController.navigate("formSTres/$tipoEquipo")
                        },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.small,
                        enabled = uiState.equipoEnUso != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorEfa,
                            disabledContainerColor = Color.Gray
                        )
                    ) {
                        Text("Continuar", color = Color.White)
                    }
                }
            }
        }
    }
}
