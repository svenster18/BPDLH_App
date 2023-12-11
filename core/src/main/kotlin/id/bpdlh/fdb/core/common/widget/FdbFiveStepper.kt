package id.bpdlh.fdb.core.common.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import id.bpdlh.fdb.core.R
import id.bpdlh.fdb.core.databinding.FdbFiveStepperBinding

class FdbFiveStepper(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {
    private var binding: FdbFiveStepperBinding? = null
    private val typedArray: TypedArray =
        context.obtainStyledAttributes(attributeSet, R.styleable.FdbStepper, 0, 0)

    init {
        binding =
            FdbFiveStepperBinding.inflate(LayoutInflater.from(context), this, true)
        setDescription(
            typedArray.getText(R.styleable.FdbStepper_description)
                ?.toString()
        )
        setStep(typedArray.getInteger(R.styleable.FdbStepper_step, 1))
    }

    fun setDescription(description: String?) {
        binding?.tvDescription?.text = description.orEmpty()
    }

    fun setStep(step: Int) {
        binding?.run {
            tvStep.text = context.getString(R.string.registrasi_text_stepper_step, step.toString())
            when (step) {
                1 -> {
                    ivStepOne.setImageResource(R.drawable.ic_step_active)
                    ivStepTwo.setImageResource(R.drawable.ic_step_disabled)
                    ivStepThree.setImageResource(R.drawable.ic_step_disabled)
                    ivStepFour.setImageResource(R.drawable.ic_step_disabled)
                    ivStepFive.setImageResource(R.drawable.ic_step_disabled)
                    viewProgressOne.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_200
                        )
                    )
                    viewProgressTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_200
                        )
                    )
                    viewProgressThree.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_200
                        )
                    )
                    viewProgressFour.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_200
                        )
                    )
                }

                2 -> {
                    ivStepOne.setImageResource(R.drawable.ic_step_active)
                    ivStepTwo.setImageResource(R.drawable.ic_step_active)
                    ivStepThree.setImageResource(R.drawable.ic_step_disabled)
                    ivStepFour.setImageResource(R.drawable.ic_step_disabled)
                    ivStepFive.setImageResource(R.drawable.ic_step_disabled)
                    viewProgressOne.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_main_color_600
                        )
                    )
                    viewProgressTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_200
                        )
                    )
                    viewProgressThree.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_200
                        )
                    )
                    viewProgressFour.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_200
                        )
                    )
                }

                3 -> {
                    ivStepOne.setImageResource(R.drawable.ic_step_active)
                    ivStepTwo.setImageResource(R.drawable.ic_step_active)
                    ivStepThree.setImageResource(R.drawable.ic_step_active)
                    ivStepFour.setImageResource(R.drawable.ic_step_disabled)
                    ivStepFive.setImageResource(R.drawable.ic_step_disabled)
                    viewProgressOne.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_main_color_600
                        )
                    )
                    viewProgressTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_main_color_600
                        )
                    )
                    viewProgressThree.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_200
                        )
                    )
                    viewProgressFour.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_200
                        )
                    )
                }

                4 -> {
                    ivStepOne.setImageResource(R.drawable.ic_step_active)
                    ivStepTwo.setImageResource(R.drawable.ic_step_active)
                    ivStepThree.setImageResource(R.drawable.ic_step_active)
                    ivStepFour.setImageResource(R.drawable.ic_step_active)
                    ivStepFive.setImageResource(R.drawable.ic_step_disabled)
                    viewProgressOne.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_main_color_600
                        )
                    )
                    viewProgressTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_main_color_600
                        )
                    )
                    viewProgressThree.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_main_color_600
                        )
                    )
                    viewProgressFour.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_200
                        )
                    )
                }

                5 -> {
                    ivStepOne.setImageResource(R.drawable.ic_step_active)
                    ivStepTwo.setImageResource(R.drawable.ic_step_active)
                    ivStepThree.setImageResource(R.drawable.ic_step_active)
                    ivStepFour.setImageResource(R.drawable.ic_step_active)
                    ivStepFive.setImageResource(R.drawable.ic_step_active)
                    viewProgressOne.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_main_color_600
                        )
                    )
                    viewProgressTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_main_color_600
                        )
                    )
                    viewProgressThree.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_main_color_600
                        )
                    )
                    viewProgressFour.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_main_color_600
                        )
                    )
                }
            }
        }
    }
}