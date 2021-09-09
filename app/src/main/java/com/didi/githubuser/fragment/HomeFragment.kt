package com.didi.githubuser.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.didi.githubuser.R
import com.didi.githubuser.ViewModel.ListUsersViewModel
import com.didi.githubuser.activity.DetailUserActivity
import com.didi.githubuser.adapter.ListUsersAdapter
import com.didi.githubuser.databinding.FragmentHomeBinding
import com.didi.githubuser.model.ListUser

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        @JvmStatic
        fun newInstance(index: Int, username: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString("username", username)
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ListUsersAdapter()
        val listUsersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ListUsersViewModel::class.java)
        val username = arguments?.getString("username")

        listUsersViewModel.getListUser().observe(viewLifecycleOwner, { listUserItems ->
            if (listUserItems != null ){
                adapter.setData(listUserItems)
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
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        if (index == 0 ){
            if (username != null) {
                listUsersViewModel.setListUser("followers", username)
            }
            binding.sectionLabel.text = getString(R.string.folllowers)

        }else{
            if (username != null) {
                listUsersViewModel.setListUser("following", username)
            }
            binding.sectionLabel.text = getString(R.string.following)
        }

    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressbar.visibility = View.VISIBLE
        }else {
            binding.progressbar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}