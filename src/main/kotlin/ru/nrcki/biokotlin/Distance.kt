package ru.nrcki.biokotlin

import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.sqrt

class Distance(){

	fun rawDistance(seq1: IBioSequence, seq2: IBioSequence): Double {
		
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

	fun jcDistance(seq1: IBioSequence, seq2: IBioSequence, nucl: Boolean = true): Double {
		val distance = rawDistance(seq1, seq2)

		// Constant different for nucleotides and proteins
		val b = if(nucl) 0.75 else 0.95
		
		return -b * ln( 1 - distance / b )
	}

	fun poissonDistance(seq1: IBioSequence, seq2: IBioSequence): Double {
		return abs( -ln( 1 - rawDistance(seq1, seq2) ) )
	}

	fun jcMatrix(alignment: List<IBioSequence>, nucl: Boolean = true){
		for(i in 1 until alignment.size){
			print(alignment[i].id)
			for(j in 0 until i){
				print("\t${jcDistance(alignment[i], alignment[j], nucl)}")
			}
			print("\n")
		}
	}

	fun jcMeanDistance(alignment: List<IBioSequence>, nucl: Boolean = true){
		alignment.forEach(){querySeq ->
			print("${querySeq.id}\t")
			println( alignment.map{jcDistance(querySeq,it,nucl)}.sum().div(alignment.size - 1) )
		}
	}

	fun calcTransitions(seq1: IBioSequence, seq2: IBioSequence): Int {
		var transitions = 0
		for(i in 0 until seq1.length){
			if( ( seq1.sequence[i] == 'A') && ( seq2.sequence[i] == 'G' ) ) transitions++
			if( ( seq1.sequence[i] == 'G') && ( seq2.sequence[i] == 'A' ) ) transitions++
			if( ( seq1.sequence[i] == 'C') && ( seq2.sequence[i] == 'T' ) ) transitions++
			if( ( seq1.sequence[i] == 'C') && ( seq2.sequence[i] == 'U' ) ) transitions++
			if( ( seq1.sequence[i] == 'T') && ( seq2.sequence[i] == 'C' ) ) transitions++
			if( ( seq1.sequence[i] == 'U') && ( seq2.sequence[i] == 'C' ) ) transitions++
		}
		return transitions
	}

	fun calcTransversions(seq1: IBioSequence, seq2: IBioSequence): Int {
		var transversions = 0
		for(i in 0 until seq1.length){
			if( ( seq1.sequence[i] == 'A') && ( seq2.sequence[i] == 'C' ) ) transversions++
			if( ( seq1.sequence[i] == 'A') && ( seq2.sequence[i] == 'T' ) ) transversions++
			if( ( seq1.sequence[i] == 'A') && ( seq2.sequence[i] == 'U' ) ) transversions++

			if( ( seq1.sequence[i] == 'C') && ( seq2.sequence[i] == 'G' ) ) transversions++
			if( ( seq1.sequence[i] == 'C') && ( seq2.sequence[i] == 'A' ) ) transversions++

			if( ( seq1.sequence[i] == 'G') && ( seq2.sequence[i] == 'C' ) ) transversions++
			if( ( seq1.sequence[i] == 'G') && ( seq2.sequence[i] == 'T' ) ) transversions++
			if( ( seq1.sequence[i] == 'G') && ( seq2.sequence[i] == 'U' ) ) transversions++

			if( ( seq1.sequence[i] == 'T') && ( seq2.sequence[i] == 'A' ) ) transversions++
			if( ( seq1.sequence[i] == 'T') && ( seq2.sequence[i] == 'G' ) ) transversions++
			if( ( seq1.sequence[i] == 'U') && ( seq2.sequence[i] == 'A' ) ) transversions++
			if( ( seq1.sequence[i] == 'U') && ( seq2.sequence[i] == 'G' ) ) transversions++
		}
		return transversions
	}

	fun kimuraDistance(seq1: IBioSequence, seq2: IBioSequence): Double {
		val transitions = calcTransitions(seq1, seq2).toDouble() / seq1.length
		val transversions = calcTransversions(seq1, seq2).toDouble() / seq1.length
		val distance = -0.5 * ln( (1.0 - 2 * transitions - transversions ) * sqrt( 1.0 - 2 * transversions ) )
		return abs(distance)
	}

	fun tamuraDistance(seq1: IBioSequence, seq2: IBioSequence): Double {
		val gc1 = seq1.sequence.getGCContent()
		val gc2 = seq2.sequence.getGCContent()
		val C = gc1 + gc2 - 2 * gc1 * gc2
		val transitions = calcTransitions(seq1, seq2).toDouble() / seq1.length
		val transversions = calcTransversions(seq1, seq2).toDouble() / seq1.length
		val distance = -C * ln(1.0 - transitions / C - transversions) - 0.5 * (1.0 - C) * ln(1.0 - 2 * transversions)
		return abs(distance)
	}

}