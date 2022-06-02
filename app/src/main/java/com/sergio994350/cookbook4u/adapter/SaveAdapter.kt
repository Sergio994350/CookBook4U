package com.sergio994350.cookbook4u.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sergio994350.cookbook4u.R
import com.sergio994350.cookbook4u.Model.Meal
import kotlinx.android.synthetic.main.item_save.view.*

class SaveAdapter : RecyclerView.Adapter<SaveAdapter.viewHolder>() {
    inner class viewHolder(item: View) : RecyclerView.ViewHolder(item)

    private val differCallback = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_save, parent, false)
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val save = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(context).load(save.strMealThumb).into(image_view_saved_meal)
            text_view_saved_meal_title.text = save.strMeal
            text_view_saved_meal_description.text = save.strArea
            setOnClickListener {
                onItemClickListener?.let { it(save) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Meal) -> Unit)? = null
    fun setOnItemClickListener(listener: (Meal) -> Unit) {
        onItemClickListener = listener
    }
}