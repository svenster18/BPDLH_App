package id.bpdlh.fdb.core.domain.mapper

import id.bpdlh.fdb.core.data.entities.LoginSourceApi
import id.bpdlh.fdb.core.data.entities.LogoutSourceApi
import id.bpdlh.fdb.core.data.entities.ProfileSourceApi
import id.bpdlh.fdb.core.data.entities.VerifyForgotPasswordSourceApi
import id.bpdlh.fdb.core.domain.entities.LoginEntity
import id.bpdlh.fdb.core.domain.entities.LogoutEntity
import id.bpdlh.fdb.core.domain.entities.ProfileEntity
import id.bpdlh.fdb.core.domain.entities.UserMetaDataEntity
import id.bpdlh.fdb.core.domain.entities.VerifyForgotPasswordEntity

fun LoginSourceApi?.map(): LoginEntity {
    return LoginEntity(
        accessToken = this?.accessToken ?: "",
        userID = this?.userID ?: "",
        email = this?.email ?: "",
        role = this?.role ?: ""
    )
}

fun LogoutSourceApi?.map(): LogoutEntity {
    return LogoutEntity(logout = this?.logout ?: false)
}

fun ProfileSourceApi?.map(): ProfileEntity {
    return ProfileEntity(
        email = this?.email ?: "",
        phoneNumber = this?.phoneNumber ?: "",
        userMetaData = UserMetaDataEntity(
            this?.userMetaData?.name ?: "",
            this?.userMetaData?.clientId ?: "",
            this?.userMetaData?.warehouseID ?: ""
        ),
        userType = this?.userType ?: "",
        userId = this?.userId ?: "",
        username = this?.username ?: "",
        roles = this?.roles?.filterNotNull() ?: emptyList()
    )
}

fun VerifyForgotPasswordSourceApi?.map(): VerifyForgotPasswordEntity {
    return VerifyForgotPasswordEntity(
        email = this?.email ?: ""
    )
}
