/* 
 * $RCSfile: ODBTestCase.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:54 $
 * 
 * Copyright 2003 Cetip
 */
package com.dialogy.odb.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.dialogy.odb.core.session.Context;

public class ODBTestCase extends TestCase {
    private static Map map = new HashMap();
    private boolean errorWhileCheckingSession ;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        //Context.killCurrentSession();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */

    protected void tearDown() throws Exception {
        if(Context.existSession()){
            map.put(Thread.currentThread(),Boolean.TRUE);
        }
    }
    
    public void testzzzzz(){
        Boolean b = (Boolean) map.get(Thread.currentThread());
        if(b!=null){
            map.remove(Thread.currentThread());
            fail("The Junit " + getClass().getName() + " did close its session!");
        }
    }
}
