package ru.nrcki.bioKotlin

import java.io.*
import ru.nrcki.bioKotlin.*
/*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
*/
data class GenomeData(val fileName: String, 
	val totalLength: Int, val contigCount: Int,
	val GC: Double,	val n50: Int, val n90: Int, 
	val totalN: Int, val maxLength: Int)

//GC content in percent
fun getGC(dna: String): Double = dna.toUpperCase()
	.count({(it == 'C') || (it == 'G')}).times(100.0).div(dna.length)
fun getNs(dna: String): Int = dna.toUpperCase().count({it == 'N'})

fun mapToJSONString(m: Map<String, Any>): String =  m
		.map{(k,v) -> if (v is String) "\"$k\": \"$v\"" else "\"$k\": $v"}
		.joinToString(separator=", \n", prefix="{", postfix="}")

fun genomeStats(frList :List<Fasta.Record>): Map<String,Any>{

	val totalDNA = frList.map{it.sequence}.joinToString(separator="")
	val totalLength = totalDNA.length
	val totalGC = getGC(totalDNA)
	val totalN = getNs(totalDNA)
	val contigCount = frList.size

	val lengths = frList.map{it.length}.sortedWith(compareBy({-it}))
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

	val genomeDataMap = mapOf("totalLength" to totalLength, 
			"contigCount" to contigCount,
			"totalGC" to totalGC, "n50" to n50, "n90" to n90,
			"l50" to l50, "l90" to l90, "totalN" to totalN, 
			"maxLength" to lengths[0])
	return genomeDataMap
}

fun printGenomeStats(genomeDataMap: Map<String, Any>,f: String){
	if(f=="json"){
		println(mapToJSONString(genomeDataMap))
	}else{
		println("Total length: ${genomeDataMap.get("totalLength")}")
		println("Contig count: ${genomeDataMap.get("contigCount")}")
		println("Maximum contig length: ${genomeDataMap.get("maxLength")}")
		println("N50: ${genomeDataMap.get("n50")}\nN90: ${genomeDataMap.get("n90")}")
		println("L50: ${genomeDataMap.get("l50")}\nL90: ${genomeDataMap.get("l90")}")
		println("GC, %: ${"%.2f".format(genomeDataMap.get("totalGC"))}")
		println("Ambiguous bases: ${genomeDataMap.get("totalN")}")
	}
	/*
	val gson = GsonBuilder().setPrettyPrinting().create()
	val jsonOut: String = gson.toJson(genomeData)
	println(jsonOut)
	*/
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
	val genomeDataMap = genomeStats(fastaRecords)
	printGenomeStats(genomeDataMap,outMode)

	if(longMode){
		println("ID\tLength\tGC, %\tN")
		fastaRecords.forEach(){
			println("${it.id}\t${it.length}\t${getGC(it.sequence)}\t${getNs(it.sequence)}")
		}
	}
}