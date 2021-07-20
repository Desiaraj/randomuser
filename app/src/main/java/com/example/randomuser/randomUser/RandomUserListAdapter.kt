package com.example.randomuser.randomUser

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randomuser.databinding.UserdetaillayoutBinding
import com.example.randomuser.network.RandomUserDataClass

/**
 * Created by desiaraj on 18/07/2021
 */

class RandomUserListAdapter(
    val context: Context,
    private var userslist: List<RandomUserDataClass>,
    val view: RandomUserDataContract.View
) :
    ListAdapter<RandomUserDataClass, RandomUserListAdapter.UserDataViewHolder>(DiffCallback) {


    class UserDataViewHolder(private val binding: UserdetaillayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): UserDataViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val userdatalistbinding =
                    UserdetaillayoutBinding.inflate(layoutInflater, parent, false)
                return UserDataViewHolder(userdatalistbinding)
            }
        }

        fun bind(
            context: Context,
            userdetail: RandomUserDataClass,
            view: RandomUserDataContract.View
        ) {
            binding.tvUsername.text = userdetail.name?.first.plus(" ").plus(userdetail.name?.last)
            Glide.with(context)
                .load(userdetail.picture?.medium)
                .into(binding.imgUserphoto)
            binding.llUserdata.setOnClickListener {
                view.userClicked(userdetail)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<RandomUserDataClass>() {
        override fun areItemsTheSame(
            oldItem: RandomUserDataClass,
            newItem: RandomUserDataClass
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: RandomUserDataClass,
            newItem: RandomUserDataClass
        ): Boolean {
            return oldItem.id?.value == newItem.id?.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDataViewHolder {
        return UserDataViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: UserDataViewHolder, position: Int) {
        holder.bind(context, userslist[position], view)
    }
}