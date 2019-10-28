package ru.nrcki.biokotlin

import ru.nrcki.biokotlin.io.Fasta

class Tetra(){

	fun makeTetraList(redundant: Boolean = false): List<String>{
		val dinucs = mutableListOf<String>()
		val tetranucs = mutableListOf<String>()
		val nucs = listOf("A","C","G","T")
		for(n in nucs){
			nucs.map{dinucs.add(n + it)}
		}
		
		if(redundant){
			for(n in dinucs){
				dinucs.map{tetranucs.add(n + it)}
			}
		}else{
			for(n in dinucs){
				dinucs.map{
					if(! tetranucs.contains((n+it).revComp())) tetranucs.add(n + it)}
			}
		}

		return tetranucs.toList().sorted()
	}

	fun calcTetra(seq: String): Map<String,Int>{
		val tetraList = makeTetraList()
		val tMap =  tetraList.associateWith { 0 }.toMutableMap<String,Int>()
		val lastIdx = seq.length - 4
		for(i in 0..lastIdx){
			var piece = seq.substring(i,i+4)
			if(piece in tetraList) tMap.put(piece, (tMap[piece] ?: 0) + 1)
			piece = piece.revComp()
			if(piece.revComp() in tetraList) tMap[piece] = (tMap[piece] ?: 0) + 1
		}
		return tMap
	}

	fun calcAllTetra(seq: String): Map<String,Int>{
		val tMap = makeTetraList(true).associateWith { 0 }.toMutableMap<String,Int>()
		val lastIdx = seq.length - 4
		for(i in 0..lastIdx){
			val piece = seq.substring(i,i+4)
			tMap[piece] = (tMap[piece] ?: 0) + 1
		}
		return tMap
	}

	fun genomeTetraStats(filename: String, redundant: Boolean = false){
		val seqs = Fasta().read(filename)
		val allTetra = makeTetraList(redundant)
		if(redundant) {
			for (seq in seqs) {
				val tMap = calcAllTetra(seq.sequence)
				print(seq.id)
				allTetra.forEach(){
					print("\t${tMap.getOrElse(it,{0}).toDouble().times(100.0).div(seq.length-3)}")
					// More time consumption if format() used
					// print("\t%.5f".format(tMap.getOrElse(it,{0}).toDouble().div(seq.length-3)))
				}
				print("\n")
			}
		} else {
			for (seq in seqs){
				val tMap = calcTetra(seq.sequence)
				print(seq.id)
				allTetra.forEach() {
					print("\t${tMap.getOrElse(it,{0}).toDouble().times(100.0).div(seq.length-3)}")
					// More time consumption if format() used
					// print("\t%.5f".format(tMap.getOrElse(it,{0}).toDouble().div(seq.length-3)))
				}
				print("\n")

			}
		}
	}


}
	