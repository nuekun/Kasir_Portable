package com.nuedevlop.kasirportable

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.nuedevlop.kasirportable.utils.QrCodeAnalyzer

class ScanActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    private lateinit var textureView: TextureView
    internal var animator: ObjectAnimator? = null
    lateinit var scannerLayout: View
    lateinit var scannerBar: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)


        textureView = findViewById(R.id.view_finder)

        //Scanner overlay
        scannerLayout = findViewById(R.id.scannerLayout)
        scannerBar = findViewById(R.id.scannerBar)


        /*
        animation frame scanner
        source http://kunmii.blogspot.com/2018/01/building-animated-qr-scanner-viewfinder.html
        */
        val vto = scannerLayout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                scannerLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val destination = scannerLayout.y + scannerLayout.height
                animator = ObjectAnimator.ofFloat(scannerBar, "translationY",
                        scannerLayout.y,
                        destination)

                animator!!.repeatMode = ValueAnimator.REVERSE
                animator!!.repeatCount = ValueAnimator.INFINITE
                animator!!.interpolator = AccelerateDecelerateInterpolator()
                animator!!.duration = 3000
                animator!!.start()

            }
        })

        // Request camera permissions
        if (isCameraPermissionGranted()) {
            textureView.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

    }

    private fun startCamera() {
        val previewConfig = PreviewConfig.Builder()
                // We want to show input from back camera of the device
                .setLensFacing(CameraX.LensFacing.BACK)

                .build()

        val preview = Preview(previewConfig)


        preview.setOnPreviewOutputUpdateListener { previewOutput ->
            textureView.surfaceTexture = previewOutput.surfaceTexture
        }

        textureView.setOnClickListener{

            if (preview.isTorchOn){
                preview.enableTorch(false)
            }else{
                preview.enableTorch(true)
            }
        }

        val imageAnalysisConfig = ImageAnalysisConfig.Builder()
                .build()
        val imageAnalysis = ImageAnalysis(imageAnalysisConfig)

        val qrCodeAnalyzer = QrCodeAnalyzer { barCodes ->


            barCodes.forEach {

                val txtPriview: TextView = findViewById(R.id.txtScanPriview)
                val btnOk: Button = findViewById(R.id.btnOk)
                txtPriview.text = it.rawValue
                txtPriview.visibility = View.VISIBLE
                btnOk.visibility = View.VISIBLE
                btnOk.setOnClickListener {
                    finish()
                }

                Log.d("ScanerActivity", "QR Code detected: ${it.rawValue}.")

            }

        }

        imageAnalysis.analyzer = qrCodeAnalyzer


        // We need to bind preview and imageAnalysis use cases
        CameraX.bindToLifecycle(this as LifecycleOwner, preview, imageAnalysis)
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission = ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                textureView.post { startCamera() }
            } else {
                Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


}
