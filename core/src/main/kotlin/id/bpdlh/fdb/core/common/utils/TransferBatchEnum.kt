package id.bpdlh.fdb.core.common.utils

enum class RequestStatusTransfer(val status: String) {
    REQUESTED("requested"),
    ASSIGNED("assigned"),
    REJECTED("rejected"),
    PROCESSED("processed"),
    DELIVERED("delivered")
}

enum class ActionTransfer(val action: String) {
    BAD_TO_GOOD("good-stock-transfer"),
    GOOD_TO_BAD("bad-stock-transfer"),
    RETURN_TO_SUPPLIER("bad-stock-return"),
    BREAKAGE_STOCK("bad-stock-eliminate"),
    REQUEST_WAREHOUSE_TRANSFER("request-warehouse-transfer"),
    WAREHOUSE_TRANSFER("warehouse-transfer")
}