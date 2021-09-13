package com.didi.githubuser.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didi.githubuser.BuildConfig.GITHUB_API_KEY
import com.didi.githubuser.model.ListUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import kotlin.collections.ArrayList

class SearchUserViewModel: ViewModel() {
    companion object {
        private val TAG = SearchUserViewModel::class.java.simpleName
    }

    val listUsers = MutableLiveData<ArrayList<ListUser>?>()

    fun setSearchUser(username: String) {
        val listItems = ArrayList<ListUser>()
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

                    Log.d(TAG + " result", result)

                    if (list.length() != 0){
                        for (i in 0 until list.length()) {
                            val user = list.getJSONObject(i)
                            val login = user.getString("login")
                            val avatr_url = user.getString("avatar_url")
                            val url_detail = user.getString("url")
                            val html_url = user.getString("html_url")
                            val userItems = ListUser(login, avatr_url, url_detail, html_url)
                            listItems.add(userItems)
                        }
                    }

                    listUsers.postValue(listItems)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d(TAG + " error", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(TAG + " failure", error?.message.toString())
                listUsers.postValue(listItems)
            }

        })
    }

    fun getSearchUser(): MutableLiveData<ArrayList<ListUser>?> {
        return listUsers
    }


}
