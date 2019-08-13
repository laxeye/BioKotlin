package ru.nrcki.bioKotlin

import kotlin.test.assertEquals
import kotlin.test.Test
import ru.nrcki.bioKotlin.*

class AppTest {
	@Test fun reverseComplementDNA() {
		assertEquals("ATGC",DNA().revComp("GCAT"))
	}
	@Test fun `length of Record`() {
		assertEquals(4,Fasta.Record("ID","GCAT").length)
	}
	@Test fun `id of Record`() {
		assertEquals("ID",Fasta.Record("ID [Taxon]","GCAT").id)
	}
	@Test fun headerOfRecord() {
		assertEquals("ID [Taxon]",Fasta.Record("ID [Taxon]","GCAT").header)
	}
	@Test fun sequenceOfRecord() {
		assertEquals("GCAT",Fasta.Record("ID","GCAT").sequence)
	}
	@Test fun stringifyFastq() {
		assertEquals("@ID\nGCAT\n+\nFFFF\n",Fastq.Record("ID","GCAT","FFFF").asFastq())
	}
	@Test fun stringifyFasta() {
		assertEquals(">ID\nGCAT",Fasta.Record("ID","GCAT").asFasta())
	}
	@Test fun readFastqFromFileBufferedReader() {
		assertEquals(Fastq().readAutoBR("src/test/resources/SRR030257_1.head.fq")[0].asFastq(),
			Fastq.Record("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
	@Test fun readFastqFromFileScanner() {
		assertEquals(Fastq().readAutoSC("src/test/resources/SRR030257_1.head.fq")[0].asFastq(),
			Fastq.Record("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
	@Test fun `read Fastq from gzip file with BufferedReader`() {
		assertEquals(Fastq().readAutoBR("src/test/resources/SRR030257_1.head.fq.gz")[0].asFastq(),
			Fastq.Record("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
	@Test fun `read Fastq from gzip file with Scanner`() {
		assertEquals(Fastq().readAutoSC("src/test/resources/SRR030257_1.head.fq.gz")[0].asFastq(),
			Fastq.Record("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
	@Test fun `read Fastq from bzip2 file with BufferedReader`() {
		assertEquals(Fastq().readAutoBR("src/test/resources/SRR030257_1.head.fq.bz2")[0].asFastq(),
			Fastq.Record("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
	@Test fun `read Fastq from bzip2 file with Scanner`() {
		assertEquals(Fastq().readAutoSC("src/test/resources/SRR030257_1.head.fq.bz2")[0].asFastq(),
			Fastq.Record("SRR030257.1 HWI-EAS_4_PE-FC20GCB:6:1:385:567/1",
				"TTACACTCCTGTTAATCCATACAGCAACAGTATTGG",
				"AAA;A;AA?A?AAAAA?;?A?1A;;????566)=*1").asFastq())
	}
	@Test fun `check GC content`() {
		assertEquals(0.5,DNA().getGCContent("ACGT"))
	}
	
	@Test fun `check GC skew`() {
		assertEquals((3.0-2.0)/3.0,DNA().getGCSkew("GCGT"))
	}
	
	@Test fun `convert DNA to RNA`() {
		assertEquals("ACGU",DNA().toRNA("ACGT"))
	}

	@Test fun `total gap length in Record`() {
		assertEquals(4,Fasta.Record("ID","G--C-A-T").gaplength)
	}
	
	@Test fun `remove gaps from Record`() {
		assertEquals("GCAT",Sequence().removeGaps("G--C-A-T"))
	}
	
}