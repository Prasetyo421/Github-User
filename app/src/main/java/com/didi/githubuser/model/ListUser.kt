package com.didi.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListUser (
    val login: String,
    val avatarUrl: String,
    val url: String,
    val htmlUrl: String
): Parcelable