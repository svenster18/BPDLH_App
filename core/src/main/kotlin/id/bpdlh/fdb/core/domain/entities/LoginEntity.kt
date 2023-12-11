package id.bpdlh.fdb.core.domain.entities

import androidx.annotation.Keep

@Keep
data class LoginEntity(
    val accessToken: String,
    val userID: String,
    val email: String,
    val role: String
)
