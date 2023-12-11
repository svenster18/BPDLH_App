package id.bpdlh.fdb.core.common.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IntDef
import id.bpdlh.fdb.core.R
import id.bpdlh.fdb.core.databinding.FdbTickerBinding

open class FdbTicker(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {
    private var binding: FdbTickerBinding? = null

    private val typedArray: TypedArray =
        context.obtainStyledAttributes(attributeSet, R.styleable.FdbTicker, 0, 0)

    companion object {
        const val INFO = 1
        const val SUCCESS = 2
        const val WARNING = 3
        const val ERROR = 4
    }

    init {
        binding = FdbTickerBinding.inflate(LayoutInflater.from(context), this, true)
        setTitle(typedArray.getText(R.styleable.FdbTicker_tickerTitle)?.toString())
        setMessage(typedArray.getText(R.styleable.FdbTicker_tickerMessage)?.toString())
        setTickerType(typedArray.getInteger(R.styleable.FdbTicker_tickerType, INFO))
        setDrawable(typedArray.getResourceId(R.styleable.FdbTicker_tickerDrawable, 0))

    }

    fun setTitle(title: String?) {
        title?.let {
            binding?.tvTitle?.text = it
            binding?.tvTitle?.visibility = View.VISIBLE
        } ?: run {
            binding?.tvTitle?.visibility = View.VISIBLE
        }
    }

    fun setMessage(message: String?) {
        message?.let {
            binding?.tvMessage?.text = message
            binding?.tvMessage?.visibility = View.VISIBLE
        } ?: run {
            binding?.tvMessage?.visibility = View.GONE
        }
    }

    fun setDrawable(resource: Int) {
        if (resource != 0) {
            binding?.ivIcon?.setImageResource(resource)
            binding?.ivIcon?.visibility = View.VISIBLE
        } else {
            binding?.ivIcon?.visibility = View.GONE
        }
    }

    fun setTickerType(@TickerType type: Int) {
        val backgroundDrawable = when (type) {
            INFO -> {
                binding?.ivIcon?.setImageResource(R.drawable.ic_calendar)
                binding?.ivIcon?.visibility = View.VISIBLE
                R.drawable.ticker_bg_info
            }

            WARNING -> {
                binding?.ivIcon?.setImageResource(R.drawable.ic_alert_triangle)
                binding?.ivIcon?.visibility = View.VISIBLE
                R.drawable.ticker_bg_warning
            }

            else -> {
                binding?.ivIcon?.visibility = View.GONE
                R.drawable.ticker_bg_info
            }
        }
        binding?.clBackground?.setBackgroundResource(backgroundDrawable)
    }

    @IntDef(INFO, SUCCESS, WARNING, ERROR)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TickerType
}