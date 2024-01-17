package com.example.passwordmanager.domain.model

data class Password(
    val id: Int = 0,
    val title: String,
    val link: String,
    val login: String,
    val password: String
)
