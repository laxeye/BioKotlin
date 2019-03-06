package ru.nrcki.bioKotlin

import ru.nrcki.bioKotlin.Fasta
import ru.nrcki.bioKotlin.Fastq

fun main(args: Array<String>){
	if (args[0] == "fasta"){
		//Read FASTA from file
		val faList = Fasta().read(args[1])

		//Get the first record
		val firstRecord = faList[0]
		
		//Print id, length and sequence
		println("Sequence ${firstRecord.id} has ${firstRecord.length} letters:")

		//Print as FASTA
		println(firstRecord.asFasta())

	}else{
		val fqList = Fastq().read(args[1])

		//Get the first record
		val firstRecord = fqList[0]

		//Print id, length and sequence
		println("Sequence ${firstRecord.id} has ${firstRecord.length} letters:")

		//Print as FASTA
		println(firstRecord.asFasta())
		//Print as FASTQ
		println(firstRecord.asFastq())
	}
}
