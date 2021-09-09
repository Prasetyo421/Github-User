package com.didi.githubuser.ViewModel

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didi.githubuser.BuildConfig.GITHUB_API_KEY
import com.didi.githubuser.R
import com.didi.githubuser.activity.DetailUserActivity
import com.didi.githubuser.model.DetailUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailUserViewModel: ViewModel() {
    companion object {
        val TAG = DetailUserActivity::class.java.simpleName
    }

    private var detailUser = MutableLiveData<DetailUser>()

    fun setDetailUser(username: String){
        val url = "https://api.github.com/users/$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", GITHUB_API_KEY)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d(TAG, result)
                    val responseObject = JSONObject(result)
                    Log.d(TAG, responseObject.getString("login"))
                    val login = responseObject.getString("login")
                    val avatar_url = responseObject.getString("avatar_url")
                    val bio = responseObject.getString("bio")
                    val name = responseObject.getString("name")
                    val location = responseObject.getString("location")
                    val follower = responseObject.getInt("followers")
                    val following = responseObject.getInt("following")
                    val repository = responseObject.getInt("public_repos")

                    val user = DetailUser(login, name, avatar_url, bio, follower, following, repository, location)
                    user.location?.let { Log.d(TAG, it) }

                    detailUser.postValue(user)

                }catch (e: Exception){
                    Log.d(TAG, e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(TAG, error?.message.toString())
            }
        })
    }

    fun getDetailUser(): LiveData<DetailUser>{
        return detailUser
    }
}