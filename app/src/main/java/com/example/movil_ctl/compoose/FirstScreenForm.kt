package com.example.movil_ctl.compoose

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.excludeFromSystemGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.movil_ctl.data.constants.FormularioHolder
import com.example.movil_ctl.ui.theme.colorEfa
import com.example.movil_ctl.ui.theme.colorUnoSw
import com.example.movil_ctl.view.FirstFormSViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreenForm(
    tipoEquipo: String,
    contratistaId: String,
    equipoId: String,
    navController: NavController,
    viewModel: FirstFormSViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val nucleos by viewModel.nucleos.collectAsState()
    val fincas by viewModel.fincas.collectAsState()
    val lote by viewModel.lote.collectAsState()
    val calendar = Calendar.getInstance()
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val interactionSource = remember { MutableInteractionSource() }


    var selectedZonaName by remember { mutableStateOf("") }
    var selectedNucleoName by remember { mutableStateOf("") }
    var selectedFincaName by remember { mutableStateOf("") }
    var selectedEspecieName by remember { mutableStateOf("") }
    var selectedTurnoName by remember { mutableStateOf("") }
    var selectedOperadorName by remember { mutableStateOf("") }
    var selectedFincaCode by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(calendar.time) }

    val formattedDate = remember(selectedDate) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate)
    }
    LaunchedEffect(Unit) {
        viewModel.loadFirstFormScreen(contratistaId, equipoId)
    }



    val context = LocalContext.current
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Formulario de Trabajo",
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
                .padding(paddingValues),
            color = Color.White
        ) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Cargando datos...", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Error al cargar datos",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                uiState.error ?: "Error desconocido",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = colorUnoSw
                            ),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Equipo: ${uiState.equipoNombre}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Black
                                )
                                Text(
                                    "Contratista: ${uiState.contratistaNombre}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            "Datos del Trabajo",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = Color.Black
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { datePickerDialog.show() }
                        ) {
                            OutlinedTextField(
                                value = formattedDate,
                                onValueChange = {},
                                readOnly = true,
                                enabled = false,
                                label = { Text("Fecha") },
                                leadingIcon = {
                                    Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Black)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    disabledBorderColor = Color.Black,
                                    disabledLabelColor = Color.Black,
                                    disabledTextColor = Color.Black,
                                    disabledLeadingIconColor = Color.Black,
                                    disabledTrailingIconColor = Color.Black
                                )
                            )
                        }




                        // ZONA
                        EnhancedDropdownField(
                            label = "Zona",
                            options = uiState.zonas.map { it.nombre },
                            selectedOption = selectedZonaName,
                            onOptionSelected = { nombre ->
                                selectedZonaName = nombre
                                selectedNucleoName = ""
                                selectedFincaName = ""
                                val zona = uiState.zonas.find { it.nombre == nombre }
                                zona?.let { viewModel.onZonaSelected(it.id) }
                            },
                            icon = Icons.Default.LocationOn
                        )

                        // NÚCLEO
                        EnhancedDropdownField(
                            label = "Núcleo",
                            options = nucleos.map { it.nombre },
                            selectedOption = selectedNucleoName,
                            onOptionSelected = { nombre ->
                                selectedNucleoName = nombre
                                selectedFincaName = ""
                                val nucleo = nucleos.find { it.nombre == nombre }
                                nucleo?.let { viewModel.onNucleoSelected(it.id) }
                            },
                            icon = Icons.Default.Map,
                            enabled = selectedZonaName.isNotEmpty()
                        )

                        // FINCA
                        EnhancedDropdownField(
                            label = "Finca",
                            options = fincas.map { it.nombre },
                            selectedOption = selectedFincaName,
                            onOptionSelected = { nombre ->
                                selectedFincaName = nombre

                                val finca = fincas.find { it.nombre == nombre }
                                selectedFincaCode = finca?.codFinca ?: ""
                            },
                            icon = Icons.Default.HomeWork,
                            enabled = selectedNucleoName.isNotEmpty()
                        )

                        //LOTE


                        OutlinedTextField(
                            value = lote,
                            onValueChange = {
                                viewModel.setLote(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = false,
                            maxLines = 3,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                            },
                            label = {

                                Text(
                                    text = "Lote",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Black
                                )

                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            shape = MaterialTheme.shapes.small,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = colorEfa,
                                unfocusedBorderColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                cursorColor = Color.Black
                            )

                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        EnhancedDropdownField(
                            label = "Especie",
                            options = uiState.especies.map { it.nombre },
                            selectedOption = selectedEspecieName,
                            onOptionSelected = { nombre ->
                                selectedEspecieName = nombre
                            },
                            icon = Icons.Default.Park
                        )

                        // TURNO
                        EnhancedDropdownField(
                            label = "Turno",
                            options = uiState.turnos.map { it.nombre },
                            selectedOption = selectedTurnoName,
                            onOptionSelected = { nombre ->
                                selectedTurnoName = nombre
                            },
                            icon = Icons.Default.Schedule
                        )

                        // OPERADOR
                        EnhancedDropdownField(
                            label = "Operador",
                            options = uiState.operadores.map { it.nombreOperador },
                            selectedOption = selectedOperadorName,
                            onOptionSelected = { nombre ->
                                selectedOperadorName = nombre
                            },
                            icon = Icons.Default.Person
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Botones de acción
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Text("Cancelar", color = Color.Black)
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(
                                onClick = {
                                    val formulario = viewModel.buildFormularioCompleto(
                                        tipoEquipo = tipoEquipo,
                                        selectedZonaName = selectedZonaName,
                                        selectedFincaName = selectedFincaName,
                                        selectedEspecieName = selectedEspecieName,
                                        selectedTurnoName = selectedTurnoName,
                                        selectedOperadorName = selectedOperadorName,
                                        selectedCodeFinca = selectedFincaCode,
                                        selectedFecha = selectedDate
                                    )
                                    FormularioHolder.formulario = formulario
                                    navController.navigate("formSDos/$tipoEquipo/$contratistaId/$equipoId")
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorEfa,
                                    disabledContainerColor = Color.Gray
                                ),
                                enabled = selectedZonaName.isNotEmpty() &&
                                        selectedNucleoName.isNotEmpty() &&
                                        selectedFincaName.isNotEmpty() &&
                                        selectedEspecieName.isNotEmpty() &&
                                        selectedTurnoName.isNotEmpty() &&
                                        selectedOperadorName.isNotEmpty()
                            ) {
                                Text("Continuar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedDropdownField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    icon: ImageVector,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                label = { Text(label, color = Color.Black) },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (enabled) Color.Black
                        else Color.Gray
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorEfa,
                    unfocusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    disabledTextColor = Color.Gray
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),  // Esto permite el 1-click
                enabled = enabled,
                interactionSource = interactionSource
            )

            // Menú desplegable
            ExposedDropdownMenu(
                expanded = expanded && enabled,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .excludeFromSystemGesture()
                    .background(Color.White)
            ) {
                if (options.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No hay opciones disponibles", color = Color.Black) },
                        onClick = { expanded = false }
                    )
                } else {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = Color.Black) },
                            onClick = {
                                onOptionSelected(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}