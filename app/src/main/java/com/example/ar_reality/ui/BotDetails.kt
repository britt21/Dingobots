package com.example.ar_reality.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Choreographer
import android.view.GestureDetector
import android.view.SurfaceView
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ar_reality.databinding.DetailsActivityBinding
import com.example.ar_reality.helper.LoadingHome
import com.example.ar_reality.helper.NetworkHelper
import com.example.ar_reality.model.bot_response.BotResponse
import com.google.android.filament.utils.KTXLoader
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.Utils
import java.nio.ByteBuffer

class BotDetails : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: DetailsActivityBinding
    lateinit var surfaceView: SurfaceView
    lateinit var modelViewer: ModelViewer
    private lateinit var choreographer: Choreographer

    lateinit var gestureListener: GestureDetector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = DetailsActivityBinding.inflate(layoutInflater)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        fullScreen(this)
        setContentView(binding.root)

        surfaceView = binding.etanim1
        modelViewer = ModelViewer(surfaceView)

        Utils.init()

        choreographer = Choreographer.getInstance()

        loadGlb()
        createIndirectLights()

        getbotdetails()
    }

    fun getbotdetails() {
        mainViewModel.getBotDetail()
        observeResponse()
    }

    private fun loadGlb() {
        val glbBuffer = readAsset("models/bob.glb")

        var viewer = modelViewer
        viewer.loadModelGlb(glbBuffer)
        viewer.transformToUnitCube()

    }

    fun observeResponse() {
        mainViewModel.livebotResponse.observe(this, Observer { response ->
            print("ERECHHERE: ${response.data}")
            when (response) {
                is NetworkHelper.Loading -> {
                    binding.dimbg.visibility = VISIBLE
                }

                is NetworkHelper.Message -> {
                    binding.dimbg.visibility = GONE
                        showToast (this, response.message!!)


                }

                is NetworkHelper.Success -> {
                    binding.dimbg.visibility = GONE
                    handldeSuccess(response.data)


                }

            }
        })

    }

    private fun handldeSuccess(data: BotResponse?) {

        initViews(
            data!!.username,
            data!!.email,
            data!!.phone_number,
            data!!.date_of_birth,
            data!!.address.country,
            data!!.address.city,
            data!!.subscription.plan,
            data!!.address.street_address,
            data!!.subscription.term,
            data!!.subscription.plan,
            data!!.subscription.status,
            data!!.subscription.payment_method,


            )
    }

    fun initViews(
        username: String,
        email: String,
        phone: String,
        dob: String,
        country: String,
        city: String,
        stname: String,
        adress: String,
        plan: String,
        terms: String,
        status: String,
        pmethod: String,
    ) {
        println("DDDGA ${username} ${email}")
        binding.etuname.text = username
        binding.etemail.text = email
        binding.etphone.setText("${phone}")
        binding.etdob.setText("${dob}")
        binding.etcountry.setText("${country}")
        binding.etstreetname.setText("${stname}")
        binding.etstadress.setText("${adress}")
        binding.etcity.setText("${city}")
        binding.etplan.setText("${plan}")
        binding.etstatus.setText("${status}")
        binding.etpaymethod.setText("${pmethod}")
        binding.etterms.setText("${terms}")
    }

    val startTime = System.nanoTime()

    private val frameCallback = object : Choreographer.FrameCallback {
        // ... (existing code)

        override fun doFrame(currentTime: Long) {
            choreographer.postFrameCallback(this)


            val seconds = (currentTime - startTime).toDouble() / 1_000_000_000

            modelViewer.animator?.apply {
                if (animationCount > 0) {
                    val animationTime = (seconds % getAnimationDuration(0))
                    applyAnimation(6, animationTime.toFloat())
                }
                updateBoneMatrices()
            }
            modelViewer.render(currentTime)

        }

    }

    fun createIndirectLights() {


        val ibl = "venetian_crossroads_2k"

        var viewer = modelViewer
        val engine = viewer.engine
        val scene = viewer.scene

        readCompressAsset("envs/${ibl}/${ibl}_ibl.ktx").let {
            scene.indirectLight = KTXLoader.createIndirectLight(engine, it)
            scene.indirectLight!!.intensity = 40_000f
        }

        readCompressAsset("envs/${ibl}/${ibl}_skybox.ktx").let {
            scene.skybox = KTXLoader.createSkybox(engine, it)


        }
    }

    private fun readCompressAsset(s: String): ByteBuffer {
        val input = assets.open(s)
        val bytes = ByteArray(input.available())
        input.read(bytes)
        return ByteBuffer.wrap(bytes)
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