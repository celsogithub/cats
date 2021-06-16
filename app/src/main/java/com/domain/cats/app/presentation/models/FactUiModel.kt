package com.domain.cats.app.presentation.models

import com.domain.cats.app.domain.models.Cat

data class FactUiModel(
    val fullName: String,
    val text: String
)

fun Cat.toUiModel(): FactUiModel {
    val fullName = this.user?.let { String.format("%s %s", it.name.first, it.name.last) } ?: ""

    return FactUiModel(
        fullName = fullName,
        text = this.text
    )
}