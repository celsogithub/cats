package com.domain.cats.app.data.remote.models

import com.domain.cats.app.domain.models.UserName

data class UserNameResponse(
    val first: String,
    val last: String
) {
    fun toUserName() = UserName(
        first = first,
        last = last
    )
}