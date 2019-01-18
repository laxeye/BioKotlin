package ru.nrcki.BioKotlin.gff

import java.io.*

// GFF schema: seqid, source, type, start, end, score, strand, frame or phase, attribute
class Feature(val seqname: String, val source: String = ".", val type: String, 
	val start: Int, val end: Int, val score: Double?, val strand: String, 
	val frame: Int?, val attr: String){
}

fun lineToFeature(val line: String): Feature{
	val fields = line.toString().split("\t")
	val frame = if(fields[7] ==".") null else fields[7].toInt()
	val score = if(fields[5] == ".") null else fields[5].toDouble()
	return Feature(fields[0], fields[1], fields[2], fields[3].toInt(), fields[4].toInt(), score, 
			fields[6], frame, fields[8])
}

fun main(args: Array<String>){
	println("Hello!")
	val genome = mutableListOf<Feature>()
	val filename = args[0]
	val reader = File(filename).readLines()
	val debug_print = false
	for (line in reader){
		genome.add(lineToFeature(line))
	}
	if(debug_print){
		for (j in genome){
			println("${j.type} from ${j.start} to ${j.end}")
		}	
	}
}
