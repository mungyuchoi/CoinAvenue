package com.moon.coinavenue.ui.main

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.moon.coinavenue.R
import com.moon.coinavenue.const.Const
import com.moon.coinavenue.databinding.FragmentAvenueBinding
import com.moon.coinavenue.network.model.CandleUpbitData
import com.moon.coinavenue.network.model.DayData
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.model.ValueShape
import lecho.lib.hellocharts.view.LineChartView

class AvenueFragment : Fragment() {

    private lateinit var avenueViewModel: AvenueViewModel
    private val avenueAdapter = AvenueAdapter(arrayListOf())
    private var _binding: FragmentAvenueBinding? = null
    private val binding get() = _binding!!
    private var exitDialog: Dialog? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Const.BACK_PRESSED && userVisibleHint) {
                exitDialog?.show()
            }
            if (intent.action == Const.MARKET_REFRESH) {
                val market = intent.getStringExtra("market")
                val name = intent.getStringExtra("name")

                binding.market.text = name
                avenueViewModel.getOneMinuteData(market)
                avenueViewModel.getFiveMinuteData(market)
                avenueViewModel.getHalfHourData(market)
                avenueViewModel.getHourData(market)
                avenueViewModel.getDaysData(market)
                avenueViewModel.getWeeksData(market)
                avenueViewModel.getMonthsData(market)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAvenueBinding.inflate(inflater, container, false)
        exitDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.exit_dialog)
        }
        exitDialog?.findViewById<Button>(R.id.review)?.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${requireContext().packageName}")
                )
            )
        }
        exitDialog?.findViewById<Button>(R.id.exit)?.setOnClickListener {
            activity?.finish()
        }
        //Test
//        val adLoader = AdLoader.Builder(requireContext(), "ca-app-pub-3578188838033823/8269642538")
        val adLoader = AdLoader.Builder(requireContext(), "ca-app-pub-3578188838033823/8269642538")
            .forUnifiedNativeAd { ad: UnifiedNativeAd ->
                exitDialog?.findViewById<TemplateView>(R.id.template)?.setNativeAd(ad)
            }
            .withAdListener(object : AdListener() {
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val intentFilter = IntentFilter().apply {
            addAction(Const.MARKET_REFRESH)
            addAction(Const.BACK_PRESSED)
        }
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerview.run {
            val staggeredGridLayoutManager =
                StaggeredGridLayoutManager(
                    3, LinearLayoutManager.HORIZONTAL
                )
            layoutManager = staggeredGridLayoutManager
            adapter = avenueAdapter
        }


        val viewModelFactory = AvenueViewModelFactory()
        avenueViewModel = ViewModelProvider(this, viewModelFactory).get(AvenueViewModel::class.java)

        avenueViewModel.getMargetCode()
        avenueViewModel.getOneMinuteData("KRW-BTC")
        avenueViewModel.getFiveMinuteData("KRW-BTC")
        avenueViewModel.getHalfHourData("KRW-BTC")
        avenueViewModel.getHourData("KRW-BTC")
        avenueViewModel.getDaysData("KRW-BTC")
        avenueViewModel.getWeeksData("KRW-BTC")
        avenueViewModel.getMonthsData("KRW-BTC")

        avenueViewModel.marketMutableLiveData.observe(viewLifecycleOwner, { list ->
            binding.recyclerview.adapter = AvenueAdapter(list).apply {
                setContext(requireContext())
            }
        })

        avenueViewModel.oneMinuteMutableLiveData.observe(viewLifecycleOwner, { list ->
            if (list == null) {
                return@observe
            }
            val entries = ArrayList<PointValue>()
            for (stock in list) {
                entries.add(PointValue(stock.timestamp.toFloat(), stock.tradePrice.toFloat()))
            }
            drawChart(entries, binding.oneMinutesChart)
        })

        avenueViewModel.fiveMinuteMutableLiveData.observe(viewLifecycleOwner, { list ->
            if (list == null) {
                return@observe
            }
            val entries = ArrayList<PointValue>()
            for (stock in list) {
                entries.add(PointValue(stock.timestamp.toFloat(), stock.tradePrice.toFloat()))
            }
            drawChart(entries, binding.fiveMinutesChart)
        })

        avenueViewModel.halfHourMutableLiveData.observe(viewLifecycleOwner, { list ->
            if (list == null) {
                return@observe
            }
            val entries = ArrayList<PointValue>()
            for (stock in list) {
                entries.add(PointValue(stock.timestamp.toFloat(), stock.tradePrice.toFloat()))
            }
            drawChart(entries, binding.halfHourChart)
        })

        avenueViewModel.hourMutableLiveData.observe(viewLifecycleOwner, { list ->
            if (list == null) {
                return@observe
            }
            val entries = ArrayList<PointValue>()
            for (stock in list) {
                entries.add(PointValue(stock.timestamp.toFloat(), stock.tradePrice.toFloat()))
            }
            drawChart(entries, binding.hourChart)
        })

        avenueViewModel.daysMutableLiveData.observe(viewLifecycleOwner, { list ->
            if (list == null) {
                return@observe
            }
            val entries = ArrayList<PointValue>()
            for (stock in list) {
                entries.add(PointValue(stock.timestamp.toFloat(), stock.tradePrice.toFloat()))
            }
            drawChart(entries, binding.daysChart)
        })

        avenueViewModel.weeksMutableLiveData.observe(viewLifecycleOwner, { list ->
            if (list == null) {
                return@observe
            }
            val entries = ArrayList<PointValue>()
            for (stock in list) {
                entries.add(PointValue(stock.timestamp.toFloat(), stock.tradePrice.toFloat()))
            }
            drawChart(entries, binding.weeksChart)
        })

        avenueViewModel.monthsMutableLiveData.observe(viewLifecycleOwner, { list ->
            if (list == null) {
                return@observe
            }
            val entries = ArrayList<PointValue>()
            for (stock in list) {
                entries.add(PointValue(stock.timestamp.toFloat(), stock.tradePrice.toFloat()))
            }
            drawChart(entries, binding.monthsChart)
        })

    }

    private fun drawChart(entries: ArrayList<PointValue>, chart: LineChartView) {
        val line = Line(entries).apply {
            color = Color.DKGRAY
            isCubic = true
            shape = ValueShape.CIRCLE
        }

        val data = LineChartData().apply {
            lines = arrayListOf(line)
        }

        chart.run {
            lineChartData = data
            setBackgroundColor(Color.WHITE)
        }
    }
}