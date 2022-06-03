package com.sergio994350.cookbook4u.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sergio994350.cookbook4u.R
import com.sergio994350.cookbook4u.adapter.SaveAdapter
import com.sergio994350.cookbook4u.viewModel.FoodViewModel
import kotlinx.android.synthetic.main.fragment_save_food.*
import kotlinx.android.synthetic.main.item_save.*


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

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val food = saveFood.differ.currentList[position]
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    viewModel.deleteFood(food)
                    Snackbar.make(view, "Recipe Deleted", Snackbar.LENGTH_SHORT).apply {
                        animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                        setBackgroundTint(Color.DKGRAY)
                        setTextColor(Color.WHITE)
                        show()
                    }.setAction("undo") {
                        viewModel.saveFood(food)
                    }
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(save_rv)
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