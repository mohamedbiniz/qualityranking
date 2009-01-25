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
			Service.fuzzy(dataSet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HibernateDAO.getInstance().closeSession();

	}

}
