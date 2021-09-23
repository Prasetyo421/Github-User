package com.didi.githubuser.networking

import com.didi.githubuser.model.ResponseDetailUser
import com.didi.githubuser.model.ResponseSearchUser
import retrofit2.Call
import retrofit2.http.*
import com.didi.githubuser.BuildConfig.GITHUB_API_KEY
import com.didi.githubuser.model.ResponseFollow

interface ApiService {
    @Headers("Authorization: $GITHUB_API_KEY")
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<ResponseDetailUser>

    @Headers("Authorization: $GITHUB_API_KEY")
    @GET("search/users")
    fun getSearchUser(
        @Query("q") username: String
    ): Call<ResponseSearchUser>

    @Headers("Authorization: $GITHUB_API_KEY")
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ResponseFollow>>

    @Headers("Authorization: $GITHUB_API_KEY")
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ResponseFollow>>



}