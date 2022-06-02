package com.sergio994350.cookbook4u.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sergio994350.cookbook4u.R
import com.sergio994350.cookbook4u.Model.Meal
import kotlinx.android.synthetic.main.item_food.view.*

class FoodAdapter : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {
    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item)

    private val differCallback = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(food.strMealThumb).into(image_view_item_food)
            name_food.text = food.strMeal
            country.text = food.strArea
            card_save.setOnClickListener {
                onItemClickListener2?.let { it(food) }
                card_save.setCardBackgroundColor(Color.WHITE);
            }
            setOnClickListener {
                onItemClickListener?.let { it(food) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Meal) -> Unit)? = null
    private var onItemClickListener2: ((Meal) -> Unit)? = null
    fun setOnItemClickListener(listener: (Meal) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemClickListener2(listener: (Meal) -> Unit) {
        onItemClickListener2 = listener
    }

}