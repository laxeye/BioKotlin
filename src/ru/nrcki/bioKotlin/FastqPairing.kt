package ru.nrcki.bioKotlin

import ru.nrcki.bioKotlin.Fastq
import java.io.*

fun main(args: Array<String>){
	require(args.size == 2){"Ypu should provide 2 files!"}
	var readsR = Fastq().readBR(args[1])
	val readsIdR = readsR.map{it.id}
	readsR.clear()
	System.err.println("First file red.")
	var readsF = Fastq().readBR(args[0])
	System.err.println("Secondfile red.")
	val bothID = readsF.map{it.id}.intersect(readsIdR)
	System.err.println("Intersection ready.")
	val bfWriterF = File("${args[0]}.paired").bufferedWriter()
	readsF.filter{it.id in bothID}.sortedBy{it.id}.forEach{
		bfWriterF.write(it.asFastq())
	}
	readsF.clear()
	bfWriterF.close()

	val bfWriterR = File("${args[1]}.paired").bufferedWriter()
	Fastq().readBR(args[1]).filter{it.id in bothID}.sortedBy{it.id}.forEach{
		bfWriterR.write(it.asFastq())
	}
	bfWriterR.close()

}