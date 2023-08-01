package com.example.submission1.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission1.data.ApiConfig
import com.example.submission1.data.User
import com.example.submission1.ui.home.HomeViewModel
import com.example.submission1.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    lateinit var username: String

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _userFollowers = MutableLiveData<List<User>>()
    val userFollowers: LiveData<List<User>> = _userFollowers

    private val _userFollowing = MutableLiveData<List<User>>()
    val userFollowing: LiveData<List<User>> = _userFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFollowLoading = MutableLiveData<Boolean>()
    val isFollowLoading: LiveData<Boolean> = _isFollowLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    companion object{
        private const val TAG = "DetailViewModel"
    }

    fun getUserData() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    responseBody.let {
                        _user.value = it
                    }
                } else {
                    _errorMessage.value = Event(response.message().toString())
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}", t)
            }
        })
    }

    fun getUserFollowList(code: Int){
        _isFollowLoading.value = true
        val client = if (code == FollowFragment.FOLLOWERS_CODE) {
            ApiConfig.getApiService().getUserFollowers(username)
        } else {
            ApiConfig.getApiService().getUserFollowing(username)
        }
        client.enqueue(object : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isFollowLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    if (code == FollowFragment.FOLLOWERS_CODE) {
                        responseBody.let {
                            _userFollowers.value = it
                        }
                    } else {
                        responseBody.let {
                            _userFollowing.value = it
                        }
                    }
                } else {
                    _errorMessage.value = Event(response.message().toString())
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isFollowLoading.value = false
                _errorMessage.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}", t)
            }
        })
    }
}