package com.didi.githubuser.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.didi.githubuser.database.User
import com.didi.githubuser.repository.UserRepository

class UserViewModel(application: Application) : ViewModel() {
    private val mUserRepository = UserRepository(application)

    fun getAllUsers(): LiveData<List<User>> = mUserRepository.getAllUsers()
}