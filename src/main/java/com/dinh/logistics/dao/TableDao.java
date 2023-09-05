package com.dinh.logistics.dao;

import java.io.File;
import java.sql.ResultSet;

public interface TableDao {

	File exportToExcelWithResultSet(String sql, String excel_output_file, int row_start, int column_start);

}
