package com.sano.gmailed.utils

import android.content.Intent

fun Intent?.getExtraStringMap() : Map<String, String>? {
    this ?: return null

    this.extras ?: return null

    val result = mutableMapOf<String, String>()

    for(key in this.extras.keySet()) {
        if(!this.extras.getString(key).isNullOrEmpty()) {
            result[key] = this.extras.getString(key)
        }
    }

    return result
}