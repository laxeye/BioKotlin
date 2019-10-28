package ru.nrcki.biokotlin

import ru.nrcki.biokotlin.io.Fasta
import ru.nrcki.biokotlin.io.Fastq
import java.io.File

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
	FastaToSeqPhylip <File> [nostrict]- convert Fasta alignment to strict sequental Phylip. 
	When "nostrict" mode used name of sequnces will not be trimmed to 9 symbols.
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
			Fasta().read(args[1]).filter{it.length>=args[2].toInt()}.map{println(it.formatted())}
		}
		"FilterShortSeqsFastq" -> {
			Fastq().read(args[1]).filter{it.length>=args[2].toInt()}.map{println(it.formatted())}
		}
		"RemoveByName" -> {
			val removeList = File(args[2]).readLines()
			Fasta().read(args[1]).filter{ it.id !in removeList }.map{println(it.formatted())}
		}
		"ExtractByName" -> {
			val extractList = File(args[2]).readLines()
			Fasta().read(args[1]).filter{ it.id in extractList }.map{println(it.formatted())}
		}
		"FastaToSeqPhylip" -> {
			if((args.size > 2) && (args[2] == "nostrict")){
				println(Alignment().asPhylipSeq(Fasta().read(args[1]), false))
			}else{
				println(Alignment().asPhylipSeq(Fasta().read(args[1])))
			}
		}
		"FastqPairing" 		-> Fastq().fastqPairing(args[1],args[2])
		"DistMatNuclJC" 	-> Distance().jcMatrix(Fasta().read(args[1]),true)
		"DistMatProtJC" 	-> Distance().jcMatrix(Fasta().read(args[1]),false)
		"DistMeanProtJC" 	-> Distance().jcMeanDistance(Fasta().read(args[1]),false)
		else -> System.err.print(helpMessage)
	}

}