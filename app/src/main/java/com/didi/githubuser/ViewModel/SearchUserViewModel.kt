package com.didi.githubuser.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didi.githubuser.model.ItemsItem
import com.didi.githubuser.model.ResponseSearchUser
import com.didi.githubuser.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserViewModel: ViewModel() {
    companion object {
        private val TAG = SearchUserViewModel::class.java.simpleName
    }

    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers: LiveData<List<ItemsItem>> = _listUsers
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchUsers(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(username)
        client.enqueue(object : Callback<ResponseSearchUser>{
            override fun onResponse(
                call: Call<ResponseSearchUser>,
                response: Response<ResponseSearchUser>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (responseBody != null){
                    _listUsers.value = responseBody.items
                }else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseSearchUser>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }
}
