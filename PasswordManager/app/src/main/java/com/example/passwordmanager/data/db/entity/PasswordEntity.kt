package com.example.passwordmanager.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.passwordmanager.domain.model.Password

@Entity(tableName = "passwords")
data class PasswordEntity(
    val title: String,
    val link: String,
    val login: String,
    val password: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun toModel(): Password = Password(id, title, link, login, password)
}