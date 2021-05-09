package com.jyotimoykashyap.runanddetect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jyotimoykashyap.runanddetect.databinding.ActivityPoseDetectBinding

class PoseDetectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPoseDetectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoseDetectBinding.inflate(layoutInflater)


        setContentView(binding.root)
    }
}