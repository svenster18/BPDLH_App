package id.bpdlh.fdb.features.self_assesment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.databinding.FragmentSelfAssesmentStartBinding

class SelfAssesmentStartFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentSelfAssesmentStartBinding

    private var validate = false
    override fun injectComponent() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelfAssesmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.setOnClickListener {
            if (!validate) {
                validate = true
                binding.badgeFormNotCompleate.visibility = View.VISIBLE
            } else {
                var fr = fragmentManager?.beginTransaction()
                fr?.replace(R.id.self_assesment_content, SelfAssesmentQuestionFragment())
                fr?.commit()
            }
        }
    }
}