package utilities;

import org.testng.annotations.DataProvider;

import java.io.IOException;

public class DataProviders {

    // Data Provider 1

    // Name should be different from one data provider to another data provider
    @DataProvider(name="LoginData")
    public String [][] getData() throws IOException {
        String path = ".\\testData\\Opencart_LoginData.xlsx"; // Taking Excel file from testData
        ExcelUtility xlutil = new ExcelUtility(path); // Creating an object for Excel utility and passing location of excel file

        // Below two lines provide total number of rows and columns
        int totalrows = xlutil.getRowCount("Sheet1");
        int totalcols = xlutil.getCellCount("Sheet1", 1);

        String logindata[][] = new String[totalrows][totalcols]; // Created two dimensional array which can store

        /*
         - Below code reads data from excel sheet and places it in the two-dimensional array
         - Outer for loop represents rows and inner for loop represents columns
         - In the below we are starting i with 1 instead of 0 because 0 represents header in rows
         - We are taking column as 0 because we need to read value from each column
         */
        for (int i = 1; i < totalrows; i++) { // 1 // Read the data from excel storing in two-dimensional array
                for (int j = 0; j < totalcols; j++) { //0  i is rows j is columns
                    /*
                    In below line of code, we are taking data from Excel sheet(("Sheet1", i, j) using i and j which
                    represent row and column data and stores them in two-dimensional (logindata[i - 1][j]) array in
                    same order
                    We took i-1 as array index starts with zero
                     */
                    logindata[i - 1][j] = xlutil.getCellData("Sheet1", i, j); //1,0
                }
            }
            return logindata; // returning two dimensional array
        }

        // Based on requirement, we can go ahead and add other data providers as well

        // Data Provider 2

       // Data Provider 3
    }
