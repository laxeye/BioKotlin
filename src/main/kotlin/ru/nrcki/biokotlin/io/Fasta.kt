package ru.nrcki.biokotlin.io

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import ru.nrcki.biokotlin.IBioSequence
import ru.nrcki.biokotlin.BioSeqQual
import ru.nrcki.biokotlin.BioSequence
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.util.zip.GZIPInputStream

interface IBioIO {
	fun read(filename: String): List<IBioSequence>
	fun write(filename: String, list: List<IBioSequence>)
}

class Fasta() : IBioIO {

	fun asMultiFasta(recList: List<BioSequence>): String{
		return recList.map{it.formatted()}.joinToString(separator="\n",prefix="",postfix="")
	}

	override fun read(filename: String): List<IBioSequence>{
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
			listOf<BioSequence>()
		}

	}

	fun readGzip(filename: String): List<BioSequence>{
		return try {
			readBR(GZIPInputStream(FileInputStream(filename)).bufferedReader())
		} catch (e: Exception) {
			System.err.println(e)
			System.exit(1)
			listOf<BioSequence>()
		}
	}

	fun readBzip2(filename: String): List<BioSequence>{
		return try {
			readBR(BZip2CompressorInputStream(FileInputStream(filename)).bufferedReader())
		} catch (e: Exception) {
			System.err.println(e)
			System.exit(1)
			listOf<BioSequence>()
		}
	}

	fun readBR(br: BufferedReader): List<BioSequence>{
		val temporaryList = mutableListOf<BioSequence>()
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
						temporaryList.add(BioSequence(id, seq))
					}
					id = line.drop(1)
				}else{
					lineArray.add(line)
				}
			}
		}

		temporaryList.add(BioSequence(id, lineArray.joinToString(separator="")))
		return temporaryList.toList()
	}

	override fun write(filename: String, list: List<IBioSequence>){
		val bfWriter: BufferedWriter = File(filename).bufferedWriter()
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

	fun asMultiFastq(recList: List<BioSeqQual>): String{
		return recList.map{it.formatted()}.joinToString(separator="\n",prefix="",postfix="")
	}

	fun readBR(br: BufferedReader): MutableList<BioSeqQual>{
		val temporaryList = mutableListOf<BioSeqQual>()

		var tmpHead = ""
		var tmpSeq = ""
		var i = 0
		var line = br.readLine()
		
		while(line != null){
			i++
			when(i.rem(4)){
				1 -> tmpHead = line.drop(1)
				2 -> tmpSeq = line
				0 -> temporaryList.add(BioSeqQual(tmpHead, tmpSeq, line))
			}
			line = br.readLine()
		}
		br.close()
		return temporaryList
	}

	override fun read(filename: String): List<BioSeqQual>{
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

	override fun write(filename: String, list: List<IBioSequence>){
		val bfWriter: BufferedWriter = File(filename).bufferedWriter()
		list.forEach {bfWriter.write(it.formatted())}
		bfWriter.close()
	}


}
