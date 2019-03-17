package com.org.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	@Autowired
	DataSource dataSource;

	@Value("${testQuery}")
	private String testQuery;

	@RequestMapping("/healthcheck")
	public String greeting() {
		return "OK";
	}

	@RequestMapping("/getdata")
	public String getData() throws SQLException {
		StringBuilder response = new StringBuilder();
		Connection conn = dataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement(testQuery);
		ResultSet rs = ps.executeQuery();
		// Get column count
		int columnCount = rs.getMetaData().getColumnCount();
		while (rs.next()) {
			// Column index starts from 1 in ResultSet
			for (int i = 1; i <= columnCount; i++) {
				response.append(rs.getString(i) + ".");
			}
			// New line after every record
			response.append("\n");
		}
		conn.close();
		return response.toString();
	}
}
