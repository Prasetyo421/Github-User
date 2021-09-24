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
import com.didi.githubuser.MainActivity.Companion.TEST
import com.didi.githubuser.MainActivity.Companion.USERNAME
import com.didi.githubuser.ViewModel.ListUsersViewModel
import com.didi.githubuser.activity.DetailUserActivity
import com.didi.githubuser.adapter.ListUsersAdapter
import com.didi.githubuser.databinding.FragmentFollowBinding
import com.didi.githubuser.model.ResponseItem

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding as FragmentFollowBinding

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        const val FOLLOWERS = "followers"
        const val FOLLOWING = "following"
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
        val username = arguments?.getString(USERNAME)

        listUsersViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            showLoading(isLoading)
        })

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        if (index == 0 ){
            listUsersViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
                showLoading(isLoading)
            })
            showLoading(true)
            showEmpty(false)
            if (username != null) {
                listUsersViewModel.setListUser(FOLLOWERS, username)
            }
        }else{
            showLoading(true)
            showEmpty(false)
            if (username != null) {
                listUsersViewModel.setListUser(FOLLOWING, username)
            }
        }

        listUsersViewModel.listUsers.observe(viewLifecycleOwner, { listFollow ->
            Log.d(TEST, "get list")
            Log.d("test size", listFollow.size.toString())
            val size = listFollow.size
            if (size != 0){
                adapter.setData(ArrayList(listFollow))
                showList(true)
            }else {
                Log.d("test", "show empty")
                showEmpty(true)
            }
        })

//        listUsersViewModel.getListUser().observe(viewLifecycleOwner, { listUserItems ->
//            showLoading(false)
//            Log.d("test size", listUserItems.size.toString())
//            val size = listUserItems.size
//            if (size != 0){
//                adapter.setData(listUserItems)
//                showList(true)
//            }else {
//                Log.d("test", "show empty")
//                showEmpty(true)
//            }
//        })
        binding.rvListUser.layoutManager = LinearLayoutManager(view.context)
        binding.rvListUser.adapter = adapter
        adapter.setOnItemClickCallback(object : ListUsersAdapter.OnItemClickCallback{
            override fun onItemCLicked(data: ResponseItem) {
                val move = Intent(view.context, DetailUserActivity::class.java)
                move.putExtra(USERNAME, data.login)
                startActivity(move)
            }
        })
        adapter.setOnBtnGithubClickCallback(object : ListUsersAdapter.OnBtnGithubClickCallback{

            override fun onItemClicked(data: ResponseItem) {
                val url = data.htmlUrl
                val move = Intent(ACTION_VIEW)
                move.data = Uri.parse(url)
                startActivity(move)
            }

        })
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