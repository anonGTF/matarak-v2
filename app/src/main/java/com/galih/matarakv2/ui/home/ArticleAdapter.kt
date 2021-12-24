package com.galih.matarakv2.ui.home

import android.os.Build
import android.text.Html
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
        val content = truncateString(item.content, 80)
        with(binding) {
            tvTitle.text = item.title
            tvContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
                            } else {
                                Html.fromHtml(content)
                            }
            Picasso.get().load(item.thumbnail).into(ivThumbnail)
        }
    }

    private fun truncateString(string: String?, maxSize: Int): String? {
        if (string != null) {
            if (string.length < maxSize) return string
            return string.take(maxSize) + "..."
        } else {
            return null
        }
    }
}