package ru.nrcki.biokotlin

import ru.nrcki.biokotlin.Sequence
import kotlin.math.ln

class Distance(){

	fun rawDistance(seq1: Sequence, seq2: Sequence): Double {
		
		if(seq1.gaplength == seq1.length) {
			throw Exception("Error! Impossible to calculate distance: ${seq1.id} contains only gaps")
		}
		if(seq2.gaplength == seq2.length) {
			throw Exception("Error! Impossible to calculate distance: ${seq2.id} contains only gaps")
		}
		
		var matches = 0
		var positions = 0
		for(i in 0 until seq1.length){
			if (seq1.sequence[i] == seq2.sequence[i]){
				if(seq1.sequence[i] != '-'){
					positions += 1
					matches += 1
				}
			}else{
				positions += 1
			}
		}

		return 1.0 - matches.toDouble()/positions.toDouble()
	}

	fun jcDistance(seq1: Sequence, seq2: Sequence, nucl: Boolean = true): Double{
		val distance = rawDistance(seq1, seq2)

		// Constant different for nucleotides and proteins
		val b = if(nucl) 0.75 else 0.95
		
		return -b * ln( 1 - distance / b )
	}

	fun jcMatrix(alignment: List<Sequence>, nucl: Boolean = true){
		for(i in 1 until alignment.size){
			print("${alignment[i].id}")
			for(j in 0 until i){
				print("\t${jcDistance(alignment[i], alignment[j], nucl)}")
			}
			print("\n")
		}
	}

	fun jcMeanDistance(alignment: List<Sequence>, nucl: Boolean = true){
		alignment.forEach(){querySeq ->
			print("${querySeq.id}\t")
			println( alignment.map{jcDistance(querySeq,it,nucl)}.sum().toDouble().div(alignment.size - 1) )
		}
	}


}