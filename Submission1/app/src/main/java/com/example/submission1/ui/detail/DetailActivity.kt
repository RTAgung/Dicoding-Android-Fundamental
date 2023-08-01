package com.example.submission1.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.submission1.R
import com.example.submission1.data.User
import com.example.submission1.databinding.ActivityDetailBinding
import com.example.submission1.ui.adapter.SectionsPagerAdapter
import com.example.submission1.utils.Event
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    companion object{
        private const val EXTRA_USERNAME = "extra_username"

        fun detailActivityIntent(context: Context, username: String): Intent{
            val detailIntent = Intent(context, DetailActivity::class.java)
            detailIntent.putExtra(EXTRA_USERNAME, username)
            return detailIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra(EXTRA_USERNAME).let {username ->
            supportActionBar?.title = username
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.elevation = 0f
            if (username != null) {
                viewModel.username = username
            }
        }

        viewModel.user.observe(this){user ->
            setData(user)
        }

        viewModel.errorMessage.observe(this){
            showError(it)
        }

        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        viewModel.getUserData()
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
        }
    }

    private fun setTabLayout(user: User) {
        val tabTitles = arrayOf(
            getString(R.string.detail_followers, user.followers),
            getString(R.string.detail_following, user.following)
        )

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager){tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun setData(user: User) {
        Glide.with(this)
            .load(user.avatarUrl)
            .placeholder(R.drawable.baseline_account_circle_24)
            .error(R.drawable.baseline_account_circle_24)
            .into(binding.ivPhoto)
        binding.tvName.text = user.name
        binding.tvUsername.text = user.login
        setTabLayout(user)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> finish()
            R.id.menu_open -> {
                val url = "https://github.com/" + viewModel.username
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}