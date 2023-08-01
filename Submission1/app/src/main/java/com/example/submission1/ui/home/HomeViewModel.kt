package com.example.submission1.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission1.data.ApiConfig
import com.example.submission1.data.SearchUser
import com.example.submission1.data.User
import com.example.submission1.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    init {
        getAllUser()
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

    fun getAllUser() {
        _isLoading.value = true
        val random = (1..1000).random()
        val client = ApiConfig.getApiService().getAllUser(random)
        client.enqueue(object : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _isLoading.value = false
                    responseBody.let {
                        _userList.value = it
                    }
                } else {
                    _isLoading.value = false
                    _errorMessage.value = Event(response.message().toString())
                    Log.e(TAG, "onResponse: ${response.message()}, $responseBody")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}", t)
            }
        })
    }

    fun getSearchUser(query: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(query)
        client.enqueue(object : Callback<SearchUser>{
            override fun onResponse(call: Call<SearchUser>, response: Response<SearchUser>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    _isLoading.value = false
                    val userList = responseBody.items
                    _userList.value = userList
                } else {
                    _isLoading.value = false
                    _errorMessage.value = Event(response.message().toString())
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchUser>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}", t)
            }
        })
    }
}