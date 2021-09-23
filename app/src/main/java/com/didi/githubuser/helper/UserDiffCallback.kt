package com.didi.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.didi.githubuser.database.User

class UserDiffCallback(private val mOldFavoriteList: List<User>, private val mNewfavoriteList: List<User>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavoriteList.size
    }

    override fun getNewListSize(): Int {
        return mNewfavoriteList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavoriteList[oldItemPosition].id == mNewfavoriteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFavoriteList[oldItemPosition]
        val newEmployee = mNewfavoriteList[newItemPosition]
        return oldEmployee.login == newEmployee.login &&
                oldEmployee.url == newEmployee.url &&
                oldEmployee.htmlUrl == newEmployee.htmlUrl &&
                oldEmployee.avatarUrl == newEmployee.avatarUrl
    }

}