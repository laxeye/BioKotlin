package ru.nrcki.bioKotlin

import ru.nrcki.bioKotlin.Fasta
import ru.nrcki.bioKotlin.DNA

/*Returns all possible or only non self reverse-complemented tetramers*/
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
				if(! tetranucs.contains(DNA().revComp(n + it))) tetranucs.add(n + it)}
		}
	}

	return tetranucs.toList().sorted()
}

fun calcTetra(seq: String): Map<String,Int>{
	val tetraList = makeTetraList()
	val tMap =  tetraList.associate{ it to 0 }.toMutableMap<String,Int>()
	val lastIdx = seq.length - 4
	for(i in 0..lastIdx){
		var piece = seq.substring(i,i+4)
		if(piece in tetraList) tMap.put(piece, (tMap.get(piece) ?: 0) + 1)
		piece = DNA().revComp(piece)
		if(DNA().revComp(piece) in tetraList) tMap.put(piece, (tMap.get(piece) ?: 0) + 1)
	}
	return tMap
}

fun calcAllTetra(seq: String): Map<String,Int>{
	val tMap = makeTetraList(true).associate{ it to 0 }.toMutableMap<String,Int>()
	val lastIdx = seq.length - 4
	for(i in 0..lastIdx){
		val piece = seq.substring(i,i+4)
		tMap.put(piece, (tMap.get(piece) ?: 0) + 1)
	}
	return tMap
}

fun main(args: Array<String>){
	require(args.size>0) {"Provide file as a first argument"}
	val seqs = Fasta().read(args[0])
	val redundant = false
	val allTetra = makeTetraList(redundant)
	if(redundant){
		for (seq in seqs){
			val tMap = calcAllTetra(seq.sequence)
			print(seq.id)
			allTetra.forEach(){
				print("\t${tMap.getOrElse(it,{0}).toDouble().times(100.0).div(seq.length-3)}")
				// More time consumption if format() used
				// print("\t%.5f".format(tMap.getOrElse(it,{0}).toDouble().div(seq.length-3)))
			}
			print("\n")
		}
	}else{
		for (seq in seqs){
			val tMap = calcTetra(seq.sequence)
			print(seq.id)
			allTetra.forEach(){
				print("\t${tMap.getOrElse(it,{0}).toDouble().times(100.0).div(seq.length-3)}")
				// More time consumption if format() used
				// print("\t%.5f".format(tMap.getOrElse(it,{0}).toDouble().div(seq.length-3)))
			}
			print("\n")

		}
	}
}
