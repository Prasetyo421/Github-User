package com.didi.githubuser.model

import com.google.gson.annotations.SerializedName

data class ResponseItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("html_url")
	val htmlUrl: String,
)
