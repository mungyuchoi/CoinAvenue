package com.moon.coinavenue.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moon.coinavenue.network.AvenueService
import com.moon.coinavenue.network.model.MarketCode
import com.moon.coinavenue.network.repository.AvenueRepo
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AvenueViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val avenueRepository: AvenueRepo = AvenueRepo(AvenueService.avenueApi)

    val marketLiveData = MutableLiveData<MutableList<MarketCode>>()

    fun getMargetCode() {
        scope.launch {
            val marketCodes = avenueRepository.getMarketCode()
            marketLiveData.postValue(marketCodes)
        }
    }

    fun cancelRequests() = coroutineContext.cancel()
}