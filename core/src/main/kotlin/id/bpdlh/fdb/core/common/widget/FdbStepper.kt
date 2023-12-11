package id.bpdlh.fdb.core.common.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import id.bpdlh.fdb.core.R
import id.bpdlh.fdb.core.databinding.FdbStepperBinding

class FdbStepper(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {
    private var binding: FdbStepperBinding? = null
    private val typedArray: TypedArray =
        context.obtainStyledAttributes(attributeSet, R.styleable.FdbStepper, 0, 0)

    init {
        binding = FdbStepperBinding.inflate(LayoutInflater.from(context), this, true)
        setDescription(typedArray.getText(R.styleable.FdbStepper_description)?.toString())
        setStep(typedArray.getInteger(R.styleable.FdbStepper_step, 1))
    }

    fun setDescription(description: String?) {
        binding?.tvDescription?.text = description.orEmpty()
    }

    fun setStep(step: Int) {
        binding?.run {
            tvStep.text = context.getString(R.string.text_stepper_step, step.toString())
            when (step) {
                1 -> {
                    ivStepOne.setImageResource(R.drawable.ic_step_active)
                    ivStepTwo.setImageResource(R.drawable.ic_step_disabled)
                    ivStepThree.setImageResource(R.drawable.ic_step_disabled)
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
                }

                2 -> {
                    ivStepOne.setImageResource(R.drawable.ic_step_base)
                    ivStepTwo.setImageResource(R.drawable.ic_step_active)
                    ivStepThree.setImageResource(R.drawable.ic_step_disabled)
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
                }

                else -> {
                    ivStepOne.setImageResource(R.drawable.ic_step_base)
                    ivStepTwo.setImageResource(R.drawable.ic_step_base)
                    ivStepThree.setImageResource(R.drawable.ic_step_active)
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
                }
            }

        }
    }
}