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
	return tetranucs.toList()
}

fun main(args: Array<String>){
	val allTetra = makeAllTetra()
	val seqs = Fasta().read(args[0])
	val fMap = mutableMapOf<String,Double>()
	for (seq in seqs){
		val tMap = mutableMapOf<String,Int>()
		var i = 0
		while(i+3 < seq.length){
			val piece = seq.sequence.substring(i,i+4)
			if(tMap.contains(piece)){
				println(tMap.get(piece))
				tMap.put(piece,tMap.get(piece)!!.inc())
			}
			tMap.put(piece, 1)
			i++
		}
		tMap.keys.sorted().forEach(){
			println("$it\t${tMap.get(it)}")
//			println("$it\t${tMap.getOrElse(it,0)}")
		}
		println("")
	}
}
