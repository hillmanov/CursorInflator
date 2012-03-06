package com.hillman.cyclewhat.inflator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

public class CursorInflator {
	
	public static <T> List<T> inflateList(Cursor cursor, Class<T> entityClass) {
		return inflateFromCursor(entityClass, cursor);
	}
	
	public static <T> T inflateOne(Cursor cursor, Class<T> entityClass) {
		List<T> entityList = inflateList(cursor, entityClass);
		if (entityList.size() > 0) {
			return entityList.get(0);
		}
		else {
			return null;
		}
	}
	
	private static <T> List<T> inflateFromCursor(Class<T> entityClass, Cursor c) {
		List<T> returnList = new ArrayList<T>();
		try {
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						T current = entityClass.newInstance();
						Field[] fields = entityClass.getDeclaredFields();
						for (Field field : fields) {
							field.setAccessible(true);
							Column column = field.getAnnotation(Column.class);
							if (column != null && (c.getColumnIndex(column.name()) >= 0)) {
								if (field.getType() == String.class) {
									field.set(current, c.getString(c.getColumnIndex(column.name())));
								} else if (field.getType() == int.class) {
									field.setInt(current, c.getInt(c.getColumnIndex(column.name())));
								} else if (field.getType() == long.class) {
									field.setLong(current, c.getLong(c.getColumnIndex(column.name())));
								}
							}
						}
						returnList.add(current);
					} while (c.moveToNext());
					c.close();
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} finally {
			c.close();
		}

		return returnList;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Column {
		String name();
	}

}
