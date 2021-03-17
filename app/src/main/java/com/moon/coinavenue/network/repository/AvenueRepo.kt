package com.moon.coinavenue.network.repository

import com.moon.coinavenue.network.AvenueApiInterface
import com.moon.coinavenue.network.model.*
import java.time.Month

class AvenueRepo(private val apiInterface: AvenueApiInterface) : BaseRepository() {

    suspend fun getMarketCode(): MutableList<MarketCode>? {
        return safeApiCall(
            call = { apiInterface.getMarketCode().await() },
            error = "Error Market Code"
        )?.toMutableList()
    }

    suspend fun getOneMinuteData(marketCode: String): MutableList<CandleUpbitData>? {
        return safeApiCall(
            call = { apiInterface.getOneMinuteData(market = marketCode, count = 25).await() },
            error = "Error oneminute data"
        )?.toMutableList()
    }

    suspend fun getFiveMinuteData(marketCode: String): MutableList<CandleUpbitData>? {
        return safeApiCall(
            call = { apiInterface.getFiveMinuteData(market = marketCode, count = 25).await() },
            error = "Error fiveminute data"
        )?.toMutableList()
    }

    suspend fun getHalfHourData(marketCode: String): MutableList<CandleUpbitData>? {
        return safeApiCall(
            call = { apiInterface.getHalfHourData(market = marketCode, count = 25).await() },
            error = "Error half hour data"
        )?.toMutableList()
    }

    suspend fun getHourData(marketCode: String): MutableList<CandleUpbitData>? {
        return safeApiCall(
            call = { apiInterface.getHourData(market = marketCode, count = 25).await() },
            error = "Error hour data"
        )?.toMutableList()
    }

    suspend fun getDaysData(marketCode: String): MutableList<DayData>? {
        return safeApiCall(
            call = { apiInterface.getDaysData(market = marketCode, count = 25).await() },
            error = "Error hour data"
        )?.toMutableList()
    }

    suspend fun getWeeksData(marketCode: String): MutableList<WeekData>? {
        return safeApiCall(
            call = { apiInterface.getWeeksData(market = marketCode, count = 25).await() },
            error = "Error hour data"
        )?.toMutableList()
    }

    suspend fun getMonthsData(marketCode: String): MutableList<MonthData>? {
        return safeApiCall(
            call = { apiInterface.getMonthsData(market = marketCode, count = 25).await() },
            error = "Error hour data"
        )?.toMutableList()
    }
}