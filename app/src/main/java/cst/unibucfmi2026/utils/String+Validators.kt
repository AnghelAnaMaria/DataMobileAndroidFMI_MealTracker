package cst.unibucfmi2026.utils

import android.util.Patterns

fun String.isValidEmail() = isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean {
    val hasNumber = any { it.isDigit() }
    val hasSpecial = any { !it.isLetterOrDigit() }
    val hasMinLength = length >= 6

    return hasMinLength && hasNumber && hasSpecial
}