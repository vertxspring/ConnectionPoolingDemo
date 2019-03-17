package com.org;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegrationTest {
	@Autowired
	private DataSource dataSource;
	
	@Value("${testQuery}")
	private String testQuery;

	@Test
	public void hikariConnectionPoolIsConfigured() {

		assertEquals("com.zaxxer.hikari.HikariDataSource", dataSource.getClass().getName());
	}

	@Test
	public void connectionTest() throws SQLException {

		Connection conn = dataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement(testQuery);
		ResultSet rs = ps.executeQuery();
		//Get column count
		int columnCount = rs.getMetaData().getColumnCount();
		while (rs.next()) {
			// Column index starts from 1 in ResultSet
			for (int i = 1; i <= columnCount; i++) {
				System.out.print(rs.getString(i) + ".");
			}
			//New line after every record
			System.out.println();
		}
		conn.close();
	}
}
