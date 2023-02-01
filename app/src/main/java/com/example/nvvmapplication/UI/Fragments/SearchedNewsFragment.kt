package com.example.nvvmapplication.UI.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nvvmapplication.R
import com.example.nvvmapplication.UI.Adapters.NewsAdapter
import com.example.nvvmapplication.UI.IUAI.NVVMApp
import com.example.nvvmapplication.UI.IUAI.NewsViewModel
import com.example.nvvmapplication.UI.Util.Constants
import com.example.nvvmapplication.UI.Util.Constants.Companion.SEARCHED_NEWS_TIME_DELAY
import com.example.nvvmapplication.UI.Util.Resource
import com.example.nvvmapplication.databinding.SearchedNewsBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchedNewsFragment : Fragment(R.layout.searched_news) {

    private var _binding: SearchedNewsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: NewsViewModel //for instantiate the viewmodel
    val TAG = "SearchNewsFragment"
    lateinit var newsAdapter: NewsAdapter//

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NVVMApp).viewModel //viewmodel instantiated
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
val currentId = findNavController().currentDestination?.id
            if(currentId == R.id.searchedNewsFragment) {
                findNavController().navigate(
                    R.id.action_searchedNewsFragment_to_articleFragment,
                    bundle

                )
            }

        }

        //add delay until we add a littlebit of a request

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCHED_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
        //subscribe changes to that live data, whenever you have breaking news this observer will be call
        viewModel.searchNews.observe(
            viewLifecycleOwner,
            Observer { response ->//pass a lycecycle owner casue it is one and the observer, set name to response
                when (response) {
                    is Resource.Success -> {// functions called from Resurces files
                        hideProgressBar() //
                        response.data?.let { newsResponse -> // if data is not equal to null , name it news Response
                            newsResponse.articles?.let {
                                newsAdapter.submitList(it)// newsAdapter from recycler setup . differ from the NewsAdapter . article from news adapter
                            }
                            newsResponse.totalResults?.let {
                                val totalPages = it / Constants.QUERY_PAGE_SIZE + 2
                                isLastPage = viewModel.searchNewsPage == totalPages
                                if (isLastPage) {
                                    binding.rvSearchNews.setPadding(0, 0, 0, 0)
                                }
                            }
                        }
                    }
                    is Resource.Error -> {// if our resource is error
                        hideProgressBar() // hide progress barr
                        response.message?.let { message -> //
                            Log.e(TAG, "An error ocurred : $message")
                            Toast.makeText(
                                activity,
                                "an Error Occurred : $message",
                                Toast.LENGTH_LONG
                            ).show()


                        }
                    }
                    is Resource.Loading -> {// if it is loading
                        showProgressBar()// progress bar show

                    }
                }
            })
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }


        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBegining = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBegining && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.searchNews(binding.etSearch.text.toString())
                isScrolling = false

            }
        }


    }

    private fun setupRecyclerView() {// function to setup recycler view
        newsAdapter =
            NewsAdapter(requireContext())// variable newsAdapter set it up to NewsAdapter(the real adapter)
        binding.rvSearchNews.apply {   // apply the adapter in the layout
            adapter = newsAdapter // set the adapter to news adapter
            layoutManager =
                LinearLayoutManager(requireContext()) // set layout Manager to linear Layout Manager activity
            addOnScrollListener(this@SearchedNewsFragment.scrollListener)
        }
    }
}