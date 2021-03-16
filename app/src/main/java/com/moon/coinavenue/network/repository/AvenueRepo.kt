package com.moon.coinavenue.network.repository

import com.moon.coinavenue.network.AvenueApiInterface
import com.moon.coinavenue.network.model.CandleUpbitData
import com.moon.coinavenue.network.model.MarketCode

class AvenueRepo(private val apiInterface: AvenueApiInterface) : BaseRepository() {

    suspend fun getMarketCode(): MutableList<MarketCode>? {
        return safeApiCall(
            call = { apiInterface.getMarketCode().await() },
            error = "Error Market Code"
        )?.toMutableList()
    }

    suspend fun getOneMinuteData(marketCode: String): MutableList<CandleUpbitData>? {
        return safeApiCall(
            call = { apiInterface.getOneMinuteData(market = marketCode, count = 200).await() },
            error = "Error oneminute data"
        )?.toMutableList()
    }

    suspend fun getFiveMinuteData(marketCode: String): MutableList<CandleUpbitData>? {
        return safeApiCall(
            call = { apiInterface.getHourData(market = marketCode, count = 200).await() },
            error = "Error fiveminute data"
        )?.toMutableList()
    }
}