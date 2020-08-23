package pe.gadolfolozano.listdetailroombackupapp.ui.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import pe.gadolfolozano.listdetailroombackupapp.R
import pe.gadolfolozano.listdetailroombackupapp.ui.util.InputTextBottomSheetFragment

class TaskListFragment : Fragment() {

    private val taskViewModel : TaskListViewModel by viewModel()

    lateinit var adapter: TaskAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var emptyMessageTextView: TextView
    lateinit var fabAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        emptyMessageTextView = view.findViewById(R.id.empty_message_text_view)
        fabAdd = view.findViewById(R.id.fab_add)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TaskAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter.setItemClickListener { _, _ ->
            findNavController().navigate(R.id.action_TaskListFragment_to_TaskDetailFragment)
        }

        fabAdd.setOnClickListener {
            val fragment =
                InputTextBottomSheetFragment.newInstance(
                    InputTextBottomSheetFragment.InputType.TASK_NAME,
                    "Create New Task"
                )
            fragment.show(childFragmentManager, "inputTextFragment")
        }

        taskViewModel.taskListLiveData.observe(viewLifecycleOwner) { tasks ->
            adapter.setData(tasks)
            emptyMessageTextView.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
            recyclerView.visibility = if (tasks.isEmpty()) View.GONE else View.VISIBLE
        }

        taskViewModel.fetchTasks()
    }
}