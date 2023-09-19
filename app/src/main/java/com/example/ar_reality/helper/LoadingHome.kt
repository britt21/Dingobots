package com.example.ar_reality.helper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.ar_reality.databinding.LoadingHomeBinding

class LoadingHome : DialogFragment() {
    lateinit var binding : LoadingHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = LoadingHomeBinding.inflate(inflater)


        return binding.root
    }
}