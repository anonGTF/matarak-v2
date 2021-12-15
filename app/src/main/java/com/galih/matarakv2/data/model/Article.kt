package com.galih.matarakv2.data.model

import android.os.Parcelable
import android.util.Log
import com.galih.matarakv2.base.BaseModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    override val id: String,
    val title : String = "",
    val thumbnail : String = "",
    val content : String = "",
    val source: String = ""
): BaseModel(id), Parcelable {
    companion object {
        fun DocumentSnapshot.toArticle(): Article? {
            return try {
                val title = getString("title")!!
                val thumbnail = getString("thumbnail")!!
                val content = getString("content")!!
                val source = getString("source")!!
                Article(id, title, thumbnail, content, source)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting article", e)
                FirebaseCrashlytics.getInstance().log("Error converting article")
                FirebaseCrashlytics.getInstance().setCustomKey("articleId", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        private val TAG = "article"
    }
}
