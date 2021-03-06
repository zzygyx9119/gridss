package au.edu.wehi.idsv.ncbi;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TaxonomyHelperTest {
    @Test
    public void shouldParseNodesDmp() throws IOException {
        TaxonomyNode[] result = TaxonomyHelper.toArray(TaxonomyHelper.parseFull(new File("src/test/resources/ncbi/test.nodes.dmp")));
        Assert.assertEquals(2758539 + 1, result.length);
        Assert.assertEquals("no rank", result[1].rank);
        Assert.assertEquals(1, result[1].taxId);
        Assert.assertEquals(1, result[1].parentTaxId);

        Assert.assertEquals("species", result[2758539].rank);
        Assert.assertEquals(2758539, result[2758539].taxId);
        Assert.assertEquals(655722, result[2758539].parentTaxId);

        Assert.assertEquals(1707, result[11].parentTaxId);
        Assert.assertEquals("species", result[11].rank);
        Assert.assertEquals("CG", result[11].emblCode);
        Assert.assertEquals("effective current name;", result[11].comments);
    }

    @Test
    public void lookupShouldIncludeEntireTree() throws IOException {
        boolean[] lookup = TaxonomyHelper.createInclusionLookup(ImmutableList.of(9606), TaxonomyHelper.parseMinimal(new File("src/test/resources/ncbi/homo_sapiens.nodes.dmp")));
        Assert.assertTrue(lookup[9606]);
        Assert.assertFalse(lookup[9605]);

        lookup = TaxonomyHelper.createInclusionLookup(ImmutableList.of(8287), TaxonomyHelper.parseMinimal(new File("src/test/resources/ncbi/homo_sapiens.nodes.dmp")));
        Assert.assertTrue(lookup[9606]);
        Assert.assertTrue(lookup[376912]);
        Assert.assertTrue(lookup[376913]);
        Assert.assertFalse(lookup[117571]);

        lookup = TaxonomyHelper.createInclusionLookup(ImmutableList.of(314293, 376912), TaxonomyHelper.parseMinimal(new File("src/test/resources/ncbi/homo_sapiens.nodes.dmp")));
        Assert.assertTrue(lookup[9606]);
        Assert.assertTrue(lookup[376912]);
        Assert.assertTrue(lookup[314293]);
        Assert.assertFalse(lookup[9443]);
    }
    @Test
    public void leadNodesShouldReturnOnlyLeaves() throws IOException {
        boolean[] lookup = TaxonomyHelper.leafNodes(TaxonomyHelper.parseMinimal(new File("src/test/resources/ncbi/homo_sapiens.nodes.dmp")));
        for (int i : new int[] {9606,376912,10239,}) {
            Assert.assertTrue(lookup[i]);
        }
        for (int i : new int[] {1, 2759, 6072, 7711, 7742, 7776, 8287, 9347, 9443, 9526, 9604, 9605, 32523, 32524, 32525, 33154, 33208, 33213, 33511, 40674, 89593, 117570, 117571, 131567, 207598, 314146, 314293, 314295, 376913, 1338369,}) {
            Assert.assertFalse(lookup[i]);
        }
    }
}