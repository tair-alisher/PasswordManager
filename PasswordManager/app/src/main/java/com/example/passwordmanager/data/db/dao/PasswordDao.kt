package com.example.passwordmanager.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.passwordmanager.data.db.entity.PasswordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {

    @Query("SELECT * FROM passwords WHERE id = :id")
    suspend fun getById(id: Int): PasswordEntity

    @Query("SELECT * FROM passwords ORDER BY title ASC")
    fun getAll(): Flow<List<PasswordEntity>>

    @Update
    suspend fun update(password: PasswordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPassword(password: PasswordEntity)

    @Query("DELETE FROM passwords WHERE id = :id")
    suspend fun deletePassword(id: Int)

    @Query("SELECT * FROM passwords where title LIKE :searchVal OR login LIKE :searchVal")
    fun searchPassword(searchVal: String): Flow<List<PasswordEntity>>
}