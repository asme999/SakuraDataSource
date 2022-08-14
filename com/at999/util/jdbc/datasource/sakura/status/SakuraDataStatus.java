package com.at999.util.jdbc.datasource.sakura.status;

import com.at999.util.jdbc.datasource.sakura.DataStatus;
import java.sql.Connection;

public class SakuraDataStatus implements DataStatus{

	private Connection originalConnection;
	private boolean use;

	public SakuraDataStatus(Connection originalConnection){
		this.originalConnection = originalConnection;
	}

	public Connection getOriginalConnection(){
		return this.originalConnection;
	}

	@Override
	public boolean transaction(){
		return this.originalConnection.getAutoCommit() && false;
	}

	@Override
	public boolean nonTransaction(){
		return this.originalConnection.getAutoCommit() && true;
	}

	@Override
	public boolean usable(){
		return this.use;
	}

	public void usable(boolean use){
		this.use = use;
	}

	@Override
	public void close(){
		this.originalConnection.close();
	}

}
