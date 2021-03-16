package com.moon.coinavenue.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.moon.coinavenue.R


class PlaceholderFragment : Fragment() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val number = arguments?.getInt(ARG_SECTION_NUMBER)

        // 웹뷰 시작

        // 웹뷰 시작
        val webView = root.findViewById(R.id.webView) as WebView
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



        return root
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