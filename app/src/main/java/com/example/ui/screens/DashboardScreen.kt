package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Boy
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Girl
import androidx.compose.material.icons.filled.LineAxis
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KmsBlue
import com.example.ui.theme.KmsDark
import com.example.ui.theme.KmsGreen
import com.example.ui.theme.KmsOrange
import com.example.ui.theme.KmsPink
import com.example.ui.theme.KmsPinkContainer
import com.example.ui.theme.KmsPinkLight
import com.example.ui.theme.KmsYellow
import com.example.viewmodel.KmsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: KmsViewModel,
    onNavigateToTab: (String) -> Unit, // bottom tabs routing helper
    onNavigateToWeightForm: () -> Unit,
    onNavigateToKmsChart: () -> Unit,
    onNavigateToChildForm: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentParent by viewModel.currentParent.collectAsState()
    val children by viewModel.childrenList.collectAsState()
    val selectedChild by viewModel.selectedChild.collectAsState()
    val scrollState = rememberScrollState()

    // Tips generator
    val tips = listOf(
        "Berikan makanan bergizi seimbang untuk tumbuh kembang optimal si kecil.",
        "Ibu, pastikan si kecil mendapat ASI Eksklusif sampai usia 6 bulan.",
        "Pantau berat badan si kecil sebulan sekali untuk deteksi dini gejala stunting.",
        "Kenalkan protein hewani sejak MPASI dini seperti telur dan ikan kembung kaya Omega-3.",
        "Jaga kebersihan botol susu dan peralatan makan si kecil untuk mencegah bakteri pencernaan."
    )
    val tipOfDay = tips[((currentParent?.name?.length ?: 0) + (selectedChild?.name?.length ?: 0)) % tips.size]

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBFF))
            .verticalScroll(scrollState)
    ) {
        // Dynamic Greeting Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(KmsPink, KmsPink.copy(alpha = 0.85f))
                    )
                )
                .padding(top = 28.dp, start = 24.dp, end = 24.dp, bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Halo, ${currentParent?.name ?: "Ibu"}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Selamat datang kembali 💛",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Small helper logout icon button
                    IconButton(
                        onClick = {
                            viewModel.logout()
                            onLogout()
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                            .testTag("dashboard_logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Active Child Info Panel Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    if (selectedChild == null) {
                        Text(
                            text = "Profil Anak Aktif",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = KmsDark
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Belum Ada Anak Terpilih",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = KmsDark.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "Daftarkan atau pilih anak untuk merekam berat badan.",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Button(
                                onClick = onNavigateToChildForm,
                                colors = ButtonDefaults.buttonColors(containerColor = KmsPink),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = ButtonDefaults.ContentPadding
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Child",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tambah", fontSize = 12.sp, color = Color.White)
                            }
                        }
                    } else {
                        val child = selectedChild!!
                        val genderIcon = if (child.gender == "Perempuan") Icons.Default.Girl else Icons.Default.Boy
                        val genderColor = if (child.gender == "Perempuan") KmsPink else KmsBlue

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(genderColor.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = genderIcon,
                                        contentDescription = "Child icon",
                                        tint = genderColor,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = child.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = KmsDark
                                    )
                                    Text(
                                        text = "${child.gender}, Lahir: ${child.birthDate}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(KmsPinkLight)
                                    .clickable { onNavigateToTab("anak") }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Ubah",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KmsPink
                                )
                            }
                        }
                    }
                }
            }

            // Core features Grid Buttons (exactly matching Mockup layout list)
            Text(
                text = "Menu Layanan KMS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = KmsDark,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Action 1: Data Anak
                DashboardMenuButton(
                    title = "Data Anak",
                    subtitle = "Profil si kecil",
                    icon = Icons.Default.ChildCare,
                    iconColor = KmsPink,
                    bgColor = Color.White,
                    onClick = { onNavigateToTab("anak") },
                    modifier = Modifier.weight(1f)
                )

                // Action 2: Input Berat Badan
                DashboardMenuButton(
                    title = "Input Berat",
                    subtitle = "Timbang anak",
                    icon = Icons.Default.Assignment,
                    iconColor = KmsBlue,
                    bgColor = Color.White,
                    onClick = onNavigateToWeightForm,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Action 3: Grafik KMS
                DashboardMenuButton(
                    title = "Grafik KMS",
                    subtitle = "Lacak tumbuh",
                    icon = Icons.Default.LineAxis,
                    iconColor = KmsGreen,
                    bgColor = Color.White,
                    onClick = onNavigateToKmsChart,
                    modifier = Modifier.weight(1f)
                )

                // Action 4: Edukasi Gizi
                DashboardMenuButton(
                    title = "Edukasi Gizi",
                    subtitle = "Artikel nutrisi",
                    icon = Icons.Default.Book,
                    iconColor = KmsOrange,
                    bgColor = Color.White,
                    onClick = { onNavigateToTab("edukasi") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tips Hari Ini Panel
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)), // sweet warm light orange
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(KmsOrange.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TipsAndUpdates,
                            contentDescription = "Tips",
                            tint = KmsOrange,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tips Hari Ini",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = KmsOrange
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tipOfDay,
                            fontSize = 13.sp,
                            color = KmsDark.copy(alpha = 0.8f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardMenuButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    bgColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = KmsDark
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
