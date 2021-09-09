package com.didi.githubuser.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didi.githubuser.BuildConfig.GITHUB_API_KEY
import com.didi.githubuser.model.SearchUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class SearchUserViewModel: ViewModel() {
    companion object {
        private val TAG = SearchUserViewModel::class.java.simpleName
    }

    val listUsers = MutableLiveData<ArrayList<SearchUser>>()

    fun setSearchUser(username: String) {
        val listItems = ArrayList<SearchUser>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization", GITHUB_API_KEY)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    if (list.length() != 0){
                        Log.d(TAG, result)
                        for (i in 0 until list.length()) {
                            val user = list.getJSONObject(i)
                            val login = user.getString("login")
                            val avatr_url = user.getString("avatar_url")
                            val url_detail = user.getString("url")
                            val html_url = user.getString("html_url")
                            val userItems = SearchUser(login, avatr_url, url_detail, html_url)
                            listItems.add(userItems)
                        }

                        listUsers.postValue(listItems)
                    }
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

    fun getSearchUser(): LiveData<ArrayList<SearchUser>> {
        return listUsers
    }


}
