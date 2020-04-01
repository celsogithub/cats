package com.domain.cats.app.presentation.models

import android.os.Parcelable
import com.domain.cats.app.domain.models.Cat
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FactUiModel(
    val fullName: String,
    val text: String
) : Parcelable

fun Cat.toUiModel(): FactUiModel {
    val fullName = this.user?.let { String.format("%s %s", it.name.first, it.name.last) } ?: ""

    return FactUiModel(
        fullName = fullName,
        text = this.text
    )
}