package br.ufrj.htmlbase;

import java.util.Collection;
import java.util.Iterator;

public class Teste {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GenerateJung g = new GenerateJung();
		int i = 0;
		Collection vertices = g.obterVertices();
		
		Iterator iter = vertices.iterator();
		System.out.println("*Vertices "+vertices.size());
		while (iter.hasNext()) {
			PageCrawler element = (PageCrawler) iter.next();
			
			System.out.println(++i + " \""+element.url+"\"");
			
		}
		
		
		
		

	}

}
