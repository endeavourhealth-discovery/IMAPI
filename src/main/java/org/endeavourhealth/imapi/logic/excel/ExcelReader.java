package org.endeavourhealth.imapi.logic.excel;

import org.apache.poi.ss.usermodel.*;
import org.endeavourhealth.imapi.model.casdoor.User;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelReader {
  public List<User> readUserImportFile(String path) throws IOException {
    List<User> users = new ArrayList<>();
    FileInputStream fis = new FileInputStream(path);

    // Works for both .xls and .xlsx
    try (Workbook workbook = WorkbookFactory.create(fis)) {
      Sheet sheet = workbook.getSheetAt(0); // First sheet
      int rowCount = 0;
      for (Row row : sheet) {
        if (rowCount != 0) {
          int columnCount = 0;
          User user = new User();
          for (Cell cell : row) {
            switch (columnCount) {
              case 1:
                user.setUsername(cell.getStringCellValue());
                break;
              case 8:
                user.adminSetPassword(cell.getStringCellValue());
                break;
              case 17:
                user.setEmail(cell.getStringCellValue());
                break;
              default:
            }
            columnCount++;
          }
          users.add(user);
        }
        rowCount++;
      }
    }
    return users;
  }
}
