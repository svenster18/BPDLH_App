package id.bpdlh.fdb.core.data.entities

import com.google.gson.annotations.SerializedName


data class GetFileSourceApi(

	@field:SerializedName("bucket")
	val bucket: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("size")
	val size: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("etag")
	val etag: String,

	@field:SerializedName("isAttached")
	val isAttached: Boolean,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("acl")
	val acl: String,

	@field:SerializedName("mimeType")
	val mimeType: String,

	@field:SerializedName("key")
	val key: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
