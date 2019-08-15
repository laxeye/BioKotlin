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
	
	fun getAmbCount(aln: List<Sequence>, type: String) = if(type == "DNA") aln.map(){it.ncount}.sum() else aln.map(){it.xcount}.sum()
		
	fun getGapLength(aln: List<Sequence>) = aln.map(){it.gaplength}.sum()

}