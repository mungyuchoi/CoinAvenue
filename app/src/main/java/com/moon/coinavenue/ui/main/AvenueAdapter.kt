package com.moon.coinavenue.ui.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.moon.coinavenue.R
import com.moon.coinavenue.const.Const.Companion.MARKET_REFRESH
import com.moon.coinavenue.network.model.MarketCode
import java.util.ArrayList

class AvenueAdapter(val items: List<MarketCode>?) :
    RecyclerView.Adapter<AvenueAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvenueAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.market_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items == null) {
            return
        }
        items[position].run {
            holder.title.text = koreanName
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    fun setItems(list: List<MarketCode>) {
        (items as ArrayList).run {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    fun setContext(context: Context) {
        this.context = context
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var title: TextView = view.findViewById(R.id.title)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (items == null) {
                return
            }
            val intent = Intent(MARKET_REFRESH).apply {
                putExtra("market", items[adapterPosition].market)
                putExtra("name", items[adapterPosition].koreanName)
            }
            LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
        }
    }
}