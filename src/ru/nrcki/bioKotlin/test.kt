package ru.nrcki.bioKotlin

import ru.nrcki.bioKotlin.*

fun main(args: Array<String>){
	//Read FASTA from file
	val fastaList = readFasta(args[0])
	//Get the first record
	val firstRecord = fastaList[0]
	//Print id, length and sequence
	println("Sequence ${firstRecord.id} has ${firstRecord.length} letters:")
	println(firstRecord.sequence)
	//Print as FASTA
	println(firstRecord.asFasta())
}