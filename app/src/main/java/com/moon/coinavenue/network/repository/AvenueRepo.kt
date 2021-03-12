package com.moon.coinavenue.network.repository

import com.moon.coinavenue.network.AvenueApiInterface
import com.moon.coinavenue.network.model.MarketCode

class AvenueRepo(private val apiInterface: AvenueApiInterface) : BaseRepository() {

    suspend fun getMarketCode(): MutableList<MarketCode>? {
        return safeApiCall(
            call = { apiInterface.getMarketCode().await() },
            error = "Error Market Code"
        )?.toMutableList()
    }


}