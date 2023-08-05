package com.snow.diary.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.snow.diary.database.model.DreamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DreamDao {
    @Transaction
    @Upsert
    fun upsert(vararg dream: DreamEntity)

    @Transaction
    @Delete
    fun delete(vararg dream: DreamEntity)

    @Transaction
    @Query("SELECT * FROM dream")
    fun getAllDreams(): Flow<List<DreamEntity>>

    @Transaction
    @Query("SELECT * FROM Dream WHERE id = :dreamId")
    fun getDreamById(dreamId: Long): Flow<DreamEntity?>
}