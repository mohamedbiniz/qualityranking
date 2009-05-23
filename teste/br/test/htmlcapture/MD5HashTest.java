package br.test.htmlcapture;

import junit.framework.TestCase;

import java.io.*;

import br.ufrj.htmlbase.io.MD5Hash;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 2, 2006
 * Time: 2:45:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class MD5HashTest extends TestCase {

    public void testHash(){
        File file = new File("/opt/lixo/Page.java");

        MD5Hash md5 = MD5Hash.digest(file);

        System.out.println(md5.halfDigest());

    }



}
