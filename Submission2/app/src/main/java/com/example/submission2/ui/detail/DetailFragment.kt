package com.example.submission2.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission2.R
import com.example.submission2.data.Result
import com.example.submission2.data.model.User
import com.example.submission2.databinding.FragmentDetailBinding
import com.example.submission2.ui.ViewModelFactory
import com.example.submission2.ui.adapter.SectionPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DetailViewModel
    private var myMenu: Menu? = null

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_followers, R.string.tab_text_following
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initArguments()
        initAppbar()
        initAppbarMenu()
        initTabLayout()
        getDetailUserObserve()
        checkFavoriteObserve()
    }

    private fun checkFavoriteObserve() {
        viewModel.checkFavorite().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite != null) {
                viewModel.setFavorite(isFavorite)
            }
        }
        viewModel.getFavorite().observe(viewLifecycleOwner) { isFavorite ->
            setFavoriteState(isFavorite)
        }
    }

    private fun setFavoriteState(isFavorite: Boolean) {
        viewModel.isFavoriteState = isFavorite
        if (isFavorite) {
            binding?.ibDetailFavorite?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(), R.drawable.baseline_favorite_primary_24
                )
            )
            if (myMenu != null) {
                myMenu?.findItem(R.id.detail_menu_favorite)?.icon = ContextCompat.getDrawable(
                    requireActivity(), R.drawable.baseline_favorite_white_24
                )
            }
        } else {
            binding?.ibDetailFavorite?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(), R.drawable.baseline_favorite_border_primary_24
                )
            )
            if (myMenu != null) {
                myMenu?.findItem(R.id.detail_menu_favorite)?.icon = ContextCompat.getDrawable(
                    requireActivity(), R.drawable.baseline_favorite_border_white_24
                )
            }
        }
    }

    private fun getDetailUserObserve() {
        viewModel.getDetailUser().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        viewModel.data = result.data
                        setView(result.data)
                    }

                    is Result.Error -> showLoading(false)
                }
            }
        }
    }

    private fun setView(data: User) {
        binding?.tvDetailUsername?.text = data.login
        binding?.tvDetailRepo?.text = (data.repositories ?: 0).toString()
        binding?.tvDetailFollowers?.text = (data.followers ?: 0).toString()
        binding?.tvDetailFollowing?.text = (data.following ?: 0).toString()
        binding?.collapsingToolbar?.title = data.login

        if (data.name != null) {
            binding?.tvDetailName?.text = data.name
            binding?.tvDetailName?.visibility = View.VISIBLE
        }
        if (data.email != null) {
            binding?.tvDetailEmail?.text = data.email
            binding?.tvDetailEmail?.visibility = View.VISIBLE
        }
        if (data.bio != null) {
            binding?.tvDetailBio?.text = data.bio
            binding?.tvDetailBio?.visibility = View.VISIBLE
        }

        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.baseline_account_circle_gray_24)
        requestOptions.error(R.drawable.baseline_account_circle_gray_24)
        binding?.civDetailAvatar?.let {
            Glide.with(requireActivity()).setDefaultRequestOptions(requestOptions)
                .load(data.avatarUrl).into(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.linearLoading?.visibility = View.VISIBLE
        } else {
            binding?.linearLoading?.visibility = View.GONE
        }
    }

    private fun initArguments() {
        viewModel.username = DetailFragmentArgs.fromBundle(arguments as Bundle).username
    }

    private fun initTabLayout() {
        val sectionPagerAdapter =
            SectionPagerAdapter(activity as AppCompatActivity, viewModel.username) { username ->
                navigateToDetail(username)
            }
        val viewPager = binding?.viewPager as ViewPager2
        viewPager.adapter = sectionPagerAdapter
        val tabs = binding?.tabs as TabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun navigateToDetail(username: String) {
        val navigateToDetail = DetailFragmentDirections.actionNavigationDetailSelf()
        navigateToDetail.username = username
        view?.findNavController()?.navigate(navigateToDetail)
    }

    private fun initAppbarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                myMenu = menu
                menuInflater.inflate(R.menu.detail_menu, menu)
                showOption(false, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.detail_menu_favorite -> {
                        setFavorite()
                    }

                    android.R.id.home -> view?.findNavController()?.popBackStack()
                }
                return true
            }
        }, viewLifecycleOwner, androidx.lifecycle.Lifecycle.State.RESUMED)

        val appBarLayout = binding?.appbar
        appBarLayout?.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (myMenu != null) {
                    if (isShowToolbar(appBarLayout, verticalOffset)) {
                        binding?.toolbar?.visibility = View.VISIBLE
                    } else {
                        binding?.toolbar?.visibility = View.GONE
                    }
                    if (isShowOption(appBarLayout, verticalOffset)) {
                        showOption(true, myMenu!!)
                    } else {
                        showOption(false, myMenu!!)
                    }
                }
            }

            private fun isShowOption(layout: AppBarLayout, offset: Int): Boolean =
                layout.totalScrollRange + offset in 0..45

            private fun isShowToolbar(layout: AppBarLayout, offset: Int): Boolean =
                layout.totalScrollRange + offset < layout.totalScrollRange.toFloat() * 0.5
        })
    }

    private fun setFavorite() {
        if (viewModel.isFavoriteState) {
            viewModel.deleteFavorite()
            viewModel.setFavorite(false)
        } else {
            viewModel.insertFavorite()
            viewModel.setFavorite(true)
        }
    }

    private fun showOption(isShow: Boolean, myMenu: Menu) {
        val item: MenuItem = myMenu.findItem(R.id.detail_menu_favorite)
        item.isVisible = isShow
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(isShow)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(isShow)
    }

    private fun initAppbar() {
        val toolbar = binding?.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val collapsingToolbar = binding?.collapsingToolbar
        collapsingToolbar?.title = viewModel.username
        collapsingToolbar?.setCollapsedTitleTextColor(
            ContextCompat.getColor(requireActivity(), R.color.white)
        )
        collapsingToolbar?.setExpandedTitleColor(
            ContextCompat.getColor(requireActivity(), android.R.color.transparent)
        )

        binding?.ibDetailFavorite?.setOnClickListener {
            setFavorite()
        }

        binding?.ibDetailBack?.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(requireActivity())
        val initViewModel: DetailViewModel by viewModels { factory }
        viewModel = initViewModel
    }
}