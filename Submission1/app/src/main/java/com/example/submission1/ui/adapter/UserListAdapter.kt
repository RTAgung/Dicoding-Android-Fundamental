package com.example.submission1.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission1.R
import com.example.submission1.data.User
import com.example.submission1.databinding.UserItemListBinding

class UserListAdapter(private val userList: List<User>, private val callback: (String) -> Unit): RecyclerView.Adapter<UserListAdapter.HomeViewHolder>() {
    class HomeViewHolder(private val binding: UserItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User){
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .placeholder(R.drawable.baseline_account_circle_24)
                .error(R.drawable.baseline_account_circle_24)
                .into(binding.ivPhotoUser)

            binding.tvUsernameUser.text = user.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = UserItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(userList[position])
        holder.itemView.setOnClickListener{callback(userList[position].login)}
    }
}