/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import android.util.Log

class MainActivity : AppCompatActivity() {

    /**
     * DO NOT ALTER ANY VARIABLE OR VALUE NAMES OR THEIR INITIAL VALUES.
     *
     * Anything labeled var instead of val is expected to be changed in the functions but DO NOT
     * alter their initial values declared here, this could cause the app to not function properly.
     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has been drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // === DO NOT ALTER THE CODE IN THE FOLLOWING IF STATEMENT ===
        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === END IF STATEMENT ===

        lemonImage = findViewById(R.id.imageView)
        setViewElements()
        lemonImage!!.setOnClickListener {

            clickLemonImage()

        }
        lemonImage!!.setOnLongClickListener {
            showSnackbar()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }


    private fun clickLemonImage() {

        when (lemonadeState){

            SELECT -> {
                lemonadeState   = SQUEEZE
                lemonSize       = lemonTree.pick()
                squeezeCount    = 0
            }

            SQUEEZE -> {
                squeezeCount ++
                lemonSize --

                Log.d("Lemonade", "Squeezed $squeezeCount times, lemonSize:$lemonSize")

                if (lemonSize == 0) {
                    // If lemonSize is 0, change state to DRINK
                    lemonadeState = DRINK
                    // Set lemonSize to -1 (no longer relevant)
                    lemonSize = -1
                }
            }

            DRINK ->{
                lemonadeState = RESTART
            }

            RESTART ->{
                lemonadeState = SELECT
            }
        }

        setViewElements()


    }


     //Set up the view elements according to the state.

    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)
        lemonImage = findViewById(R.id.imageView)
        val textToSet: String
        val lemonSelecttxt      = getString(R.string.lemon_select)
        val lemonSqueeztxt      = getString(R.string.lemon_squeeze)
        val lemonSqueezCountTxt = getString(R.string.squeeze_count)
        val lemonDrinktxt       = getString(R.string.lemon_drink)
        val lemonRestarttxt     = getString(R.string.lemon_empty_glass)

        //  set up a conditional that tracks the lemonadeState
        val drawableResource = when (lemonadeState) {
            SELECT ->{
                textToSet = lemonSelecttxt
                R.drawable.lemon_tree
            }
            SQUEEZE->{
                if (squeezeCount == 0){
                    textToSet = lemonSqueeztxt
                }else{
                    textToSet = lemonSqueezCountTxt
                }
                R.drawable.lemon_squeeze
            }
            DRINK ->{
                textToSet = lemonDrinktxt
                R.drawable.lemon_drink

            }
            RESTART ->{
                textToSet = lemonRestarttxt
                R.drawable.lemon_restart
            }
            else ->{
                textToSet = lemonSelecttxt
                R.drawable.lemon_tree

            }
        }
        lemonImage!!.setImageResource(drawableResource)
        textAction.text = textToSet


    }

    //Long clicking the lemon image will show how many times the lemon has been squeezed.
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            Log.d("Lemonade", "Snackbar not shown because lemonadeState is not SQUEEZE")
            return false
        }
        Log.d("Lemonade", "Squeeze count: $squeezeCount")
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

//A Lemon tree class with a method to "pick" a lemon. The "size" of the lemon is randomized
//and determines how many times a lemon needs to be squeezed before you get lemonade.
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
