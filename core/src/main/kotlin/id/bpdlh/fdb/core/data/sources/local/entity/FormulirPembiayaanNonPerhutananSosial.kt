package id.bpdlh.fdb.core.data.sources.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.widget.FdbBadge
import kotlinx.parcelize.Parcelize

@Entity(tableName = "formulir_pembiayaan_non_perhutanan_sosial")
@Parcelize
data class FormulirPembiayaanNonPerhutananSosial(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var jenis_layanan: String = "",
    var nominal_permohonan: Long = 0L,
    var jangka_waktu: Int = 0,
    var skema_pembiayaan: String = "",
    var rencana_jaminan: String = "",
    var jenis_usaha: String = "",
    var komoditas_hhbk: String = "",
    var komoditas_usaha: String = "",
    var jenis_kegiatan: String = "",
    var lama_usaha: Int = 0,
    var satuan_lama_usaha: String = "",
    var produktivitas: String = "",
    var sumber_bahan_baku: String = "",
    var kapasitas_usaha: String = "",
    var harga: Long = 0L,
    var luas_lahan: Double = 0.0,
    var omzet: Long = 0L,
    var hpp: Long = 0L,
    var pendapatan_bersih: Long = 0L,
    var tujuan_pemasaran: String = "",
    var tipe_usaha: Int = Constants.DIKELOLA_ORANG_LAIN,
    var satuan_siklus_usaha: String = "",
    var siklus_usaha: Int = 0,
    var laporan_laba_rugi_path: String = "",
    var kuantitas_komoditas: String = "",
    var rencana_penggunaan: String = "",
    var tujuan_pengajuan: Int = Constants.MODAL_KERJA,
    var penjelasan_tujuan: String = "",
    var data_jaminan_path: String = "",
    var ijin_lahan_path: String = "",
    var surat_tanah_path: String = "",
    var surat_jual_beli_path: String = "",
    var sppt_path: String = "",
    var foto_lahan_path: String = "",
    var dibuat_pada_tanggal: String = "",
    var dibuat_pada_tempat: String = "",
    var status: Int = FdbBadge.DRAFT,
    var laporan_laba_rugi_id: String = "",
    var data_jaminan_id: String = "",
    var ijin_lahan_id: String = "",
    var surat_tanah_id: String = "",
    var surat_jual_beli_id: String = "",
    var sppt_id: String = "",
    var foto_lahan_id: String = "",
    @Ignore
    var businessPartners: List<Mitra> = emptyList()
) : Parcelable