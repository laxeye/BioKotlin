package ru.nrcki.bioKotlin

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import java.io.*

// Classes for serialization

@Serializable
data class BlastOut(val BlastOutput2: List<BlastResult>)

@Serializable
data class BlastResult(val report: BlastReport)

@Serializable
data class BlastReport(val program: String, val version: String,
	val reference: String, val search_target: Target,
	val params: Params, val results: Results)

@Serializable
data class Results(val bl2seq: List<Bl2seq>)

@Serializable
data class Bl2seq(val query_id: String,
val query_title: String,
val query_len: Int,
@Optional val query_masking: List<QueryMasking>? = null,
val hits: List<Hit>,
val stat: Stat)

@Serializable
data class QueryMasking(val from: Int, val to: Int)

@Serializable
data class Target(val subjects: List<String>)

@Serializable
data class Params(val expect: Float,
        val sc_match: Int,
        val sc_mismatch: Int,
        val gap_open: Int,
        val gap_extend: Int,
        val filter: String)

@Serializable
data class Description(val id: String, val title: String)

@Serializable
data class Hsp(val num: Int, val bit_score: Float,
val score: Int, val evalue: Double,
val identity: Int, val query_from: Int,
val query_to: Int, val query_strand: String,
val hit_from: Int, val hit_to: Int,
val hit_strand: String, val align_len: Int,
val gaps: Int, val qseq: String,
val hseq: String, val midline: String)

@Serializable
data class Hit(val num: Int, val description: List<Description>, val len: Int,
  val hsps: List<Hsp>)

@Serializable
data class Stat(val hsp_len: Int, 
  val eff_space: Int, val kappa: Float,
  val lambda: Float, val entropy: Float)

//End

fun parseNcbiBlastJson(filename: String): BlastOut{
	val jsonString = File(filename).readLines().joinToString(" ")
	val obj = Json(JsonConfiguration.Stable).parse(BlastOut.serializer(), jsonString)
	return(obj)
}

fun blastOutput2AsListofReports(obj: BlastOut): List<BlastReport> = obj.BlastOutput2.map{it.report}
