package com.example.nvvmapplication.UI.IUAI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

import androidx.navigation.ui.setupWithNavController
import com.example.nvvmapplication.R
import com.example.nvvmapplication.UI.DataBase.ArticleDataBase
import com.example.nvvmapplication.UI.Repository.NewsRepository
import com.example.nvvmapplication.databinding.ActivityNvvmappBinding


class NVVMApp : AppCompatActivity() {

    //to call the view model
    lateinit var viewModel: NewsViewModel
    // store the viewbinding in a var
    private lateinit var binding: ActivityNvvmappBinding
    // finds the nav host fragment to define it as Nav Controller
    private val navController by lazy { (supportFragmentManager.findFragmentById(R.id.FrNewsNavHost) as NavHostFragment).navController }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNvvmappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDataBase(this)) //instantiate our news repository , passing the Data base
        val viewModelProviderFactory = NewsViewModelProviderFactory(application,newsRepository) //instantiate ViewModelProviderFactory
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java) // instantiate our viewmodel with the ViewModel Provider Factory
        binding.bottomNavigationView.setupWithNavController(navController)// defines the bottom navigation view as the nav controller

    }

    //Retrofit Setup
    //json to kotlin class


}