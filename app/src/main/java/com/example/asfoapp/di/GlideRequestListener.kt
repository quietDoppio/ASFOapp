package com.example.asfoapp.di

import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.asfoapp.data.Constants

class GlideRequestListener : RequestListener<Drawable> {
    override fun onLoadFailed(
        e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
    ): Boolean {
        e?.logRootCauses(Constants.LOG_TAG)
        Log.e(Constants.LOG_TAG, "Load of image failed", e)
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        return false
    }
}