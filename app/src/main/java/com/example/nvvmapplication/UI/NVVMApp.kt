package com.example.nvvmapplication.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.ui.setupWithNavController
import com.example.nvvmapplication.R
import com.example.nvvmapplication.databinding.ActivityNvvmappBinding


class NVVMApp : AppCompatActivity() {

    private lateinit var binding: ActivityNvvmappBinding
    private val navController by lazy { (supportFragmentManager.findFragmentById(R.id.FrNewsNavHost) as NavHostFragment).navController }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNvvmappBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}