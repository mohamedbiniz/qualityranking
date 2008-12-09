import br.ufrj.cos.db.PopulateDB;

/**
 * 
 */

/**
 * @author Fabricio
 * 
 */
public class TestePopulateDB {
	public static void main(String[] args) {
		try {
			PopulateDB populate = (new PopulateDB());
			populate.limparDB();
			populate.popularSearch();
			// populate.popularTradicionalEconomia();
			// populate.popularTradicionalBDR();
			// populate.teste();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
