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
import android.text.style.UnderlineSpan
import android.text.SpannableString
import com.didi.githubuser.model.ItemsItem


class SearchUserAdapter(): RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {
    private var mData = ArrayList<ItemsItem>()
    private var onItemClicCallback: OnItemClickCallback? = null
    private var onBtnGithubClickCallback: OnBtnGithubClickCallback? = null

    fun setOnBtnGithubClickCallback(onBtnGithubClickCallback: OnBtnGithubClickCallback) {
        this.onBtnGithubClickCallback = onBtnGithubClickCallback
    }

    interface OnBtnGithubClickCallback {
        fun onBtnGithubClickCallback(data: ItemsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClicCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(item: ArrayList<ItemsItem>){
        mData = item
        notifyDataSetChanged()
    }

    inner class SearchUserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListUserBinding.bind(itemView)
        fun bind(searchUser: ItemsItem){
            with(itemView){
                binding.tvUsername.text = searchUser.login
                val linkGithub = SpannableString(searchUser.htmlUrl)
                linkGithub.setSpan(UnderlineSpan(), 0, linkGithub.length, 0)
                binding.tvUrlHtml.text = linkGithub

                val url = searchUser.avatarUrl
                val uri = Uri.parse(url)
                Glide.with(context)
                    .load(uri)
                    .into(binding.image)
                itemView.setOnClickListener{
                    onItemClicCallback?.onItemClicked(searchUser)
                }
                binding.tvUrlHtml.setOnClickListener {
                    onBtnGithubClickCallback?.onBtnGithubClickCallback(searchUser)
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