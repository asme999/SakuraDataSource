package com.at999.util.jdbc.datasource.sakura;

import com.at999.util.jdbc.datasource.sakura.DataAccess;
import java.util.LinkedList;

public interface DataAccessInfo{

	DataAccess getDataAccess();

	LinkedList<Long> getApplication();

	long getCreateTime();

	long getStartInitialzeTime();

	long getFinishInitialzeTime();

	int getCount();

/*
	void count(boolean up);
*/

	void setCount(int count);

	void push();

/*
	void setDataAccess(DataAccess da);

*/

/*
	void setDates(LinkedList<Long> lds);
*/

	default Object getExtendInfo(){return null;}

}
