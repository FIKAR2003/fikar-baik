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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SportsGymnastics
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Article
import com.example.ui.theme.KmsBlue
import com.example.ui.theme.KmsDark
import com.example.ui.theme.KmsGreen
import com.example.ui.theme.KmsOrange
import com.example.ui.theme.KmsPink
import com.example.ui.theme.KmsYellow
import com.example.viewmodel.KmsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationScreen(
    viewModel: KmsViewModel,
    onNavigateToDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favoriteArticleIds.collectAsState()
    val articles = Article.DEFAULT_ARTICLES

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edukasi Gizi",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Materi Informasi & Nutrisi Penting",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = KmsDark,
                    modifier = Modifier.padding(start = 2.dp)
                )
                Text(
                    text = "Pelajari rujukan pola makan, asupan, serta stimulasi penunjang pertumbuhan balita sehat.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 2.dp, top = 2.dp, bottom = 4.dp),
                    lineHeight = 16.sp
                )
            }

            items(articles) { article ->
                val isFav = favorites.contains(article.id)
                
                // Assign a cute unique aesthetic element to each topic
                val (colorTheme, icon) = when (article.id) {
                    "gizi_seimbang" -> Pair(KmsGreen, Icons.Default.Restaurant)
                    "makanan_bergizi" -> Pair(KmsOrange, Icons.Default.FoodBank)
                    "stimulasi_tumbuh" -> Pair(KmsBlue, Icons.Default.SportsGymnastics)
                    else -> Pair(KmsPink, Icons.Default.HealthAndSafety)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.selectArticle(article)
                            onNavigateToDetail()
                        }
                        .testTag("article_card_${article.id}"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Cute topic visual representations
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(colorTheme.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = article.title,
                                tint = colorTheme,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = article.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = KmsDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = article.shortDescription,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                maxLines = 2,
                                lineHeight = 16.sp
                            )
                        }

                        // Play / Favorite controls block
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = { viewModel.toggleFavorite(article.id) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("favorite_button_${article.id}")
                            ) {
                                Icon(
                                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Simpan ke favorit",
                                    tint = KmsPink,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Detail",
                                tint = Color.LightGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
