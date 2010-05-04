package org.jtester.tutorial.debugit;

import java.util.List;

import org.jtester.tutorial.beans.PhoneGroup;
import org.jtester.tutorial.beans.PhoneItem;
import org.jtester.tutorial.daos.PhoneGroupDao;
import org.jtester.tutorial.daos.PhoneItemDao;
import org.jtester.tutorial.services.PhoneBookService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DebugIt {
	public static void main(String[] args) {
		// ��ʼ������
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
				"spring/data-source.xml", "spring/beans.xml" });
		// ׼������
		PhoneGroupDao phoneGroupDao = (PhoneGroupDao) context.getBean("phoneGroupDao");
		long groupId = phoneGroupDao.insertPhoneGroup(new PhoneGroup("classmate"));
		PhoneItemDao phoneItemDao = (PhoneItemDao) context.getBean("phoneItemDao");
		long phoneId1 = phoneItemDao.insertPhoneItem(new PhoneItem("darui.wu", "15900001111"));
		phoneGroupDao.addPhoneItemToGroup(phoneId1, groupId);

		long phoneId2 = phoneItemDao.insertPhoneItem(new PhoneItem("jobs.he", "13900001111"));
		phoneGroupDao.addPhoneItemToGroup(phoneId2, groupId);

		// ��ʼ����
		PhoneBookService phoneBookService = (PhoneBookService) context.getBean("phoneBookService");
		List<PhoneItem> items = phoneBookService.findPhoneItemsByGroupName("classmate");

		// ����Ϣ��ӡ����������֤
		System.out.println(items.size());
		for (PhoneItem item : items) {
			System.out.println("user name:" + item.getUsername() + ", mobile:" + item.getMobile());
		}
	}
}
