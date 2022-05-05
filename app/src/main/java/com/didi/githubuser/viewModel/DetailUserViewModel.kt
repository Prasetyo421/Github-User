package com.didi.githubuser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didi.githubuser.activity.DetailUserActivity
import com.didi.githubuser.model.ResponseDetailUser
import com.didi.githubuser.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel: ViewModel() {
    companion object {
        private val TAG = DetailUserActivity::class.java.simpleName
    }

    private val _detailUser = MutableLiveData<ResponseDetailUser>()
    val detailUser: LiveData<ResponseDetailUser> = _detailUser
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setDetailUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<ResponseDetailUser>{
            override fun onResponse(
                call: Call<ResponseDetailUser>,
                response: Response<ResponseDetailUser>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null){
                    _detailUser.value = response.body()
                }else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
