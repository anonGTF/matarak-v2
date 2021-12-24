package com.galih.matarakv2.ui.home

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import com.galih.matarakv2.base.BaseActivity
import com.galih.matarakv2.data.model.Article
import com.galih.matarakv2.databinding.ActivityDetailArticleBinding
import com.squareup.picasso.Picasso

class DetailArticleActivity : BaseActivity<ActivityDetailArticleBinding>() {

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }

    override val bindingInflater: (LayoutInflater) -> ActivityDetailArticleBinding
        = ActivityDetailArticleBinding::inflate

    override fun setup() {
        setTitle("Detail Artikel")
        setupBackButton()
        setupBackButton()
        val bundle = intent.extras
        if (bundle != null) {
            val article = intent.getParcelableExtra<Article>(EXTRA_ARTICLE)
            populateData(article)
        }
    }

    private fun populateData(article: Article?) {
        with(binding) {
            tvTitle.text = article?.title
            tvContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(article?.content, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(article?.content)
            }
            tvSource.text = article?.source
            Picasso.get().load(article?.thumbnail).into(imgDetailThumbnail)
        }
    }
}