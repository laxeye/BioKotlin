package ru.nrcki.bioKotlin

import java.io.*

//To do: write FASTA (concatenate asFasta() represenations of all records)

class Fasta() {

	open class Record(val header: String, val sequence: String){
		//Take sequence ID 
		val id = header.split(" ")[0]
		//Provide sequence length as FastaRecord().length
		val length = sequence.length

		val width = 60

		fun fw(s: String, w: Int = width): String{
			val lineList = mutableListOf<String>()
			val steps = s.length.div(w)
			for (i in 0..steps-1){
				lineList.add(s.substring(w*i,w*(i+1)))
			}
			lineList.add(s.substring(w*steps))
			return lineList.joinToString(separator="\n",prefix="",postfix="")
		}

		fun asFasta(): String{
			return ">$header\n${fw(sequence)}"
		}

		open fun getLocus(start: Int, end: Int): Record{
			val nstart = if(start < 1) 1 else start
			val nend = if(end > this.length + 1) this.length else end
			val seq = if(nend < nstart) revComp(this.sequence.substring(nend - 1, nstart)) else
				this.sequence.substring(nstart - 1, nend)
			val head = "${this.id} ($start:$end)"
			return Record(head, seq)
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
