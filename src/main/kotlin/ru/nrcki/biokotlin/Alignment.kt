package ru.nrcki.biokotlin

class Alignment(){

	fun getAlnCols(aln: List<IBioSequence>): List<String>{
		val mList = mutableListOf<String>()
		val length = aln[0].length
		for(i in 0 until length){
			mList.add(aln.map{it.getChar(i)}.joinToString(""))
		}
		return mList.toList()
	}
	
	fun getAmbCount(aln: List<BioSequence>, type: String) {
		if((type == "DNA") || (type == "RNA")){
 			aln.map{it.ncount}.sum()
		} else {
			aln.map{it.xcount}.sum()
		}
	}
		
	fun getGapLength(aln: List<IBioSequence>): Int = aln.map{it.gaplength}.sum()

	fun getGapShare(aln: List<IBioSequence>): Double = getGapLength(aln)
		.toDouble().div(aln[0].length * aln.size)

	fun printAlnInfo(aln: List<IBioSequence>) {
		val valid = checkLength(aln)
		val gapShare = getGapShare(aln)
		val size = aln.size
		val length = aln[0].length
		println("Alignment is ${if(valid) "valid" else "invalid"}")
		println("There are $size sequences with length of $length symbols.")
		println("Share of gaps in alignment: $gapShare.")
	}

	fun checkLength(aln: List<IBioSequence>): Boolean = aln.filter{it.length != aln[0].length}.size == 0

	// Sequential PHYLIP alignment format
	fun asPhylipSeq(aln: List<IBioSequence>, strict: Boolean = true): String{
		
		// Strict mode (ID length eq 9) may produce duplicating IDs
		val tmpPhylip = mutableListOf<String>("${aln.size} ${aln[0].length}")
		for(seq in aln){
			if(strict){
				val strictID = if(seq.id.length > 9) seq.id.substring(0,9) else seq.id.padEnd(9,' ')
				tmpPhylip.add("$strictID ${seq.sequence}")
			}else{
				tmpPhylip.add("${seq.id} ${seq.sequence}")
			}
		}
		return tmpPhylip.joinToString(separator = "\n")
	}

	fun clearGappedColumns(aln: List<IBioSequence>, maxGapShare: Double = 0.5): List<IBioSequence> {
		val alnColumns = getAlnCols(aln)
		val cleanColumns = alnColumns.filter{(it.count {it == '-'}.toDouble() / it.length.toDouble()) < maxGapShare}
		val cleanedFasta = mutableListOf<IBioSequence>()
		for(i in aln.indices){
			cleanedFasta.add(BioSequence(aln[i].header, cleanColumns.map{ it[i] }.joinToString("")))
		}
		
		return cleanedFasta.toList()

	}


}