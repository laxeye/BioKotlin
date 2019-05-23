package ru.nrcki.bioKotlin

import ru.nrcki.bioKotlin.Fasta
import ru.nrcki.bioKotlin.Fastq
import ru.nrcki.bioKotlin.*

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

		//Print revcomplement
		println(DNA().revComp(firstRecord.sequence))

		//Get locus 5..10  and print as FASTA
		println(firstRecord.getLocus(5,10).asFasta())

		//Get locus 10..5  and print as FASTA
		println(firstRecord.getLocus(10,5).asFasta())


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
		//Print first eight nucleotides
		println(firstRecord.getLocus(1,8).asFasta())

	}
}
