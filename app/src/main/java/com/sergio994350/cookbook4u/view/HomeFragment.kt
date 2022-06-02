package com.sergio994350.cookbook4u.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergio994350.cookbook4u.R
import com.sergio994350.cookbook4u.adapter.CategoryAdapter
import com.sergio994350.cookbook4u.adapter.FoodAdapter
import com.sergio994350.cookbook4u.Model.Category
import com.sergio994350.cookbook4u.util.Resource
import com.sergio994350.cookbook4u.viewModel.FoodViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var viewModel: FoodViewModel
    lateinit var foodAdapter: FoodAdapter
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var ctg: Category
    val TAG = "HomeFragment"

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setupRecyclerViewF()
        setupRecyclerViewC()

        titleRE.text = "Beef"
        var job: Job? = null

        search_food.text.clear()
        search_food.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)

                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.getSearchMeal(editable.toString())
                        viewModel.searchMeals.observe(viewLifecycleOwner, Observer { response ->
                            when (response) {
                                is Resource.Success -> {
                                    response.data?.let { Random ->
                                        foodAdapter.differ.submitList(null)
                                        foodAdapter.differ.submitList(Random.meals)
                                        for (item in Random.meals) {
                                            titleRE.text = item.strMeal
                                        }
                                        //  editable.clear()
                                    }
                                }
                                is Resource.Error -> {
                                    response.message.let { message ->
                                        Log.e(TAG, "Error: $message")
                                    }
                                }
                                else -> {}
                            }
                        })
                    }
                }
            }
        }

        foodAdapter.setOnItemClickListener2 {
            viewModel.saveFood(it)
        }


        foodAdapter.setOnItemClickListener { Meal ->
            val bundle = Bundle().apply {
                putSerializable("details", Meal)
            }
            findNavController().navigate(R.id.action_home2_to_details, bundle)
        }
        categoryAdapter.setOnItemClickListener { category ->
            categoryy = category
            titleRE.text = category.strCategory
            foodAdapter.differ.submitList(null)
            viewModel.getFilter(category.strCategory)
        }

        viewModel.filter.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { Random ->
                        foodAdapter.differ.submitList(Random.meals)
                    }
                }
                is Resource.Error -> {
                    response.message.let { msg ->
                        Log.e(TAG, "Error: $msg")
                    }
                }
                else -> {}
            }
        })

        viewModel.categoryFood.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { Category ->
                        categoryAdapter.differ.submitList(Category.categories)
                    }
                }
                is Resource.Error -> {
                    response.message.let { msg ->
                        Log.e(TAG, "Error: $msg")
                    }
                }
                else -> {}
            }
        })
    }

    private fun setupRecyclerViewF() {
        foodAdapter = FoodAdapter()
        rv_food.apply {
            adapter = foodAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun setupRecyclerViewC() {
        categoryAdapter = CategoryAdapter()
        recyclerView.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    companion object {
        var categoryy: Category? = null
    }

}