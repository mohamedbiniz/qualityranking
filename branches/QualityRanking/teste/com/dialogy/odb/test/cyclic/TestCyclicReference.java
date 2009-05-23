package com.dialogy.odb.test.cyclic;

import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.core.query.soda.Soda;
import com.dialogy.odb.core.query.soda.SodaQuery;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.country.City;
import com.dialogy.odb.test.vo.country.Country;
import com.dialogy.odb.tool.IOUtil;

public class TestCyclicReference extends ODBTestCase {

    public void setUp() throws Exception {
        Configuration.setDebugEnabled(true);
        ODB odb = ODB.open("cyclic.odb");

        for(int i=0;i<1;i++){
            City brasilia = new City("Brasilia"+i);
            Country brasil = new Country("Brasil"+i);

            brasilia.setCountry(brasil);
            brasil.setCapital(brasilia);

            odb.store(brasil);
        }
        odb.commitAndClose();

    }

    public void test1() throws Exception {
        ODB odb = ODB.open("cyclic.odb");
        List l = odb.getObjectsOf(Country.class, true);
        System.out.println(l.toString());
        Country country = (Country) l.get(0);
        assertEquals("Brasil0",country.getName());
        assertEquals("Brasilia0",country.getCapital().getName());
        odb.close();
    }

    public void test2() throws Exception {
        ODB odb = ODB.open("cyclic.odb");
        List l = odb.getObjectsOf(Country.class, true);
        System.out.println(l.toString());
        Country country = (Country) l.get(0);

        City city = new City("rio de janeiro");
        country.setCapital(city);

        odb.store(country);

        odb.commitAndClose();

        odb = ODB.open("cyclic.odb");
        l = odb.getObjectsOf(Country.class, true);
        System.out.println(l.toString());
        country = (Country) l.get(0);
        assertEquals("rio de janeiro",country.getCapital().getName());
        l = odb.getObjects(new SodaQuery(City.class, Soda.equal("name","rio de janeiro")));
        System.out.println(l);
        assertEquals(1,l.size());
        odb.close();


    }

    protected void tearDown() throws Exception {
        IOUtil.deleteFile("cyclic.odb");
    }


}
