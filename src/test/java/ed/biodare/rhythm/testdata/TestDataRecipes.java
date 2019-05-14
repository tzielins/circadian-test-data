/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author tzielins
 */
public class TestDataRecipes {
    
    double EPS = 1E-6;
    
    @Rule
    public TemporaryFolder testFolder= new TemporaryFolder();    
    
    Path outRoot = Paths.get("E:\\Temp\\Rhythmicity_tests");
    Path mainOutDir;
    
    public TestDataRecipes() {
    }
    
    @Before
    public void setUp() {
        
        mainOutDir = outRoot.resolve(LocalDate.now().toString());
        
    }

    /**
     * Data Set for testing python vs java implementation.
     * 2 days of data sampled every two and one hour,
     * 24h periods, various phases, noises 0.5 and 0.25
     */
    //@Test
    public void recipePythonVSJava() throws Exception {
        
        Path outDir = mainOutDir.resolve("python_comp");
        DataRecipes.recipePythonVSJava(outDir);
        
    }
    
    
}
