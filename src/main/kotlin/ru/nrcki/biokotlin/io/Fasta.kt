package ru.nrcki.biokotlin.io

import java.io.FileInputStream
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.util.zip.GZIPInputStream
import org.apache.commons.compress.compressors.bzip2.*
import ru.nrcki.biokotlin.BioSequence
import ru.nrcki.biokotlin.Sequence
import ru.nrcki.biokotlin.SeqQual

interface IBioIO {
	fun read(filename: String): List<BioSequence>
	fun write(file: String, list: List<BioSequence>)
}

class Fasta() : IBioIO {

	fun asMultiFasta(recList: List<Sequence>): String{
		return recList.map{it.formatted()}.joinToString(separator="\n",prefix="",postfix="")
	}

	override fun read(filename: String): List<BioSequence>{
		val suffix = filename.split(".").last()
		return try {
			when(suffix){
				"gz" -> readBR(GZIPInputStream(FileInputStream(filename)).bufferedReader())
				"bz2" -> readBR(BZip2CompressorInputStream(FileInputStream(filename)).bufferedReader())
				else -> readBR(FileInputStream(filename).bufferedReader())
			}
		} catch (e: Exception) {
			System.err.println(e)
			System.exit(1)
			listOf<Sequence>()
		}

	}

	fun readGzip(filename: String): List<Sequence>{
		return try {
			readBR(GZIPInputStream(FileInputStream(filename)).bufferedReader())
		} catch (e: Exception) {
			System.err.println(e)
			System.exit(1)
			listOf<Sequence>()
		}
	}

	fun readBzip2(filename: String): List<Sequence>{
		return try {
			readBR(BZip2CompressorInputStream(FileInputStream(filename)).bufferedReader())
		} catch (e: Exception) {
			System.err.println(e)
			System.exit(1)
			listOf<Sequence>()
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

	override fun write(file: String, list: List<BioSequence>){
		val bfWriter: BufferedWriter = File(file).bufferedWriter()
		list.forEach {bfWriter.write(it.formatted())}
		bfWriter.close()
	}

}

class Fastq() : IBioIO {

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
		return recList.map{it.formatted()}.joinToString(separator="\n",prefix="",postfix="")
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

	override fun read(filename: String): List<BioSequence>{
		val suffix = filename.split(".").last()
		val br = when(suffix){
			"gz" -> GZIPInputStream(FileInputStream(filename)).bufferedReader()
			"bz2" -> BZip2CompressorInputStream(FileInputStream(filename)).bufferedReader()
			else -> FileInputStream(filename).bufferedReader()
		}
		return readBR(br).toList()
	}


	fun fastqPairing(file1: String, file2: String){
		val readsIdR = Fastq().read(file2).map{it.id}
		val readsF = Fastq().read(file1)
		val bothID = readsF.map{it.id}.intersect(readsIdR)
		write("paired.$file1", readsF.filter{it.id in bothID}.sortedBy{it.id})
		write("paired.$file2", Fastq().read(file2).filter{it.id in bothID}.sortedBy{it.id})
	}

	override fun write(file: String, list: List<BioSequence>){
		val bfWriter: BufferedWriter = File(file).bufferedWriter()
		list.forEach {bfWriter.write(it.formatted())}
		bfWriter.close()
	}


}
