package ru.nrcki.bioKotlin

import ru.nrcki.bioKotlin.Fasta

fun makeAllTetra(): List<String>{
	val dinucs = mutableListOf<String>()
	val tetranucs = mutableListOf<String>()
	val nucs = listOf("A","C","G","T")
	for(n in nucs){
		nucs.map{dinucs.add(n + it)}
	}
	for(n in dinucs){
		dinucs.map{tetranucs.add(n + it)}
	}
	return tetranucs.toList().sorted()
}

fun main(args: Array<String>){
	require(args.size>0) {"Provide file as a first argument"}
	val allTetra = makeAllTetra()
	val seqs = Fasta().read(args[0])
	for (seq in seqs){
		val tMap = mutableMapOf<String,Int>()
		var i = 0
		while(i+3 < seq.length){
			val piece = seq.sequence.substring(i,i+4)
			val count = tMap.getOrElse(piece,{0}) + 1
			tMap.put(piece, count)
			i++
		}
		print(seq.id)
		allTetra.forEach(){
			print("\t${tMap.getOrElse(it,{0}).toDouble().div(seq.length-3)}")
			// More time consumption if format() used
			// print("\t%.5f".format(tMap.getOrElse(it,{0}).toDouble().div(seq.length-3)))
		}
		print("\n")
	}
}
