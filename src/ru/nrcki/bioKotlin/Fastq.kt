package ru.nrcki.bioKotlin

import java.io.*
import kotlin.math.min
import kotlin.math.round

//To do: write FASTA (concatenate asFasta() represenations of all records)

class Fastq() {

	class Record(val header: String, val sequence: String, val qual: String){
		//Take sequence ID 
		val id = header.split(" ")[0]
		//Provide sequence length as FastaRecord().length
		val length = sequence.length

		//To do: format to line width 60 or other
		fun asFasta(): String{
			return ">$header\n$sequence"
		}

		fun qualsAsIntList(): List<Int> = sequence.map{it.toInt()}
		fun minQual(): Int = sequence.map{it.toInt()}.min() ?: 0
		fun meanQual(): Int = sequence.map{it.toInt()}.sum().div(length.toDouble()).toInt()

	}

	fun read(filename: String): List<Fastq.Record>{
		val temporaryList = mutableListOf<Fastq.Record>()

		val seqFile = File(filename).bufferedReader().readLines()
		if(seqFile.size == 0){
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
