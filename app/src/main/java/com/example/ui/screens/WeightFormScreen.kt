package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Boy
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Girl
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KmsBlue
import com.example.ui.theme.KmsDark
import com.example.ui.theme.KmsPink
import com.example.ui.theme.KmsPinkContainer
import com.example.viewmodel.KmsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightFormScreen(
    viewModel: KmsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedChild by viewModel.selectedChild.collectAsState()
    val date by viewModel.weightDate.collectAsState()
    val age by viewModel.weightAgeMonths.collectAsState()
    val weight by viewModel.weightValue.collectAsState()
    val notes by viewModel.weightNotes.collectAsState()
    val error by viewModel.weightFormError.collectAsState()
    val scrollState = rememberScrollState()

    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val todayStr = formatter.format(calendar.time)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Input Berat Badan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = KmsDark
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("weight_form_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = KmsDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFFDFBFF),
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // Selected Child Badge Card (Header)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    selectedChild?.let { child ->
                        val color = if (child.gender == "Perempuan") KmsPink else KmsBlue
                        val icon = if (child.gender == "Perempuan") Icons.Default.Girl else Icons.Default.Boy
                        
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(color.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = child.gender,
                                tint = color,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = child.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = KmsDark
                            )
                            Text(
                                text = "${child.gender}, Lahir: ${child.birthDate}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    } ?: run {
                        Icon(
                            imageVector = Icons.Default.Boy,
                            contentDescription = "No child selected",
                            tint = Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Silakan pilih anak terlebih dahulu di tab Anak.",
                            fontSize = 13.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Input form elements
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Catat Timbangan Baru",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = KmsDark,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Recording Date selection
                    OutlinedTextField(
                        value = date,
                        onValueChange = { viewModel.weightDate.value = it },
                        label = { Text("Tanggal Timbang") },
                        placeholder = { Text("dd/mm/yyyy (cth: 12/05/2024)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Kalender",
                                tint = KmsPink
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.weightDate.value = todayStr }) {
                                Text(
                                    text = "Hari Ini",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KmsPink
                                )
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KmsPink,
                            focusedLabelColor = KmsPink,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("weight_date_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Age in Months input
                    OutlinedTextField(
                        value = age,
                        onValueChange = { viewModel.weightAgeMonths.value = it },
                        label = { Text("Umur ke- (Bulan)") },
                        placeholder = { Text("0 sampai 24 bulan") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Timeline,
                                contentDescription = "Umur bulan",
                                tint = KmsPink
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KmsPink,
                            focusedLabelColor = KmsPink,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("weight_age_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Weight Value (kg) input
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { viewModel.weightValue.value = it },
                        label = { Text("Berat Badan (kg)") },
                        placeholder = { Text("Gunakan desimal (cth: 9.2)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Scale,
                                contentDescription = "Timbangan",
                                tint = KmsPink
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KmsPink,
                            focusedLabelColor = KmsPink,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("weight_value_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Notes (optional) input
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { viewModel.weightNotes.value = it },
                        label = { Text("Keterangan Tambahan (Opsional)") },
                        placeholder = { Text("Contoh: Lahir sehat, nafsu makan lancar") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = "Keterangan",
                                tint = KmsPink
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KmsPink,
                            focusedLabelColor = KmsPink,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("weight_notes_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    AnimatedVisibility(visible = error != null) {
                        error?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = {
                            viewModel.saveWeightRecord(onSuccess = onNavigateBack)
                        },
                        enabled = selectedChild != null,
                        colors = ButtonDefaults.buttonColors(containerColor = KmsPink),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("weight_save_button")
                    ) {
                        Text(
                            text = "Simpan Data Timbangan",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
