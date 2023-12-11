package id.bpdlh.fdb.core.common.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import id.bpdlh.fdb.core.R
import id.bpdlh.fdb.core.databinding.FdbBadgeBinding

/**
 * Created by hahn on 05/09/23.
 * Project: BPDLH App
 */
class FdbBadge(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {
    private var binding: FdbBadgeBinding? = null

    private val typedArray: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.FdbBadge, 0, 0)
    companion object {
        const val DRAFT = 0
        const val SUCCESS = 1
        const val WARNING = 2
        const val ERROR = 3
        const val WAITING = 4
        const val ON_PROGRESS = 5
        const val ON_VERIFY = 6
        const val VERIFIED = 7
        const val ON_REVIEW = 8
        const val APPROVED = 9
        const val REJECTED = 10
        const val TUNDA_TEBANG = 11
        const val REFINANCING_KEHUTANAN = 12
        const val HHBK = 13
        const val KNK = 14

    }

    init {
        binding = FdbBadgeBinding.inflate(LayoutInflater.from(context), this, true)
        setMessage(typedArray.getText(R.styleable.FdbBadge_badgeMessage)?.toString())
        setDrawable(typedArray.getResourceId(R.styleable.FdbBadge_badgeDrawable, 0))
        setBadgeType(typedArray.getInteger(R.styleable.FdbBadge_badgeType, SUCCESS))
    }

    fun setMessage(message: String?) {
        message?.let {
            binding?.tvBadge?.text = it
        }
    }
    /**
     * jika ingin menampilkan custom drawable, panggil fungsi ini setelah [setBadgeType]
     */

    fun setDrawable(resource: Int) {
        if (resource != 0) {
            binding?.ivBadge?.setImageResource(resource)
            binding?.ivBadge?.visibility = View.VISIBLE
        } else {
            binding?.ivBadge?.visibility = View.GONE
        }
    }

    fun setBadgeType(@BadgeType type: Int) {
        binding?.run {
            when(type) {
                DRAFT -> {
                    setDrawable(R.drawable.ic_pencil_line)
                    tvBadge.setTextColor(ContextCompat.getColor(context, R.color.gray_700))
                    clBackground.setBackgroundResource(R.drawable.tab_chip_waiting)
                }

                SUCCESS, VERIFIED, APPROVED -> {
                    setDrawable(R.drawable.ic_check)
                    tvBadge.setTextColor(ContextCompat.getColor(context, R.color.success_700))
                    clBackground.setBackgroundResource(R.drawable.tab_chip_success)
                }

                WARNING -> {
                    setDrawable(R.drawable.ic_error_warning_line)
                    tvBadge.setTextColor(ContextCompat.getColor(context, R.color.warning_700))
                    clBackground.setBackgroundResource(R.drawable.tab_chip_warning)
                }

                ERROR, REJECTED -> {
                    setDrawable(R.drawable.ic_close_line)
                    tvBadge.setTextColor(ContextCompat.getColor(context, R.color.error_700))
                    clBackground.setBackgroundResource(R.drawable.tab_chip_error)
                }

                WAITING -> {
                    setDrawable(R.drawable.ic_time_line)
                    tvBadge.setTextColor(ContextCompat.getColor(context, R.color.gray_700))
                    clBackground.setBackgroundResource(R.drawable.tab_chip_waiting)
                }

                ON_PROGRESS, ON_VERIFY, ON_REVIEW -> {
                    setDrawable(R.drawable.ic_time_line_orange)
                    tvBadge.setTextColor(ContextCompat.getColor(context, R.color.warning_700))
                    clBackground.setBackgroundResource(R.drawable.tab_chip_warning)
                }

                TUNDA_TEBANG -> {
                    setDrawable(0)
                    tvBadge.setTextColor(ContextCompat.getColor(context, R.color.blue_light_700))
                    clBackground.setBackgroundResource(R.drawable.badge_blue_light)
                }

                REFINANCING_KEHUTANAN -> {
                    setDrawable(0)
                    tvBadge.setTextColor(ContextCompat.getColor(context, R.color.rose_700))
                    clBackground.setBackgroundResource(R.drawable.badge_rose)
                }

                HHBK -> {
                    setDrawable(0)
                    tvBadge.setTextColor(ContextCompat.getColor(context, R.color.purple_700))
                    clBackground.setBackgroundResource(R.drawable.badge_purple)
                }

                KNK -> {
                    setDrawable(0)
                    tvBadge.setTextColor(ContextCompat.getColor(context, R.color.purple_700))
                    clBackground.setBackgroundResource(R.drawable.badge_purple)
                }

                else -> {
                    setDrawable(0) //hide icon?
                    tvBadge.setTextColor(ContextCompat.getColor(context, R.color.success_700))
                    clBackground.setBackgroundResource(R.drawable.tab_chip_success)
                }
            }

        }
    }

    @IntDef(
        SUCCESS,
        WARNING,
        ERROR,
        WAITING,
        ON_PROGRESS,
        DRAFT,
        ON_VERIFY,
        VERIFIED,
        ON_REVIEW,
        APPROVED,
        REJECTED,
        TUNDA_TEBANG,
        REFINANCING_KEHUTANAN,
        HHBK,
        KNK
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class BadgeType
}