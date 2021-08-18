package ch.wenksi.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_b_m_i.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbar_history_activity)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "HISTORY"
        }

        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        this.getAllCompletedDates()
    }

    private fun getAllCompletedDates() {
        val dbHandler = SqliteOpenHelper(this, null)
        val allDates = dbHandler.getAllCompletedDatesList()
        for (i in allDates) {
            if (allDates.size > 0) {
                tv_history.visibility = View.VISIBLE
                rv_history.visibility = View.VISIBLE
                tv_no_data_available.visibility = View.GONE

                rv_history.layoutManager = LinearLayoutManager(this)
                rv_history.adapter = HistoryAdapter(this, allDates)
            } else {
                rv_history.visibility = View.GONE
                tv_history.visibility = View.GONE
                tv_no_data_available.visibility = View.VISIBLE
            }
        }
    }
}
