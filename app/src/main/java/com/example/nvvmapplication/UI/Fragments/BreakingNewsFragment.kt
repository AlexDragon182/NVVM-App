package com.example.nvvmapplication.UI.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.nvvmapplication.R
import com.example.nvvmapplication.UI.Adapters.NewsAdapter
import com.example.nvvmapplication.UI.IUAI.NVVMApp
import com.example.nvvmapplication.UI.IUAI.NewsViewModel
import com.example.nvvmapplication.UI.Util.Resource
import com.example.nvvmapplication.databinding.BreakingNewsBinding

class BreakingNewsFragment : Fragment (R.layout.breaking_news){

    private var _binding:BreakingNewsBinding? =null
    private val binding get() = _binding!!
    lateinit var viewModel: NewsViewModel //for instantiate the viewmodel
    lateinit var newsAdapter: NewsAdapter

    val TAG = "BrekingNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BreakingNewsBinding.inflate(inflater, container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NVVMApp).viewModel //viewmodel instantiated
        setupRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let{ newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e (TAG,"An error ocurred : $message")

                    }
                }
                is Resource.Loading-> {
                    showProgressBar()

                }
            }
        })
    }

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply{
            adapter = newsAdapter
            LayoutManager = LinearLayoutManager(activity)
        }
    }

}

