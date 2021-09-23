package com.didi.githubuser.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.didi.githubuser.MainActivity.Companion.USERNAME
import com.didi.githubuser.activity.DetailUserActivity
import com.didi.githubuser.database.User
import com.didi.githubuser.databinding.ListUserBinding
import com.didi.githubuser.helper.UserDiffCallback

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listUsers = ArrayList<User>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListUser(listUsers: List<User>){
//        val diffCallback = UserDiffCallback(this.listUsers, listUsers)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUsers.clear()
        this.listUsers.addAll(listUsers)
//        Log.d("test", "diffCallback: $diffCallback")
//        Log.d("test", "listUsers: ${listUsers.size}")
//        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.FavoriteViewHolder {
        Log.d("test", "onCreateViewHolder()")
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        Log.d("test", "onBindViewHolder()")
        holder.bind(listUsers[position])
    }

    override fun getItemCount(): Int = listUsers.size

    inner class FavoriteViewHolder(private val binding: ListUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val url = listUsers
        fun bind(user: User){
            with(binding){
                Log.d("test", "username: ${user.login}")
                image.loadImage(user.avatarUrl as String)
                tvUsername.text = user.login
                tvUrlHtml.text = user.htmlUrl
            }
            itemView.setOnClickListener {
                val move = Intent(it.context, DetailUserActivity::class.java)
                move.putExtra(USERNAME, user.login)
                it.context.startActivity(move)
            }
        }
    }

//    fun addItem(user: User){
//        this.listUsers.add(user)
//        notifyItemInserted(this.listUsers.size - 1)
//    }
//
//    fun removeItem(position: Int){
//        this.listUsers.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, this.listUsers.size)
//    }

    fun ImageView.loadImage(url: String){
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(100, 100))
            .into(this)
    }
}