package com.jyotimoykashyap.runanddetect

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
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
                Log.d("MyCamera", "Camera Running")
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

    lateinit var resizedBitmap: Bitmap


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
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photograph captured"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

                    // run pose detect
                    val image: InputImage
                    try {
                        image = InputImage.fromFilePath(baseContext, Uri.fromFile(photoFile))

                        val inputStream = contentResolver.openInputStream(savedUri)
                        val galleryImage = BitmapFactory.decodeStream(inputStream)
                        // get the bitmap
                        Log.d("MyCamera", "Image created")
                        createBitmap(galleryImage)

                        // pose detector to process
                        poseDetector.process(image)
                            .addOnSuccessListener {
                                Log.d("MyCamera", "Image Processing")
                                processImage(it)
                            }
                            .addOnFailureListener {
                                Log.d("MyCamera", "Image Processing failed")
                            }

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Log.d("MyCamera", "Image Processing failed")
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("MyCamera", "Photo capture failed %=${exception.message}", exception)
                }

            }
        )
    }

    fun createBitmap(bitmap: Bitmap){
        val width = bitmap.width
        val height = bitmap.height
        val rotationDegrees =0

        resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)

    }

    fun processImage(pose: Pose){
        try {
            // Get all PoseLandmarks. If no person was detected, the list will be empty
            val allPoseLandmarks = pose.getAllPoseLandmarks()

            // Or get specific PoseLandmarks individually. These will all be null if no person
            // was detected

            // Shoulder
            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)

            // Elbow
            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)

            // Wrist
            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)

            // Hips
            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)

            // Knee
            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)

            // Ankle
            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

            // nose
            val nose = pose.getPoseLandmark(PoseLandmark.NOSE)

            val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
            val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)

            val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
            val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)

            val leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)
            val rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)


            // Shoulders left and right
            val leftShoulderPoint = leftShoulder.position
            val leftShoulderX = leftShoulderPoint.x
            val leftShoulderY = leftShoulderPoint.y
            val rightShoulderPoint = rightShoulder.position
            val rightShoulderX = rightShoulderPoint.x
            val rightShoulderY = rightShoulderPoint.y

            // Elbows left and right
            val leftElbowP = leftElbow.position
            val lElbowX = leftElbowP.x
            val lElbowY = leftElbowP.y
            val rightElbowP = rightElbow.position
            val rElbowX = rightElbowP.x
            val rElbowY = rightElbowP.y

            // Wrist
            val leftWristP = leftWrist.position
            val lWristX = leftWristP.x
            val lWristY = leftWristP.y
            val rightWristP = rightWrist.position
            val rWristX = rightWristP.x
            val rWristY = rightWristP.y

            // Hips
            val leftHipP = leftHip.position
            val lHipX = leftHipP.x
            val lHipY = leftHipP.y
            val rightHipP = rightHip.position
            val rHipX = rightHipP.x
            val rHipY = rightHipP.y

            // Knees
            val leftKneeP = leftKnee.position
            val lKneeX = leftKneeP.x
            val lKneeY = leftKneeP.y
            val rightKneeP = rightKnee.position
            val rKneeX = rightKneeP.x
            val rKneeY = rightKneeP.y


            // Ankles
            val leftAnkleP = leftAnkle.position
            val lAnkleX = leftAnkleP.x
            val lAnkleY = leftAnkleP.y
            val rightAnkleP = rightAnkle.position
            val rAnkleX = rightAnkleP.x
            val rAnkleY = rightAnkleP.y

            // nose
            val noseP = nose.position
            val noseX = noseP.x
            val noseY = noseP.y

            //Eye
            val leftEyeP = leftEye.position
            val leftEyeX = leftEyeP.x
            val leftEyeY = leftEyeP.y
            val rightEyeP = rightEye.position
            val rightEyeX = rightEyeP.x
            val rightEyeY = rightEyeP.y


            // Mouth
            val leftMouthP = leftMouth.position
            val leftMouthX = leftMouthP.x
            val leftMouthY = leftMouthP.y
            val rightMouthP = rightMouth.position
            val rightMouthX = rightMouthP.x
            val rightMouthY = rightMouthP.y

            // Ear
            val leftEarP = leftEar.position
            val leftEarX = leftEarP.x
            val leftEarY = leftEarP.y
            val rightEarP = rightEar.position
            val rightEarX = rightEarP.x
            val rightEarY = rightEarP.y


            Log.d("MyCamera" , "NoseX: " + noseX.toString() + "NoseY: " + noseY.toString())

            displayAll(leftShoulderX, leftShoulderY, rightShoulderX, rightShoulderY,
                        lElbowX, lElbowY, rElbowX, rElbowY, lWristX, lWristY, rWristX, rWristY,
                        lHipX, lHipY, rHipX, rHipY, lAnkleX, lAnkleY, rAnkleX, rAnkleY,
                        lKneeX, lKneeY, rKneeX, rKneeY)





        }catch (e: Exception){
            Log.d("MyCamera", "Error in processing")
        }
    }

    // draw pose
    fun displayAll(
        lShoulderX: Float, lShoulderY: Float, rShoulderX: Float, rShoulderY: Float,
        lElbowX: Float, lElbowY: Float, rElbowX: Float, rElbowY: Float,
        lWristX: Float, lWristY: Float, rWristX: Float, rWristY: Float,
        lHipX: Float, lHipY: Float, rHipX: Float, rHipY: Float,
        lAnkleX: Float, lAnkleY: Float, rAnkleX: Float, rAnkleY: Float,
        lKneeX: Float, lKneeY: Float, rKneeX: Float, rKneeY: Float,
    ){
        val paint = Paint()
        paint.color = Color.GREEN
        val strokeWidth = 4.0f
        paint.strokeWidth = strokeWidth

        val drawBitmap = Bitmap.createBitmap(
            resizedBitmap.width,
            resizedBitmap.height,
            resizedBitmap.config
        )

        val canvas = Canvas(drawBitmap)

        canvas.drawBitmap(resizedBitmap, 0f, 0f, null)

        canvas.drawLine(rShoulderX, rShoulderY, rElbowX, rElbowY, paint)
        canvas.drawLine(lShoulderX, lShoulderY, rShoulderX, rShoulderY, paint)
        canvas.drawLine(rElbowX, rElbowY, rWristX, rWristY, paint)
        canvas.drawLine(lShoulderX, lShoulderY, lElbowX, lElbowY, paint)
        canvas.drawLine(lElbowX, lElbowY, lWristX, lWristY, paint)
        canvas.drawLine(rShoulderX, rShoulderY, rHipX, rHipY, paint)
        canvas.drawLine(lShoulderX, lShoulderY, lHipX, lHipY, paint)
        canvas.drawLine(lHipX, lHipY, rHipX, rHipY, paint)
        canvas.drawLine(rHipX, rHipY, rKneeX, rKneeY, paint)
        canvas.drawLine(lHipX, lHipY, lKneeX, lKneeY, paint)
        canvas.drawLine(rKneeX, rKneeY, rAnkleX, rAnkleY, paint)
        canvas.drawLine(lKneeX, lKneeY, lAnkleX, lAnkleY, paint)

        Log.d("MyCamera" , "Above intent")

        val intent = Intent(this, PoseDetectActivity::class.java)
        startActivity(intent)

    }

    fun getAngle(
        firstPoint: PoseLandmark,
        midPoint: PoseLandmark,
        lastPoint: PoseLandmark
    ): Double {
        var result = Math.toDegrees(
            Math.atan2(
                (lastPoint.position.y - midPoint.position.y).toDouble(), (
                        lastPoint.position.x - midPoint.position.x).toDouble()
            )
                    - Math.atan2(
                (firstPoint.position.y - midPoint.position.y).toDouble(), (
                        firstPoint.position.x - midPoint.position.x).toDouble()
            )
        )
        result = Math.abs(result) // Angle should never be negative
        if (result > 180) {
            result = 360.0 - result // Always get the acute representation of the angle
        }
        return result
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
//            val imageAnalyzer = ImageAnalysis.Builder()
//                .build()
//                .also {
//                    it.setAnalyzer(cameraExecutor, PoseDetectionAnalyzer())
//                }

            // set back camera as default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()

                // bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e("MyCamera", "Binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

}