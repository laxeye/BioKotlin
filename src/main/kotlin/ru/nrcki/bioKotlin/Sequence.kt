package ru.nrcki.bioKotlin
import kotlin.math.min
import kotlin.math.max

open class Sequence(val header: String, val sequence: String) {
	
	fun getGapLength(sequence: String): Int = sequence.count({it == '-'})

	fun getGapShare(sequence: String): Double = sequence.count({it == '-'}).times(1.0)
		.div(sequence.length)

	fun removeGaps(): String = this.sequence.filter{it != '-'}

	val id = header.split(" ")[0]
	
	val length = sequence.length

	val gaplength: Int
		get() = getGapLength(sequence)

	val xcount: Int 
		get() = sequence.toUpperCase().count({it == 'X'})

	val ncount: Int
		get() = sequence.toUpperCase().count({it == 'N'})

	val _width = 60

	fun formatFasta(seq: String, w: Int = _width): String{
		val lineList = mutableListOf<String>()
		val steps = seq.length.div(w)
		for (i in 0..steps-1){
			lineList.add(seq.substring(w*i,w*(i+1)))
		}
		lineList.add(seq.substring(w*steps))
		return lineList.joinToString(separator="\n",prefix="",postfix="")
	}

	fun asFasta(): String = ">$header\n${formatFasta(sequence)}"

	fun asAlnFasta(): String = ">$header\n$sequence"

	open fun getChar(pos: Int): Char = this.sequence.get(pos)

	open fun getLocus(start: Int, end: Int): Sequence{
		val nstart = if(start < 1) 1 else start
		val nend = if(end > this.length + 1) this.length else end
		val seq = if(nend < nstart) this.sequence.substring(nend - 1, nstart).revComp() else
			this.sequence.substring(nstart - 1, nend)
		val head = "${this.id} ($start:$end)"
		return Sequence(head, seq)
	}

}

open class SeqQual(header: String, sequence: String, val qual: String): Sequence(header, sequence){
	
	fun asFastq(): String = "@$header\n$sequence\n+\n$qual\n"
	
	fun qualsAsIntList(): List<Int> = qual.map{it.toInt()}
	
	fun minQual(): Int = qual.map{it.toInt()}.min() ?: 0

	fun maxQual(): Int = qual.map{it.toInt()}.max() ?: 0
	
	fun meanQual(): Int = qual.map{it.toInt()}.sum().div(length.toDouble()).toInt()

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
	.count({(it == 'C') || (it == 'G')}).times(1.0).div(this.length)

fun String.getNsCount(): Int = this.toUpperCase().count({it == 'N'})

fun String.revComp(): String = this.reversed().map { it.complement() } .joinToString("")

fun Char.complement(): Char{
		when(this.toUpperCase()) {
			'A' -> return 'T'
			'C' -> return 'G'
			'G' -> return 'C'
			'T' -> return 'A'
			else -> return 'N'
		}
	}

class DNA(header: String, sequence: String): Sequence(header, sequence) {

	//Provisionally RNA functionality will be here
	val _unambiguous = listOf('A','C','G','T','U')
	
	val GCContent: Double 
		get() = this.sequence.toUpperCase()
		.count({(it == 'C') || (it == 'G')}).times(1.0).div(this.length)

	// GCskew = (G - C) / (G + C)
	fun getGCSkew(): Double = (this.sequence.toUpperCase()
		.count({it == 'G'}) - this.sequence.toUpperCase().count({it == 'C'}))
		.times(1.0).div(this.sequence.toUpperCase().count({(it == 'C') || (it == 'G')}))

	fun getAmbiguous(dna: String): Int = dna.toUpperCase().count({ !( it in _unambiguous ) })

	//fun getGapLength(dna: String): Int = dna.count({it == '-'})
	
	fun revComp(seq: String): String = seq.reversed().map { it.complement() } .joinToString("")

	fun ACGTtoACGU(nucl: Char): Char {
		when(nucl.toUpperCase()) {
			'A' -> return 'A'
			'C' -> return 'C'
			'G' -> return 'G'
			'T' -> return 'U'
			else -> return 'N'
		}
	}

	fun toRNA(): String = this.sequence.map { ACGTtoACGU(it) } .joinToString("")
	
}

class RNA(header: String, sequence: String): Sequence(header, sequence){

	fun toDNA(seq: String): String = seq.map { ACGUtoACGT(it) } .joinToString("")

	fun ACGUtoACGT(nucl: Char): Char {
		when(nucl.toUpperCase()) {
			'A' -> return 'A'
			'C' -> return 'C'
			'G' -> return 'G'
			'U' -> return 'T'
			else -> return 'N'
		}
	}

}

class Protein(header: String, sequence: String): Sequence(header, sequence) {

	fun getAmbiguous(aa: String): Int = aa.toUpperCase().count({it == 'X'})

	//fun getGapLength(dna: String): Int = dna.count({it == '-'})

}