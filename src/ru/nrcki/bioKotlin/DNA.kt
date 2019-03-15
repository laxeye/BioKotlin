package ru.nrcki.bioKotlin

fun revComp(seq: String): String{
	return seq.reversed().map{complement(it)}.joinToString("")
}

fun complement(nucl: Char): Char{
	when(nucl.toUpperCase()) {
		'A' -> return 'T'
		'C' -> return 'G'
		'G' -> return 'C'
		'T' -> return 'A'
		else -> return 'N'
	}
}
