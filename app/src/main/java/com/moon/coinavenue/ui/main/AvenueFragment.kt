package com.moon.coinavenue.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.moon.coinavenue.R

class AvenueFragment : Fragment() {

    private lateinit var avenueViewModel: AvenueViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_avenue, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = AvenueViewModelFactory()
        avenueViewModel = ViewModelProvider(this, viewModelFactory).get(AvenueViewModel::class.java)

        avenueViewModel.getMargetCode()
        avenueViewModel.marketLiveData.observe(viewLifecycleOwner, Observer {
            for (market in it) {
                Log.i("MQ!", "market: $market")
            }
        })


    }
}