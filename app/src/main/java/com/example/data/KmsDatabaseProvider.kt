package com.example.data

import android.content.Context
import androidx.room.Room
import com.example.data.db.AppDatabase
import com.example.data.repository.KmsRepository

object KmsDatabaseProvider {
    private var database: AppDatabase? = null
    private var repository: KmsRepository? = null

    fun getDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "kms_database"
            )
            .fallbackToDestructiveMigration()
            .build()
            database = db
            db
        }
    }

    fun getRepository(context: Context): KmsRepository {
        return repository ?: synchronized(this) {
            val db = getDatabase(context)
            val repo = KmsRepository(
                db.parentDao(),
                db.childDao(),
                db.weightDao(),
                db.favoriteDao()
            )
            repository = repo
            repo
        }
    }
}
