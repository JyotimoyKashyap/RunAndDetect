package com.jyotimoykashyap.runanddetect

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import com.google.mlkit.vision.common.InputImage
import com.jyotimoykashyap.runanddetect.databinding.ActivityPoseDetectBinding

class PoseDetectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPoseDetectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoseDetectBinding.inflate(layoutInflater)


        val extra = intent.extras

        Log.d("MyCamera" , extra?.getString("URI") + "   :  File path")

        val filePath = extra?.getString("URI")


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