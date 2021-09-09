package com.didi.githubuser.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.didi.githubuser.R
import com.didi.githubuser.databinding.ListUserBinding
import com.didi.githubuser.model.ListUser

class ListUsersAdapter: RecyclerView.Adapter<ListUsersAdapter.ListUsersViewHolder>() {
    private val mData = ArrayList<ListUser>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemCLicked(data: ListUser)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(item: ArrayList<ListUser>){
        mData.clear()
        mData.addAll(item)
        notifyDataSetChanged()
    }

    inner class ListUsersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListUserBinding.bind(itemView)
        fun bind(listUser: ListUser){
            with(itemView){
                binding.tvUsername.text = listUser.login
                val url = listUser.avatar_url
                val uri = Uri.parse(url)
                Glide.with(context)
                    .load(uri)
                    .into(binding.image)
                itemView.setOnClickListener{
                    onItemClickCallback?.onItemCLicked(listUser)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListUsersViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        return ListUsersViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ListUsersAdapter.ListUsersViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size
}