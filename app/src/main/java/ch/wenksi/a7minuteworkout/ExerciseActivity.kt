package ch.wenksi.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_exercise_activity.setNavigationOnClickListener {
            this.customDialogForBackButton()
        }

        tts = TextToSpeech(this, this)

        this.exerciseList = Constants.defaultExerciseList()
        this.setupRestView()
        this.setupExerciseStatusRecyclerView()
    }

    override fun onDestroy() {
        if (this.restTimer != null) {
            this.restTimer!!.cancel()
            this.restProgress = 0
        }
        if (this.exerciseTimer != null) {
            this.exerciseTimer!!.cancel()
            this.exerciseProgress = 0
        }
        if (this.tts != null) {
            this.tts!!.stop()
            this.tts!!.shutdown()
        }

        if (this.player != null) {
            this.player!!.stop()
        }
        super.onDestroy()
    }

    private fun setRestProgressBar() {
        pb_rest.progress = this.restProgress
        this.restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                pb_rest.progress = 10 - restProgress
                tv_timer_rest.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() {
        pb_exercise.progress = this.exerciseProgress
        this.exerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                this@ExerciseActivity.exerciseProgress++
                pb_exercise.progress = 30 - this@ExerciseActivity.exerciseProgress
                tv_timer_exercise.text = (30 - this@ExerciseActivity.exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < this@ExerciseActivity.exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    private fun setupRestView() {
        this.playStartSound()

        ll_exercise_view.visibility = View.INVISIBLE
        ll_rest_view.visibility = View.VISIBLE

        if (this.restTimer != null) {
            this.restTimer!!.cancel()
            this.restProgress = 0
        }
        tv_upcoming_exercise_name.text =
            this.exerciseList!![this.currentExercisePosition + 1].getName()
        this.setRestProgressBar()
    }

    private fun playStartSound() {
        try {
            this.player = MediaPlayer.create(applicationContext, R.raw.press_start)
            this.player!!.isLooping = false
            this.player!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupExerciseView() {
        ll_rest_view.visibility = View.INVISIBLE
        ll_exercise_view.visibility = View.VISIBLE

        if (this.exerciseTimer != null) {
            this.exerciseTimer!!.cancel()
            this.exerciseProgress = 0
        }
        this.setExerciseProgressBar()
        iv_exercise.setImageResource(this.exerciseList!![this.currentExercisePosition].getImage())
        tv_exercise_name.text = this.exerciseList!![this.currentExercisePosition].getName()
        this.speakOut(tv_exercise_name.text.toString())
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = this.tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported.")
            }
        } else {
            Log.e("TTS", "Initialization failed.")
        }
    }

    private fun speakOut(text: String) {
        this.tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setupExerciseStatusRecyclerView() {
        rv_exercise_status.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        this.exerciseAdapter = ExerciseStatusAdapter(this.exerciseList!!, this)
        rv_exercise_status.adapter = this.exerciseAdapter
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
        customDialog.tv_yes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.tv_no.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
}
