package com.moon.coinavenue.network.repository

import com.moon.coinavenue.network.AvenueApiInterface
import com.moon.coinavenue.network.model.CandleData
import com.moon.coinavenue.network.model.MarketCode

class AvenueRepo(private val apiInterface: AvenueApiInterface) : BaseRepository() {

    suspend fun getMarketCode(): MutableList<MarketCode>? {
        return safeApiCall(
            call = { apiInterface.getMarketCode().await() },
            error = "Error Market Code"
        )?.toMutableList()
    }

    suspend fun getOneMinuteData(marketCode: String): MutableList<CandleData>? {
        return safeApiCall(
            call = { apiInterface.getOneMinuteData(market = marketCode, count = 100).await() },
            error = "Error oneminute data"
        )?.toMutableList()
    }


}