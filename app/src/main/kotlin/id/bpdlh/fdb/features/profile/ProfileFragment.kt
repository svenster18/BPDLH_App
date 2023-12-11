package id.bpdlh.fdb.features.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.ConfirmationType
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.parcelable
import id.bpdlh.fdb.core.common.utils.toast
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.core.domain.entities.LogoutEntity
import id.bpdlh.fdb.databinding.FragmentProfileBinding
import id.bpdlh.fdb.features.login.LoginActivity
import id.bpdlh.fdb.features.profile.edit_account.EditAccountActivity
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import javax.inject.Inject

class ProfileFragment : BaseDaggerFragment(), ViewModelOwner<ProfileViewModel>, HasAndroidInjector {
    @Inject
    override lateinit var viewModel: ProfileViewModel

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private lateinit var binding: FragmentProfileBinding
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var preferences: LocalPreferences
    private var dataGroupProfile: GroupProfileEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dataGroupProfile = it.parcelable(DATA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.resultLogout, ::observeLogout)
        initUI()
    }

    private fun observeLogout(state: ResultState<LogoutEntity>) {
        when (state) {
            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            is ResultState.Success<LogoutEntity> -> {
                preferences.clearSharedPreference()
                val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                requireActivity().finish()
            }

            else -> requireContext().toast(state.message.toString())
        }
    }

    private fun initUI() {
        progressDialog = CustomProgressDialog(requireContext())
        preferences = LocalPreferences(requireContext())
        binding.llEditAkun.setOnClickListener { goToEditAkun() }
        binding.llChangePassword.setOnClickListener { goToChangePassword() }
        binding.llKeluar.setOnClickListener {
            val generalConfirmationBottomSheet =
                GeneralConfirmationBottomSheet(ConfirmationType.LOGOUT) {
                    viewModel.doLogout()
                    requireActivity().finishAffinity()
                }
            generalConfirmationBottomSheet.show(
                parentFragmentManager,
                GeneralConfirmationBottomSheet.TAG
            )
        }
    }

    private fun goToEditAkun() {
        dataGroupProfile?.let { EditAccountActivity.start(requireContext(), it) }
    }

    private fun goToChangePassword() {
        ChangePasswordActivity.start(requireContext())
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
        const val DATA = "data"
        fun newInstance(data: GroupProfileEntity?) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putParcelable(DATA, data)
            }
        }
    }
}