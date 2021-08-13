package ch.wenksi.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_exercise.*

class ExerciseActivity : AppCompatActivity() {
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed()
        }
        this.setupRestView()
    }

    override fun onDestroy() {
        if (this.restTimer != null) {
            this.restTimer!!.cancel()
            this.restProgress = 0
        }
        super.onDestroy()
    }

    private fun setRestProgressBar() {
        progressBar.progress = this.restProgress
        this.restTimer = object: CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 10 - restProgress
                tv_timer.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity, "Here now we start the exercise", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun setupRestView() {
        if (this.restTimer != null) {
            this.restTimer!!.cancel()
            this.restProgress = 0
        }
        this.setRestProgressBar()
    }
}
