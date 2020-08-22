package pe.gadolfolozano.listdetailroombackupapp.ui.taskdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import pe.gadolfolozano.listdetailroombackupapp.R
import pe.gadolfolozano.listdetailroombackupapp.ui.model.TaskDetailModel
import pe.gadolfolozano.listdetailroombackupapp.ui.model.TaskModel
import pe.gadolfolozano.listdetailroombackupapp.ui.taskList.TaskListViewModel
import pe.gadolfolozano.listdetailroombackupapp.ui.util.InputTextBottomSheetFragment

class TaskDetailFragment : Fragment() {

    private val taskDetailViewModel: TaskDetailViewModel by viewModel()

    lateinit var adapter: TaskDetailAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var emptyMessageTextView: TextView
    lateinit var fabAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_detail, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        emptyMessageTextView = view.findViewById(R.id.empty_message_text_view)
        fabAdd = view.findViewById(R.id.fab_add)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TaskDetailAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter.setItemClickListener { _, _ ->

        }

        fabAdd.setOnClickListener {
            val fragment =
                InputTextBottomSheetFragment.newInstance(
                    InputTextBottomSheetFragment.InputType.TASK_DETAIL,
                    "Create Task Detail"
                )
            fragment.show(childFragmentManager, "inputTextFragment")
        }

        taskDetailViewModel.tasDetailListLiveData.observe(viewLifecycleOwner) { taskDetails ->
            adapter.setData(taskDetails)
            emptyMessageTextView.visibility = if (taskDetails.isEmpty()) View.VISIBLE else View.GONE
            recyclerView.visibility = if (taskDetails.isEmpty()) View.GONE else View.VISIBLE
        }

        val tasksDetails = listOf(
            TaskDetailModel(
                "uuid",
                "Task one"
            ),
            TaskDetailModel(
                "uuid",
                "Task two"
            ),
            TaskDetailModel(
                "uuid",
                "Task three"
            )
        )
        taskDetailViewModel.updateTaskDetails(tasksDetails)
    }
}