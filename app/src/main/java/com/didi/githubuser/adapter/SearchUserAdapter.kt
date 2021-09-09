package com.didi.githubuser.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.didi.githubuser.R
import com.didi.githubuser.model.SearchUser
import com.didi.githubuser.databinding.ListUserBinding

class SearchUserAdapter: RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {
    private val mData = ArrayList<SearchUser>()
    private var onItemClicCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClicCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: SearchUser)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(item: ArrayList<SearchUser>){
        mData.clear()
        mData.addAll(item)
        notifyDataSetChanged()
    }

    inner class SearchUserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListUserBinding.bind(itemView)
        fun bind(searchUser: SearchUser){
            with(itemView){
                binding.tvUsername.text = searchUser.login
//                binding.tvUrl.text = searchUser.url
//                binding.tvUrlHtml.text = searchUser.html_url

                val url = searchUser.avatar_url
                val uri = Uri.parse(url)
                Glide.with(context)
                    .load(uri)
                    .into(binding.image)
                itemView.setOnClickListener{
                    onItemClicCallback?.onItemClicked(searchUser)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        return SearchUserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size
}