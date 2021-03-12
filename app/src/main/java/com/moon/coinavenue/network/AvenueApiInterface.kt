package com.moon.coinavenue.network

import com.moon.coinavenue.network.model.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AvenueApiInterface {
    @GET("market/all?isDetails=false")
    fun getMarketCode(): Deferred<Response<List<MarketCode>>>

    @GET("candles/minutes/1")
    fun getOneMinuteData(
        @Query("market") market: String,
        @Query("count") count: Int
    ): Response<CandleData>

    @GET("candles/minutes/5")
    fun getFiveMinuteData(
        @Query("market") market: String,
        @Query("count") count: Int
    ): Response<CandleData>

    @GET("candles/minutes/10")
    fun getTenMinuteData(
        @Query("market") market: String,
        @Query("count") count: Int
    ): Response<CandleData>

    @GET("candles/minutes/60")
    fun getHourData(
        @Query("market") market: String,
        @Query("count") count: Int
    ): Response<CandleData>

    @GET("candles/days")
    fun getDaysData(
        @Query("market") market: String,
        @Query("count") count: Int
    ): Response<DayData>

    @GET("candles/weeks")
    fun getWeeksData(
        @Query("market") market: String,
        @Query("count") count: Int
    ): Response<WeekData>

    @GET("candles/months")
    fun getMonthsData(
        @Query("market") market: String,
        @Query("count") count: Int
    ): Response<MonthData>
}