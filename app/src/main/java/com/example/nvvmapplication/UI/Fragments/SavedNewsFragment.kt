package com.example.nvvmapplication.UI.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.nvvmapplication.R
import com.example.nvvmapplication.UI.IUAI.NVVMApp
import com.example.nvvmapplication.UI.IUAI.NewsViewModel

class SavedNewsFragment : Fragment (R.layout.saved_news) {

    lateinit var viewModel: NewsViewModel //for instantiate the viewmodel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NVVMApp).viewModel //viewmodel instantiated
    }
}