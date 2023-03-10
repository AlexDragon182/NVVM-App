package com.example.nvvmapplication.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.nvvmapplication.R
import com.example.nvvmapplication.UI.IUAI.NVVMApp
import com.example.nvvmapplication.UI.IUAI.NewsViewModel
import com.example.nvvmapplication.UI.models.Article
import com.example.nvvmapplication.databinding.ArticleBinding
import com.example.nvvmapplication.databinding.SavedNewsBinding
import com.google.android.material.snackbar.Snackbar

class ArticleFragment :Fragment(R.layout.article) {


    private var _binding: ArticleBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: NewsViewModel //for instantiate the viewmodel
    private val args by navArgs<ArticleFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ArticleBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NVVMApp).viewModel //viewmodel instantiated
        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let{
                loadUrl(article.url)
            }
        }
        binding.fab.setOnClickListener{
            viewModel.savedArticle(article)
            Snackbar.make(view,"Article saved succesfully", Snackbar.LENGTH_SHORT).show()
        }

    }
}