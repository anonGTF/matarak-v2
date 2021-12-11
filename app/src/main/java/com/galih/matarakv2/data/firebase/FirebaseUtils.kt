package com.galih.matarakv2.data.firebase

import android.util.Log
import com.galih.matarakv2.data.model.User
import com.galih.matarakv2.data.model.User.Companion.toUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseUtils {
    private const val TAG = "FirebaseUtils"
    private const val KEY_EMAIL = "email"
    private const val KEY_USER_ID = "user id"
    private val db by lazy { FirebaseFirestore.getInstance() }
    val auth by lazy { FirebaseAuth.getInstance() }

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

    suspend fun getProfile(userId: String) = safeCallFirebase(
        firebaseCall = { db.collection("users").document(userId).get().await().toUser() },
        customKey = KEY_USER_ID,
        customValue = userId,
        customMessage = "Error getting user details"
    )

    private suspend fun <T> safeCallFirebase(
        firebaseCall: suspend () -> T,
        customKey: String = "key",
        customValue: String = "value",
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