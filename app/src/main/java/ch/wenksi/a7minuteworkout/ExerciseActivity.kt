package ch.wenksi.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import kotlinx.android.synthetic.main.activity_exercise.*

class ExerciseActivity : AppCompatActivity() {
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

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
        pb_rest.progress = this.restProgress
        this.restTimer = object: CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                pb_rest.progress = 10 - restProgress
                tv_timer_rest.text = (10 - restProgress).toString()
            }

            override fun onFinish() = setupExerciseView()
        }.start()
    }

    private fun setExerciseProgressBar() {
        pb_exercise.progress = this.exerciseProgress
        this.exerciseTimer = object: CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                pb_exercise.progress = 30 - exerciseProgress
                tv_timer_exercise.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() = setupRestView()
        }.start()
    }

    private fun setupRestView() {
        ll_exercise_view.visibility = View.INVISIBLE
        ll_rest_view.visibility = View.VISIBLE

        if (this.restTimer != null) {
            this.restTimer!!.cancel()
            this.restProgress = 0
        }
        this.setRestProgressBar()
    }

    private fun setupExerciseView() {
        ll_rest_view.visibility = View.INVISIBLE
        ll_exercise_view.visibility = View.VISIBLE

        if (this.exerciseTimer != null) {
            this.exerciseTimer!!.cancel()
            this.exerciseProgress = 0
        }
        this.setExerciseProgressBar()
    }
}
