package ru.nrcki.bioKotlin

import ru.nrcki.bioKotlin.Sequence

class Alignment(){

	fun getAlnCols(aln: List<Sequence>): List<String>{
		val mList = mutableListOf<String>()
		val length = aln[0].length
		for(i in 0 until length){
			mList.add(aln.map{it.getChar(i)}.joinToString(""))
		}
		return mList.toList()
	}
	
	fun getAmbCount(aln: List<Sequence>, type: String) {
		if((type == "DNA") || (type == "RNA")){
 			aln.map(){it.ncount}.sum() 
		} else {
			aln.map(){it.xcount}.sum()
		}
	}
		
	fun getGapLength(aln: List<Sequence>): Int = aln.map(){it.gaplength}.sum()

	fun getGapShare(aln: List<Sequence>): Double = getGapLength(aln)
		.times(1.0).div(aln[0].length * aln.size)

	fun prinAlnInfo(aln: List<Sequence>) {
		val valid = checkLength(aln)
		//val gapLength = getGapLength(aln)
		val gapShare = getGapShare(aln)
		val size = aln.size
		val length = aln[0].length
		println("Alignment is ${if(valid) "valid" else "invalid"}")
		println("There are $size sequences with length of $length symbols.")
		println("Share of gaps in alignment: $gapShare.")
	}

	fun checkLength(aln: List<Sequence>): Boolean {
		return aln.filter(){it.length != aln[0].length}.size == 0
	}
	fun printAsPhylipSeq(aln: List<Sequence>){
		val size = aln.size
		val length = aln[0].length
		println("Not now!")
	}

}