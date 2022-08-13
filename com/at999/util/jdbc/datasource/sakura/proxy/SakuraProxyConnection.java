package com.at999.util.jdbc.datasource.sakura.proxy;

import com.at999.util.jdbc.datasource.sakura.proxy.SakuraConnectionHandler;
import java.sql.Connection;
import java.lang.reflect.Proxy;

public class SakuraProxyConnection{

	public static Connection getConnection(Class<extends Connection> cc, SakuraConnectionHandler sch){
		return Proxy.newProxy(cc.getClassLoader(), cc.getInterface(), sch);
	}

}
