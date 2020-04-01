package com.domain.cats.app.domain.models

data class Cat(
    val id: String,
    val text: String,
    val user: User?
)