package com.didi.githubuser.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didi.githubuser.BuildConfig.GITHUB_API_KEY
import com.didi.githubuser.activity.DetailUserActivity
import com.didi.githubuser.model.DetailUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailUserViewModel: ViewModel() {
    companion object {
        private val TAG = DetailUserActivity::class.java.simpleName
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
                    val responseObject = JSONObject(result)
                    val login = responseObject.getString("login")
                    val avatar_url = responseObject.getString("avatar_url")
                    val bio = responseObject.getString("bio")
                    val name = responseObject.getString("name")
                    val location = responseObject.getString("location")
                    val follower = responseObject.getInt("followers")
                    val following = responseObject.getInt("following")
                    val repository = responseObject.getInt("public_repos")
                    val github_url = responseObject.getString("html_url")
                    val company = responseObject.getString("company")
                    val email = responseObject.getString("email")

                    val user = DetailUser(login, name, avatar_url, bio, follower, following, repository, location, github_url, company, email)
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