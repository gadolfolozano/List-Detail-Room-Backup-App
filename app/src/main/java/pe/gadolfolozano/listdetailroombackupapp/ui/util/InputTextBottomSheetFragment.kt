package pe.gadolfolozano.listdetailroombackupapp.ui.util

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import pe.gadolfolozano.listdetailroombackupapp.R

class InputTextBottomSheetFragment : BottomSheetDialogFragment() {
    private var listener: InputTextListener? = null
    private var inputType: Int = 0

    private lateinit var editText: EditText

    override fun onAttach(context: Context) {
        if (context is InputTextListener) {
            listener = context
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_fragment_input_text, container, false)

        editText = view.findViewById(R.id.edit_text)
        val saveButton = view.findViewById<Button>(R.id.save_btn)
        val titleTextView = view.findViewById<TextView>(R.id.title_text_view)
        titleTextView.text = arguments?.getString(ARG_TITLE) ?: "Title"
        inputType = arguments?.getInt(ARG_TYPE) ?: 0

        saveButton.setOnClickListener {
            listener?.onTextSaved(inputType, editText.text.toString())
            dismiss()
        }
        return view
    }

    interface InputTextListener {
        fun onTextSaved(inputType: Int, text: String)
    }

    enum class InputType(val value: Int) {
        USERNAME(1),
        TASK_NAME(2),
        TASK_DETAIL(3)
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_TYPE = "type"

        fun newInstance(inputType: InputType, title: String): InputTextBottomSheetFragment {
            val fragment =
                InputTextBottomSheetFragment()
            val bundle = Bundle()
            bundle.putString(ARG_TITLE, title)
            bundle.putInt(ARG_TYPE, inputType.value)
            fragment.arguments = bundle
            return fragment
        }
    }
}