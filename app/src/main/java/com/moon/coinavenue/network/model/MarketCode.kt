package com.moon.coinavenue.network.model

import com.google.gson.annotations.SerializedName

data class MarketCode(
    val market: String,
    @SerializedName("korean_name")
    val koreanName: String,
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("market_warning")
    val marketWarning: String
)