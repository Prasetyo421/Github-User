package com.didi.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val EXTRA_TABLE_NAME = "note_database"
        @Volatile
        private var INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): UserDatabase{
            if (INSTANCE == null){
                synchronized(UserDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserDatabase::class.java,
                        EXTRA_TABLE_NAME).build()
                }
            }
            return INSTANCE as UserDatabase
        }
    }
}