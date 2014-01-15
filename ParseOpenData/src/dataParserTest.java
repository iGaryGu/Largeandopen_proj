import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class dataParserTest {
	
//	ParseUrl parseurl = null; 
	
//	@Before
//	public void setUp(){
////		parseurl = new ParseUrl();
//	}
//	
//	@After
//	public void teardown(){
////		parseurl=null;
//	}

	@Test
	public void testGetAddr() {
		String str = "711臺南市歸仁區七甲村丁厝街52巷63弄25號";
		String temp = ParseUrl.getAddr("COAF0001");		
		assertEquals(str,temp);
	}


}
