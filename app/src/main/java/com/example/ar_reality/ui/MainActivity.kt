package com.example.ar_reality.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Choreographer
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceView
import com.example.ar_reality.databinding.ActivityMainBinding
import com.google.android.filament.*
import com.google.android.filament.utils.*
import java.nio.ByteBuffer


class MainActivity : Activity() {


    lateinit var binding: ActivityMainBinding
    private lateinit var choreographer: Choreographer
    private lateinit var modelViewer: List<ModelViewer>
    private lateinit var surfaceView: List<SurfaceView>
    var isAnimating = true
    private var clickedItemIndex = -1 // Initialize with an invalid index


    lateinit var gestureListener: GestureDetector

    val startTime = System.nanoTime()

    private val frameCallback = object : Choreographer.FrameCallback {
        // ... (existing code)

        override fun doFrame(currentTime: Long) {
            choreographer.postFrameCallback(this)


            val seconds = (currentTime - startTime).toDouble() / 1_000_000_000

            for (i in modelViewer.indices) {
                if (i == clickedItemIndex) {
                    modelViewer[i].animator?.apply {
                        if (animationCount > 0) {
                            val animationTime = (seconds % getAnimationDuration(0))
                            applyAnimation(6, animationTime.toFloat())
                        }
                        updateBoneMatrices()
                    }
                }
            }

            for (allrender in modelViewer) {
                allrender.render(currentTime)
            }
        }
    }




    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        surfaceView = listOf(
            binding.etanim1,
            binding.etanim2,
            binding.etanim3,
            binding.etanim4,
        )

        fullScreen(this)




        Utils.init()

        choreographer = Choreographer.getInstance()

        modelViewer = surfaceView.map { ModelViewer(it) }



        for (i in modelViewer.indices) {
            val viewer = modelViewer[i]
            val viewContent = AutomationEngine.ViewerContent()
            viewContent.view = viewer.view
            viewContent.sunlight = viewer.light
            viewContent.lightManager = viewer.engine.lightManager
            viewContent.scene = viewer.scene
            viewContent.renderer = viewer.renderer
        }


        for (i in surfaceView.indices) {
            surfaceView[i].setOnTouchListener { _, event ->
                modelViewer[i].onTouchEvent(event)
                gestureListener.onTouchEvent(event)
                true
            }
        }


        gestureListener = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                // Handle double-tap gestures here if needed
                return super.onDoubleTap(e)
            }
        })

