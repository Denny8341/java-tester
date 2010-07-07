package org.jtester.tutorial01.jmockit;

import java.util.List;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

import org.jtester.testng.JTester;
import org.jtester.tutorial01.beans.PhoneItem;
import org.jtester.tutorial01.daos.PhoneGroupDao;
import org.jtester.tutorial01.daos.impl.PhoneGroupDaoImpl;
import org.jtester.tutorial01.services.PhoneBookService;
import org.jtester.unitils.dbfit.DbFit;
import org.jtester.unitils.spring.SpringBeanFor;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

@Test
@SuppressWarnings("unused")
@SpringApplicationContext( { "spring/data-source.xml", "spring/beans.xml", "spring/sqlmap-config.xml" })
public class UserServiceTest_PartitialMock extends JTester {
	final String groupname = "classmate";

	@SpringBeanByName
	PhoneGroupDao phoneGroupDao;

	@Test
	@DbFit(when = "org/jtester/tutorial/debugit/testFindPhoneItemsByGroupName.wiki")
	public void partialMock1() {
		new MockUp<PhoneGroupDaoImpl>() {
			@Mock
			public long getGroupIdByName(String name) {
				want.string(name).isEqualTo(groupname);
				throw new RuntimeException();
			}
		};
		// ����ԭ�е��߼�
		List<PhoneItem> items = phoneGroupDao.findPhoneItemsByGroupId(1L);
		want.collection(items).sizeEq(2);
		// ���õ���mock�ķ���
		try {
			phoneGroupDao.getGroupIdByName(groupname);
			want.fail();
		} catch (Exception e) {
			want.object(e).clazIs(RuntimeException.class);
		}
	}

	@Test
	@DbFit(when = "org/jtester/tutorial/debugit/testFindPhoneItemsByGroupName.wiki")
	public void partialMock2() {
		
//		want.object(list.iterator().next().getPriceList().get(0)).reflectionEq(price001);
//		List<Object> list = null;
//		want.collection(list).sizeEq(1).reflectionEq(new Object[] {price001});
		new Expectations() {
			@Mocked(methods = "getGroupIdByName")
			PhoneGroupDaoImpl dao;
			{
				when(dao.getGroupIdByName(the.string().isEqualTo(groupname).wanted())).thenReturn(2L);
			}
		};
		List<PhoneItem> items = phoneGroupDao.findPhoneItemsByGroupId(1L);
		want.collection(items).sizeEq(2);
		long groupId = phoneGroupDao.getGroupIdByName(groupname);
		want.number(groupId).isEqualTo(2);
	}

}
