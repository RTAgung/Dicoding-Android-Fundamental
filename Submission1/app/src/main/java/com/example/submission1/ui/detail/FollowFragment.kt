package com.example.submission1.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1.data.User
import com.example.submission1.databinding.FragmentFollowBinding
import com.example.submission1.ui.adapter.UserListAdapter
import com.example.submission1.utils.Event
import com.google.android.material.snackbar.Snackbar

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by activityViewModels()

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val FOLLOWERS_CODE = 0
        const val FOLLOWING_CODE = 1

        @StringRes
        private val CODE = intArrayOf(
            FOLLOWERS_CODE,
            FOLLOWING_CODE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollow.layoutManager = layoutManager

        viewModel.isFollowLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            showError(it)
        }

        position?.let {
            if (CODE[it] == FOLLOWERS_CODE) {
                viewModel.userFollowers.observe(viewLifecycleOwner) { userList ->
                    setAdapter(userList)
                }
            } else {
                viewModel.userFollowing.observe(viewLifecycleOwner) { userList ->
                    setAdapter(userList)
                }
            }
            viewModel.getUserFollowList(CODE[it])
        }
    }

    private fun setAdapter(userList: List<User>) {
        binding.rvFollow.adapter = UserListAdapter(userList) {
            startActivity(DetailActivity.detailActivityIntent(requireActivity(), it))
        }
    }

    private fun showError(event: Event<String>) {
        event.getContentIfNotHandled()?.let { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}