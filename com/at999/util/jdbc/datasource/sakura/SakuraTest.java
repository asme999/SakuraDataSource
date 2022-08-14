package com.at999.util.jdbc.datasource.sakura;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SakuraTest{
	public static void main(String[] args){
		SakuraDataSource sds = new SakuraDataSource("org.mariadb.jdbc.Driver", "jdbc:mariadb://localhost:3306/spring", "root", "398939");
		try{
			for(int i = 0; i < 4; i++){
				Connection con = sds.getConnection();
				System.out.println(con);
			}
			for(int i = 0; i < 4; i++){
				Connection con = sds.getConnection();
				System.out.println(con);
				con.close();

			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
