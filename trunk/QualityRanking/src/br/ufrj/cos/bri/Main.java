/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.bri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Heraldo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if (args.length < 1) {
            System.err.println("Use: BRIFinal [operação]");
            System.exit(1);
        }
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/brifinal?user=root&password=123");
        if (args[0].equals("add")) {
            if (args.length < 2) {
                System.err.println("Use: BRIFinal add [url]");
                System.exit(1);
            }
            String url = args[1];
            WebFile wf = new WebFile(url);
            System.out.println("Forward:");
            Map<String, Integer> forwardLinks = wf.getForwardLinks();
            for (String linkUrl : forwardLinks.keySet()) {
                System.out.println(forwardLinks.get(linkUrl) + ": " + linkUrl);
            }
            System.out.println("Back:");
            Map<String, Integer> backLinks = wf.getBackLinks();
            for (String linkUrl : backLinks.keySet()) {
                System.out.println(backLinks.get(linkUrl) + ": " + linkUrl);
            }
        } else if (args[0].equals("graph")) {
        }
    }
}
