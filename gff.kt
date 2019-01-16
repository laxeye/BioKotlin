package ru.nrcki.BioKotlin.gff

import java.io.*

class Feature(val seqname: String, val source: String = ".", val type: String, 
	val start: Int, val end: Int, val score: Double, val strand: String, 
	val frame: Int?, val attr: String){
}

fun main(args: Array<String>){
	println("Hello!")
	val genome = mutableListOf<Feature>()
	val filename = args[0]
	val reader = File(filename).readLines()
	val debug_print = false
	for (i in reader){
		val line = i.toString().split("\t")
		val frame = if(line[7] ==".") null else line[7].toInt()
		val score = if(line[5] == ".") 0.0 else line[5].toDouble()
		val feat1 = Feature(line[0], line[1], line[2], line[3].toInt(), line[4].toInt(), score, 
			line[6], frame, line[8])
		genome.add(feat1)
	}
	if(debug_print){
		for (j in genome){
			println("${j.type} from ${j.start} to ${j.end}")
		}	
	}
}
