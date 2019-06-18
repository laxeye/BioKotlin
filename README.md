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

- FASTA, FASTQ and gzipped FASTQ reading from files.
- Getting any locus of sequence from FASTA file.
- Genome statistics from FASTA: N50, L50, N90, L90, GC(%), N's.
- GFF parsing.
- Tetranucleotide composition statistics.
- Removing orphan reads from paired-end FASTQ files (memory unefficient, but working).
