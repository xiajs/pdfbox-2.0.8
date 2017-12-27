package com.xiajs.pdf;

import java.math.BigDecimal;
import java.math.RoundingMode;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
//        assertTrue( true );
    	
    	long i = 3333;
    	long i2 = 3;
    	long i3 = 3333*10000/106*6;
    	BigDecimal d1 = BigDecimal.valueOf(1.06);
    	BigDecimal d = BigDecimal.valueOf(3333);
    	BigDecimal d2 = d.divide(d1, d.scale(), RoundingMode.HALF_EVEN);
    	BigDecimal d3 = d2.multiply(BigDecimal.valueOf(0.06)).setScale(0, RoundingMode.HALF_EVEN);
    	System.out.println(d3.toString());
    }
}
