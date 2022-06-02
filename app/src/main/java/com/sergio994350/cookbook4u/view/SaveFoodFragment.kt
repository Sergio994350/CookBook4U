package com.sergio994350.cookbook4u.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergio994350.cookbook4u.R
import com.sergio994350.cookbook4u.adapter.SaveAdapter
import com.sergio994350.cookbook4u.viewModel.FoodViewModel
import kotlinx.android.synthetic.main.fragment_save_food.*


class SaveFoodFragment : Fragment(R.layout.fragment_save_food) {

    lateinit var viewModel: FoodViewModel
    lateinit var saveFood: SaveAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.getAllFood().observe(viewLifecycleOwner, Observer { it ->
            saveFood.differ.submitList(it)
        })
        saveFood.setOnItemClickListener { Meal ->
            val bundle = Bundle().apply {
                putSerializable("details", Meal)
            }
            findNavController().navigate(R.id.action_saveFood_to_details, bundle)
        }
    }

    private fun setupRecyclerView() {
        saveFood = SaveAdapter()
        save_rv.apply {
            adapter = saveFood
            layoutManager = LinearLayoutManager(context)
        }
    }
}