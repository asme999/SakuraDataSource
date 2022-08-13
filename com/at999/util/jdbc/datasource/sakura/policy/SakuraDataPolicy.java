package com.at999.util.jdbc.datasource.sakura.policy;

import com.at999.util.jdbc.datasource.sakura.DataPolicy;
import com.at999.util.jdbc.datasource.sakura.DataAccessInfo;
import com.at999.util.jdbc.datasource.sakura.pool.SakuraRestrictedPool;

public class SakuraDataPolicy implements DataPolicy{

	private static SakuraDataPolicy dp;

	private SakuraDataPolicy(){}

	public static SakuraDataPolicy newInstance(){
		if(dp == null)
			return dp = new SakuraDataPolicy();
		return dp;
	}

}
