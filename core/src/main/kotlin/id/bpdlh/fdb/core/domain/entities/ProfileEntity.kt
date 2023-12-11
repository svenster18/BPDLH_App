package id.bpdlh.fdb.core.domain.entities

data class ProfileEntity(
    val email: String,
    val phoneNumber: String,
    val userMetaData: UserMetaDataEntity,
    val userType: String,
    val userId: String,
    val username: String,
    val roles: List<String>
)

data class UserMetaDataEntity(
    val name: String,
    val clientId: String,
    val warehouseID: String
)