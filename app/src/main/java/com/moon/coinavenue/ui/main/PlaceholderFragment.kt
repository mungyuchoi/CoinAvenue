package com.moon.coinavenue.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.moon.coinavenue.R
import com.moon.coinavenue.const.Const.Companion.BACK_PRESSED


class PlaceholderFragment : Fragment() {

    private lateinit var webView: WebView
    private var exitDialog: Dialog? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BACK_PRESSED && userVisibleHint) {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    exitDialog?.show()
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val number = arguments?.getInt(ARG_SECTION_NUMBER)

        // 웹뷰 시작
        webView = root.findViewById(R.id.webView) as WebView
        webView.run {
            webViewClient = WebViewClient()
            webView.settings.run {
                javaScriptEnabled = true // 웹페이지 자바스클비트 허용 여부
                setSupportMultipleWindows(false) // 새창 띄우기 허용 여부
                javaScriptCanOpenWindowsAutomatically = false // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
                loadWithOverviewMode = true // 메타태그 허용 여부
                useWideViewPort = true // 화면 사이즈 맞추기 허용 여부
                setSupportZoom(false) // 화면 줌 허용 여부
                builtInZoomControls = false // 화면 확대 축소 허용 여부
                layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN // 컨텐츠 사이즈 맞추기
                cacheMode = WebSettings.LOAD_DEFAULT // 브라우저 캐시 허용 여부
                domStorageEnabled = true // 로컬저장소 허용 여부
            }
        }
        when (number) {
            1 -> {
                webView.loadUrl("https://gall.dcinside.com/board/lists/?id=bitcoins")
            }
            2 -> {
                webView.loadUrl("https://cobak.co.kr/")
            }
            3 -> {
                webView.loadUrl("https://www.ddengle.com/")
            }
        }

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
        return root
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter(BACK_PRESSED))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}