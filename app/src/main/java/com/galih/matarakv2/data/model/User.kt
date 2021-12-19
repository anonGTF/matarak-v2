package com.galih.matarakv2.data.model

import android.os.Parcelable
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val userId: String,
                val name: String,
                val birthDate: String,
                val address: String,
                val gender: Boolean,
                val imageUrl: String) : Parcelable {

    companion object {
        fun DocumentSnapshot.toUser(): User? {
            return try {
                val name = getString("name")!!
                val birthDate = getString("birthDate")!!
                val address = getString("address")!!
                val gender = getBoolean("gender")!!
                val imageUrl = getString("imageUrl")!!
                User(id, name, birthDate, address, gender, imageUrl)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                FirebaseCrashlytics.getInstance().log("Error converting user profile")
                FirebaseCrashlytics.getInstance().setCustomKey("userId", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        fun getDefaultUser(name: String) = hashMapOf(
            "name" to name,
            "imageUrl" to DEFAULT_PROFILE_PICTURE,
            "birthDate" to NOT_FILLED_YET,
            "address" to NOT_FILLED_YET,
            "gender" to true
        )

        const val DEFAULT_PROFILE_PICTURE = "https://firebasestorage.googleapis.com/v0/b/matarak-v2.appspot.com/o/person_placeholder.png?alt=media&token=077106ea-d808-40aa-933e-6023258cfbc5"
        private const val NOT_FILLED_YET = "Not filled yet"
        private const val TAG = "User"
    }
}