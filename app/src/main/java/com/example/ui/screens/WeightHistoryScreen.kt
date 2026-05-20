package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Boy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Girl
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KmsBlue
import com.example.ui.theme.KmsDark
import com.example.ui.theme.KmsPink
import com.example.ui.theme.KmsPinkContainer
import com.example.viewmodel.KmsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightHistoryScreen(
    viewModel: KmsViewModel,
    modifier: Modifier = Modifier
) {
    val selectedChild by viewModel.selectedChild.collectAsState()
    val records by viewModel.weightRecords.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Riwayat Berat Badan",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = KmsDark
                    )
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

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
                            imageVector = Icons.Default.Info,
                            contentDescription = "No child selected",
                            tint = KmsPink,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Pilih anak terlebih dahulu di menu Utama / Anak.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = KmsDark
                        )
                    }
                }
            }

            if (selectedChild == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pilih profil anak untuk melihat tabel riwayat.")
                }
            } else if (records.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(KmsPinkContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = "History empty list",
                            tint = KmsPink,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Belum Ada Catatan Timbangan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = KmsDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Silakan catat berat badan si kecil melalui menu 'Input Berat' di Menu Utama.",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Text(
                    text = "Tabel Rekaman Timbangan (${records.size} log)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = KmsDark,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

                // TABLE LOG (matching item 9 exactly!)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("weight_history_table"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Table Header Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFF1F3))
                                .padding(vertical = 12.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Tanggal", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = KmsDark, modifier = Modifier.weight(1.2f))
                            Text(text = "Umur", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = KmsDark, modifier = Modifier.weight(0.8f), textAlign = TextAlign.Center)
                            Text(text = "Berat", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = KmsDark, modifier = Modifier.weight(0.9f), textAlign = TextAlign.Center)
                            Text(text = "Keterangan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = KmsDark, modifier = Modifier.weight(1.5f))
                            Text(text = "", modifier = Modifier.width(36.dp)) // space for delete action
                        }

                        // Table Body Rows
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(records.sortedByDescending { it.ageInMonths }) { record ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp, horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = record.date,
                                        fontSize = 12.sp,
                                        color = KmsDark.copy(alpha = 0.8f),
                                        modifier = Modifier.weight(1.2f)
                                    )
                                    Text(
                                        text = "${record.ageInMonths} bln",
                                        fontSize = 12.sp,
                                        color = KmsDark,
                                        modifier = Modifier.weight(0.8f),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "${record.weight} kg",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = KmsPink,
                                        modifier = Modifier.weight(0.9f),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = record.notes.ifEmpty { "-" },
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.weight(1.5f),
                                        maxLines = 2
                                    )
                                    
                                    // Row deletion
                                    IconButton(
                                        onClick = { viewModel.deleteWeightRecord(record) },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .testTag("delete_weight_${record.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Hapus data timbangan",
                                            tint = Color.Gray.copy(alpha = 0.6f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(Color(0xFFEEEEEE))
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
