package ru.nrcki.biokotlin

import ru.nrcki.biokotlin.io.Fasta
import ru.nrcki.biokotlin.io.Fastq
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
	@Test fun `Reverse complement DNA`() {
		assertEquals("ATGC","GCAT".revComp())
	}

	@Test fun `Sequence length`() {
		assertEquals(4,Sequence("ID","GCAT").length)
	}

	@Test fun `Sequence id`() {
		assertEquals("ID",Sequence("ID [Taxon]","GCAT").id)
	}

	@Test fun `Sequence header`() {
		assertEquals("ID [Taxon]",Sequence("ID [Taxon]","GCAT").header)
	}

	@Test fun `Sequence sequence`() {
		assertEquals("GCAT",Sequence("ID","GCAT").sequence)
	}

	@Test fun `Stringify Fastq with asFastq`() {
		assertEquals("@ID\nGCAT\n+\nFFFF\n",SeqQual("ID","GCAT","FFFF").formatted())
	}

	@Test fun `Stringify Fasta with asFasta`() {
		assertEquals(">ID\nGCAT",Sequence("ID","GCAT").formatted())
	}

	@Test fun `Read Fasta from File with BufferedReader`() {
		assertEquals(Fasta().read("src/test/resources/antiTGFPv1M13F.fasta")[0].formatted(),
			Sequence("antiTGFPv1M13F",
				"AGGCGGATCGATCCAAGGTCGGGCAGGAAGAGGGCCTATTTCCCATGATTCCTTCATATTTGCATATACGATACAAGGCTGTTAGAGAGATAATTGGAATTAATTTGACTGTAAACACAAAGATATTAGTACAAAATACGTGACGTAGAAAGTAATAATTTCTTGGGTAGTTTGCAGTTTTAAAATTATGTTTTAAAATGGACTATCATATGCTTACCGTAACTTGAAAGTATTTCGATTTCTTGGCTTTATATATCTTGTGGAAAGGACGAAACACCGCTGCACGCCATCAACAACGGGTTTTAGAGCTAGAAATAGCAAGTTAAAATAAGGCTAGTCCGTTATCAACTTGAAAAAGTGGCACCGAGTCGGTGCTTTTTTTCTCCGCTGAGCGTACTGAGACGCCGCGGTGGAGCTCCAGCTTTTGTTCCCTTTAGTGAGGGTTAATTGCGCGCTTGGCGTAATCATGGTCATAGCTGTTTCCTGTGTGAAATTGTTATCCGCTCACAATTCCACACAACATACGAGCCGGAAGCATAAAGTGTAAAGCCTGGGGTGCCTAATGAGTGAGCTAACTCACATTAATTGCGTTGCGCTCACTGCCCGCTTTCCAGTCGGGAAACCTGTCGTGCCAGCTGCATTAATGAATCGGCCAACGCGCGGGGAGAGGCGGTTTGCGTATTGGGCGCTCTTCCGCTTCCTCGCTCACTGACTCGCTGCGCTCGGTCGTTCGGCTGCGGC")
			.formatted())
	}

	@Test fun `Read gzipped Fasta from File with BufferedReader`() {
		assertEquals(Fasta().readGzip("src/test/resources/antiTGFPv1M13F.fasta.gz")[0].formatted(),
			Sequence("antiTGFPv1M13F",
				"AGGCGGATCGATCCAAGGTCGGGCAGGAAGAGGGCCTATTTCCCATGATTCCTTCATATTTGCATATACGATACAAGGCTGTTAGAGAGATAATTGGAATTAATTTGACTGTAAACACAAAGATATTAGTACAAAATACGTGACGTAGAAAGTAATAATTTCTTGGGTAGTTTGCAGTTTTAAAATTATGTTTTAAAATGGACTATCATATGCTTACCGTAACTTGAAAGTATTTCGATTTCTTGGCTTTATATATCTTGTGGAAAGGACGAAACACCGCTGCACGCCATCAACAACGGGTTTTAGAGCTAGAAATAGCAAGTTAAAATAAGGCTAGTCCGTTATCAACTTGAAAAAGTGGCACCGAGTCGGTGCTTTTTTTCTCCGCTGAGCGTACTGAGACGCCGCGGTGGAGCTCCAGCTTTTGTTCCCTTTAGTGAGGGTTAATTGCGCGCTTGGCGTAATCATGGTCATAGCTGTTTCCTGTGTGAAATTGTTATCCGCTCACAATTCCACACAACATACGAGCCGGAAGCATAAAGTGTAAAGCCTGGGGTGCCTAATGAGTGAGCTAACTCACATTAATTGCGTTGCGCTCACTGCCCGCTTTCCAGTCGGGAAACCTGTCGTGCCAGCTGCATTAATGAATCGGCCAACGCGCGGGGAGAGGCGGTTTGCGTATTGGGCGCTCTTCCGCTTCCTCGCTCACTGACTCGCTGCGCTCGGTCGTTCGGCTGCGGC")
			.formatted())
	}

	@Test fun `Read bzipped Fasta from File with BufferedReader`() {
		assertEquals(Fasta().readBzip2("src/test/resources/antiTGFPv1M13F.fasta.bz2")[0].formatted(),
			Sequence("antiTGFPv1M13F",
				"AGGCGGATCGATCCAAGGTCGGGCAGGAAGAGGGCCTATTTCCCATGATTCCTTCATATTTGCATATACGATACAAGGCTGTTAGAGAGATAATTGGAATTAATTTGACTGTAAACACAAAGATATTAGTACAAAATACGTGACGTAGAAAGTAATAATTTCTTGGGTAGTTTGCAGTTTTAAAATTATGTTTTAAAATGGACTATCATATGCTTACCGTAACTTGAAAGTATTTCGATTTCTTGGCTTTATATATCTTGTGGAAAGGACGAAACACCGCTGCACGCCATCAACAACGGGTTTTAGAGCTAGAAATAGCAAGTTAAAATAAGGCTAGTCCGTTATCAACTTGAAAAAGTGGCACCGAGTCGGTGCTTTTTTTCTCCGCTGAGCGTACTGAGACGCCGCGGTGGAGCTCCAGCTTTTGTTCCCTTTAGTGAGGGTTAATTGCGCGCTTGGCGTAATCATGGTCATAGCTGTTTCCTGTGTGAAATTGTTATCCGCTCACAATTCCACACAACATACGAGCCGGAAGCATAAAGTGTAAAGCCTGGGGTGCCTAATGAGTGAGCTAACTCACATTAATTGCGTTGCGCTCACTGCCCGCTTTCCAGTCGGGAAACCTGTCGTGCCAGCTGCATTAATGAATCGGCCAACGCGCGGGGAGAGGCGGTTTGCGTATTGGGCGCTCTTCCGCTTCCTCGCTCACTGACTCGCTGCGCTCGGTCGTTCGGCTGCGGC")
			.formatted())
	}

	@Test fun `Read Fastq from file with BufferedReader`() {
		assertEquals(Fastq().read("src/test/resources/SRR030257_1.head.fq")[0].formatted(),
			SeqQual("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").formatted())
	}
