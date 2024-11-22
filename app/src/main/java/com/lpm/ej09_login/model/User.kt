package com.lpm.ej09_login.model

data class User(
    val id: String?,
    val userId: String?,
    val displayName: String?,
    val avatarUrl: String?,
    val quote: String?,
    val profession: String?,

    ) {
    fun toMap(): MutableMap<String, String?>{
        return mutableMapOf(
            "user_Id" to this.userId,
            "display_name" to this.displayName,
            "profession" to this.profession,
            "quote" to this.quote,
            "avatar_url" to this.avatarUrl
        )
    }
}