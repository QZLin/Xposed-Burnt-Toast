package com.mohammadag.burnttoast

import android.R
import de.robv.android.xposed.IXposedHookZygoteInit
import kotlin.Throws
import de.robv.android.xposed.IXposedHookZygoteInit.StartupParam
import de.robv.android.xposed.callbacks.XC_LayoutInflated
import de.robv.android.xposed.callbacks.XC_LayoutInflated.LayoutInflatedParam
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.content.pm.PackageManager
import android.content.res.XResources
import android.content.res.Resources.NotFoundException
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView

class XposedMod : IXposedHookZygoteInit {
    @Throws(Throwable::class)
    override fun initZygote(startupParam: StartupParam) {
        val hook: XC_LayoutInflated = object : XC_LayoutInflated() {
            @Throws(Throwable::class)
            override fun handleLayoutInflated(liparam: LayoutInflatedParam) {
                val layout = liparam.view as LinearLayout
                val context = layout.context
                val view = liparam.view.findViewById<View>(R.id.message) as TextView
                val params = LinearLayout.LayoutParams(
                    GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT
                )
                params.gravity = Gravity.CENTER
                view.layoutParams = params
                val pm = context.packageManager
                val imageView = ImageView(context)
                imageView.maxHeight = view.height + 128
                imageView.maxWidth = view.height + 128
                imageView.adjustViewBounds = true
                imageView.setImageDrawable(pm.getApplicationIcon(context.packageName))
                val params1 = LinearLayout.LayoutParams(
                    GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT
                )
                params1.gravity = Gravity.CENTER
                params1.rightMargin = 10
                imageView.layoutParams = params1
                layout.orientation = LinearLayout.HORIZONTAL
                layout.addView(imageView, 0)
            }
        }
        XResources.hookSystemWideLayout("android", "layout", "transient_notification", hook)
        try {
            XResources.hookSystemWideLayout("android", "layout", "tw_transient_notification", hook)
        } catch (_: NotFoundException) {
        } catch (_: Throwable) {
        }
    }
}