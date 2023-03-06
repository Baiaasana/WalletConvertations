package com.example.walletconvertation.presentation.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.backend.data.model.WalletModel
import com.example.walletconvertation.R
import com.example.walletconvertation.common.Utility
import com.example.walletconvertation.databinding.CustomWalletViewBinding

class WalletAdapter :
    ListAdapter<WalletModel, WalletAdapter.WalletViewHolder>(ItemCallback), Utility {

    var onWalletClickListener: ((WalletModel) -> Unit)? = null
    inner class WalletViewHolder(private val binding: CustomWalletViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            val item = getItem(adapterPosition)
            binding.apply {
                tvTitle.text = item.title.toString()
                tvAccountNumber.text = item.account_number.toString().plus("(${item.currency})")
                tvAmount.text = item.balance.toString()
                tvCurrency.text = setSymbol(item.currency.toString())
                ivEndIcon.setImageResource(R.drawable.ic_check)
                if (item.is_selected_from || item.is_selected_to) {
                    ivEndIcon.visibility = View.VISIBLE
                } else {
                    ivEndIcon.visibility = View.GONE
                }
                if(!item.enable){
                    itemView.isEnabled = false
                }
                itemView.setOnClickListener {
                    onWalletClickListener?.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder =
        WalletViewHolder(CustomWalletViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) = holder.bind()

    object ItemCallback : DiffUtil.ItemCallback<WalletModel>() {
        override fun areItemsTheSame(
            oldItem: WalletModel,
            newItem: WalletModel,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WalletModel,
            newItem: WalletModel,
        ): Boolean {
            return oldItem == newItem
        }
    }
}