package com.am24.lab2android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.navigation.findNavController

class OptionsFragment : Fragment(R.layout.fragment_options) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnOrder = view.findViewById<Button>(R.id.btnOrder)
        val cbSmall = view.findViewById<CheckBox>(R.id.cbSmall)
        val cbMedium = view.findViewById<CheckBox>(R.id.cbMedium)
        val cbLarge = view.findViewById<CheckBox>(R.id.cbLarge)
        val cbPepperoni = view.findViewById<CheckBox>(R.id.cbPepperoni)
        val cbMargherita = view.findViewById<CheckBox>(R.id.cbMargherita)
        val cbBBQChicken = view.findViewById<CheckBox>(R.id.cbBBQChicken)
        val cbTomatoes = view.findViewById<CheckBox>(R.id.cbTomatoes)
        val cbPineapple = view.findViewById<CheckBox>(R.id.cbPineapple)
        val cbCheese = view.findViewById<CheckBox>(R.id.cbCheese)
        val cbMushrooms = view.findViewById<CheckBox>(R.id.cbMushrooms)

        val sizeGroup = listOf(cbSmall, cbMedium, cbLarge)
        val typeGroup = listOf(cbPepperoni, cbMargherita, cbBBQChicken)

        sizeGroup.forEach { box ->
            box.setOnClickListener { sizeGroup.forEach { if (it != box) it.isChecked = false } }
        }
        typeGroup.forEach { box ->
            box.setOnClickListener { typeGroup.forEach { if (it != box) it.isChecked = false } }
        }

        btnOrder.setOnClickListener {
            val result = StringBuilder("Your order:\n\n")
            result.append("Size: ${sizeGroup.find { it.isChecked }?.text ?: "none"}\n")
            result.append("Type: ${typeGroup.find { it.isChecked }?.text ?: "none"}\n")

            val extras = mutableListOf<String>()
            if (cbTomatoes.isChecked) extras.add("Tomatoes")
            if (cbPineapple.isChecked) extras.add("Pineapple")
            if (cbCheese.isChecked) extras.add("Cheese")
            if (cbMushrooms.isChecked) extras.add("Mushrooms")

            result.append("Extras: ${if(extras.isEmpty()) "none" else extras.joinToString(", ")}")

            val message = result.toString()

            val action = OptionsFragmentDirections.actionOptionsToOrder(message)

            view.findNavController()
                .navigate(action)
        }
    }
}