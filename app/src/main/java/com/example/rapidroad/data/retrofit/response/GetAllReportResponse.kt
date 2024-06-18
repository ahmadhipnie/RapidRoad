package com.example.rapidroad.data.retrofit.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetAllReportResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)
@Parcelize
data class DataItem(
	@field:SerializedName("desa")
	val desa: String,

	@field:SerializedName("keterangan")
	val keterangan: String,

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("id_laporan")
	val idLaporan: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("nama_jalan")
	val namaJalan: String,

	@field:SerializedName("kecamatan")
	val kecamatan: String,

	@field:SerializedName("tanggal")
	val tanggal: String,

	@field:SerializedName("nama_user")
	val namaUser: String,

	@field:SerializedName("path_foto_laporan")
	val pathFotoLaporan: String,

	@field:SerializedName("longitude")
	val longitude: Double,

	@field:SerializedName("status")
	val status: String
) : Parcelable

