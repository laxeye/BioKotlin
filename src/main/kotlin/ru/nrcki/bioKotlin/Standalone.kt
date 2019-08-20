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
	FastaToSeqPhylip <File> - convert Fasta alignment to strict sequental Phylip.
	FastqPairing <Forward> <Reverse> - check files for orphaned reads and write sorted.
	"""

	require(args.size>1) {helpMessage}

	when(args[0]){
		"GenomeStats" -> GenomeStats().genomeStats(args[1])
		"TetraStats" -> Tetra().genomeTetraStats(args[1])
		"AlignmentStats" -> Alignment().prinAlnInfo(Fasta().read(args[1]))
		"FastaToSeqPhylip" -> println(Alignment().asPhylipSeq(Fasta().read(args[1])))
		"FastqPairing" -> Fastq().fastqPairing(args[1],args[2])
		else -> println(helpMessage)
	}

}