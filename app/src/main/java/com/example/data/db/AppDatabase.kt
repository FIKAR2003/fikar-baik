package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.dao.ChildDao
import com.example.data.dao.FavoriteDao
import com.example.data.dao.ParentDao
import com.example.data.dao.WeightDao
import com.example.data.model.Child
import com.example.data.model.FavoriteArticle
import com.example.data.model.ParentUser
import com.example.data.model.WeightRecord

@Database(
    entities = [
        ParentUser::class,
        Child::class,
        WeightRecord::class,
        FavoriteArticle::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun parentDao(): ParentDao
    abstract fun childDao(): ChildDao
    abstract fun weightDao(): WeightDao
    abstract fun favoriteDao(): FavoriteDao
}
