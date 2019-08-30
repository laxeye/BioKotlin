package ru.nrcki.biokotlin

import ru.nrcki.biokotlin.*
import ru.nrcki.biokotlin.io.*
import ru.nrcki.biokotlin.GenomeStats

fun main(args: Array<String>){
	val helpMessage = """
	BioKotlin could work in standalone mode.
	Next commands are now supported:
	GenomeStats <File> - output statistics for Fasta file.
	TetraStats <File> - output statistics for Fasta file.
	AlignmentStats <File> - output statistics for Fasta alignment.
	AlignmentClearGaps <File> <0-100> - treshold gap share to remove column from alignment.
	FilterShortSeqsFasta <File> <minimum length> - filter out short sequences FASTA.
	FilterShortSeqsFastq <File> <minimum length> - filter out short sequences FASTQ.
	FastaToSeqPhylip <File> - convert Fasta alignment to strict sequental Phylip.
	FastqPairing <Forward> <Reverse> - check files for orphaned reads and write sorted.
	"""

	require(args.size>1) {helpMessage}

	when(args[0]){
		"GenomeStats" -> GenomeStats().genomeStats(args[1])
		"TetraStats" -> Tetra().genomeTetraStats(args[1])
		"AlignmentStats" -> Alignment().printAlnInfo(Fasta().read(args[1]))
		"AlignmentClearGaps" -> {
			val gapShare = if(args.size > 2) args[2].toDouble().div(100) else 0.5
			Alignment().clearGappedColumns(Fasta().read(args[1]),gapShare).map{println(it.toString())}
		}
		"FilterShortSeqsFasta" -> {
			Fasta().readAuto(args[1]).filter(){it.length>=args[2].toInt()}.map{println(it.asFasta())}
		}
		"FilterShortSeqsFastq" -> {
			Fastq().readAutoBR(args[1]).filter(){it.length>=args[2].toInt()}.map{println(it.asFastq())}
		}
		"FastaToSeqPhylip" -> println(Alignment().asPhylipSeq(Fasta().read(args[1])))
		"FastqPairing" -> Fastq().fastqPairing(args[1],args[2])
		else -> println(helpMessage)
	}

}