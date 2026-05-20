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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Article
import com.example.ui.theme.KmsBlue
import com.example.ui.theme.KmsDark
import com.example.ui.theme.KmsGreen
import com.example.ui.theme.KmsOrange
import com.example.ui.theme.KmsPink
import com.example.ui.theme.KmsPinkContainer
import com.example.viewmodel.KmsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationDetailScreen(
    viewModel: KmsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val article by viewModel.selectedArticle.collectAsState()
    val favorites by viewModel.favoriteArticleIds.collectAsState()
    val scrollState = rememberScrollState()

    if (article == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Pilih artikel terlebih dahulu.")
        }
        return
    }

    val currentArticle = article!!
    val isFav = favorites.contains(currentArticle.id)

    // Assign aesthetic values matching topic
    val (colorTheme, icon) = when (currentArticle.id) {
        "gizi_seimbang" -> Pair(KmsGreen, Icons.Default.Restaurant)
        "makanan_bergizi" -> Pair(KmsOrange, Icons.Default.FoodBank)
        "stimulasi_tumbuh" -> Pair(KmsBlue, Icons.Default.SportsGymnastics)
        else -> Pair(KmsPink, Icons.Default.HealthAndSafety)
    }

    // Interactive simulated audio player logic
    var isPlaying by remember { mutableStateOf(false) }
    var currentSeconds by remember { mutableIntStateOf(0) }
    val totalSeconds = currentArticle.durationSeconds

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (currentSeconds < totalSeconds) {
                delay(1000)
                currentSeconds++
            }
            isPlaying = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentArticle.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = KmsDark
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = KmsDark
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleFavorite(currentArticle.id) },
                        modifier = Modifier.testTag("detail_favorite_button")
                    ) {
                        Icon(
                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Simpan ke favorit",
                            tint = KmsPink
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // Hero Illustration Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                colors = CardDefaults.cardColors(containerColor = colorTheme.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = currentArticle.title,
                        tint = colorTheme,
                        modifier = Modifier.size(90.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Simulated Audio Player widget (Matching Item 8 in Mockup exactly!)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("audio_player_widget"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF37474F)), // dark slate background
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = "Audiobook",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Simulasi Audio KMS Digital",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Playback timer format
                        Text(
                            text = "${formatTime(currentSeconds)} / ${formatTime(totalSeconds)}",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Play/Pause button
                        IconButton(
                            onClick = { 
                                if (currentSeconds >= totalSeconds) {
                                    currentSeconds = 0
                                }
                                isPlaying = !isPlaying 
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .background(KmsPink, shape = CircleShape)
                                .testTag("audio_play_pause_button")
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Draggable Slider simulation
                        Slider(
                            value = currentSeconds.toFloat(),
                            onValueChange = { 
                                isPlaying = false
                                currentSeconds = it.toInt() 
                            },
                            valueRange = 0f..totalSeconds.toFloat(),
                            colors = SliderDefaults.colors(
                                activeTrackColor = KmsPink,
                                inactiveTrackColor = Color.White.copy(alpha = 0.24f),
                                thumbColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Text Details Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Penjelasan Lengkap :",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = KmsDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentArticle.fullContent,
                    fontSize = 13.sp,
                    color = KmsDark.copy(alpha = 0.8f),
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Justify
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ingat! Callout Card at bottom
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = KmsPinkContainer.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Love icon",
                        tint = KmsPink,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Ingat!\nBiasakan anak makan makanan sehat, beragam, dan cukup gizi setiap hari.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = KmsPink,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

// Time formator helper
private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}
