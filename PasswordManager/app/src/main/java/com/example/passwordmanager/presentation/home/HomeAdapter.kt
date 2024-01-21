package com.example.passwordmanager.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.R
import com.example.passwordmanager.databinding.ItemPasswordBinding
import com.example.passwordmanager.domain.model.Password

interface PasswordClickListener {
    fun showDetails(password: Password)

    fun copyToClipboard(password: String)
}

class HomeAdapter(private val listener: PasswordClickListener) :
    ListAdapter<Password, HomeAdapter.ViewHolder>(DiffCallback()), View.OnClickListener {

    class ViewHolder(val binding: ItemPasswordBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPasswordBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.copyImageView.setOnClickListener(this)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            holder.itemView.tag = item
            copyImageView.tag = item

            socialName.text = item.title
            login.text = item.login
        }
    }

    override fun onClick(v: View) {
        val password = v.tag as Password

        when (v.id) {
            R.id.copyImageView -> {
                listener.copyToClipboard(password.password)
            }
            else -> {
                listener.showDetails(password)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Password>() {
        override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem == newItem
        }

    }

}