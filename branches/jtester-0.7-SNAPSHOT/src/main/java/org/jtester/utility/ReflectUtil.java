package org.jtester.utility;

import java.lang.reflect.Field;

import org.jtester.exception.JTesterException;

/**
 * POJO反射处理工具类
 * 
 * @author darui.wudr
 * 
 */
public class ReflectUtil {
	/**
	 * 给对象obj名为fieldname的属性赋值
	 * 
	 * @param obj
	 *            赋值对象
	 * @param fieldName
	 *            赋值属性
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static void setFieldValue(Object obj, String fieldName, Object value) {
		assert obj != null : "the obj can't be null";
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(accessible);
		} catch (Exception e) {
			String error = "Unable to update the value in field[" + fieldName + "]";
			throw new JTesterException(error, e);
		}
	}

	/**
	 * 获得对象obj的属性名为fieldname的字段值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		assert obj != null : "the obj can't be null";
		return getFieldValue(obj.getClass(), obj, fieldName);
	}

	/**
	 * 获得对象obj的属性名为fieldname的字段值
	 * 
	 * @param claz
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static Object getFieldValue(Class<?> claz, Object obj, String fieldName) {
		assert obj != null : "the obj can't be null";
		try {
			Field field = claz.getDeclaredField(fieldName);
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			Object o = field.get(obj);
			field.setAccessible(accessible);
			return o;
		} catch (Exception e) {
			throw new JTesterException("Unable to get the value in field[" + fieldName + "]", e);
		}
	}
}
