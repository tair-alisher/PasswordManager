package com.example.passwordmanager.data.shared_pref

import android.content.Context

private const val SHARED_PREF_NAME = "shared_pref_name"
private const val KEY_ONBOARDING_STATE = "onboarding_state"

class SharedPrefStorageImpl(context: Context) : SharedPrefStorage {

    private val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    override fun getOnBoardingState(): Boolean {
        return sharedPref.getBoolean(KEY_ONBOARDING_STATE, false)
    }

    override fun updateOnBoardingState(state: Boolean) {
        sharedPref.edit().putBoolean(KEY_ONBOARDING_STATE, state).apply()
    }
}