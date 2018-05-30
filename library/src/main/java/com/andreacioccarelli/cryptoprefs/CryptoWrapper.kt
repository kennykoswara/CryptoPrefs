package com.andreacioccarelli.cryptoprefs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle

/**
 * Created by andrea on 2018/May.
 * Part of the package com.andreacioccarelli.cryptoprefs.sharedPreferences
 */

@SuppressLint("GetInstance")
internal class CryptoWrapper(context: Context, autoPrefs: Pair<String, String>) {

    private val crypto = PreferencesEncrypter(context, autoPrefs)

    val all: Map<String, *> = crypto.prefReader.all!!

    fun getAllPreferencesBundle(): Bundle {
        val result = Bundle()

        for (pref in crypto.prefReader.all) {
            result.putString(crypto.decrypt(pref.key), crypto.decrypt(pref.value.toString()))
        }

        return result
    }

    fun getAllPreferencesMap(): Map<String, String> {
        val result = HashMap<String, String>()

        for (pref in crypto.prefReader.all) {
            result[crypto.decrypt(pref.key)] = crypto.decrypt(pref.value.toString())
        }

        return result
    }

    fun getAllPreferencesList(): ArrayList<Pair<String, String>> {
        val result = ArrayList<Pair<String, String>>()

        for (pref in crypto.prefReader.all) {
            result.add(Pair(crypto.decrypt(pref.key), crypto.decrypt(pref.value.toString())))
        }

        return result
    }

    fun get(key: String, default: Any): String {
        val encryptedString = crypto.prefReader.getString(crypto.encrypt(key), crypto.encrypt(default.toString()))
        return crypto.decrypt(encryptedString)
    }


    fun put(key: String, value: Any) {
        crypto.prefWriter.putString(crypto.encrypt(key), crypto.encrypt(value.toString())).apply()
    }

    fun queue(key: String, value: Any) {
        val encryptedKey = crypto.encrypt(key)
        val encryptedValue = crypto.encrypt(value.toString())
        crypto.prefWriter.putString(encryptedKey, encryptedValue)
    }

    fun apply() {
        crypto.prefWriter.apply()
    }

    fun remove(key: String) {
        crypto.prefWriter.remove(key).apply()
    }

    fun erase() {
        crypto.prefWriter.clear().apply()
    }
}