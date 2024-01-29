package com.example.passwordmanager.domain.usecase

import java.security.SecureRandom

class GeneratePasswordUseCase {
    operator fun invoke(
        length: Int,
        includeSymbols: Boolean,
        includeDigits: Boolean): String {

        val symbols = listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_')

        val charset: List<Char> = ('A'..'Z') + ('a'..'z') +
            if (includeSymbols) symbols else emptyList<Char>() +
            if (includeDigits) ('0'..'9') else emptyList<Char>()

        val secureRandom = SecureRandom()

        return (1..length).map {
            charset[secureRandom.nextInt(charset.size)]
        }.joinToString("")

    }
}