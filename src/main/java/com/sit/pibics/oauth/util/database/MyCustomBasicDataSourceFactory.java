package com.sit.pibics.oauth.util.database;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory;


public class MyCustomBasicDataSourceFactory extends BasicDataSourceFactory {
	
	protected static final String DATA_SOURCE_FACTORY_PROP_PASSWORD = "password";
	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?,?> environment) throws Exception {
		Reference ref = (Reference) obj;
		for (int i = 0; i < ref.size(); i++) {
			RefAddr ra = ref.get(i);
			if (ra.getType().equals(DATA_SOURCE_FACTORY_PROP_PASSWORD)) {
				if (ra.getContent() != null && ra.getContent().toString().length() > 0) {
					String pwd = ra.getContent().toString();
					ref.remove(i);
					ref.add(i, new StringRefAddr(DATA_SOURCE_FACTORY_PROP_PASSWORD, pwd));
				}
				break;
			}
		}
		return super.getObjectInstance(obj, name, nameCtx, environment);
	}
}
