package com.domain.cats.app.data.remote.models

import com.domain.cats.app.domain.models.Cat
import com.google.gson.annotations.SerializedName

data class CatResponse(
    @SerializedName("_id") val id: String,
    val text: String,
    val user: UserResponse?
) {
    fun toCat() = Cat(
        id = id,
        text = text,
        user = user?.toUser()
    )
}