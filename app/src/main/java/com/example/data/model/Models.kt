package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parents")
data class ParentUser(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String, // Username used for parent login
    val name: String,     // Parent's display name
    val pin: String       // Password / PIN
)

@Entity(tableName = "children")
data class Child(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val parentId: Long,
    val name: String,
    val birthDate: String, // e.g. "12/03/2023"
    val gender: String     // "Perempuan" or "Laki-laki"
)

@Entity(tableName = "weight_records")
data class WeightRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val date: String,      // e.g. "12/05/2024"
    val ageInMonths: Int,  // Calculated or entered
    val weight: Double,    // in kg
    val notes: String = "" // Optional notes
)

@Entity(tableName = "favorite_articles")
data class FavoriteArticle(
    @PrimaryKey val articleId: String
)
