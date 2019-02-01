package ru.nrcki.bioKotlin

import java.io.*
import kotlin.math.*
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
	.map{if ((it == 'C') || (it == 'G')) 1 else 0}.sum().times(100.0).div(dna.length)
fun getNs(dna: String): Int = dna.toUpperCase().map{if (it == 'N') 1 else 0}.sum()

fun mapToJSONString(m: Map<String, Any>): String{
	val oList = mutableListOf<String>()
	var oItem: String
		for((k,v) in m){
			oItem = if (v is String) "\"$k\": \"$v\"" else "\"$k\": $v"
			oList.add(oItem)
		}
	return oList.joinToString(separator=", \n", prefix="{", postfix="}")
}

fun main(args: Array<String>){
	val longMode = false
	val outMode = "text"
	if (args.size == 0){
		println("Error: missing filename!")
		return
	}
	val filename = args[0]

	val seqList = readFasta(filename)
	
	//val totalLength = seqList.map{it.length}.sum()
	val totalDNA = seqList.map{it.sequence}.joinToString(separator="")
	val totalLength = totalDNA.length
	val totalGC = getGC(totalDNA)
	val totalN = getNs(totalDNA)
	val contigCount = seqList.size
	val lengths = seqList.map{it.length}.sortedWith(compareBy({-it}))
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

	if(outMode=="json"){
		val genomeDataMap = mapOf("Filename" to filename, "Total_length" to totalLength, 
			"Contig_count" to contigCount,
			"Total_GC" to totalGC, "N50" to n50, "N90" to n90, "Ambiguous_bases" to totalN, 
			"Max_length" to lengths[0])

		println(mapToJSONString(genomeDataMap))
	}else{
		println("Total length: $totalLength\nContig count: $contigCount")
		println("Maximum contig length: ${lengths[0]}")
		println("GC, %: ${"%.2f".format(totalGC)}\nAmbiguous bases: $totalN")
		println("N50: $n50\nN90: $n90")
		println("L50: $l50\nN90: $l90")
	}

/*	val genomeData = GenomeData(fileName, totalLength, contigCount,
		totalGC, n50, n90, totalN, lengths[0])
	println(genomeData.toString())
*/

/*
	val gson = GsonBuilder().setPrettyPrinting().create()
	val jsonOut: String = gson.toJson(genomeData)
	println(jsonOut)
*/
	if(longMode){
		println("ID\tLength\tGC, %\tN")
		seqList.forEach(){
			println("${it.id}\t${it.length}\t${getGC(it.sequence)}\t${getNs(it.sequence)}")
		}
	}
}