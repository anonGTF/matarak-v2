package com.galih.matarakv2.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<VB: ViewBinding, Item: BaseModel>:
    RecyclerView.Adapter<BaseAdapter<VB, Item>.BaseViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldModel: Item, newModel: Item) =  oldModel == newModel
    }

    inner class BaseViewHolder(val binding: VB): RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, differCallback)
    protected var onRvItemClickListener: ((Item) -> Unit)? = null

    abstract val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB
    abstract fun bind(binding: VB, item: Item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemBinding = bindingInflater.invoke(LayoutInflater.from(parent.context), parent, false)
        return BaseViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = differ.currentList[position]
        bind(holder.binding, item)

        holder.itemView.setOnClickListener {
            onRvItemClickListener?.let { it(item) }
        }
    }

    override fun getItemCount() = differ.currentList.size

    fun setOnItemClickListener(listener: (Item) -> Unit) {
        onRvItemClickListener = listener
    }
}