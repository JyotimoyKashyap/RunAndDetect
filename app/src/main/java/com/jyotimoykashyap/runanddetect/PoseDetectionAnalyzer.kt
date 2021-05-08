package com.jyotimoykashyap.runanddetect

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import java.lang.Exception

class PoseDetectionAnalyzer(private val view: GraphicOverlay) : ImageAnalysis.Analyzer{

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()

        val poseDetector = PoseDetection.getClient(options)

        val mediaImage = imageProxy.image
        if(mediaImage != null){
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // processing the image
            val result = poseDetector.process(image)
                .addOnSuccessListener {
                    processImage(it)
                }
                .addOnFailureListener{
                    it.printStackTrace()
                }

            imageProxy.close()

        }
    }

    fun processImage(pose: Pose){
        try {
            // Get all PoseLandmarks. If no person was detected, the list will be empty
            val allPoseLandmarks = pose.allPoseLandmarks

            // Or get specific PoseLandmarks individually. These will all be null if no person
            // was detected
            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)
            val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
            val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
            val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
            val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
            val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
            val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
            val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
            val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
            val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
            val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)
            val nose = pose.getPoseLandmark(PoseLandmark.NOSE)
            val leftEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)
            val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
            val leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER)
            val rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER)
            val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)
            val rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER)
            val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
            val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
            val leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)
            val rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)


            val leftShoulderX = leftShoulder.position.x
            val leftShoulderY = leftShoulder.position.y

            Log.d("MyCamera" ,
                "Left Shoulder : x "  + leftShoulderX.toString() + "Left Shoulder : y "  + leftShoulderY.toString())

            val rightShoulderX = rightShoulder.position.x
            val rightShoulderY = rightShoulder.position.y

            Log.d("MyCamera" ,
                "Right Shoulder : x "  + rightShoulderX.toString() + "Right Shoulder : y "  + rightShoulderY.toString())

            val noseX = nose.position.x
            val noseY = nose.position.y

            Log.d("MyCamera" ,
                "Nose : x "  + rightShoulderX.toString() + "Nose : y "  + rightShoulderY.toString())

            // adding graphic overlay
//            view.clear()
//            allPoseLandmarks.forEach{
//                val poseGraphic = PoseGraphic(view,
//                    pose,
//                    true,
//                    true,
//                    true)
//
//                view.add(poseGraphic)
//            }
//
//            view.postInvalidate()







        }catch (e: Exception){
            Log.d("MyCamera" , "Error in processing")
        }
    }


}