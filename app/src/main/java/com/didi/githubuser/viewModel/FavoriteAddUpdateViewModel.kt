package com.didi.githubuser.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.didi.githubuser.database.User
import com.didi.githubuser.repository.UserRepository

class FavoriteAddUpdateViewModel(application: Application) : ViewModel() {
    private val mUserRepository = UserRepository(application)

    fun getAllUser(): LiveData<List<User>> = mUserRepository.getAllUsers()

    fun insert(user: User){
        mUserRepository.insert(user)
    }

    fun deleteByUsername(username: String){
        mUserRepository.deleteByUsername(username)
    }
}