package ch.wenksi.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_b_m_i.*
import kotlinx.android.synthetic.main.activity_exercise.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"
    var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        setSupportActionBar(toolbar_bmi_activity)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "CALCULATE BMI"
        }

        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        btn_calculate_units.setOnClickListener {
            if (this.currentVisibleView == METRIC_UNITS_VIEW) {
                this.calculateMetricUnits()
            } else {
                this.calculateUSUnits()
            }

        }

        this.makeVisibleMetricUnitsView()
        rg_units.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_metric_units) this.makeVisibleMetricUnitsView()
            else this.makeVisibleUSUnitsView()
        }
    }

    private fun calculateMetricUnits() {
        if (this.validateMetricUnits()) {
            val heightValue: Float = et_metric_unit_height.text.toString().toFloat() / 100
            val weightValue: Float = et_metric_unit_weight.text.toString().toFloat()
            val bmi = weightValue / (heightValue * heightValue)
            this.displayBMIResult(bmi)
        } else {
            Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateUSUnits() {
        if (this.validateUSUnits()) {
            val heightFeet: String = et_us_unit_height_feet.text.toString()
            val heightInch: String = et_us_unit_height_inch.text.toString()
            val heightValue: Float = heightFeet.toFloat() * 12 + heightInch.toFloat()
            val weightValue: Float = et_us_unit_weight.text.toString().toFloat()
            val bmi = 703 * (weightValue / (heightValue * heightValue))
            this.displayBMIResult(bmi)
        } else {
            Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        ll_display_bmi_result.visibility = View.VISIBLE
        /* tv_your_bmi.visibility = View.VISIBLE
        tv_bmi_value.visibility = View.VISIBLE
        tv_bmi_type.visibility = View.VISIBLE
        tv_bmi_description.visibility = View.VISIBLE */

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tv_bmi_value.text = bmiValue
        tv_bmi_type.text = bmiLabel
        tv_bmi_description.text = bmiDescription
    }

    private fun makeVisibleMetricUnitsView() {
        this.currentVisibleView = METRIC_UNITS_VIEW

        et_metric_unit_height.text!!.clear()
        et_metric_unit_weight.text!!.clear()

        til_metric_unit_weight.visibility = View.VISIBLE
        til_metric_unit_height.visibility = View.VISIBLE

        til_us_unit_weight.visibility = View.GONE
        ll_us_units_height.visibility = View.GONE

        ll_display_bmi_result.visibility = View.INVISIBLE
    }

    private fun makeVisibleUSUnitsView() {
        this.currentVisibleView = US_UNITS_VIEW

        et_us_unit_weight.text!!.clear()
        et_us_unit_height_feet.text!!.clear()
        et_us_unit_height_inch.text!!.clear()

        til_metric_unit_weight.visibility = View.GONE
        til_metric_unit_height.visibility = View.GONE

        til_us_unit_weight.visibility = View.VISIBLE
        ll_us_units_height.visibility = View.VISIBLE

        ll_display_bmi_result.visibility = View.INVISIBLE
    }

    private fun validateMetricUnits(): Boolean {
        return !(et_metric_unit_weight.text.toString().isEmpty()
            || et_metric_unit_height.text.toString().isEmpty()
        )
    }

    private fun validateUSUnits(): Boolean {
        return !(et_us_unit_weight.text.toString().isEmpty()
            || et_us_unit_height_feet.text.toString().isEmpty()
            || et_us_unit_height_inch.text.toString().isEmpty()
        )
    }
}
