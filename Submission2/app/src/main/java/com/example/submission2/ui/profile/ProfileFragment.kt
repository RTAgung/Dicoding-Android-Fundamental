package com.example.submission2.ui.profile

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
import com.example.submission2.databinding.FragmentProfileBinding
import com.example.submission2.ui.ViewModelFactory
import com.example.submission2.ui.adapter.SectionPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ProfileViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_followers, R.string.tab_text_following
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initAppbar()
        initAppbarMenu()
        initTabLayout()
        getDetailUserObserve()
    }

    private fun getDetailUserObserve() {
        viewModel.getDetailUser().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        val data = result.data
                        setView(data)
                    }

                    is Result.Error -> showLoading(false)
                }
            }
        }
    }

    private fun setView(data: User) {
        binding?.tvProfileUsername?.text = data.login
        binding?.tvProfileRepo?.text = (data.repositories ?: 0).toString()
        binding?.tvProfileFollowers?.text = (data.followers ?: 0).toString()
        binding?.tvProfileFollowing?.text = (data.following ?: 0).toString()
        binding?.collapsingToolbar?.title = data.login

        if (data.name != null) {
            binding?.tvProfileName?.text = data.name
            binding?.tvProfileName?.visibility = View.VISIBLE
        }
        if (data.email != null) {
            binding?.tvProfileEmail?.text = data.email
            binding?.tvProfileEmail?.visibility = View.VISIBLE
        }
        if (data.bio != null) {
            binding?.tvProfileBio?.text = data.bio
            binding?.tvProfileBio?.visibility = View.VISIBLE
        }

        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.baseline_account_circle_gray_24)
        requestOptions.error(R.drawable.baseline_account_circle_gray_24)
        binding?.civProfileAvatar?.let {
            Glide.with(requireActivity()).setDefaultRequestOptions(requestOptions)
                .load(data.avatarUrl).into(it)
        }
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
        val navigateToDetail = ProfileFragmentDirections.actionProfileFragmentToDetailFragment()
        navigateToDetail.username = username
        view?.findNavController()?.navigate(navigateToDetail)
    }

    private fun initAppbarMenu() {
        var myMenu: Menu? = null

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                myMenu = menu
                menuInflater.inflate(R.menu.profile_menu, menu)
                showOption(false, menu, R.id.profile_menu_setting)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.profile_menu_setting -> {
                        navigateToSetting()
                    }
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
                        showOption(true, myMenu!!, R.id.profile_menu_setting)
                    } else {
                        showOption(false, myMenu!!, R.id.profile_menu_setting)
                    }
                }
            }

            private fun isShowOption(layout: AppBarLayout, offset: Int): Boolean =
                layout.totalScrollRange + offset in 0..45

            private fun isShowToolbar(layout: AppBarLayout, offset: Int): Boolean =
                layout.totalScrollRange + offset < layout.totalScrollRange.toFloat() * 0.5
        })
    }

    private fun showOption(isShow: Boolean, myMenu: Menu, id: Int) {
        val item: MenuItem = myMenu.findItem(id)
        item.isVisible = isShow
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

        binding?.ibProfileSetting?.setOnClickListener {
            navigateToSetting()
        }
    }

    private fun navigateToSetting() {
        val navigateToSetting = ProfileFragmentDirections.actionProfileFragmentToSettingFragment()
        view?.findNavController()?.navigate(navigateToSetting)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.linearLoading?.visibility = View.VISIBLE
        } else {
            binding?.linearLoading?.visibility = View.GONE
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(requireActivity())
        val initViewModel: ProfileViewModel by viewModels { factory }
        viewModel = initViewModel
    }
}