//        createReandable()

        for (modelViewers in modelViewer) {
            val view = modelViewers.view
            view.renderQuality = view.renderQuality.apply {
                hdrColorBuffer = View.QualityLevel.HIGH
            }


            view.dynamicResolutionOptions = view.dynamicResolutionOptions.apply {
                enabled = true
                quality = View.QualityLevel.HIGH
            }
//        binding.etbg.setOnClickListener {
//             isAnimating = !isAnimating // Toggle animation when the button is clicked
//        }

            setUpClicks()

            view.multiSampleAntiAliasingOptions = view.multiSampleAntiAliasingOptions.apply {
                enabled = true

            }

            view.ambientOcclusionOptions = view.ambientOcclusionOptions.apply {
                enabled = true
            }

            view.antiAliasing = View.AntiAliasing.NONE
        }


        loadGlb()

        onItemClick(0)

    }

    fun setUpClicks() {
        val itemsbgs = listOf(
            binding.et1bg,
            binding.et2bg,
            binding.et3bg,
            binding.et4bg
        )


        for ((index, item) in itemsbgs.withIndex()) {
            item.setOnClickListener {
                when (index) {
                    0 -> {
                        onItemClick(index)
                    }

                    1 -> {
                        onItemClick(index)
                    }

                    2 -> {
                        onItemClick(index)
                    }

                    3 -> {
                        onItemClick(index)

                    }

                    else -> {

                    }
                }

            }
        }
    }

    fun onItemClick(position: Int) {
        // Set the clicked item index to start animation for that item
        clickedItemIndex = position
        when (position) {
            0 -> {
                createIndirectLights(position, 40_000f)
                createIndirectLights(1, 0_000f)
                createIndirectLights(2, 0_000f)
                createIndirectLights(3, 0_000f)
            }

            1 -> {
                createIndirectLights(position, 40_000f)
                createIndirectLights(0, 0_000f)
                createIndirectLights(2, 0_000f)
                createIndirectLights(3, 0_000f)

            }

            2 -> {
                createIndirectLights(position, 40_000f)
                createIndirectLights(0, 0_000f)
                createIndirectLights(1, 0_000f)
                createIndirectLights(3, 0_000f)

            }

            3 -> {
                createIndirectLights(position, 40_000f)
                createIndirectLights(0, 0_000f)
                createIndirectLights(2, 0_000f)
                createIndirectLights(1, 0_000f)

            }else ->{
            createIndirectLights(null, 10_000f)

        }


        }


    }


    fun createIndirectLights(postion: Int? = null, light: Float) {

        when(postion) {
            0 -> {
                val ibl = "venetian_crossroads_2k"

                var viewer = modelViewer
                val engine = viewer[postion!!].engine
                val scene = viewer[postion].scene

                readCompressAsset("envs/${ibl}/${ibl}_ibl.ktx").let {
                    scene.indirectLight = KTXLoader.createIndirectLight(engine, it)
                    scene.indirectLight!!.intensity = light
                }

                readCompressAsset("envs/${ibl}/${ibl}_skybox.ktx").let {
                    scene.skybox = KTXLoader.createSkybox(engine, it)
                }

            }

            1 -> {
                val ibl = "venetian_crossroads_2k"

                var viewer = modelViewer
                val engine = viewer[postion!!].engine
                val scene = viewer[postion].scene

                readCompressAsset("envs/${ibl}/${ibl}_ibl.ktx").let {
                    scene.indirectLight = KTXLoader.createIndirectLight(engine, it)
                    scene.indirectLight!!.intensity = light
                }

                readCompressAsset("envs/${ibl}/${ibl}_skybox.ktx").let {
                    scene.skybox = KTXLoader.createSkybox(engine, it)
                }

            }

            2 -> {
                val ibl = "venetian_crossroads_2k"

                var viewer = modelViewer
                val engine = viewer[postion!!].engine
                val scene = viewer[postion].scene

                readCompressAsset("envs/${ibl}/${ibl}_ibl.ktx").let {
                    scene.indirectLight = KTXLoader.createIndirectLight(engine, it)
                    scene.indirectLight!!.intensity = light
                }

                readCompressAsset("envs/${ibl}/${ibl}_skybox.ktx").let {
                    scene.skybox = KTXLoader.createSkybox(engine, it)
                }

            }

            3 -> {
                val ibl = "venetian_crossroads_2k"

                var viewer = modelViewer
                val engine = viewer[postion!!].engine
                val scene = viewer[postion].scene

                readCompressAsset("envs/${ibl}/${ibl}_ibl.ktx").let {
                    scene.indirectLight = KTXLoader.createIndirectLight(engine, it)
                    scene.indirectLight!!.intensity = light
                }

                readCompressAsset("envs/${ibl}/${ibl}_skybox.ktx").let {
                    scene.skybox = KTXLoader.createSkybox(engine, it)
                }

            }else ->{
            val ibl = "venetian_crossroads_2k"

            var viewer = modelViewer
            val engine = viewer[postion!!].engine
            val scene = viewer[postion].scene

            readCompressAsset("envs/${ibl}/${ibl}_ibl.ktx").let {
                scene.indirectLight = KTXLoader.createIndirectLight(engine, it)
                scene.indirectLight!!.intensity = light
            }

            readCompressAsset("envs/${ibl}/${ibl}_skybox.ktx").let {
                scene.skybox = KTXLoader.createSkybox(engine, it)
            }

        }
        }

    }

    private fun readCompressAsset(s: String): ByteBuffer {
        val input = assets.open(s)
        val bytes = ByteArray(input.available())
        input.read(bytes)
        return ByteBuffer.wrap(bytes)
    }




    private fun loadGlb() {
        val glbBuffer = readAsset("models/bob.glb")

        for (viewer in modelViewer) {
//        viewer.scene.indirectLight?.intensity = 4000.0f // Adjust the intensity as needed
            viewer.loadModelGlb(glbBuffer)
            viewer.transformToUnitCube()
        }
    }


    private fun readAsset(assetName: String): ByteBuffer {
        val input = assets.open(assetName)
        val bytes = ByteArray(input.available())
        input.read(bytes)
        return ByteBuffer.wrap(bytes)
    }


    override fun onResume() {
        super.onResume()
        choreographer.postFrameCallback(frameCallback)
    }

    override fun onPause() {
        super.onPause()
        choreographer.removeFrameCallback(frameCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        choreographer.removeFrameCallback(frameCallback)
    }
}