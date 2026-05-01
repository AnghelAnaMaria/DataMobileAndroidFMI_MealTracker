package cst.unibucfmi2026.utils

import android.util.Patterns

fun String.isValidEmail() = isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword() = this.length > 3