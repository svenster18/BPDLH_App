package id.bpdlh.fdb.features.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.toast
import id.bpdlh.fdb.databinding.ActivityChangePasswordBinding
import javax.inject.Inject

class ChangePasswordActivity : BaseDaggerActivity(),
    TextWatcherTextChange, HasAndroidInjector {
    private var strKataSandiBaru = ""

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private val binding by lazy { ActivityChangePasswordBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initActionBar()
        initUI()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.incAppbar.toolbar)
        supportActionBar?.title = resources.getString(R.string.ubah_kata_sandi)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initUI() {
        binding.apply {
            textILKataSandiSekarang.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@ChangePasswordActivity)
            }
            textILKataSandiBaru.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@ChangePasswordActivity)
            }
            textILKataSandiBaru2.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@ChangePasswordActivity)
            }
            btnUbah.setOnClickListener {
                validateInput()
            }
        }
    }

    private fun validateInput() {
        binding.apply {
            val strKataSandiSekarang = textILKataSandiSekarang.editText?.text.toString()
            val kataSandiSekarangError = when {
                strKataSandiSekarang.isEmpty() -> getString(id.bpdlh.fdb.core.R.string.label_empty)
                strKataSandiSekarang.length < 8 -> getString(R.string.label_pass_less_then_eight)
                else -> null
            }
            binding.textILKataSandiSekarang.error = kataSandiSekarangError
            if (kataSandiSekarangError != null) return
            val strKataSandiBaru = textILKataSandiBaru.editText?.text.toString()
            val kataSandiBaruError = when {
                strKataSandiBaru.isEmpty() -> getString(id.bpdlh.fdb.core.R.string.label_empty)
                strKataSandiBaru.length < 8 -> getString(R.string.label_pass_less_then_eight)
                else -> null
            }
            binding.textILKataSandiBaru.error = kataSandiBaruError
            if (kataSandiBaruError != null) return
            val strKataSandiBaru2 = textILKataSandiBaru2.editText?.text.toString()
            val kataSandiBaru2Error = when {
                strKataSandiBaru2.isEmpty() -> getString(id.bpdlh.fdb.core.R.string.label_empty)
                strKataSandiBaru2.length < 8 -> getString(R.string.label_pass_less_then_eight)
                strKataSandiBaru2 != strKataSandiBaru -> getString(R.string.label_pass_not_same)
                else -> null
            }
            binding.textILKataSandiBaru2.error = kataSandiBaru2Error
            if (kataSandiBaru2Error != null) return
            toast(R.string.berhasil_edit)
            finish()
        }

    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (editText) {
            binding.textILKataSandiSekarang.editText -> {
                val strKataSandiSekarang = editText?.text.toString()
                binding.textILKataSandiSekarang.error = when {
                    strKataSandiSekarang.isEmpty() -> getString(id.bpdlh.fdb.core.R.string.label_empty)
                    strKataSandiSekarang.length < 8 -> getString(R.string.label_pass_less_then_eight)
                    else -> null
                }
            }

            binding.textILKataSandiBaru.editText -> {
                strKataSandiBaru = editText?.text.toString()
                binding.textILKataSandiBaru.error = when {
                    strKataSandiBaru.isEmpty() -> getString(id.bpdlh.fdb.core.R.string.label_empty)
                    strKataSandiBaru.length < 8 -> getString(R.string.label_pass_less_then_eight)
                    else -> null
                }
            }

            binding.textILKataSandiBaru2.editText -> {
                val strKataSandiBaru2 = editText?.text.toString()
                binding.textILKataSandiBaru2.error = when {
                    strKataSandiBaru2.isEmpty() -> getString(id.bpdlh.fdb.core.R.string.label_empty)
                    strKataSandiBaru2.length < 8 -> getString(R.string.label_pass_less_then_eight)
                    strKataSandiBaru2 != strKataSandiBaru -> getString(R.string.label_pass_not_same)
                    else -> null
                }
            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ChangePasswordActivity::class.java)
            context.startActivity(intent)
        }
    }
}