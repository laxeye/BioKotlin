package ru.nrcki.biokotlin

import kotlin.math.round

interface BioSequence {
	val header: String
	val gaplength: Int
	val sequence: String
	val length: Int
	val id: String

	fun formatted(): String
	fun getChar(i: Int): Char
}

open class Sequence(final override val header: String, final override val sequence: String): BioSequence {

	fun getGapLength(sequence: String): Int = sequence.count {it == '-'}

	fun getGapShare(sequence: String): Double = sequence.count {it == '-'}.toDouble()
		.div(sequence.length)

	fun removeGaps(): Sequence = Sequence(this.header, this.sequence.filter {it != '-'})

	override val id = this.header.split(" ")[0]

	override val length = sequence.length

	override val gaplength: Int
		get() = getGapLength(sequence)

	fun getGapCount(): Int {
		var count = 0
		var prev = 'A'
		this.sequence.forEach(){
			if ( (it == '-') && (prev != '-') ) count++
			prev = it
		}
		return count
	}

	val xcount: Int 
		get() = sequence.toUpperCase().count {it == 'X'}

	val ncount: Int
		get() = sequence.toUpperCase().count {it == 'N'}

	private val _width = 60

	fun formatFasta(seq: String, w: Int = _width): String{
		val lineList = mutableListOf<String>()
		val steps = seq.length.div(w)
		for (i in 0 until steps){
			lineList.add(seq.substring(w*i,w*(i+1)))
		}
		lineList.add(seq.substring(w*steps))
		return lineList.joinToString(separator="\n",prefix="",postfix="")
	}

	override fun formatted(): String = ">$header\n${formatFasta(sequence)}"

	override fun toString(): String = ">$header\n$sequence"

	override fun getChar(i: Int): Char = this.sequence[i]

	/* 1-based! */
	open fun getLocus(start: Int, end: Int): Sequence{
		val nstart = if(start < 1) 1 else start
		val nend = if(end > this.length + 1) this.length else end
		val subseq = if(nend < nstart) this.sequence.substring(nend - 1, nstart).revComp() else
			this.sequence.substring(nstart - 1, nend)
		val head = "${this.id} ($start:$end)"
		return Sequence(head, subseq)
	}

}

class SeqQual(header: String, sequence: String, val qual: String): Sequence(header, sequence){
	
	override fun formatted(): String = "@$header\n$sequence\n+\n$qual\n"
	
	fun qualsAsIntList(): List<Int> = qual.map{it.toInt()}

	fun qualsAsPhredScore(delta: Int = 33): List<Int> = this.qual.map { it.toInt() - delta }

	val phredScoreList
		get() = this.qualsAsPhredScore()
	
	fun minQual(): Int = phredScoreList.min() ?: 0

	fun maxQual(): Int = phredScoreList.max() ?: 0
	
	fun meanQual(): Int = round(phredScoreList.sum().toDouble().div(length)).toInt()

	fun medianQual(): Int = phredScoreList.map{it.toInt()}.sum().div(length.toDouble()).toInt()

	override fun getLocus(start: Int, end: Int): SeqQual{
		val nstart = if(start < 1) 1 else start
		val nend = if(end > this.length + 1) this.length else end
		val seq = if(nend < nstart) this.sequence.substring(nend - 1, nstart).revComp() else
			this.sequence.substring(nstart - 1, nend)
		val qual = if(nend < nstart) this.qual.reversed().substring(nend - 1, nstart) else
			this.sequence.substring(nstart - 1, nend)
		val head = "${this.id} ($start:$end)"
		return SeqQual(head, seq, qual)
	}

}

fun String.removeGaps(): String = this.filter{it != '-'}

fun String.getGCContent(): Double = this.toUpperCase()
	.count {(it == 'C') || (it == 'G')} .toDouble().div(this.length)

fun String.getNsCount(): Int = this.toUpperCase().count {it == 'N'}

fun String.revComp(): String = this.reversed().map { it.complement() } .joinToString("")

fun Char.complement(): Char{
	return when(this.toUpperCase()) {
		'A' -> 'T'
		'C' -> 'G'
		'G' -> 'C'
		'T' -> 'A'
		else -> 'N'
	}
	}

class DNA(header: String, sequence: String): Sequence(header, sequence) {

	//Provisionally RNA functionality will be here
	private val unambiguous = listOf('A','C','G','T')

	val baseCount: Map<Char,Int>
		get() = mapOf(Pair('A', this.sequence.toUpperCase().count{it == 'A'}),
			Pair('C', this.sequence.toUpperCase().count{it == 'C'}),
			Pair('G', this.sequence.toUpperCase().count{it == 'G'}),
			Pair('T', this.sequence.toUpperCase().count{it == 'T'})
		)

	val GCContent: Double 
		get() = (( baseCount['C'] ?: 0 ) + (baseCount['G'] ?: 0) ).toDouble().div(this.length)

	/* GC skew = (G - C) / (G + C) */
	val GCSkew: Double
		get() = ( (baseCount['G'] ?: 0) - (baseCount['G'] ?: 0) ).toDouble()
			.div( (baseCount['G'] ?: 0) + (baseCount['G'] ?: 0) )

	fun getAmbiguousCount(dna: String): Int = dna.toUpperCase().count { it !in unambiguous }

	fun revComp(): String = this.sequence.reversed().map { it.complement() } .joinToString("")

	fun convertACGTtoACGU(nucl: Char): Char {
		return when(nucl.toUpperCase()) {
			'A' -> 'A'
			'C' -> 'C'
			'G' -> 'G'
			'T' -> 'U'
			else -> 'N'
		}
	}

	fun toRNA(): String = this.sequence.map { convertACGTtoACGU(it) } .joinToString("")
	
}

class RNA(header: String, sequence: String): Sequence(header, sequence){

	fun toDNA(seq: String): String = seq.map { convertACGUtoACGT(it) } .joinToString("")

	fun convertACGUtoACGT(nucl: Char): Char {
		return when(nucl.toUpperCase()) {
			'A' -> 'A'
			'C' -> 'C'
			'G' -> 'G'
			'U' -> 'T'
			else -> 'N'
		}
	}

}

class Protein(header: String, sequence: String): Sequence(header, sequence) {

	fun getAmbiguous(aa: String): Int = aa.toUpperCase().count {it == 'X'}

	//fun getGapLength(dna: String): Int = dna.count({it == '-'})

}