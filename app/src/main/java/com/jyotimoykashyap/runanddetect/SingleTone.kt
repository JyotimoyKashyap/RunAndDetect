package com.jyotimoykashyap.runanddetect

import android.graphics.Bitmap


class SingleTone {
    private lateinit var myImage : Bitmap

    private var singleton: SingleTone? = null



    fun getMyImage(): Bitmap? {
        return myImage
    }

    fun setMyImage(myImage: Bitmap?) {
        this.myImage = myImage!!
    }

    fun getInstance(): SingleTone? {
        if (singleton == null) {
            singleton = SingleTone()
        }
        return singleton
    }
}