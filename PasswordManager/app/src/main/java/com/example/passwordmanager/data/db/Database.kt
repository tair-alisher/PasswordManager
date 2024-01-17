package com.example.passwordmanager.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.passwordmanager.data.db.dao.PasswordDao
import com.example.passwordmanager.data.db.entity.PasswordEntity

@Database(
    entities = [PasswordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class Database: RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
}