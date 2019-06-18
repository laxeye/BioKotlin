package ru.nrcki.bioKotlin

import java.io.FileInputStream
import java.io.BufferedReader
import java.util.Scanner
import kotlin.math.min
import kotlin.math.round
import java.util.zip.GZIPInputStream
import org.apache.commons.compress.compressors.bzip2.*

class Fastq() {

	class Record(header: String, sequence: String, val qual: String): Fasta.Record(header, sequence){

		fun asFastq(): String = "@$header\n$sequence\n+\n$qual\n"
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

	fun asMultiFastq(recList: List<Fastq.Record>): String{
		return recList.map{it.asFastq()}.joinToString(separator="\n",prefix="",postfix="")
	}

	fun readSC(sc: Scanner): List<Fastq.Record>{
		val temporaryList = mutableListOf<Fastq.Record>()

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
		sc.close()
		return temporaryList.toList()
	}

	fun readBR(br: BufferedReader): MutableList<Fastq.Record>{
		val temporaryList = mutableListOf<Fastq.Record>()

		var tmpHead = ""
		var tmpSeq = ""
		var i = 0
		var line = br.readLine()
		
		while(line != null){
			i++
			val field = i.rem(4)
			when(field){
				1 -> tmpHead = line.drop(1)
				2 -> tmpSeq = line
				0 -> temporaryList.add(Fastq.Record(tmpHead, tmpSeq, line))
			}
			line = br.readLine()
		}
		br.close()
		return temporaryList
	}

	fun readAutoSC(filename: String): List<Fastq.Record>{
		val suffix = filename.split(".").last()
		val sc = when(suffix){
			"gz" -> Scanner(GZIPInputStream(FileInputStream(filename))) 
			"bz2" -> Scanner(BZip2CompressorInputStream(FileInputStream(filename)))
			else -> Scanner(FileInputStream(filename))
		}
		return readSC(sc)
	}
	fun readAutoBR(filename: String): MutableList<Fastq.Record>{
		val suffix = filename.split(".").last()
		val br = when(suffix){
			"gz" -> GZIPInputStream(FileInputStream(filename)).bufferedReader()
			"bz2" -> BZip2CompressorInputStream(FileInputStream(filename)).bufferedReader()
			else -> FileInputStream(filename).bufferedReader()
		}
		return readBR(br)
	}

}
