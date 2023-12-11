package id.bpdlh.fdb.features.self_assesment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.databinding.FragmentSelfAssesmentFinishBinding

class SelfAssesmentFinishFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentSelfAssesmentFinishBinding

    override fun injectComponent() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelfAssesmentFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}