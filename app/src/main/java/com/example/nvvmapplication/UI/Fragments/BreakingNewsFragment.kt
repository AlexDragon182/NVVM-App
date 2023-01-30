package com.example.nvvmapplication.UI.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.nvvmapplication.R
import com.example.nvvmapplication.UI.Adapters.NewsAdapter
import com.example.nvvmapplication.UI.IUAI.NVVMApp
import com.example.nvvmapplication.UI.IUAI.NewsViewModel
import com.example.nvvmapplication.UI.Util.Resource
import com.example.nvvmapplication.databinding.BreakingNewsBinding

class BreakingNewsFragment : Fragment (R.layout.breaking_news){

    private var _binding:BreakingNewsBinding? =null// to implement viewbindign
    private val binding get() = _binding!!// for implement view binding
    lateinit var viewModel: NewsViewModel //for instantiate the viewmodel
    lateinit var newsAdapter: NewsAdapter// for the recycle view adapter

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
        // this is the activity you pas to the setuprecyclerAdapter
        viewModel = (activity as NVVMApp).viewModel //viewmodel instantiated
        setupRecyclerView() // call the view model to use it here

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply{
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle

            )
        }

        //subscribe changes to that live data, whenever you have breaking news this observer will be call
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->//pass a lycecycle owner casue it is one and the observer, set name to response
            when (response){
                is Resource.Success -> {// functions called from Resurces files
                    hideProgressBar() //
                    response.data?.let{ newsResponse -> // if data is not equal to null , name it news Response
                        newsAdapter.differ.submitList(newsResponse.articles)// newsAdapter from recycler setup . differ from the NewsAdapter . article from news adapter
                    }
                }
                is Resource.Error ->{// if our resource is error
                    hideProgressBar() // hide progress barr
                    response.message?.let { message -> //
                        Log.e (TAG,"An error ocurred : $message")

                    }
                }
                is Resource.Loading-> {// if it is loading
                    showProgressBar()// progress bar show

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

    private fun setupRecyclerView(){// function to setup recycler view
        newsAdapter = NewsAdapter()// variable newsAdapter set it up to NewsAdapter(the real adapter)
        binding.rvBreakingNews.apply{   // apply the adapter in the layout
            adapter = newsAdapter // set the adapter to news adapter
            layoutManager = LinearLayoutManager(activity) // set layout Manager to linear Layout Manager activity
        }
    }

}

