package com.at999.util.jdbc.datasource.sakura;

public interface DataStatus{

	boolean transaction();

	boolean nonTransaction();

	boolean usable();

}
