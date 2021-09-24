package com.didi.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getAllUsers() : LiveData<List<User>>

    @Query("DELETE FROM user WHERE login = :username")
    fun deleteByUsername(username: String)
}
