package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Boy
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Girl
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KmsBlue
import com.example.ui.theme.KmsDark
import com.example.ui.theme.KmsPink
import com.example.viewmodel.KmsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildFormScreen(
    viewModel: KmsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val name by viewModel.childName.collectAsState()
    val dob by viewModel.childBirthDate.collectAsState()
    val gender by viewModel.childGender.collectAsState()
    val error by viewModel.childFormError.collectAsState()
    val scrollState = rememberScrollState()

    // Helper to auto fill today's date if requested
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val todayStr = formatter.format(calendar.time)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Data Anak",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = KmsDark
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("child_form_back_button")
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
                        text = "Tambah Profil Tumbuh Kembang Anak",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = KmsDark,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Child Name input
                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.childName.value = it },
                        label = { Text("Nama Anak") },
                        placeholder = { Text("Masukkan nama lengkap anak") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.ChildCare,
                                contentDescription = "Nama",
                                tint = KmsPink
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KmsPink,
                            focusedLabelColor = KmsPink,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("child_name_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Date of Birth input
                    OutlinedTextField(
                        value = dob,
                        onValueChange = { viewModel.childBirthDate.value = it },
                        label = { Text("Tanggal Lahir") },
                        placeholder = { Text("dd/mm/yyyy (cth: 12/03/2023)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Tanggal Lahir",
                                tint = KmsPink
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.childBirthDate.value = todayStr }) {
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
                            .testTag("child_dob_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Jenis Kelamin",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = KmsDark,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Gender Radio selections (Row)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Gender: Perempuan Custom Button Selection
                        GenderSelectCard(
                            label = "Perempuan",
                            icon = Icons.Default.Girl,
                            color = KmsPink,
                            isSelected = gender == "Perempuan",
                            onClick = { viewModel.childGender.value = "Perempuan" },
                            modifier = Modifier.weight(1f)
                        )

                        // Gender: Laki-laki Custom Button Selection
                        GenderSelectCard(
                            label = "Laki-laki",
                            icon = Icons.Default.Boy,
                            color = KmsBlue,
                            isSelected = gender == "Laki-laki",
                            onClick = { viewModel.childGender.value = "Laki-laki" },
                            modifier = Modifier.weight(1f)
                        )
                    }

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
                            viewModel.saveChild(onSuccess = onNavigateBack)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = KmsPink),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("child_save_button")
                    ) {
                        Text(
                            text = "Simpan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenderSelectCard(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(54.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color.copy(alpha = 0.12f) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) color else Color(0xFFE0E0E0)
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) color else KmsDark
            )
        }
    }
}
