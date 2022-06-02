package com.sergio994350.cookbook4u.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sergio994350.cookbook4u.R
import com.sergio994350.cookbook4u.util.Resource
import com.sergio994350.cookbook4u.viewModel.FoodViewModel
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment(R.layout.fragment_details) {

    lateinit var viewModel: FoodViewModel
    val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val food = args.details
        card_view_back.setOnClickListener {
            findNavController().navigate(R.id.action_details_to_home2)
        }

        card_view_save_dish.setOnClickListener {
            viewModel.saveFood(food)
        }

        viewModel.getDetails(food.idMeal.toString())

        // TODO переделать передачу ингредиентов
        viewModel.details.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { meal ->
                        for (item in meal.meals) {
                            Glide.with(this).load(item.strMealThumb).into(image_dish_big)
                            name_food_D.text = item.strMeal
                            countryD.text = item.strArea
                            tv_dish_description.text = item.strInstructions

                            ingredient.text = """
                                ${item.strMeasure1 + " " + item.strIngredient1}
                                ${item.strMeasure2 + " " + item.strIngredient2}
                                ${item.strMeasure3 + " " + item.strIngredient3}
                                ${item.strMeasure4 + " " + item.strIngredient4}
                                ${item.strMeasure5 + " " + item.strIngredient5}
                                ${item.strMeasure6 + " " + item.strIngredient6}
                                ${item.strMeasure7 + " " + item.strIngredient7}
                                ${item.strMeasure8 + " " + item.strIngredient8}
                                ${item.strMeasure9 + " " + item.strIngredient9}
                                ${item.strMeasure10 + " " + item.strIngredient10}
                                ${item.strMeasure11 + " " + item.strIngredient11}
                                ${item.strMeasure12 + " " + item.strIngredient12}
                                ${item.strMeasure13 + " " + item.strIngredient13}
                                ${item.strMeasure14 + " " + item.strIngredient14}
                                ${item.strMeasure15 + " " + item.strIngredient15}
                                ${item.strMeasure16 + " " + item.strIngredient16}
                                ${item.strMeasure17 + " " + item.strIngredient17}
                                ${item.strMeasure18 + " " + item.strIngredient18}
                                ${item.strMeasure19 + " " + item.strIngredient19}
                                ${item.strMeasure20 + " " + item.strIngredient20}
                            """.trimIndent()


                            // go to youtube url
                            play.setOnClickListener {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.strYoutube))
                                startActivity(intent)
                            }
                        }
                    }
                }
                else -> {}
            }
        })
    }
}