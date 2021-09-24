package com.didi.githubuser.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didi.githubuser.fragment.FollowFragment.Companion.FOLLOWERS
import com.didi.githubuser.fragment.FollowFragment.Companion.FOLLOWING
import com.didi.githubuser.model.ResponseFollow
import com.didi.githubuser.model.ResponseItem
import com.didi.githubuser.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListUsersViewModel: ViewModel() {
    companion object {
        private val TAG = ListUsersViewModel::class.java.simpleName
    }

    private val _listUsers = MutableLiveData<List<ResponseItem>>()
    val listUsers: LiveData<List<ResponseItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setListUser(type: String, username: String) {
        if (type == FOLLOWERS){
            _isLoading.value = true
            val client = ApiConfig.getApiService().getFollowers(username)
            getFollow(client)

        }else if (type == FOLLOWING) {
            val client = ApiConfig.getApiService().getFollowing(username)
            getFollow(client)
        }
    }

    private fun getFollow(client: Call<List<ResponseItem>>){
        client.enqueue(object : Callback<List<ResponseItem>>{
            override fun onResponse(
                call: Call<List<ResponseItem>>,
                response: Response<List<ResponseItem>>
            ) {
                Log.d(TAG, "onResponse(): ${response.body()?.size} ")
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null){
                    _listUsers.value = response.body()
                }else {
                    Log.d(TAG, "onResponse() onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseItem>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

}