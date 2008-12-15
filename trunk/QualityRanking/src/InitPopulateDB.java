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
			PopulateDB.limparDB();
			PopulateDB.initFoxSet();
			// PopulateDB.popularSearchPofN(5);
			// PopulateDB.popularSearchQF(5);
			// PopulateDB.popularTradicionalEconomia();
			// PopulateDB.popularTradicionalBDR();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
