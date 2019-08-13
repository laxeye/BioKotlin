package ru.nrcki.bioKotlin

import ru.nrcki.bioKotlin.Fasta

class Alignment(){

	fun getAlnCols(aln: List<Fasta.Record>): List<String>{
		val mList = mutableListOf<String>()
		val length = aln[0].sequence.length
		for(i in 0 until length){
			mList.add(aln.map{it.getChar(i)}.joinToString(""))
		}
		return mList.toList()
	}
	
	fun getAmbCount(aln: List<Fasta.Record>, type: String) = if(type == "DNA") aln.map(){it.ncount}.sum() else aln.map(){it.xcount}.sum()
		
	fun getGapLength(aln: List<Fasta.Record>) = aln.map(){it.gaplength}.sum()

}