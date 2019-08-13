package ru.nrcki.bioKotlin

import ru.nrcki.bioKotlin.DNA
import ru.nrcki.bioKotlin.Fasta
import kotlinx.serialization.json.Json
import kotlinx.serialization.*
import kotlinx.serialization.Serializable

@Serializable
data class GenomeData(
	val totalLength: Int, val contigCount: Int,
	val GC: Double,	val N50: Int, val L50: Int, 
	val N90: Int, val L90: Int,
	val totalN: Int, val maxLength: Int)

/* Deprecated
fun mapToJSONString(m: Map<String, Any>): String =  m
		.map{(k,v) -> if (v is String) "\"$k\": \"$v\"" else "\"$k\": $v"}
		.joinToString(separator=", \n", prefix="{", postfix="}")
*/

fun genomeStats(fastaRecords: List<Fasta.Record>): GenomeData{

	val totalDNA = fastaRecords.map{it.sequence}.joinToString(separator="")
	val totalLength = totalDNA.length
	val totalGC = DNA().getGCContent(totalDNA).times(100.0)
	val totalN = DNA().getNsCount(totalDNA)
	val contigCount = fastaRecords.size

	val lengths = fastaRecords.map{it.length}.sortedWith(compareBy({-it}))
	var sum = 0
	var n50 = 0
	var n90 = 0
	var l50 = 0
	var l90 = 0

	lengths.forEach(){
		sum += it
		if(n50 == 0 && sum > totalLength/2){
			n50 = it
			l50 = lengths.indexOf(it)
		}
		if(n90 == 0 && sum*1.0 > totalLength*0.9){
			n90 = it
			l90 = lengths.indexOf(it)
		}
	}
	val maxLength = lengths[0]
	val genomeData = GenomeData(totalLength, contigCount,
		totalGC, n50, l50, n90, l90, totalN, maxLength)

	return genomeData
}

fun printGenomeStats(genomeData: GenomeData, format: String){
	if(format=="json"){
		@UseExperimental(kotlinx.serialization.UnstableDefault::class)
		println(Json.stringify(GenomeData.serializer(),genomeData))
	}else{
		println("Total length: ${genomeData.totalLength}")
		println("Contig count: ${genomeData.contigCount}")
		println("Maximum contig length: ${genomeData.maxLength}")
		println("N50: ${genomeData.N50}\nN90: ${genomeData.N90}")
		println("L50: ${genomeData.L50}\nL90: ${genomeData.L90}")
		println("GC, %: ${"%.2f".format(genomeData.GC)}")
		println("Ambiguous bases: ${genomeData.totalN}")
	}
}

fun main(args: Array<String>){
	val longMode = false
	val outMode = "json"
	if (args.size == 0){
		println("Error: missing filename!")
		return
	}
	val filename = args[0]
	val fastaRecords = Fasta().read(filename)
	if(fastaRecords.size == 0){
		System.err.println("Empty file.")
		System.exit(1)
	}
	val genomeData = genomeStats(fastaRecords)
	printGenomeStats(genomeData, outMode)

	if(longMode){
		println("ID\tLength\tGC, %\tN")
		fastaRecords.forEach(){
			println("${it.id}\t${it.length}\t${DNA().getGCContent(it.sequence)}\t${DNA().getNsCount(it.sequence)}")
		}
	}
}