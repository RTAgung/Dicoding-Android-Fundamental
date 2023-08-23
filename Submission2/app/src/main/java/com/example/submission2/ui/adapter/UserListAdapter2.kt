package com.example.submission2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission2.R
import com.example.submission2.data.model.User
import com.example.submission2.databinding.UserItemBinding

class UserListAdapter2 : RecyclerView.Adapter<UserListAdapter2.MyViewHolder>() {
    private var userListData = ArrayList<User>()

    fun setData(userList: List<User>) {
        userListData.clear()
        userListData.addAll(userList)
        notifyDataSetChanged()
    }

    class MyViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvUsername.text = user.login

            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.baseline_account_circle_gray_24)
            requestOptions.error(R.drawable.baseline_account_circle_gray_24)
            Glide.with(itemView.context)
                .setDefaultRequestOptions(requestOptions)
                .load(user.avatarUrl)
                .into(binding.civAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = userListData.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(userListData[position])
    }
}