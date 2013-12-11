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
	private String id;
	private dataParser dataParse;
//	private parseUrl parseurl;
	dataParsertest(String farmid) {
		id = farmid;
	}
	@Before
	public void setUp(){
		dataParse = new dataParser();
//		parseurl = new parseUrl(id);
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
	
	public void testrparseUrl(){
		boolean isfind = false;
		parseUrl parseurl = new parseUrl(id);
		
		if(parseurl.getAddr()!=null){
			isfind = true;
		}
		else{
			isfind = false;
		}
		assertEquals(true,isfind);
	}

}
