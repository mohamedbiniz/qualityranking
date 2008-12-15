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

			// Descomentar abaixo para inicializar o BD a fim de permitir
			// sistema entrar em produ��o de modo correto
			// PopulateDB.initFoxSet();

			// Descomentar abaixo para testar o pOfN (contexto economia)
			PopulateDB.popularSearchPofN(5);

			// Descomentar abaixo para testar o quality fuzzy (contexto
			// economia)
			// PopulateDB.popularSearchQF(5);

			// Descomentar abaixo para testar o crawler com contexto de economia
			// PopulateDB.popularTradicionalEconomia();

			// Descomentar abaixo para testar o crawler com contexto de banco de
			// dados relacinal
			// PopulateDB.popularTradicionalBDR();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
