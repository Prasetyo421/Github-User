package com.didi.githubuser.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didi.githubuser.BuildConfig
import com.didi.githubuser.model.ListUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class ListUsersViewModel: ViewModel() {
    companion object {
        val TAG = ListUsersViewModel::class.java.simpleName
    }

    val listUsers = MutableLiveData<ArrayList<ListUser>>()

    fun setListUser(type: String, username: String) {
        val listItems = ArrayList<ListUser>()

        var url: String? = null
        if (type == "followers"){
            url = "https://api.github.com/users/$username/followers"
        }else if (type == "following") {
            url = "https://api.github.com/users/$username/following"
        }

        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_API_KEY)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val list = JSONArray(result)

                    Log.d(TAG, result)
                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val login = user.getString("login")
                        val avatr_url = user.getString("avatar_url")
                        val url_detail = user.getString("url")
                        val html_url = user.getString("html_url")
                        val userItems = ListUser(login, avatr_url, url_detail, html_url)
                        listItems.add(userItems)
                    }

                    listUsers.postValue(listItems)

                } catch (e: Exception) {
                    e.printStackTrace()
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

    fun getListUser(): LiveData<ArrayList<ListUser>> {
        return listUsers
    }

}