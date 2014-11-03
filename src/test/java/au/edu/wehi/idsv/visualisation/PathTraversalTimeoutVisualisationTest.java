package au.edu.wehi.idsv.visualisation;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import au.edu.wehi.idsv.AssemblyEvidenceSource;
import au.edu.wehi.idsv.IntermediateFilesTest;
import au.edu.wehi.idsv.ProcessingContext;
import au.edu.wehi.idsv.SAMEvidenceSource;

import com.google.common.collect.ImmutableList;


public class PathTraversalTimeoutVisualisationTest extends IntermediateFilesTest {
	@Test
	public void should_export_on_path_timeout_gexf() throws IOException {
		File output = new File(super.testFolder.getRoot(), "chr12-244000.vcf");
		setReference(new File("C:/dev/chr12.fa"));
		createInput(new File("src/test/resources/chr12-244000.bam"));
		SAMEvidenceSource ses = new SAMEvidenceSource(getCommandlineContext(), input, false);
		ses.process();
		AssemblyEvidenceSource aes = new AssemblyEvidenceSource(getCommandlineContext(), ImmutableList.of(ses), output);
		aes.ensureAssembled();
		assertTrue(new File(new File(super.testFolder.getRoot(), "visualisation"), "pathTraversalTimeout-1.subgraph.gexf").exists());
	}
	@Override
	public ProcessingContext getCommandlineContext(boolean perChr) {
		ProcessingContext pc = super.getCommandlineContext(perChr);
		pc.getAssemblyParameters().maxBaseMismatchForCollapse = 1;
		pc.getAssemblyParameters().collapseBubblesOnly = false;
		pc.getAssemblyParameters().debruijnGraphVisualisationDirectory = new File(super.testFolder.getRoot(), "visualisation");
		pc.getAssemblyParameters().visualiseTimeouts = true;
		pc.getAssemblyParameters().maxPathTraversalNodes = 0;
		return pc;
	}
}