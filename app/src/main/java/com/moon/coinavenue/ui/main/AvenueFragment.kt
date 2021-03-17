package com.moon.coinavenue.ui.main

import android.R.attr
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
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
        val adLoader = AdLoader.Builder(requireContext(), "ca-app-pub-3940256099942544/2247696110")
//        val adLoader = AdLoader.Builder(this, "ca-app-pub-3578188838033823/8269642538")
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

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter(Const.BACK_PRESSED))
    }

    override fun onPause() {
        super.onPause()
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
        avenueViewModel.marketMutableLiveData.observe(viewLifecycleOwner, { list ->
            binding.recyclerview.adapter = AvenueAdapter(list)
        })

        avenueViewModel.getOneMinuteData("KRW-BTC")
        avenueViewModel.oneMinuteMutableLiveData.observe(viewLifecycleOwner, { list ->
            Log.i("MQ!", "oneMinute data list: $list")
            if (list == null) {
                return@observe
            }
            drawChart(list, binding.oneMinutesChart)
        })

        avenueViewModel.getFiveMinuteData("KRW-BTC")
        avenueViewModel.fiveMinuteMutableLiveData.observe(viewLifecycleOwner, { list ->
            Log.i("MQ!", "fiveMinute data list: $list")
            if (list == null) {
                return@observe
            }
            drawChart(list, binding.fiveMinutesChart)
        })

    }

    private fun drawChart(list: MutableList<CandleUpbitData>, chart: CandleStickChart) {
        val entries = ArrayList<CandleEntry>()
        for (stock in list) {
            entries.add(
                CandleEntry(
                    stock.timestamp.toFloat(),
                    stock.highPrice.toFloat(),
                    stock.lowPrice.toFloat(),
                    stock.openingPrice.toFloat(),
                    stock.tradePrice.toFloat()
                )
            )
        }

        val dataSet = CandleDataSet(entries, "").apply {
            setColor(Color.rgb(80, 80, 80));
            setShadowColor(Color.DKGRAY);
            setShadowWidth(0.7f);
            setDecreasingColor(Color.RED);
            setDecreasingPaintStyle(Paint.Style.FILL);
            setIncreasingColor(Color.rgb(122, 242, 84));
            setIncreasingPaintStyle(Paint.Style.STROKE);
            setNeutralColor(Color.BLUE);
            setValueTextColor(Color.RED);

//            color = Color.rgb(80, 80, 80);
//            shadowColor = Color.LTGRAY
//            shadowWidth = 0.8f
//            decreasingColor = Color.BLUE
//            decreasingPaintStyle = Paint.Style.FILL
//            increasingColor = Color.RED
//            increasingPaintStyle = Paint.Style.STROKE
//            neutralColor = Color.BLUE
//            setDrawValues(false)
//            setDrawIcons(false)

//            axisDependency = YAxis.AxisDependency.LEFT
//            shadowColor = Color.LTGRAY
//            shadowWidth = 1f
//            decreasingColor = Color.BLUE
//            decreasingPaintStyle = Paint.Style.STROKE
//            increasingColor = Color.RED
//            increasingPaintStyle = Paint.Style.STROKE
//            neutralColor = Color.DKGRAY
        }

        chart.axisLeft.run {
            setLabelCount(7, false)
            setDrawGridLines(false)
        }
        chart.axisRight.run {
            isEnabled = false
        }
        chart.xAxis.run {
            textColor = Color.TRANSPARENT
            granularity = 1f
            isGranularityEnabled = true
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setAvoidFirstLastClipping(true)
        }

        chart.legend.run {
            isEnabled = false
        }

        chart.run {
            data = CandleData(dataSet)
            isHighlightPerDragEnabled = true
            description.isEnabled = false
            setBackgroundColor(Color.WHITE)
            setPinchZoom(false)
            setMaxVisibleValueCount(60)
            setDrawGridBackground(false)
            setDrawBorders(true)
            setBorderColor(Color.LTGRAY)
            requestDisallowInterceptTouchEvent(false)
            invalidate()
        }
    }
}