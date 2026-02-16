package com.am24.lab1android

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnOrder = findViewById<Button>(R.id.btnOrder)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        val cbSmall = findViewById<CheckBox>(R.id.cbSmall)
        val cbMedium = findViewById<CheckBox>(R.id.cbMedium)
        val cbLarge = findViewById<CheckBox>(R.id.cbLarge)

        val cbPepperoni = findViewById<CheckBox>(R.id.cbPepperoni)
        val cbMargherita = findViewById<CheckBox>(R.id.cbMargherita)
        val cbBBQChicken = findViewById<CheckBox>(R.id.cbBBQChicken)

        val cbTomatoes = findViewById<CheckBox>(R.id.cbTomatoes)
        val cbPineapple = findViewById<CheckBox>(R.id.cbPineapple)
        val cbCheese = findViewById<CheckBox>(R.id.cbCheese)
        val cbMushrooms = findViewById<CheckBox>(R.id.cbMushrooms)

        val sizeGroup = listOf(cbSmall, cbMedium, cbLarge)
        val typeGroup = listOf(cbPepperoni, cbMargherita, cbBBQChicken)

        sizeGroup.forEach { currentBox ->
            currentBox.setOnClickListener {
                sizeGroup.forEach {
                    if (it != currentBox) it.isChecked = false
                }
            }
        }

        typeGroup.forEach { currentBox ->
            currentBox.setOnClickListener {
                typeGroup.forEach {
                    if (it != currentBox) it.isChecked = false
                }
            }
        }

        btnOrder.setOnClickListener {
            val result = StringBuilder()
            result.append("Your order:\n\n")

            result.append("Size: ")
            val selectedSize = sizeGroup.find { it.isChecked }
            result.append(selectedSize?.text ?: "wasn't chosen")
            result.append("\n")

            result.append("Type: ")
            val selectedType = typeGroup.find { it.isChecked }
            result.append(selectedType?.text ?: "wasn't chosen")
            result.append("\n")

            val extras = mutableListOf<String>()
            if (cbTomatoes.isChecked) extras.add("Tomatoes")
            if (cbPineapple.isChecked) extras.add("Pineapple")
            if (cbCheese.isChecked) extras.add("Cheese")
            if (cbMushrooms.isChecked) extras.add("Mushrooms")

            if (extras.isNotEmpty()) {
                result.append("Extras: ${extras.joinToString(", ")}")
            } else {
                result.append("There is no extras")
            }

            tvResult.text = result.toString()
        }
    }
}
