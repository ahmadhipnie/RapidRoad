package com.example.rapidroad.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class AddResponse(

	@field:SerializedName("desa")
	val desa: String? = null,

	@field:SerializedName("kota")
	val kota: String? = null,

	@field:SerializedName("keterangan_ml")
	val keteranganMl: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("id_laporan")
	val idLaporan: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("keterangan_user")
	val keteranganUser: String? = null,

	@field:SerializedName("nama_jalan")
	val namaJalan: String? = null,

	@field:SerializedName("kecamatan")
	val kecamatan: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("path_foto_laporan")
	val pathFotoLaporan: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
