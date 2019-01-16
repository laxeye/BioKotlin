package ru.nrcki.BioKotlin.GenomeStats

import java.io.*
import kotlin.math.*
/*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
*/
data class GenomeData(val fileName: String, 
	val totalLength: Int, val contigCount: Int,
	val GC: Double,	val n50: Int, val n90: Int, 
	val totalN: Int, val maxLength: Int)

fun GC(dna: String): Double = 100.0 * dna
	.map{if ((it == 'C') || (it == 'G')) 1 else 0}.sum()

class Sequence(val id: String, val dna: String){
	val length = dna.length
	val gc = GC(dna.toUpperCase()).times(100).div(length).roundToLong().div(100.0)
	val N = dna.toUpperCase().map{if (it == 'N') 1 else 0}.sum()
}

fun mapToJSONString(m: Map<String, Any>): String{
	val oList = mutableListOf<String>()
	var oItem: String
		for((k,v) in m){
			oItem = if (v is String) "\"$k\": \"$v\"" else "\"$k\": $v"
			oList.add(oItem)
		}
	return oList.joinToString(separator=", \n", prefix="{", postfix="}")
}

fun checkID(str: String): Boolean = ((str.first() == '>') && (str.length > 1))

fun main(args: Array<String>){
	val longMode = false
	val outMode = "json"
	val seqList = mutableListOf<Sequence>()
	if (args.size == 0){
		println("Error: missing filename!")
		return
	}
	val fileName = args[0]
	val seqFile = File(fileName).bufferedReader().readLines()
	if(seqFile.size == 0){
		println("Error: empty file")
		return
	}

	if(!checkID(seqFile[0])){
		println("Error: bad sequence file!")
		return
	}

	var seq: String
	var id: String = ""

	var lineArray = mutableListOf<String>()
	for (line in seqFile){
		if(line.length > 0){
			if (line[0] == '>'){
				seq = lineArray.joinToString(separator="")
				lineArray = mutableListOf<String>()
				if (seq.length > 0){
					seqList.add(Sequence(id, seq))
				}
				id = line.drop(1)
			}else{
				lineArray.add(line)
			}
		}
	}

	seqList.add(Sequence(id, lineArray.joinToString(separator="")))
	
	val totalLength = seqList.map{it.length}.sum()
	val totalDNA = seqList.map{it.dna}.joinToString(separator="").toUpperCase()
	val totalGC = GC(totalDNA).times(100).div(totalLength).roundToLong().div(100.0)
	val totalN = seqList.map{it.N}.sum()
	val contigCount = seqList.size
	val lengths = seqList.map{it.length}.sortedWith(compareBy({-it}))
	var sum = 0
	var n50 = 0
	var n90 = 0
	lengths.forEach(){
		sum += it
		if(n50 == 0 && sum > totalLength/2){
			n50 = it
		}
		if(n90 == 0 && sum*1.0 > totalLength*0.9){
			n90 = it
		}
	}

	if(outMode=="json"){
		val genomeDataMap = mapOf("Filename" to fileName, "Total_length" to totalLength, 
			"Contig_count" to contigCount,
			"Total_GC" to totalGC, "N50" to n50, "N90" to n90, "Ambiguous_bases" to totalN, 
			"Max_length" to lengths[0])

		println(mapToJSONString(genomeDataMap))
	}else{
		println("Total length: $totalLength\nContig count: $contigCount")
		println("Maximum contig length: ${lengths[0]}")
		println("GC, %: $totalGC\nAmbiguous bases: $totalN")
		println("N50: $n50\nN90: $n90")			
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
			println("${it.id}\t${it.length}\t${it.gc}\t${it.N}")
		}
	}
}