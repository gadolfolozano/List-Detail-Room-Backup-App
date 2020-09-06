package pe.gadolfolozano.listdetailroombackupapp.ui.taskList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import pe.gadolfolozano.listdetailroombackupapp.R
import pe.gadolfolozano.listdetailroombackupapp.ui.model.TaskModel
import pe.gadolfolozano.listdetailroombackupapp.ui.util.ItemClickListener

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    var items: List<TaskModel>? = null
    private var itemClickListener: ItemClickListener<TaskModel>? = null

    fun setData(data: List<TaskModel>?) {
        items = data
        notifyDataSetChanged()
    }

    fun setItemClickListener(itemClickListener: ItemClickListener<TaskModel>) {
       this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        items?.let { return it.size }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(
            itemView,
            itemClickListener
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        items?.let { holder.onBind(it[position]) }
    }

    class TaskViewHolder(
        private val view: View,
        private val itemClickListener: ItemClickListener<TaskModel>?
    ) : RecyclerView.ViewHolder(view) {

        private val cardView = view.findViewById<CardView>(R.id.card_view)
        private val taskNameTextView = view.findViewById<TextView>(R.id.task_name_text_view)

        fun onBind(model: TaskModel) {
            cardView.setOnClickListener {
                itemClickListener?.invoke(model, adapterPosition)
            }
            taskNameTextView.text = model.name
        }
    }
}