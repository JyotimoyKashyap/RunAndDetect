package com.jyotimoykashyap.runanddetect

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.R
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.jyotimoykashyap.runanddetect.databinding.ActivityCameraBinding
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


interface PoseDetectionListener{
    fun poseDetection(poseDetection: Task<Pose>) : Unit
}


class Camera : AppCompatActivity(){

    private lateinit var binding: ActivityCameraBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var graphicOverlay: GraphicOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //graphicOverlay = binding.graphicOverlay

        // set up listeners
        binding.run {

            // to take photos
            captureBtn.setOnClickListener{
                Log.d("MyCamera" , "Camera Running")
                takePhoto()
            }

            //output directory
            outputDirectory = getOutputDirectory()
            cameraExecutor = Executors.newSingleThreadExecutor()

            // start camera
            startCamera()
        }

    }

    companion object{
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, "RunAndDetect").apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir

    }

    // accurate pose detector
    // Accurate pose detector on static images, when depending on the pose-detection-accurate sdk
    val options = AccuratePoseDetectorOptions.Builder()
        .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
        .build()

    val poseDetector = PoseDetection.getClient(options)


    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // creating time stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )

        // creating output options object which contains file and metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // setting up image capture listener
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photograph captured"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

                    // run pose detect
                    val image : InputImage
                    try {
                        image = InputImage.fromFilePath(baseContext, Uri.fromFile(photoFile))

                        // pose detector to process
                        poseDetector.process(image)
                            .addOnSuccessListener {
                                processImage(it)
                            }
                            .addOnFailureListener{
                                Log.d("MyCamera" , "Image Processing failed")
                            }

                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("MyCamera" , "Photo capture failed %=${exception.message}" , exception)
                }

            }
        )
    }

    fun processImage(pose: Pose){
        try {
            // Get all PoseLandmarks. If no person was detected, the list will be empty
            val allPoseLandmarks = pose.getAllPoseLandmarks()

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

            val leftShoulderPoint = leftShoulder.position
            val leftX = leftShoulder.position.x
            val leftY = leftShoulder.position.y

            Log.d("MyCamera" , "Left Shoulder : x "  + leftX.toString() + "Left Shoulder : y "  + leftY.toString())
        }catch (e: Exception){
            Log.d("MyCamera" , "Error in processing")
        }
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.createSurfaceProvider())
                }

            // image capture builder
            imageCapture = ImageCapture.Builder().build()

            //image analyzer
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, PoseDetectionAnalyzer())
                }

            // set back camera as default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                // bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalyzer)
            }catch (exc: Exception){
                Log.e("MyCamera", "Binding failed",exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

}