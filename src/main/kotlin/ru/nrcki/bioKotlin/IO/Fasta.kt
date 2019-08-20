package ru.nrcki.biokotlin.io

import java.io.FileInputStream
import java.io.BufferedReader
import java.io.File
import java.util.Scanner
import java.util.zip.GZIPInputStream
import org.apache.commons.compress.compressors.bzip2.*
import ru.nrcki.biokotlin.Sequence
import ru.nrcki.biokotlin.SeqQual

class Fasta(){

	fun asMultiFasta(recList: List<Sequence>): String{
		return recList.map{it.asFasta()}.joinToString(separator="\n",prefix="",postfix="")
	}

	fun read(filename: String): List<Sequence>{
		try {
			return readBR(FileInputStream(filename).bufferedReader())
		}
		catch (e: Exception) {
			System.err.println(e)
			System.exit(1)
			return listOf<Sequence>()
		}
	}

	fun readGzip(filename: String): List<Sequence>{
		try {
			return readBR(GZIPInputStream(FileInputStream(filename)).bufferedReader())
		}
		catch (e: Exception) {
			System.err.println(e)
			System.exit(1)
			return listOf<Sequence>()
		}
	}

	fun readBzip2(filename: String): List<Sequence>{
		try {
			return readBR(BZip2CompressorInputStream(FileInputStream(filename)).bufferedReader())
		}
		catch (e: Exception) {
			System.err.println(e)
			System.exit(1)
			return listOf<Sequence>()
		}
	}

	fun readBR(br: BufferedReader): List<Sequence>{
		val temporaryList = mutableListOf<Sequence>()
		val seqFile = br.readLines()
		br.close()
		if(seqFile.size == 0){
			throw Exception("Empty FASTA file")
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
						temporaryList.add(Sequence(id, seq))
					}
					id = line.drop(1)
				}else{
					lineArray.add(line)
				}
			}
		}

		temporaryList.add(Sequence(id, lineArray.joinToString(separator="")))
		return temporaryList.toList()
	}
}

class Fastq() {

	/* Legacy fastq file reader */
	/*
	fun readBR(filename: String): List<SeqQual>{
		val temporaryList = mutableListOf<SeqQual>()

		val seqFile = File(filename).bufferedReader().readLines()
		if(seqFile.size < 4){
			throw Exception("Empty FASTQ file $filename")
		}
		var i = 0;
		while (i + 3 < seqFile.size){
			temporaryList.add(SeqQual(seqFile[i].drop(1), seqFile[i+1], seqFile[i+3]))
			i += 4
		}

		return temporaryList.toList()
	}
	*/

	fun asMultiFastq(recList: List<SeqQual>): String{
		return recList.map{it.asFastq()}.joinToString(separator="\n",prefix="",postfix="")
	}

	fun readSC(sc: Scanner): List<SeqQual>{
		val temporaryList = mutableListOf<SeqQual>()

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
				0 -> temporaryList.add(SeqQual(tmpHead, tmpSeq, line))
			}
		}
		sc.close()
		return temporaryList.toList()
	}

	fun readBR(br: BufferedReader): MutableList<SeqQual>{
		val temporaryList = mutableListOf<SeqQual>()

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
				0 -> temporaryList.add(SeqQual(tmpHead, tmpSeq, line))
			}
			line = br.readLine()
		}
		br.close()
		return temporaryList
	}

	fun readAutoSC(filename: String): List<SeqQual>{
		val suffix = filename.split(".").last()
		val sc = when(suffix){
			"gz" -> Scanner(GZIPInputStream(FileInputStream(filename))) 
			"bz2" -> Scanner(BZip2CompressorInputStream(FileInputStream(filename)))
			else -> Scanner(FileInputStream(filename))
		}
		return readSC(sc)
	}
	fun readAutoBR(filename: String): MutableList<SeqQual>{
		val suffix = filename.split(".").last()
		val br = when(suffix){
			"gz" -> GZIPInputStream(FileInputStream(filename)).bufferedReader()
			"bz2" -> BZip2CompressorInputStream(FileInputStream(filename)).bufferedReader()
			else -> FileInputStream(filename).bufferedReader()
		}
		return readBR(br)
	}


	fun fastqPairing(file1: String, file2: String){
		val readsIdR = Fastq().readAutoBR(file2).map{it.id}
		System.err.println("Forward IDs loaded.")
		var readsF = Fastq().readAutoBR(file1)
		System.err.println("Secondfile red.")
		val bothID = readsF.map{it.id}.intersect(readsIdR)
		System.err.println("Intersection ready.")
		val bfWriterF = File("paired.$file1").bufferedWriter()
		readsF.filter{it.id in bothID}.sortedBy{it.id}.forEach{
			bfWriterF.write(it.asFastq())
		}
		//readsF = mutableListOf<Fastq.Record>("")
		readsF.clear()
		bfWriterF.close()

		val bfWriterR = File("paired.$file2").bufferedWriter()
		Fastq().readAutoBR(file2).filter{it.id in bothID}.sortedBy{it.id}.forEach{
			bfWriterR.write(it.asFastq())
		}
		bfWriterR.close()

	}


}
