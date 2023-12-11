package id.bpdlh.fdb.core.common.utils

sealed class Errors
    (msg: String) : Exception(msg) {
    class OfflineException(msg: String = "Not Connected to Internet") : Errors(msg)
    class FetchException(msg: String) : Errors(msg)
}     