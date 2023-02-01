package com.example.nvvmapplication.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nvvmapplication.R
import com.example.nvvmapplication.UI.Adapters.NewsAdapter
import com.example.nvvmapplication.UI.IUAI.NVVMApp
import com.example.nvvmapplication.UI.IUAI.NewsViewModel
import com.example.nvvmapplication.databinding.SavedNewsBinding
import com.example.nvvmapplication.databinding.SearchedNewsBinding
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment (R.layout.saved_news) {

    private var _binding: SavedNewsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: NewsViewModel //for instantiate the viewmodel
    val TAG = "SearchNewsFragment"
    lateinit var newsAdapter: NewsAdapter//

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NVVMApp).viewModel //viewmodel instantiated
        setupRecyclerView()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply{
                putSerializable("article",it)
            }


                findNavController().navigate(
                    R.id.action_savedNewsFragment_to_articleFragment,
                    bundle

                )



        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.currentList.get(position)
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Succesfullu deleted article",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.savedArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.submitList(articles)
        })
    }


    private fun setupRecyclerView(){// function to setup recycler view
        newsAdapter = NewsAdapter(requireContext())// variable newsAdapter set it up to NewsAdapter(the real adapter)
        binding.rvSavedNews.apply{   // apply the adapter in the layout
            adapter = newsAdapter // set the adapter to news adapter
            layoutManager = LinearLayoutManager(activity) // set layout Manager to linear Layout Manager activity
        }
    }

}