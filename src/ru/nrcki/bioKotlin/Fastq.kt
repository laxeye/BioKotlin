package ru.nrcki.bioKotlin

import java.io.*
import kotlin.math.min
import kotlin.math.round

//To do: write FASTA (concatenate asFasta() represenations of all records)

class Fastq() {

	class Record(header: String, sequence: String, val qual: String): Fasta.Record(header, sequence){

		fun asFastq(): String{
			return "@$header\n$sequence\n+\n$qual"
		}

		fun qualsAsIntList(): List<Int> = qual.map{it.toInt()}
		fun minQual(): Int = qual.map{it.toInt()}.min() ?: 0
		fun meanQual(): Int = qual.map{it.toInt()}.sum().div(length.toDouble()).toInt()

	}

	fun read(filename: String): List<Fastq.Record>{
		val temporaryList = mutableListOf<Fastq.Record>()

		val seqFile = File(filename).bufferedReader().readLines()
		if(seqFile.size < 4){
			throw Exception("Empty FASTQ file $filename")
		}
		var i = 0;
		while (i + 3 < seqFile.size){
			temporaryList.add(Fastq.Record(seqFile[i].drop(1), seqFile[i+1], seqFile[i+3]))
			i += 4
		}

		return temporaryList.toList()
	}

}
