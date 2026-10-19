package com.duoc.madertimber.model.auth

import androidx.core.util.PatternsCompat

data class AuthErrors(
    val name: Boolean = false,
    val email: Boolean = false,
    val password: Boolean = false,
    val confirmation: Boolean = false
) {
    val hasErrors: Boolean get() = name || email || password || confirmation
}

object AuthValidator {
    fun validate(
        registering: Boolean,
        name: String,
        email: String,
        password: String,
        confirmation: String
    ): AuthErrors = AuthErrors(
        name = registering && name.isBlank(),
        email = !PatternsCompat.EMAIL_ADDRESS.matcher(email.trim()).matches(),
        password = password.isBlank() || (registering && password.length < 8),
        confirmation = registering && confirmation != password
    )
}