/*
	@Test fun `Read Fastq from file with Scanner`() {
		assertEquals(Fastq().readAutoSC("src/test/resources/SRR030257_1.head.fq")[0].asFastq(),
			SeqQual("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
*/
	@Test fun `Read Fastq from gzip file with BufferedReader`() {
		assertEquals(Fastq().read("src/test/resources/SRR030257_1.head.fq.gz")[0].formatted(),
			SeqQual("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").formatted())
	}
/*
	@Test fun `Read Fastq from gzip file with Scanner`() {
		assertEquals(Fastq().readAutoSC("src/test/resources/SRR030257_1.head.fq.gz")[0].asFastq(),
			SeqQual("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
*/
	@Test fun `Read Fastq from bzip2 file with BufferedReader`() {
		assertEquals(Fastq().read("src/test/resources/SRR030257_1.head.fq.bz2")[0].formatted(),
			SeqQual("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").formatted())
	}
/*
	@Test fun `Read Fastq from bzip2 file with Scanner`() {
		assertEquals(Fastq().readAutoSC("src/test/resources/SRR030257_1.head.fq.bz2")[0].asFastq(),
			SeqQual("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
*/
	@Test fun `check GC content String method`() {
		assertEquals(0.5, "ACGT".getGCContent() )
	}
	
	@Test fun `check GC content val`() {
		assertEquals(0.5, DNA("","ACGT").GCContent)
	}
	

	@Test fun `check GC skew val`() {
		assertEquals( (3.0-2.0)/3.0, DNA("","GCGT").GCSkew)
	}
	
	@Test fun `convert DNA to RNA`() {
		assertEquals("ACGU",DNA("","ACGT").toRNA())
	}

	@Test fun `total gap length in sequence`() {
		assertEquals(4, Sequence("ID","G--C-A-T").gaplength)
	}
	
	@Test fun `remove gaps from sequence String method`() {
		assertEquals( "GCAT", Sequence("","G--C-A-T").sequence.removeGaps() )
	}
	
	@Test fun `remove gaps from sequence Sequence method`() {
		assertEquals( "GCAT", Sequence("","G--C-A-T").removeGaps().sequence )
	}
	
	@Test fun `Convert Fasta to Phylip`(){
		assertEquals("10 120",
			Alignment().asPhylipSeq(Fasta().read("src/test/resources/alignment.16s.fasta")).lines()[0])
	}

	@Test fun `Raw distance`(){
		assertEquals(0.0, Distance().rawDistance(Sequence("ID","-ACG"),Sequence("ID","-ACG")))
	}

	@Test fun `Jukes-Cantor distance`(){
		assertEquals(0.0, abs( Distance().jcDistance( Sequence("ID","TACG"), Sequence("ID","TACG") ) ) )
	}


}