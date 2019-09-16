package ru.nrcki.biokotlin

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.nrcki.biokotlin.io.Fasta
import kotlin.system.exitProcess

class GenomeStats(){

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

	fun getStats(fastaRecords: List<BioSequence>): GenomeData{

		val totalDNA = fastaRecords.map{it.sequence}.joinToString(separator="")
		val totalLength = totalDNA.length
		val totalGC = totalDNA.getGCContent().times(100.0)
		val totalN = totalDNA.getNsCount()
		val contigCount = fastaRecords.size

		val lengths = fastaRecords.map{it.length}.sortedWith(compareBy {-it})
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
		val maxLength = lengths.get(0)

		return GenomeData(totalLength, contigCount,
			totalGC, n50, l50, n90, l90, totalN, maxLength)
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

	fun genomeStats(filename: String, longMode: Boolean = false, outMode: String = "json"){
		val fastaRecords = Fasta().read(filename)
		if(fastaRecords.size == 0){
			System.err.println("Empty file.")
			exitProcess(1)
		}
		val genomeData = getStats(fastaRecords)
		printGenomeStats(genomeData, outMode)

		if(longMode){
			println("ID\tLength\tGC, %\tN")
			fastaRecords.forEach(){
				println("${it.id}\t${it.length}\t${it.sequence.getGCContent()}\t${it.sequence.getNsCount()}")
			}
		}
	}
}