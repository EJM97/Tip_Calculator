package com.example.tipcalculator_tutorial

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENTAGE = 15

class MainActivity : AppCompatActivity() {

    private lateinit var etBaseAmount: EditText
    private lateinit var seekBar: SeekBar
    private lateinit var tvTipPercentageLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvTipDescription:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBar = findViewById(R.id.seekBar)
        tvTipPercentageLabel = findViewById(R.id.tvTipPercentageLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotal = findViewById(R.id.tvTotal)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        seekBar.progress = INITIAL_TIP_PERCENTAGE
        tvTipPercentageLabel.text = "$INITIAL_TIP_PERCENTAGE%"

        updateTipDescription(INITIAL_TIP_PERCENTAGE)


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                tvTipPercentageLabel.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        etBaseAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeTipAndTotal()
            }

        })


    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when(tipPercent){
            in 0..9 ->"Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }

        tvTipDescription.text = tipDescription

        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/seekBar.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int

        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {

        if (etBaseAmount.text.isEmpty()){
            tvTipAmount.text =""
            tvTotal.text = ""
            return

        }

        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercentage = seekBar.progress
        var tip:Double = baseAmount * tipPercentage / 100
        var total:Double = tip + baseAmount
        tvTipAmount.text ="%.2f".format(tip)
        tvTotal.text = "%.2f".format(total)

    }
}