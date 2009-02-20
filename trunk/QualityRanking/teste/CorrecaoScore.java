import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.db.HibernateDAO;
import br.ufrj.cos.services.Service;

public class CorrecaoScore {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		HibernateDAO.getInstance().openSession();
		DataSet dataSet = (DataSet) HibernateDAO.getInstance().loadById(
				DataSet.class, new Long(578));
		try {
			Service.fuzzyDataSet(dataSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HibernateDAO.getInstance().closeSession();
		// String l = "I";
		// for (int i = 11; i < 556; i++) {
		// System.out
		// .println(String
		// .format(
		// "=SE(%s%d<C2;B2;SE(%s%d<C3;B3;SE(%s%d<C4;B4;SE(%s%d<C5;B5;B6))))",
		// l, i, l, i, l, i, l, i));
		// }

	}

}
