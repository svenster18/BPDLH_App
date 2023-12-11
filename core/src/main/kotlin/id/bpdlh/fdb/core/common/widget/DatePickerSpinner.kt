package id.bpdlh.fdb.core.common.widget


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.getCustomFormatDate
import id.bpdlh.fdb.core.databinding.LayoutDatePickerSpinnerBinding

/**
 * A simple [Fragment] subclass.
 */
class DatePickerSpinner(private val input: TextView, private val formatDate: String = Constants.NEW_DATE_FORMAT) : DialogFragment() {
    private lateinit var binding: LayoutDatePickerSpinnerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = LayoutDatePickerSpinnerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnPositive.setOnClickListener {
                val date = datePicker.getCustomFormatDate(formatDate)
                input.text = date
                dismiss()
            }
            btnNegative.setOnClickListener {
                dismiss()
            }
        }
    }
}
