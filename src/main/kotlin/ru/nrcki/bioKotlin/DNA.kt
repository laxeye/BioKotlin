package ru.nrcki.bioKotlin

open class Sequence() {
	
	fun getGapLength(sequence: String): Int = sequence.count({it == '-'})

	fun getGapShare(sequence: String): Double = sequence.count({it == '-'}).times(1.0)
		.div(sequence.length)

	fun removeGaps(sequence: String): String = sequence.filter{it != '-'}

}

class DNA(): Sequence() {

	//Provisionally RNA functionality will be here
	val _unambiguous = listOf('A','C','G','T','U')
	
	fun getGCContent(dna: String): Double = dna.toUpperCase()
	.count({(it == 'C') || (it == 'G')}).times(1.0).div(dna.length)

	// GCskew = (G - C) / (G + C)
	fun getGCSkew(dna: String): Double = (dna.toUpperCase()
	.count({it == 'G'}) - dna.toUpperCase().count({it == 'C'}))
	.times(1.0).div(dna.toUpperCase().count({(it == 'C') || (it == 'G')}))

	fun getNsCount(dna: String): Int = dna.toUpperCase().count({it == 'N'})

	fun getAmbiguous(dna: String): Int = dna.toUpperCase().count({ !( it in _unambiguous ) })

	//fun getGapLength(dna: String): Int = dna.count({it == '-'})
	
	fun revComp(seq: String): String = seq.reversed().map { complement(it) } .joinToString("")

	fun complement(nucl: Char): Char{
		when(nucl.toUpperCase()) {
			'A' -> return 'T'
			'C' -> return 'G'
			'G' -> return 'C'
			'T' -> return 'A'
			else -> return 'N'
		}
	}

	fun ACGTtoACGU(nucl: Char): Char {
		when(nucl.toUpperCase()) {
			'A' -> return 'A'
			'C' -> return 'C'
			'G' -> return 'G'
			'T' -> return 'U'
			else -> return 'N'
		}
	}

	fun toRNA(seq: String): String = seq.map { ACGTtoACGU(it) } .joinToString("")
	
}

class Protein(): Sequence() {

	fun getAmbiguous(aa: String): Int = aa.toUpperCase().count({it == 'X'})

	//fun getGapLength(dna: String): Int = dna.count({it == '-'})

}