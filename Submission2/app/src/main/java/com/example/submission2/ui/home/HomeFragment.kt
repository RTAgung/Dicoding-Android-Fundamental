package com.example.submission2.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2.data.Result
import com.example.submission2.data.model.User
import com.example.submission2.databinding.FragmentHomeBinding
import com.example.submission2.ui.ViewModelFactory
import com.example.submission2.ui.adapter.UserListAdapter2
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: HomeViewModel
    private lateinit var userAdapter: UserListAdapter2
    private lateinit var userSearchAdapter: UserListAdapter2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initRecyclerView()
        initSearchRecyclerView()
        getAllUserObserve()
        setSearchView()

        binding?.swipeRefresh?.setOnRefreshListener {
            getAllUserObserve()
        }
    }

    private fun initSearchRecyclerView() {
        userSearchAdapter = UserListAdapter2()
        binding?.rvSearchUser?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userSearchAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setSearchView() {
        var timer: Timer? = null
        binding?.svUser?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s?.isNotBlank() == true) showSearchLoading(true)
                else showSearchLoading(false)
                timer?.cancel()
            }

            override fun afterTextChanged(s: Editable?) {
                timer = Timer()
                timer?.schedule(object : TimerTask() {
                    override fun run() {
                        requireActivity().runOnUiThread {
                            if (s?.isNotBlank() == true) getSearchUserObserve(s.toString())
                        }
                    }
                }, 600)
            }
        })
    }

    private fun getSearchUserObserve(query: String) {
        viewModel.getSearchUser(query).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showSearchLoading(true)
                        showSearchError(false)
                    }

                    is Result.Success -> {
                        showSearchLoading(false)
                        val data = result.data
                        if (data.isEmpty()) showSearchError(true, "no available data")
                        else showSearchError(false)
                        userSearchAdapter.setData(result.data)
                    }

                    is Result.Error -> {
                        showSearchLoading(false)
                        showSearchError(true, result.error)
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        userAdapter = UserListAdapter2()
        binding?.rvUser?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(requireActivity())
        val initViewModel: HomeViewModel by viewModels { factory }
        viewModel = initViewModel
    }

    private fun getAllUserObserve() {
        viewModel.getAllUser().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                        showError(false)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        val data = result.data
                        if (data.isEmpty()) showError(true, "no available data")
                        else showError(false)
                        userAdapter.setData(result.data)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showError(true, result.error)
                    }
                }
            }
        }
    }

    private fun showError(isError: Boolean, message: String = "") {
        if (isError) {
            binding?.tvNoData?.visibility = View.VISIBLE
            binding?.tvNoData?.text = message
        } else {
            binding?.tvNoData?.visibility = View.GONE
            binding?.tvNoData?.text = null
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.linearLoading?.visibility = View.VISIBLE
        } else {
            binding?.linearLoading?.visibility = View.GONE
            binding?.swipeRefresh?.isRefreshing = false
        }
    }

    private fun showSearchError(isError: Boolean, message: String = "") {
        if (isError) {
            binding?.tvSearchNoData?.visibility = View.VISIBLE
            binding?.tvSearchNoData?.text = message
        } else {
            binding?.tvSearchNoData?.visibility = View.GONE
            binding?.tvSearchNoData?.text = null
        }
    }

    private fun showSearchLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.searchLinearLoading?.visibility = View.VISIBLE
        } else {
            binding?.searchLinearLoading?.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}