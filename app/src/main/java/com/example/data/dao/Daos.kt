package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Child
import com.example.data.model.FavoriteArticle
import com.example.data.model.ParentUser
import com.example.data.model.WeightRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ParentDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertParent(parent: ParentUser): Long

    @Query("SELECT * FROM parents WHERE username = :username LIMIT 1")
    suspend fun getParentByUsername(username: String): ParentUser?

    @Query("SELECT * FROM parents WHERE id = :id LIMIT 1")
    fun getParentById(id: Long): Flow<ParentUser?>
}

@Dao
interface ChildDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChild(child: Child): Long

    @Query("SELECT * FROM children WHERE parentId = :parentId ORDER BY name ASC")
    fun getChildrenForParent(parentId: Long): Flow<List<Child>>

    @Query("SELECT * FROM children WHERE id = :id LIMIT 1")
    fun getChildById(id: Long): Flow<Child?>

    @Delete
    suspend fun deleteChild(child: Child)
}

@Dao
interface WeightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightRecord(record: WeightRecord): Long

    @Query("SELECT * FROM weight_records WHERE childId = :childId ORDER BY ageInMonths ASC, date ASC")
    fun getWeightRecordsForChild(childId: Long): Flow<List<WeightRecord>>

    @Delete
    suspend fun deleteWeightRecord(record: WeightRecord)
}

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(fav: FavoriteArticle)

    @Delete
    suspend fun deleteFavorite(fav: FavoriteArticle)

    @Query("SELECT * FROM favorite_articles")
    fun getAllFavorites(): Flow<List<FavoriteArticle>>
}
