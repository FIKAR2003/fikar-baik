package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Boy
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Girl
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WeightRecord
import com.example.ui.theme.KmsBlue
import com.example.ui.theme.KmsDark
import com.example.ui.theme.KmsGreen
import com.example.ui.theme.KmsGreenLight
import com.example.ui.theme.KmsOrange
import com.example.ui.theme.KmsPink
import com.example.ui.theme.KmsPinkContainer
import com.example.ui.theme.KmsPinkLight
import com.example.ui.theme.KmsYellow
import com.example.viewmodel.KmsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KmsChartScreen(
    viewModel: KmsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedChild by viewModel.selectedChild.collectAsState()
    val records by viewModel.weightRecords.collectAsState()
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf(0) } // 0 = BB/U, 1 = TB/U, 2 = BB/TB

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Grafik KMS Interaktif",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = KmsDark
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("kms_chart_back_button")
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
        ) {
            
            // Tab Selector Row (BB/U, TB/U, BB/TB) matching Mockup Item 6
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = KmsPink,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = KmsPink
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("BB/U", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("TB/U", color = Color.Gray, fontSize = 13.sp) },
                    enabled = true // Let user tap to see a nice explainer
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("BB/TB", color = Color.Gray, fontSize = 13.sp) },
                    enabled = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Child profiling banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(color.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = child.name,
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
                                tint = KmsOrange,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Pilih atau tambah anak lebih dulu di tab Anak.",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = KmsDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedTab != 0) {
                    // Explainer Card for disabled/alternate growth charts not requested but represented
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShowChart,
                                contentDescription = "Growth standard chart",
                                tint = KmsBlue,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (selectedTab == 1) "Grafik Tinggi Badan menurut Umur (TB/U)" else "Grafik Berat Badan menurut Tinggi (BB/TB)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = KmsDark,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Fitur ini mengintegrasikan standar antropometri anak WHO untuk melacak tinggi badan si kecil secara linear. Tersedia lengkap pada rilis versi selanjutnya. Gunakan Grafik BB/U untuk mencatat timbangan berat badan aktif saat ini.",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }
                } else {
                    // RENDER ACTIVE KMS WEIGHT-FOR-AGE CHART!
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("kms_weight_chart_card"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Kurva Kartu Menuju Sehat (KMS) - BB/U",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = KmsDark,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Interactive Custom Canvas KMS Chart Drawing
                            KmsCanvasChart(
                                records = records,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp)
                                    .background(Color(0xFFF9F9F9), shape = RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFF0F0F0), shape = RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Chart Legend Indicators
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LegendItem(color = KmsOrange, label = "Lebih")
                                LegendItem(color = KmsGreen, label = "Normal")
                                LegendItem(color = KmsYellow, label = "Kurang")
                                LegendItem(color = Color.Red, label = "Buruk")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dynamic Zone text status box matching bottom item 6
                    if (selectedChild != null && records.isNotEmpty()) {
                        val latest = records.maxByOrNull { it.ageInMonths }!!
                        val result = evaluateWeightZone(latest.ageInMonths, latest.weight)
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = result.colorBg),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = result.icon,
                                    contentDescription = "Status",
                                    tint = result.colorFg,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "Status Tumbuh Kembang",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = result.colorFg
                                    )
                                    Text(
                                        text = "Berat badan ${selectedChild!!.name} berada di zona ${result.statusName} (${latest.weight} kg pada usia ${latest.ageInMonths} Bulan).",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = KmsDark,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    } else if (selectedChild != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "No records",
                                    tint = KmsBlue,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Belum ada rekaman timbangan untuk ${selectedChild!!.name}. Masukkan data berat di menu utama.",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KmsDark
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

// Coordinate drawing & math helper
@Composable
fun KmsCanvasChart(
    records: List<WeightRecord>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val paddingLeft = 40f
        val paddingRight = 20f
        val paddingTop = 20f
        val paddingBottom = 40f

        val graphWidth = width - paddingLeft - paddingRight
        val graphHeight = height - paddingTop - paddingBottom

        // Draw coordinate grid guidelines
        val xMax = 24f // max 24 months
        val yMax = 18f // max 18 kg
        val yMin = 2f

        // X-axis lines (months lines every 3 months: 0, 3, 6, 9, 12, 15, 18, 21, 24)
        for (m in 0..24 step 3) {
            val xPos = paddingLeft + (m.toFloat() / xMax) * graphWidth
            drawLine(
                color = Color(0xFFEBEBEB),
                start = Offset(xPos, paddingTop),
                end = Offset(xPos, height - paddingBottom),
                strokeWidth = 2f
            )
        }

        // Y-axis lines (weight labels every 2 kg: 2, 4, 6, 8, 10, 12, 14, 16, 18)
        for (w in 2..18 step 2) {
            val yPos = height - paddingBottom - ((w.toFloat() - yMin) / (yMax - yMin)) * graphHeight
            drawLine(
                color = Color(0xFFEBEBEB),
                start = Offset(paddingLeft, yPos),
                end = Offset(width - paddingRight, yPos),
                strokeWidth = 2f
            )
        }

        // helper mapping age/weight to pixel coordinates
        fun getCoordinates(age: Float, weight: Float): Offset {
            val x = paddingLeft + (age / xMax) * graphWidth
            // Clamp weight to look nice on grid
            val clampedWeight = weight.coerceIn(yMin, yMax)
            val y = height - paddingBottom - ((clampedWeight - yMin) / (yMax - yMin)) * graphHeight
            return Offset(x, y)
        }

        // Draw standard WHO percentile background zones (Curves)
        // Set 4 lines: 
        // 1. Sangat Kurang Boundary: w = 2.0 + 0.28 * Age
        // 2. Kurang Boundary: w = 3.0 + 0.35 * Age
        // 3. Normal Upper boundary: w = 4.2 + 0.44 * Age
        // 4. Overweight upper boundary: w = 5.2 + 0.53 * Age
        val severePathList = mutableListOf<Offset>()
        val underweightPathList = mutableListOf<Offset>()
        val normalPathList = mutableListOf<Offset>()
        val overweightPathList = mutableListOf<Offset>()

        for (m in 0..24) {
            val age = m.toFloat()
            severePathList.add(getCoordinates(age, 1.8f + 0.25f * age))
            underweightPathList.add(getCoordinates(age, 2.5f + 0.32f * age))
            normalPathList.add(getCoordinates(age, 3.8f + 0.43f * age))
            overweightPathList.add(getCoordinates(age, 4.6f + 0.52f * age))
        }

        // Draw Percentile lines matching standard color tags
        fun drawPercentileCurve(points: List<Offset>, color: Color, stroke: Float = 3f) {
            for (i in 0 until points.size - 1) {
                drawLine(
                    color = color,
                    start = points[i],
                    end = points[i + 1],
                    strokeWidth = stroke,
                )
            }
        }

        drawPercentileCurve(severePathList, Color.Red.copy(alpha = 0.5f))
        drawPercentileCurve(underweightPathList, KmsYellow.copy(alpha = 0.6f))
        drawPercentileCurve(normalPathList, KmsGreen.copy(alpha = 0.6f))
        drawPercentileCurve(overweightPathList, KmsOrange.copy(alpha = 0.5f))

        // Draw client's actual data points & connect them
        if (records.isNotEmpty()) {
            val plottedPoints = records.sortedBy { it.ageInMonths }.map { record ->
                getCoordinates(record.ageInMonths.toFloat(), record.weight.toFloat())
            }

            // Draw connecting lines (Solid Bright Pink)
            for (i in 0 until plottedPoints.size - 1) {
                drawLine(
                    color = KmsPink,
                    start = plottedPoints[i],
                    end = plottedPoints[i + 1],
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )
            }

            // Draw anchor dots
            plottedPoints.forEach { point ->
                // Outer glow
                drawCircle(
                    color = KmsPink.copy(alpha = 0.3f),
                    radius = 12f,
                    center = point
                )
                // Inner solid
                drawCircle(
                    color = KmsPink,
                    radius = 7.5f,
                    center = point
                )
                // Highlight core
                drawCircle(
                    color = Color.White,
                    radius = 3f,
                    center = point
                )
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 11.sp, color = KmsDark, fontWeight = FontWeight.Medium)
    }
}

// Zone evaluation helper
data class EvaluationResult(
    val statusName: String,
    val colorBg: Color,
    val colorFg: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

fun evaluateWeightZone(ageInMonths: Int, weight: Double): EvaluationResult {
    val m = ageInMonths.toFloat()
    val severeLimit = 1.8 + 0.25 * m
    val underweightLimit = 2.5 + 0.32 * m
    val normalLimit = 3.8 + 0.43 * m

    return when {
        weight < severeLimit -> EvaluationResult(
            statusName = "Sangat Kurang (Gizi Buruk)",
            colorBg = Color(0xFFFFEBEE), // soft red
            colorFg = Color.Red,
            icon = Icons.Default.Cancel
        )
        weight < underweightLimit -> EvaluationResult(
            statusName = "Kurang (Gizi Kurang)",
            colorBg = Color(0xFFFFFDE7), // soft yellow
            colorFg = KmsOrange,
            icon = Icons.Default.Warning
        )
        weight < normalLimit -> EvaluationResult(
            statusName = "Normal (Sehat & Ideal)",
            colorBg = KmsGreenLight, // soft green
            colorFg = KmsGreen,
            icon = Icons.Default.CheckCircle
        )
        else -> EvaluationResult(
            statusName = "Lebih (Kelebihan Gizi/Overweight)",
            colorBg = Color(0xFFE3F2FD), // soft blue
            colorFg = KmsBlue,
            icon = Icons.Default.Info
        )
    }
}
