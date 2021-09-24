package com.didi.githubuser.ViewModel

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

//    fun setDetailUser(username: String){
//        val url = "https://api.github.com/users/$username"
//        val client = AsyncHttpClient()
//        client.addHeader("Authorization", GITHUB_API_KEY)
//        client.addHeader("User-Agent", "request")
//        client.get(url, object : AsyncHttpResponseHandler(){
//            override fun onSuccess(
//                statusCode: Int,
//                headers: Array<out Header>?,
//                responseBody: ByteArray
//            ) {
//                try {
//                    val result = String(responseBody)
//                    val responseObject = JSONObject(result)
//                    val login = responseObject.getString("login")
//                    val avatar_url = responseObject.getString("avatar_url")
//                    val bio = responseObject.getString("bio")
//                    val name = responseObject.getString("name")
//                    val location = responseObject.getString("location")
//                    val follower = responseObject.getInt("followers")
//                    val following = responseObject.getInt("following")
//                    val repository = responseObject.getInt("public_repos")
//                    val githubUrl = responseObject.getString("html_url")
//                    val company = responseObject.getString("company")
//                    val email = responseObject.getString("email")
//
//                    val user = ResponseDetailUser(login, name, avatar_url, bio, follower, following, repository, location, githubUrl, company, email)
//
////                    val user = DetailUser(login, name, avatar_url, bio, follower, following, repository, location, githubUrl, company, email)
//                    user.location?.let { Log.d(TAG, it) }
//
//                    detailUser.postValue(user)
//
//                }catch (e: Exception){
//                    Log.d(TAG, e.message.toString())
//                }
//            }
//
//            override fun onFailure(
//                statusCode: Int,
//                headers: Array<out Header>?,
//                responseBody: ByteArray?,
//                error: Throwable?
//            ) {
//                Log.d(TAG, error?.message.toString())
//            }
//        })
//    }

}
