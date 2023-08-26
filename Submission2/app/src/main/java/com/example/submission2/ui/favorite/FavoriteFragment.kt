package com.example.submission2.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2.databinding.FragmentFavoriteBinding
import com.example.submission2.ui.ViewModelFactory
import com.example.submission2.ui.adapter.UserListAdapter

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var userAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initRecyclerView()
        getAllFavoriteObserve()
    }

    private fun getAllFavoriteObserve() {
        viewModel.getAllFavorite().observe(viewLifecycleOwner) { listUser ->
            userAdapter.setData(listUser)
        }
    }

    private fun initRecyclerView() {
        userAdapter = UserListAdapter { username ->
            navigateToDetail(username)
        }
        binding?.rvUserFavorite?.apply {
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

    private fun navigateToDetail(username: String) {
        val navigateToDetail = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment()
        navigateToDetail.username = username
        view?.findNavController()?.navigate(navigateToDetail)
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(requireActivity())
        val initViewModel: FavoriteViewModel by viewModels { factory }
        viewModel = initViewModel
    }
}