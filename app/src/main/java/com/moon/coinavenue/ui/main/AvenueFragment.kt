package com.moon.coinavenue.ui.main

import android.R.attr
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.moon.coinavenue.databinding.FragmentAvenueBinding
import com.moon.coinavenue.network.model.CandleUpbitData


class AvenueFragment : Fragment() {

    private lateinit var avenueViewModel: AvenueViewModel
    private val avenueAdapter = AvenueAdapter(arrayListOf())
    private var _binding: FragmentAvenueBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAvenueBinding.inflate(inflater, container, false)
        return binding.root
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
            drawChart(list, binding.fiveMinutesChart)
        })

        avenueViewModel.getFiveMinuteData("KRW-BTC")
        avenueViewModel.fiveMinuteMutableLiveData.observe(viewLifecycleOwner, { list ->
            Log.i("MQ!", "fiveMinute data list: $list")
            if (list == null) {
                return@observe
            }
//            drawChart(list, binding.fiveMinutesChart)
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