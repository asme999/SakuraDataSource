package com.at999.util.jdbc.datasource.sakura;

import java.sql.SQLException;

public interface DataStatus{

	boolean transaction() throws SQLException;

	boolean nonTransaction() throws SQLException;

	boolean usable();

	void close() throws SQLException;

}
