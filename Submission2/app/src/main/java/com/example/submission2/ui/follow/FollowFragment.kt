package com.example.submission2.ui.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2.data.Result
import com.example.submission2.databinding.FragmentFollowBinding
import com.example.submission2.ui.ViewModelFactory
import com.example.submission2.ui.adapter.UserListAdapter

class FollowFragment : Fragment() {

    private var indexTab: Int = 0
    private lateinit var username: String
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: FollowViewModel
    private lateinit var userAdapter: UserListAdapter
    private var callback: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            indexTab = requireArguments().getInt(ARG_SECTION_NUMBER, 0)
            username = requireArguments().getString(ARG_USERNAME).toString()
        }

        initViewModel()
        initRecyclerView()

        if (indexTab == FOLLOWERS_TAB) {
            getUserFollowersObserve()
        } else if (indexTab == FOLLOWING_TAB) {
            getUserFollowingObserve()
        }
    }

    private fun initRecyclerView() {
        userAdapter = UserListAdapter { username ->
            callback?.invoke(username)
        }
        binding?.rvUserFollow?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun getUserFollowingObserve() {
        viewModel.getUserFollowing(username).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        userAdapter.setData(result.data)
                    }

                    is Result.Error -> {}
                }
            }
        }
    }

    private fun getUserFollowersObserve() {
        viewModel.getUserFollowers(username).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        userAdapter.setData(result.data)
                    }

                    is Result.Error -> {}
                }
            }
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(requireActivity())
        val initViewModel: FollowViewModel by viewModels { factory }
        viewModel = initViewModel
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
        const val FOLLOWERS_TAB = 0
        const val FOLLOWING_TAB = 1

        @JvmStatic
        fun newInstance(
            position: Int, username: String, callback: (String) -> Unit
        ): FollowFragment {
            val fragment = FollowFragment()
            fragment.callback = callback
            return fragment.apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, position)
                    putString(ARG_USERNAME, username)
                }
            }
        }
    }
}