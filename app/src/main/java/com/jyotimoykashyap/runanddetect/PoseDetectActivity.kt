package com.jyotimoykashyap.runanddetect

import android.animation.ObjectAnimator
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.net.toUri
import com.google.mlkit.vision.common.InputImage
import com.jyotimoykashyap.runanddetect.databinding.ActivityPoseDetectBinding
import kotlinx.coroutines.delay

class PoseDetectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPoseDetectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoseDetectBinding.inflate(layoutInflater)


        val extra = intent.extras

        Log.d("MyCamera" , extra?.getString("URI") + "   :  File path")

        val filePath = extra?.getString("URI")
        val angleText = extra?.getString("Info")

        binding.infoTextView.text = angleText

        val objectAnimator = ObjectAnimator.ofFloat(binding.infoCard, View.ALPHA, 1f)
        objectAnimator.apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            startDelay = 100
            start()
        }


        binding.imageView.setImageURI(filePath?.toUri())


        setContentView(binding.root)
    }

    fun getBitmap(contentResolver: ContentResolver, uri: Uri?): Bitmap{
        val input = uri?.let { contentResolver.openInputStream(it) }
        val bitmap = BitmapFactory.decodeStream(input)
        input?.close()
        return bitmap
    }

}