package ru.nrcki.bioKotlin

import java.io.*
import java.util.Scanner
import kotlin.math.min
import kotlin.math.round

//To do: write FASTA (concatenate asFasta() represenations of all records)

class Fastq() {

	class Record(header: String, sequence: String, val qual: String): Fasta.Record(header, sequence){

		fun asFastq(): String{
			return "@$header\n$sequence\n+\n$qual\n"
		}

		fun qualsAsIntList(): List<Int> = qual.map{it.toInt()}
		fun minQual(): Int = qual.map{it.toInt()}.min() ?: 0
		fun meanQual(): Int = qual.map{it.toInt()}.sum().div(length.toDouble()).toInt()

	}
	/* Legacy fastq file reader */
	/*
	fun readBR(filename: String): List<Fastq.Record>{
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
	*/

	fun read(filename: String): List<Fastq.Record>{
		val temporaryList = mutableListOf<Fastq.Record>()

		val sc = Scanner(FileInputStream(filename))

		var tmpHead = ""
		var tmpSeq = ""
		var i = 0

		while(sc.hasNextLine()){
			i++
			val line = sc.nextLine()
			val field = i.rem(4)
			when(field){
				1 -> tmpHead = line.drop(1)
				2 -> tmpSeq = line
				0 -> temporaryList.add(Fastq.Record(tmpHead, tmpSeq, line))
			}
		}

		return temporaryList.toList()
	}

	fun readBR(filename: String): MutableList<Fastq.Record>{
		val temporaryList = mutableListOf<Fastq.Record>()

		//val seqReader = FileInputStream(filename).bufferedReader()
		val seqReader = File(filename).bufferedReader()

		var tmpHead = ""
		var tmpSeq = ""
		var i = 0
		var line = seqReader.readLine()
		
		while(line != null){
			i++
			val field = i.rem(4)

			if(field == 1) tmpHead = line.drop(1)
			if(field == 2) tmpSeq = line
			if(field == 0){
				temporaryList.add(Fastq.Record(tmpHead, tmpSeq, line))
			}
			line = seqReader.readLine()
		}
		seqReader.close()
		return temporaryList
	}

}
