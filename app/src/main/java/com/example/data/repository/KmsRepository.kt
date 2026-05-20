package com.example.data.repository

import com.example.data.dao.ChildDao
import com.example.data.dao.FavoriteDao
import com.example.data.dao.ParentDao
import com.example.data.dao.WeightDao
import com.example.data.model.Child
import com.example.data.model.FavoriteArticle
import com.example.data.model.ParentUser
import com.example.data.model.WeightRecord
import kotlinx.coroutines.flow.Flow

class KmsRepository(
    private val parentDao: ParentDao,
    private val childDao: ChildDao,
    private val weightDao: WeightDao,
    private val favoriteDao: FavoriteDao
) {
    suspend fun registerParent(parent: ParentUser): Long {
        return parentDao.insertParent(parent)
    }

    suspend fun getParentByUsername(username: String): ParentUser? {
        return parentDao.getParentByUsername(username)
    }

    fun getParentById(id: Long): Flow<ParentUser?> {
        return parentDao.getParentById(id)
    }

    suspend fun insertChild(child: Child): Long {
        return childDao.insertChild(child)
    }

    fun getChildrenForParent(parentId: Long): Flow<List<Child>> {
        return childDao.getChildrenForParent(parentId)
    }

    fun getChildById(id: Long): Flow<Child?> {
        return childDao.getChildById(id)
    }

    suspend fun deleteChild(child: Child) {
        childDao.deleteChild(child)
    }

    suspend fun insertWeightRecord(record: WeightRecord): Long {
        return weightDao.insertWeightRecord(record)
    }

    fun getWeightRecordsForChild(childId: Long): Flow<List<WeightRecord>> {
        return weightDao.getWeightRecordsForChild(childId)
    }

    suspend fun deleteWeightRecord(record: WeightRecord) {
        weightDao.deleteWeightRecord(record)
    }

    suspend fun insertFavorite(articleId: String) {
        favoriteDao.insertFavorite(FavoriteArticle(articleId))
    }

    suspend fun deleteFavorite(articleId: String) {
        favoriteDao.deleteFavorite(FavoriteArticle(articleId))
    }

    fun getAllFavorites(): Flow<List<FavoriteArticle>> {
        return favoriteDao.getAllFavorites()
    }
}
