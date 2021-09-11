package com.didi.githubuser.database
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tUser")
data class User(
    @PrimaryKey @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "url_avatar") val urlAvatar: String?,
    @ColumnInfo(name = "url_github") val urlGithub: String?
)
