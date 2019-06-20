# BioKotlin

[Kotlin programming language](https://kotlinlang.org/) realisation of bioinformatics routine operations and file handling.
FASTA, FASTQ and GFF basics are available. 
There is a genome information module to collect some useful stats like n50, GC% etc.

`java -jar bioKotlin.jar /path/to/genome.fasta`

## Compilation

Use Gradle to build the project:

`gradle jar`

## Tests

Run `gradle test` to perform few unit tests. Hopefully we'll have more in the future.

## Project structure

- source src/main/kotlin/ru/nrcki/bioKotlin
- tests src/test/kotlin
- test data src/test/resources

##Features

- Reading FASTA (uncompressed) and FASTQ (uncompressed, gzip and bzip2) from files.
- Getting any locus of sequence from FASTA file without prior indexing (reverse complement if strart > end).
- Genome statistics from FASTA: N50, L50, N90, L90, GC(%), N's.
- GFF parsing.
- NCBI BLASTn results parsing for JSON (-outfmt 15).
- Tetranucleotide composition statistics.
- Removing orphan reads from paired-end FASTQ files (memory unefficient, but working).

##ToDo
- API description
- DNA<->RNA, translation.
- More tests.
- NCBI BLASTp results parsing.
- HMMER results parsing.
- ...
