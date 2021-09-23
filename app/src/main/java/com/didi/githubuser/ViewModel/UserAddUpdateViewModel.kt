package com.didi.githubuser.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.didi.githubuser.database.User
import com.didi.githubuser.repository.UserRepository

class UserAddUpdateViewModel(application: Application) : ViewModel() {
    private val mUserRepository = UserRepository(application)

    fun insert(user: User){
        mUserRepository.insert(user)
    }

    fun update(user: User){
        mUserRepository.update(user)
    }

    fun delete(user: User){
        mUserRepository.delete(user)
    }
}