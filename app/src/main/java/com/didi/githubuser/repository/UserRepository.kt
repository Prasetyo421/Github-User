package com.didi.githubuser.repository

import android.app.Application
import android.service.autofill.UserData
import androidx.lifecycle.LiveData
import com.didi.githubuser.database.User
import com.didi.githubuser.database.UserDao
import com.didi.githubuser.database.UserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserDatabase.getInstance(application)
        mUserDao = db.userDao()
    }

    fun getAllUsers(): LiveData<List<User>> = mUserDao.getAllUsers()

    fun insert(user: User){
        executorService.execute { mUserDao.insert(user) }
    }

    fun update(user: User){
        executorService.execute { mUserDao.update(user) }
    }

    fun delete(user: User){
        executorService.execute { mUserDao.delete(user) }
    }
}