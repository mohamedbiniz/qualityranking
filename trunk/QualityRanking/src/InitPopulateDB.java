import br.ufrj.cos.db.PopulateDB;

/**
 * 
 */

/**
 * @author Fabricio
 * 
 */
public class InitPopulateDB {
	public static void main(String[] args) {
		try {
			PopulateDB populate = (new PopulateDB());
			populate.limparDB();
			populate.initFoxSet();
			// populate.popularSearch();
			// populate.popularTradicionalEconomia();
			// populate.popularTradicionalBDR();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
