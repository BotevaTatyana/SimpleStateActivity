package com.example.simplestateactivity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.simplestateactivity.databinding.ActivityMainBinding
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import kotlin.random.Random

class SimpleState3Activity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var state: State

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.incrementButton.setOnClickListener { increment() }
        binding.randomColorButton.setOnClickListener { setRandomColor() }
        binding.switchVisibilityButton.setOnClickListener { switchVisibility() }

        state = savedInstanceState?.getParcelable(KEY_STATE) ?: State(
            counterValue = 0,
            counterTextColor = ContextCompat.getColor(this, R.color.purple_700),
            counterIsVisible = true
        )
        renderState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_STATE, state)
    }

    private fun increment() {
        state.counterValue++
        renderState()
    }

    private fun setRandomColor() {
        state.counterTextColor = Color.rgb(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
        renderState()
    }

    private fun switchVisibility() {
        state.counterIsVisible = !state.counterIsVisible
        renderState()
    }

    private fun renderState() = with(binding) {
        counterTextView.text = state.counterValue.toString()
        counterTextView.setTextColor(state.counterTextColor)
        counterTextView.visibility = if (state.counterIsVisible) View.VISIBLE else View.INVISIBLE
    }

    @Parcelize
    class State(
        var counterValue: Int,
        var counterTextColor: Int,
        var counterIsVisible: Boolean
    ) : Parcelable

    companion object {
        private val KEY_STATE = "STATE"
    }
}