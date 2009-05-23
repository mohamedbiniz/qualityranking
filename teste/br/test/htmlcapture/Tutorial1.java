package br.test.htmlcapture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.core.query.IQuery;
import com.dialogy.odb.core.query.nq.ISimpleNativeQuery;
import com.dialogy.odb.core.query.soda.Soda;
import com.dialogy.odb.core.query.soda.SodaQuery;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.test.vo.login.Profile;
import com.dialogy.odb.test.vo.login.User;
import com.dialogy.odb.tool.IOUtil;


/**
 * This is a simple tutorial
 * <pre>
 * It shows how to persist simple POJO classes :
 * - User
 * - Profile
 * - Function
 *
 * A user has a name, an email and a profile
 * A Profile has a list of Functions
 * A Function has a name.
 *
 * The classes User,Profile and Function are in the test/com.dialogy.odb.test.vo.login package
 *
 * </pre>
 * @author olivier
 *
 */
public class Tutorial1 {
	public static final String ODB_FILE_NAME = "tutorial1.odb";

	public void init(){
		System.out.println("Deleting odb tutorial file");
		IOUtil.deleteFile(ODB_FILE_NAME);
	}
	/**Persist a new user and gets it back from database
	 *
	 * @throws Exception
	 */
	public void step1() throws Exception{

		System.out.println("\nSTEP 1: Insert a new user\n");
		// Open the dialogy odb database file
		ODB odb = ODB.open(ODB_FILE_NAME);

		// Creates a profile
		Profile profile = new Profile("operator 1");

		// Adds functions
		profile.addFunction(new Function("login"));
		profile.addFunction(new Function("logout"));

		// Creates the user
		User user = new User("olivier","olivier@dialogy.com",profile);

		// Stores the user
		odb.store(user);

		// Commit the changes
		odb.commit();

		// retrieve objects from database

		// Gets all the users
		List users = odb.getObjectsOf(User.class,true);

		// Gets all the profiles
		List profiles = odb.getObjectsOf(Profile.class,true);

		// Gets all the functions
		List functions = odb.getObjectsOf(Function.class,true);

		// Displays the functions
		System.out.println("Functions : " + functions);

		// Displays the profiles
		System.out.println("Profiles  : " + profiles);

		// Displays the users
		System.out.println("Users     : "+users);

		// Close the database
		odb.close();
	}

	/** Updates a user
	 *
	 * @throws Exception
	 */
	public void step2() throws Exception{

		System.out.println("\nSTEP 2 : Update the first user\n");
		// Open the dialogy odb database file
		ODB odb = ODB.open(ODB_FILE_NAME);

		// Gets all the users
		List users = odb.getObjectsOf(User.class,true);

		// Gets the first
		User user = (User) users.get(0);

		user.setName("Olivier Smadja");

		// Stores the user
		odb.store(user);

		// Commit the changes
		odb.commit();

		// retrieve objects from database


		// Gets all the profiles
		List profiles = odb.getObjectsOf(Profile.class,true);

		// Gets all the functions
		List functions = odb.getObjectsOf(Function.class,true);

		// Displays the functions
		System.out.println("Functions : " + functions);

		// Displays the profiles
		System.out.println("Profiles  : " + profiles);

		// Displays the users
		System.out.println("Users     : "+users);

		// Close the database
		odb.close();
	}

	/** Select using SODA Query
	 * @throws Exception
	 *
	 *
	 */
	public void step3() throws Exception{

		// Builds a soda query that selects all users with a name that contains 'livi'
		IQuery sodaQuery = new SodaQuery(User.class, Soda.like("name","%livi%"));

		System.out.println("\nSTEP 3 : Select using SODA Query\n");

		// Open the dialogy odb database file
		ODB odb = ODB.open(ODB_FILE_NAME);

		// Gets objects that match the query!
		List list = odb.getObjects(sodaQuery);

		System.out.println(list);
		odb.close();

	}

	/** Select using Native Query
	 * @throws Exception
	 *
	 *
	 */
	public void step4() throws Exception{
		// Builds a native query that matches all users that have a name that contains 'livi'
		IQuery nquery = new ISimpleNativeQuery(){
			public boolean match(User user){
				return user.getName().indexOf("livi")!=-1;
			}
		};

		System.out.println("\nSTEP 4 : Select using Native Query\n");
		// Open the dialogy odb database file
		ODB odb = ODB.open(ODB_FILE_NAME);

		List list = odb.getObjects(nquery);

		System.out.println(list);
		odb.close();

	}
	public static void main(String[] args) throws Exception {
		Tutorial1 tutorial1 = new Tutorial1();

		tutorial1.init();
		tutorial1.step1();
		tutorial1.step2();
		tutorial1.step3();
		tutorial1.step4();
	}


}
