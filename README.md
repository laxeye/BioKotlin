# BioKotlin

[Kotlin programming language](https://kotlinlang.org/) realisation of bioinformatics routine operations and file handling.
FASTA, FASTQ and GFF basics are available. 

Now You can use following commands as:
`java -jar bioKotlin.jar <command> <command arguments>`

* GenomeStats <File> - output statistics (N50, GC etc.) for Fasta file.
* TetraStats <File> - output tetranucleotide composition for each contig for Fasta file.
* AlignmentStats <File> - output statistics for Fasta alignment.
* AlignmentClearGaps <File> <0-100> - treshold gap share to remove column from alignment. Integer value, columns containng less than this value still in the alignment.
* FastaToSeqPhylip <File> [nostrict] - convert Fasta alignment sequental Phylip format. It may produce duplicated IDs in strict mode. Some software can use only strict Phylip format (like PHYLIP programs), while other (e.g. FastME) may use "non-strict" alignments.
* FastqPairing <Forward> <Reverse> - check files for orphaned reads and write sorted.
* FilterShortSeqsFasta <File> <minimum length> - filter out short sequences FASTA.
* FilterShortSeqsFastq <File> <minimum length> - filter out short sequences FASTQ.

Data will be printed to standard output.	

To see a short help message just run without any commands and arguments:
`java -jar bioKotlin.jar`

## Compilation

Use [Gradle](https://gradle.org) to build the project:

`gradle jar`

## Tests

Run `gradle test` to perform few unit tests. Hopefully we'll have more in the future.

## Project structure

- source src/main/kotlin/ru/nrcki/bioKotlin
- tests src/test/kotlin
- test data src/test/resources

## Features

- Read FASTA (uncompressed, gzip, bzip2) and FASTQ (uncompressed, gzip and bzip2) from files.
- Get any locus of sequence from FASTA file without prior indexing (reverse complement if start > end).
- Filter sequences by length.
- Genome statistics from FASTA: N50, L50, N90, L90, GC(%), N's.
- GFF parsing.
- NCBI BLASTn results parsing for JSON (-outfmt 15).
- Tetranucleotide composition statistics.
- Removing orphan reads from paired-end FASTQ files (memory unefficient, but working).

## ToDo
- **API description.**
- DNA and RNA translation.
- More tests.
- Catch more exceptions.
- NCBI BLASTp results parsing.
- HMMER results parsing.
- ...
