package com.galih.matarakv2.data.firebase

import android.util.Log
import com.galih.matarakv2.data.model.Article.Companion.toArticle
import com.galih.matarakv2.data.model.Banner.Companion.toBanner
import com.galih.matarakv2.data.model.User
import com.galih.matarakv2.data.model.User.Companion.toUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseUtils {
    private const val TAG = "FirebaseUtils"
    private const val KEY_EMAIL = "email"
    private const val KEY_ID = "id"
    private const val KEY_USER_ID = "user id"
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    fun getCurrentUser() = auth.currentUser

    fun getUserId() = auth.currentUser?.uid ?: ""

    fun getUserEmail() = auth.currentUser?.email ?: ""

    fun isLoggedIn() = getCurrentUser() != null

    suspend fun login(email: String, password: String) = safeCallFirebase(
        firebaseCall = { auth.signInWithEmailAndPassword(email, password).await().user },
        customKey = KEY_EMAIL,
        customValue = email,
        customMessage = "Error signing in user"
    )

    suspend fun register(name: String, email: String, password: String) = safeCallFirebase(
        firebaseCall = {
            val data = User.getDefaultUser(name)
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            db.collection("users").document(user?.uid ?: "").set(data).await()
        },
        customKey = KEY_EMAIL,
        customValue = email,
        customMessage = "Error signing up user"
    )

    suspend fun logout() = safeCallFirebase(
        firebaseCall = { auth.signOut() }
    )

    suspend fun getProfile() = safeCallFirebase(
        firebaseCall = { db.collection("users").document(getUserId()).get().await().toUser() },
        customKey = KEY_USER_ID,
        customMessage = "Error getting user details"
    )

    suspend fun getArticles() = safeCallFirebase(
        firebaseCall = { db.collection("articles").get().await().mapNotNull { it.toArticle() } },
        customMessage = "Error getting article for this user"
    )

    suspend fun getBanners() = safeCallFirebase(
        firebaseCall = { db.collection("banners").get().await().mapNotNull { it.toBanner() } },
        customMessage = "Error getting banner for this user"
    )

    private suspend fun <T> safeCallFirebase(
        firebaseCall: suspend () -> T,
        customKey: String = KEY_ID,
        customValue: String = getUserId(),
        customMessage: String = "Error getting data"
    ): T? = try {
        firebaseCall.invoke()
    } catch (e: Exception) {
        Log.e(TAG, customMessage, e)
        FirebaseCrashlytics.getInstance().log(customMessage)
        FirebaseCrashlytics.getInstance().setCustomKey(customKey, customValue)
        FirebaseCrashlytics.getInstance().recordException(e)
        null
    }
}