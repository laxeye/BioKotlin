package ru.nrcki.bioKotlin

import java.io.*

//To do: write FASTA (concatenate asFasta() represenations of all records)

class Fasta() {

	class Record(val header: String, val sequence: String){
		//Take sequence ID 
		val id = header.split(" ")[0]
		//Provide sequence length as FastaRecord().length
		val length = sequence.length

		//To do: format to line width 60 or other
		fun asFasta(): String{
			return ">$header\n$sequence"
		}
	}

	fun read(filename: String): List<Fasta.Record>{
		val temporaryList = mutableListOf<Fasta.Record>()

		val seqFile = File(filename).bufferedReader().readLines()
		if(seqFile.size == 0){
			throw Exception("Empty FASTA file $filename")
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
						temporaryList.add(Fasta.Record(id, seq))
					}
					id = line.drop(1)
				}else{
					lineArray.add(line)
				}
			}
		}

		temporaryList.add(Fasta.Record(id, lineArray.joinToString(separator="")))
		return temporaryList.toList()
	}

}
