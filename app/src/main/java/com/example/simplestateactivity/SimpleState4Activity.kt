package com.example.simplestateactivity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.simplestateactivity.databinding.ActivityMainBinding

const val APP_PREFERENCES = "APP_PREFERENCES"
const val PREF_SOME_TXT_VALUE = "PREF_SOME_TXT_VALUE"
const val PREF_SOME_TXT_COLOR = "PREF_SOME_TXT_COLOR"
const val PREF_SOME_TXT_VISIB = "PREF_SOME_TXT_VISIB"

class SimpleState4Activity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var preferences: SharedPreferences
    private val preferencesListener =
        SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            if (key == PREF_SOME_TXT_VALUE) {
                binding.savedValue.setText(preferences.getInt(key, 0).toString())
            }
            if (key == PREF_SOME_TXT_COLOR){
                binding.savedValue.setTextColor(preferences.getInt(key, 0))
            }
            if (key == PREF_SOME_TXT_VISIB){
                if (preferences.getBoolean(key,true))
                    binding.savedValue.visibility = View.VISIBLE
                else
                    binding.savedValue.visibility = View.INVISIBLE
            }
        }

    private val viewModel by viewModels<SimpleState4ViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.incrementButton.setOnClickListener { viewModel.increment() }
        binding.randomColorButton.setOnClickListener { viewModel.setRandomColor() }
        binding.switchVisibilityButton.setOnClickListener { viewModel.switchVisibility() }

        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        viewModel.initState(
            savedInstanceState?.getParcelable(KEY_STATE) ?: SimpleState4ViewModel.State(
                counterValue = preferences.getInt(PREF_SOME_TXT_VALUE, 0),
                counterTextColor = preferences.getInt(PREF_SOME_TXT_COLOR, 0),
                counterIsVisible = preferences.getBoolean(PREF_SOME_TXT_VISIB, true)
            )
        )


        var currentValue = preferences.getInt(PREF_SOME_TXT_VALUE, 0)
        var currentColor = preferences.getInt(PREF_SOME_TXT_COLOR, 0)
        var currentVisib = preferences.getBoolean(PREF_SOME_TXT_VISIB, true)


        binding.savedValue.setText(currentValue.toString())

        binding.savedValue.setTextColor(currentColor)
        if (currentVisib){

            binding.savedValue.visibility = View.VISIBLE
        }
        else{
            
            binding.savedValue.visibility = View.INVISIBLE
        }

        binding.saveButton.setOnClickListener {
            val value = binding.counterTextView.text.toString().toInt()
            val color = binding.counterTextView.currentTextColor
            val visib = binding.counterTextView.isVisible
            preferences.edit()
                .putInt(PREF_SOME_TXT_VALUE, value)
                .putInt(PREF_SOME_TXT_COLOR, color)
                .putBoolean(PREF_SOME_TXT_VISIB, visib)
                .apply()
        }

        preferences.registerOnSharedPreferenceChangeListener(preferencesListener)


        viewModel.state.observe(this) {
            renderState(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_STATE, viewModel.state.value)
    }


    private fun renderState(state: SimpleState4ViewModel.State) = with(binding) {
        counterTextView.text = state.counterValue.toString()
        counterTextView.setTextColor(state.counterTextColor)
        counterTextView.visibility = if (state.counterIsVisible) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        val KEY_STATE = "STATE"
    }
}