package ch.wenksi.a7minuteworkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_exercise_status.view.*

class ExerciseStatusAdapter(val items: ArrayList<ExerciseModel>, val context: Context) :
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvItem = view.tv_item
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExerciseModel = this.items[position]
        holder.tvItem.text = model.getId().toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_exercise_status, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return this.items.size
    }
}