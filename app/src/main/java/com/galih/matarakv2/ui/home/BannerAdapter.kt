package com.galih.matarakv2.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.galih.matarakv2.base.BaseAdapter
import com.galih.matarakv2.data.model.Banner
import com.galih.matarakv2.databinding.ItemBannerBinding
import com.squareup.picasso.Picasso

class BannerAdapter: BaseAdapter<ItemBannerBinding, Banner>() {
    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemBannerBinding
        = ItemBannerBinding::inflate

    override fun bind(binding: ItemBannerBinding, item: Banner) {
        Picasso.get().load(item.photo).into(binding.imgBanner)
    }

}