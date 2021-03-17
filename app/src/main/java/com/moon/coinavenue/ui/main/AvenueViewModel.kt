package com.moon.coinavenue.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moon.coinavenue.network.AvenueService
import com.moon.coinavenue.network.model.*
import com.moon.coinavenue.network.repository.AvenueRepo
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AvenueViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val avenueRepository: AvenueRepo = AvenueRepo(AvenueService.avenueApi)

    val marketMutableLiveData = MutableLiveData<MutableList<MarketCode>>()
    val oneMinuteMutableLiveData = MutableLiveData<MutableList<CandleUpbitData>>()
    val fiveMinuteMutableLiveData = MutableLiveData<MutableList<CandleUpbitData>>()
    val halfHourMutableLiveData = MutableLiveData<MutableList<CandleUpbitData>>()
    val hourMutableLiveData = MutableLiveData<MutableList<CandleUpbitData>>()
    val daysMutableLiveData = MutableLiveData<MutableList<DayData>>()
    val weeksMutableLiveData = MutableLiveData<MutableList<WeekData>>()
    val monthsMutableLiveData = MutableLiveData<MutableList<MonthData>>()

    fun getMargetCode() {
        scope.launch {
            val marketCodes = avenueRepository.getMarketCode()
            marketMutableLiveData.postValue(
                marketCodes!!.sortedBy { it.koreanName }!!.distinctBy { it.koreanName }
                    .toMutableList()
            )

        }
    }

    fun getOneMinuteData(market: String) {
        scope.launch {
            val data = avenueRepository.getOneMinuteData(market)
            oneMinuteMutableLiveData.postValue(data)
        }
    }

    fun getFiveMinuteData(market: String) {
        scope.launch {
            val data = avenueRepository.getFiveMinuteData(market)
            fiveMinuteMutableLiveData.postValue(data)
        }
    }

    fun getHalfHourData(market: String) {
        scope.launch {
            val data = avenueRepository.getHalfHourData(market)
            halfHourMutableLiveData.postValue(data)
        }
    }

    fun getHourData(market: String) {
        scope.launch {
            val data = avenueRepository.getHourData(market)
            hourMutableLiveData.postValue(data)
        }
    }

    fun getDaysData(market: String) {
        scope.launch {
            val data = avenueRepository.getDaysData(market)
            daysMutableLiveData.postValue(data)
        }
    }
    fun getWeeksData(market: String) {
        scope.launch {
            val data = avenueRepository.getWeeksData(market)
            weeksMutableLiveData.postValue(data)
        }
    }

    fun getMonthsData(market: String) {
        scope.launch {
            val data = avenueRepository.getMonthsData(market)
            monthsMutableLiveData.postValue(data)
        }
    }


    fun cancelRequests() = coroutineContext.cancel()
}