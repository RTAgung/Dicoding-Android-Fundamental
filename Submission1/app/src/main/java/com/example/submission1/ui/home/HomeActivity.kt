package com.example.submission1.ui.home

import android.app.SearchManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1.R
import com.example.submission1.databinding.ActivityHomeBinding
import com.example.submission1.ui.adapter.UserListAdapter
import com.example.submission1.ui.detail.DetailActivity
import com.example.submission1.utils.Event
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User"

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        viewModel.userList.observe(this){userList ->
            binding.rvUser.adapter = UserListAdapter(userList) {
                startActivity(DetailActivity.detailActivityIntent(this@HomeActivity, it))
            }
        }

        viewModel.isLoading.observe(this){isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(this){
            showError(it)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getAllUser()
        }
    }

    private fun showError(event: Event<String>) {
        event.getContentIfNotHandled()?.let {message ->
            Snackbar.make(
                window.decorView.rootView,
                message,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)

        val searchManager = getSystemService<SearchManager>()
        val searchVew = menu.findItem(R.id.menu_search).actionView as SearchView

        searchVew.setSearchableInfo(searchManager?.getSearchableInfo(componentName))
        searchVew.queryHint = getString(R.string.search_hint)
        searchVew.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(text: String?): Boolean {
                text?.let { viewModel.getSearchUser(it) }
                searchVew.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh){
            viewModel.getAllUser()
        }
        return super.onOptionsItemSelected(item)
    }
}