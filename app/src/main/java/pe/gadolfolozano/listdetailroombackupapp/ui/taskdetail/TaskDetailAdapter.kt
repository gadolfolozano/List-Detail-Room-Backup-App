package pe.gadolfolozano.listdetailroombackupapp.ui.taskdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import pe.gadolfolozano.listdetailroombackupapp.R
import pe.gadolfolozano.listdetailroombackupapp.ui.model.TaskDetailModel
import pe.gadolfolozano.listdetailroombackupapp.ui.util.ItemClickListener

class TaskDetailAdapter : RecyclerView.Adapter<TaskDetailAdapter.TaskViewHolder>() {
    var items: List<TaskDetailModel>? = null
    private var itemClickListener: ItemClickListener<TaskDetailModel>? = null

    fun setData(data: List<TaskDetailModel>?) {
        items = data
        notifyDataSetChanged()
    }

    fun setItemClickListener(itemClickListener: ItemClickListener<TaskDetailModel>) {
       this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        items?.let { return it.size }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_detail, parent, false)
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
        private val itemClickListener: ItemClickListener<TaskDetailModel>?
    ) : RecyclerView.ViewHolder(view) {

        val cardView = view.findViewById<CardView>(R.id.card_view)
        val taskDetailTextView = view.findViewById<TextView>(R.id.task_detail_text_view)

        fun onBind(model: TaskDetailModel) {
            cardView.setOnClickListener {
                itemClickListener?.invoke(model, adapterPosition)
            }
            taskDetailTextView.text = model.name
        }
    }
}