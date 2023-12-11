package id.bpdlh.fdb.core.common.utils

import id.bpdlh.fdb.core.base.BaseViewModel


interface ViewModelOwner<T : BaseViewModel> {
    val viewModel: T
}
