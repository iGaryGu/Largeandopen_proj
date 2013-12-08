/**
 * This unit test file
 * 
 * you just click the run button in eclipse
 * and you can see if it is successful
 * 
 * I use Junit to test the readFileToString function in the dataParser  class
 * 
 * 
 */
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class dataParsertest {
	private dataParser dataParse;
	@Before
	public void setUp(){
		dataParse = new dataParser();
	}
	
	@After
	public void teardown(){
		dataParse = null;
	}
	
	@Test
	public void testReadFileToString() throws IOException {
		String str = "hello handsome guy!\n";
		String temp = dataParse.readFileToString("../test.txt");
		assertEquals(str,temp);
	}

}
