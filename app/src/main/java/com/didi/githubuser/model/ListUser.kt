package com.didi.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListUser (
    val login: String,
    val avatar_url: String,
    val url: String,
    val html_url: String
): Parcelable