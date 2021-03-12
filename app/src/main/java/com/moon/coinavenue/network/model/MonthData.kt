package com.moon.coinavenue.network.model

import com.google.gson.annotations.SerializedName

data class MonthData(
    val market: String,
    @SerializedName("candle_date_time_utc")
    val candleDateTimeUtc: String,
    @SerializedName("candle_date_time_kst")
    val candleDateTimeKst: String,
    @SerializedName("opening_price")
    val openingPrice: Double,
    @SerializedName("high_price")
    val highPrice: Double,
    @SerializedName("low_price")
    val lowPrice: Double,
    @SerializedName("trade_price")
    val tradePrice: Double,
    val timestamp: Long,
    @SerializedName("candle_acc_trade_price")
    val candleAccTradePrice: Double,
    @SerializedName("candle_acc_trade_volume")
    val candleAccTradeVolume: Double,
    @SerializedName("first_day_of_period")
    val firstDayOfPeriod: String
)