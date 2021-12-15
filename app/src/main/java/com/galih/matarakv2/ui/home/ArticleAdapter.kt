package com.galih.matarakv2.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.galih.matarakv2.base.BaseAdapter
import com.galih.matarakv2.data.model.Article
import com.galih.matarakv2.databinding.ItemArticleBinding
import com.squareup.picasso.Picasso

class ArticleAdapter: BaseAdapter<ItemArticleBinding, Article>() {

    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemArticleBinding
        = ItemArticleBinding::inflate

    override fun bind(binding: ItemArticleBinding, item: Article) {
        with(binding) {
            tvTitle.text = item.title
            tvContent.text = item.content
            Picasso.get().load(item.thumbnail).into(ivThumbnail)
        }
    }
}