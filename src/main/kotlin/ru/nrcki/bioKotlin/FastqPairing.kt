package ru.nrcki.bioKotlin

import ru.nrcki.bioKotlin.IO.Fastq
import java.io.*

fun main(args: Array<String>){
	require(args.size == 2){"Ypu should provide 2 files!"}
	val readsIdR = Fastq().readAutoBR(args[1]).map{it.id}
	System.err.println("Forward IDs loaded.")
	var readsF = Fastq().readAutoBR(args[0])
	System.err.println("Secondfile red.")
	val bothID = readsF.map{it.id}.intersect(readsIdR)
	System.err.println("Intersection ready.")
	val bfWriterF = File("${args[0]}.paired").bufferedWriter()
	readsF.filter{it.id in bothID}.sortedBy{it.id}.forEach{
		bfWriterF.write(it.asFastq())
	}
	//readsF = mutableListOf<Fastq.Record>("")
	readsF.clear()
	bfWriterF.close()

	val bfWriterR = File("${args[1]}.paired").bufferedWriter()
	Fastq().readAutoBR(args[1]).filter{it.id in bothID}.sortedBy{it.id}.forEach{
		bfWriterR.write(it.asFastq())
	}
	bfWriterR.close()

}