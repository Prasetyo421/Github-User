package com.didi.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailUser(
    val  login: String?,
    val  name: String?,
    val  avatarUrl: String?,
    val  bio: String?,
    val  follower: Int,
    val  following: Int,
    val  repository: Int,
    val  location: String?,
    val  github_url: String?,
    val  company: String?,
    val  email: String?
): Parcelable