package com.moyu.floatingtouch

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.webkit.WebView
import android.widget.ImageView

class FloatingService : Service() {
    private var windowManager: WindowManager? = null
    private var floatingButton: ImageView? = null
    private var menuView: WebView? = null
    private var isMenuOpen = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Tombol melayang
        floatingButton = ImageView(this).apply {
            setImageResource(android.R.drawable.ic_dialog_info)
            setOnClickListener { toggleMenu() }
        }

        val params = WindowManager.LayoutParams(
            150, 150,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 20
            y = 300
        }
        windowManager?.addView(floatingButton, params)
    }

    private fun toggleMenu() {
        if (isMenuOpen) {
            windowManager?.removeView(menuView)
            isMenuOpen = false
        } else {
            menuView = WebView(this).apply {
                settings.javaScriptEnabled = true
                loadUrl("file:///android_asset/menu.html")
            }
            val params = WindowManager.LayoutParams(
                850, 1100,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.END
                x = 20
                y = 500
            }
            windowManager?.addView(menuView, params)
            isMenuOpen = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        floatingButton?.let { windowManager?.removeView(it) }
        menuView?.let { windowManager?.removeView(it) }
    }
}
