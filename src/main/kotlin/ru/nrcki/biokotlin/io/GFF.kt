package ru.nrcki.biokotlin.io

import java.io.File

class GFF() {

	// GFF schema: seqid, source, type, start, end, score, strand, frame or phase, attribute
	data class Feature(val seqname: String, val source: String = ".", val type: String,
		val start: Int, val end: Int, val score: Double?, val strand: String, 
		val frame: Int?, val attr: String)

	fun lineToFeature(line: String): Feature{
		val fields = line.split("\t")
		val frame = if(fields[7] ==".") null else fields[7].toInt()
		val score = if(fields[5] == ".") null else fields[5].toDouble()
		return Feature(fields[0], fields[1], fields[2], fields[3].toInt(), fields[4].toInt(), score, 
				fields[6], frame, fields[8])
	}

	fun featureToLine(feature: Feature): String {
		return "${feature.seqname}\t${feature.source}\t${feature.type}\t"
	}

	fun read(filename: String) = File(filename).readLines().map{lineToFeature(it)}
}
