package id.bpdlh.fdb.features.self_assesment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.ConfirmationType
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.domain.entities.SelfAssesmentQuestionEntity
import id.bpdlh.fdb.core.domain.entities.SelfAssesmentQuestionListEntity
import id.bpdlh.fdb.core.domain.usecase.SelfAssesmentUseCase
import id.bpdlh.fdb.databinding.FragmentSelfAssesmentQuestionBinding
import id.bpdlh.fdb.features.fdkns.BaseFormDaftarNonSosialFragment
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import javax.inject.Inject
import id.bpdlh.fdb.core.R as CoreR

class SelfAssesmentQuestionFragment : BaseDaggerFragment(), ViewModelOwner<SelfAssesmentViewModel>,
    HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    override lateinit var viewModel: SelfAssesmentViewModel
    private lateinit var binding: FragmentSelfAssesmentQuestionBinding

    private var currentStep = 0
    private var currentQuestion = 0
    val selfAssesmentQuestionList: MutableList<SelfAssesmentQuestionEntity> = mutableListOf()

    override fun injectComponent() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelfAssesmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = SelfAssesmentViewModel(SelfAssesmentUseCase())
        initUI()
    }

    private fun initUI() {
        with(binding){
            incFooter.btnSelanjutnya.setOnClickListener {
                nextQuestion()
            }
            incFooter.btnKembali.setOnClickListener {
                activity?.finish()
            }
            incFooter.btnSimpanDraft.setOnClickListener {
                onSavDraft()
            }
        }
        viewModel.fetchQuestion(1)
        observe(viewModel.result, ::onQuestionDataLoaded)
    }

    private fun onQuestionDataLoaded(state: ResultState<SelfAssesmentQuestionListEntity>) {
        when (state) {
            is ResultState.Success -> {
                state.data?.let {
                    // Set Data
                    state.data?.data?.let { it1 -> selfAssesmentQuestionList.addAll(it1) }
                    setQuestion()
                }
            }

            else -> {
                // Todo Error
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun nextQuestion() {
        if ((currentStep + 1) == selfAssesmentQuestionList.size) {
            // Check last Question
            if ((currentQuestion + 1) == selfAssesmentQuestionList[currentStep].question.size) {
                // Question Finish
                var fr = fragmentManager?.beginTransaction()
                fr?.replace(R.id.self_assesment_content, SelfAssesmentFinishFragment())
                fr?.commit()
            } else {
                currentQuestion++
                setQuestion()
            }
        } else {
            if ((currentQuestion + 1) == selfAssesmentQuestionList[currentStep].question.size) {
                // Next Type Question
                currentQuestion = 0
                currentStep++
                // Update steper
                binding.viewProgressOne.setBackgroundResource(CoreR.color.primary_main_color_600)
                binding.ivStepOne.setImageResource(CoreR.drawable.ic_step_base)
                binding.ivStepTwo.setImageResource(CoreR.drawable.ic_step_active)
                setQuestion()
            } else {
                currentQuestion++
                setQuestion()
            }
        }
    }

    private fun setQuestion() {
        var step = currentStep + 1
        var question = currentQuestion + 1
        with(binding) {
            tvTitle.text = selfAssesmentQuestionList[currentStep].title
            tvStep.text = "$step dari ${selfAssesmentQuestionList.size}"
            tvStepQuestion.text = "$question dari ${selfAssesmentQuestionList[currentStep].question.size}"
            tvPertanyaan.text = selfAssesmentQuestionList[currentStep].question.get(currentQuestion)?.question
            if (selfAssesmentQuestionList[currentStep].question.get(currentQuestion).type == "1") {
                radioGroupOption.visibility = View.VISIBLE
                tilAnswer.visibility = View.GONE
                radioButtonA.text = selfAssesmentQuestionList[currentStep].question.get(currentQuestion)?.answers?.get(0)?.answer
                radioButtonB.text = selfAssesmentQuestionList[currentStep].question.get(currentQuestion)?.answers?.get(1)?.answer
                radioButtonC.text = selfAssesmentQuestionList[currentStep].question.get(currentQuestion)?.answers?.get(2)?.answer
                radioButtonD.text = selfAssesmentQuestionList[currentStep].question.get(currentQuestion)?.answers?.get(3)?.answer
            } else{
                radioGroupOption.visibility = View.GONE
                tilAnswer.visibility = View.VISIBLE
            }
        }
    }

    fun onSubmit() {
        val generalConfirmationBottomSheet =
            GeneralConfirmationBottomSheet(
                mode = ConfirmationType.SAVE,
                onClickSubmit = {
                    requireActivity().finish()
                }
            )
        generalConfirmationBottomSheet.show(
            activity?.supportFragmentManager!!,
            GeneralConfirmationBottomSheet.TAG
        )
    }

    fun onSavDraft() {
        val generalConfirmationBottomSheet =
            GeneralConfirmationBottomSheet(
                onClickSubmit = {
                    requireActivity().finish()
                }
            )
        generalConfirmationBottomSheet.show(
            activity?.supportFragmentManager!!,
            GeneralConfirmationBottomSheet.TAG
        )
    }


    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}