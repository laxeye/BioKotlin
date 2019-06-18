package ru.nrcki.bioKotlin

class DNA() {

	fun getGCContent(dna: String): Double = dna.toUpperCase()
	.count({(it == 'C') || (it == 'G')}).times(1.0).div(dna.length)

	fun getNsCount(dna: String): Int = dna.toUpperCase().count({it == 'N'})
	
	fun revComp(seq: String): String = seq.reversed().map{complement(it)}.joinToString("")

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

	fun toDNA(seq: String): String = seq.map { ACGTtoACGU(it) } .joinToString("")
	
}
