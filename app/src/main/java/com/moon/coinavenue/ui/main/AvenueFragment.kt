package com.moon.coinavenue.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.moon.coinavenue.databinding.FragmentAvenueBinding
import com.moon.coinavenue.network.model.MarketCode

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
        })


    }
}