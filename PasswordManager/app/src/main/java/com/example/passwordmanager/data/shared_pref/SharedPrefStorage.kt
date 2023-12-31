package com.example.passwordmanager.data.shared_pref

interface SharedPrefStorage {
    // is on boarding done
    fun getOnBoardingState(): Boolean

    fun updateOnBoardingState(state: Boolean)
}