package org.jtester.unitils.dbwiki;

import java.util.Collection;

import org.jtester.dbtest.bean.Address;
import org.jtester.dbtest.bean.User;
import org.jtester.dbtest.service.UserService;
import org.jtester.testng.JTester;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.unitils.core.Unitils;
import org.unitils.dbunit.datasetfactory.DataSetFactory;
import org.unitils.dbunit.util.MultiSchemaDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

@Test(groups = { "JTester" })
@SpringApplicationContext( { "classpath:/org/jtester/dbtest/spring/project.xml" })
public class WikiDbUnitModuleTest extends JTester {
	@SpringBeanByType
	private UserService userService;

	@Test
	@WikiDataSet("get user.wiki")
	public void getUser() {
		User user1 = userService.getUser(1);
		want.object(user1).notNull();
		User user2 = userService.getUser(2);
		want.object(user2).notNull();

		User user3 = userService.getUser(3);
		want.object(user3).isNull();
		User user4 = userService.getUser(4);
		want.object(user4).isNull();
	}

	@Test
	@WikiDataSet( { "lazy address.wiki" })
	public void getUser_LazyAddress() {
		User user = userService.getUser(1);
		want.object(user).notNull();

		want.object(user.getAddresses()).notNull();
		Collection<Address> addresses = user.getAddresses();
		want.object(addresses.size()).isEqualTo(1);
		for (Address address : addresses) {
			want.string(address.getCity()).contains("city");
		}
	}

	private WikiDbUnitModule module = null;

	@BeforeClass
	public void setup() {
		module = new WikiDbUnitModule();
		module.init(Unitils.getInstance().getConfiguration());
	}

	@Test
	public void getDataSet() throws SecurityException, NoSuchMethodException {
		MultiSchemaDataSet dataSet = module.getDataSet(this.getClass().getMethod("getUser"), this);
		want.object(dataSet).notNull();
	}

	@Test
	public void getDefaultDataSetFactory() {
		DataSetFactory factory = module.getDefaultDataSetFactory();
		want.object(factory).type(MultiSchemaWikiDataSetFactory.class);
	}
}
