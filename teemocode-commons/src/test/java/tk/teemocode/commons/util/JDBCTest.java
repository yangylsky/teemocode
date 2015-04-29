package tk.teemocode.commons.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JDBCTest {
	private static final Log log = LogFactory.getLog(JDBCTest.class);

	public static void main(String[] args) {
		log.error("Start....");
		String driver = "com.mysql.jdbc.Driver";
		String dbase = "jdbc:mysql://127.0.0.1:3306/dlhjpos";
		String dbuser = "keepshops";
		String dbpw = "PKhGapCqCBfUbP3BIrlu";

		try {
			log.error("Class.forName");
			Class.forName(driver);

			log.error("Connection");
			Connection con = DriverManager.getConnection(dbase, dbuser, dbpw);
			log.error("Statement");
			Statement stmt = con.createStatement();
			log.error("ResultSet");
			ResultSet rs = stmt.executeQuery("show databases;");
			log.error("ResultSet");
			while (rs.next())
			{
				log.error("rs.next()....");
			}

			log.error("closing....");
			rs.close();
			stmt.close();
			con.close();
			log.error("End....");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
