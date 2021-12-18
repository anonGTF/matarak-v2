package com.galih.matarakv2.ui.detection

import android.view.LayoutInflater
import android.view.ViewGroup
import com.galih.matarakv2.R
import com.galih.matarakv2.base.BaseAdapter
import com.galih.matarakv2.data.model.DetectionResult
import com.galih.matarakv2.databinding.ItemHistoryBinding
import com.squareup.picasso.Picasso

class HistoryAdapter: BaseAdapter<ItemHistoryBinding, DetectionResult>() {
    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemHistoryBinding
        = ItemHistoryBinding::inflate

    override fun bind(binding: ItemHistoryBinding, item: DetectionResult) {
        with(binding) {
            tvTitle.text = item.title
            tvTime.text = item.time
            tvConfidence.text = binding.root.context.getString(R.string.decimal_formatter, item.confidence)

            Picasso.get().load(item.imageUrl).into(imgHistory)
        }
    }
}