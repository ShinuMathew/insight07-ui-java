package com.insightui.dbconnect;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.insightui.logger.Logger;
import com.insightui.variables.CommonVariables;
import com.insightui.variables.CommonVariables.DBMS;

public class DBHandler {

	public static void openDBConnection(CommonVariables.DBMS dbType) {
		try {
			if (dbType == DBMS.MYSQL) {
				CommonVariables.con = DriverManager.getConnection(CommonVariables.globalData.get("MySqlConn"),
						CommonVariables.globalData.get("MySqlUser"), CommonVariables.globalData.get("MySqlPswd"));

			} else if (dbType == DBMS.POSTGRES) {
				Class.forName("org.postgresql.Driver");
				CommonVariables.con = DriverManager.getConnection(CommonVariables.globalData.get("PostgresConn"),
						CommonVariables.globalData.get("PostgresUser"), CommonVariables.globalData.get("PostgresPswd"));
			}
		} catch (Exception ex) {
			Logger.logFatalError("Following exception occured in openDBConnection()" + ex.getMessage());
		}
	}

	public static ResultSet executeSafeQuery(String sql, Map<Integer, String> params) {
		try {
			CommonVariables.prepStmt = CommonVariables.con.prepareStatement(sql);
			for (Entry<Integer, String> paramKey : params.entrySet())
				CommonVariables.prepStmt.setString(paramKey.getKey(), paramKey.getValue());
			CommonVariables.rs = CommonVariables.prepStmt.executeQuery();
			return CommonVariables.rs;
		} catch (Exception ex) {
			Logger.logFatalError("Following exception occured in executeSafeQuery()" + ex.getMessage());
			return CommonVariables.rs;
		}
	}

	public static ResultSet executeQuery(String sql) {
		try {
			CommonVariables.stmt = CommonVariables.con.createStatement();
			CommonVariables.rs = CommonVariables.stmt.executeQuery(sql);
			return CommonVariables.rs;
		} catch (Exception ex) {
			Logger.logFatalError("Following exception occured in executeQuery()" + ex.getMessage());
			return CommonVariables.rs;
		}
	}

	public static int executeNonQuery(String sql) {
		int count = 0;
		try {
			CommonVariables.stmt = CommonVariables.con.createStatement();
			count = CommonVariables.stmt.executeUpdate(sql);
			return count;
		} catch (Exception ex) {
			Logger.logFatalError("Following exception occured in executeNonQuery()" + ex.getMessage());
			return count;
		}
	}

	public static void closeDBConnection() {
		try {
			CommonVariables.con.close();
		} catch (Exception ex) {
			Logger.logFatalError("Following exception occured when closing DB connections" + ex.getMessage());
		}
	}

	public static void closeAllDBConnections() {
		try {
			if (!CommonVariables.con.isClosed())
				CommonVariables.con.close();
		} catch (Exception ex) {
			Logger.logFatalError("Following exception occured when closing DB connections" + ex.getMessage());
		}
	}
}