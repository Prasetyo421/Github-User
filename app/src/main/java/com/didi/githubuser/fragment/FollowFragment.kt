package com.didi.githubuser.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.didi.githubuser.ViewModel.ListUsersViewModel
import com.didi.githubuser.activity.DetailUserActivity
import com.didi.githubuser.adapter.ListUsersAdapter
import com.didi.githubuser.databinding.FragmentFollowBinding
import com.didi.githubuser.model.ListUser

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding as FragmentFollowBinding

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        @JvmStatic
        fun newInstance(index: Int, username: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString("username", username)
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ListUsersAdapter()
        val listUsersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ListUsersViewModel::class.java)
        val username = arguments?.getString("username")

        listUsersViewModel.getListUser().observe(viewLifecycleOwner, { listUserItems ->
            showLoading(false)
            Log.d("test size", listUserItems.size.toString())
            val size = listUserItems.size
            if (size != 0){
                adapter.setData(listUserItems)
                showList(true)
            }else {
                Log.d("test", "show empty")
                showEmpty(true)
            }
        })
        adapter.notifyDataSetChanged()
        binding.rvListUser.layoutManager = LinearLayoutManager(view.context)
        binding.rvListUser.adapter = adapter
        adapter.setOnItemClickCallback(object : ListUsersAdapter.OnItemClickCallback{
            override fun onItemCLicked(data: ListUser) {
                val mMove = Intent(view.context, DetailUserActivity::class.java)
                mMove.putExtra("username", data.login)
                startActivity(mMove)
            }
        })
        adapter.setOnBtnGithubClickCallback(object : ListUsersAdapter.OnBtnGithubClickCallback{
            override fun onItemClicked(data: ListUser) {
                val url = data.html_url
                val mMove = Intent(ACTION_VIEW)
                mMove.data = Uri.parse(url)
                startActivity(mMove)
            }

        })
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        if (index == 0 ){
            showLoading(true)
            showEmpty(false)
            if (username != null) {
                listUsersViewModel.setListUser("followers", username)
            }
        }else{
            showLoading(true)
            showEmpty(false)
            if (username != null) {
                listUsersViewModel.setListUser("following", username)
            }
        }
    }

    private fun showList(state: Boolean){
        if (state){
            binding.rvListUser.visibility = View.VISIBLE
            binding.imgEmpty.visibility = View.GONE
            binding.shimmer.visibility = View.GONE
        }else {
            binding.rvListUser.visibility = View.GONE
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.shimmer.visibility = View.VISIBLE
            binding.imgEmpty.visibility = View.GONE
            binding.rvListUser.visibility = View.GONE
        }else {
            binding.shimmer.visibility = View.GONE
        }
    }

    private fun showEmpty(state: Boolean){
        if (state){
            binding.imgEmpty.visibility = View.VISIBLE
            binding.rvListUser.visibility = View.GONE
            binding.shimmer.visibility = View.GONE
        }else {
            binding.imgEmpty.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}