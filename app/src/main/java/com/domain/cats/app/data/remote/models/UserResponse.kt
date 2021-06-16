package com.domain.cats.app.data.remote.models

import com.domain.cats.app.domain.models.User
import com.squareup.moshi.Json

data class UserResponse(
    @Json(name = "_id") val id: String,
    val name: UserNameResponse
) {
    fun toUser() = User(
        id = id,
        name = name.toUserName()
    )
}