package utils;
import org.apache.poi.ss.usermodel.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
   *     读取excel
 * @author zhaoai
 *
 */
public class ExcelDataproviderUtil implements Iterator<Object[]> {
    public static void main(String[] args) {
        ExcelDataproviderUtil excelDataproviderUtil = new ExcelDataproviderUtil("/testdata/login.xlsx", "Sheet1");
        while (excelDataproviderUtil.hasNext()){
            Object[] next = excelDataproviderUtil.next();
            System.out.println(Arrays.deepToString(next));
        }

    }
    private String path;
    private String sheetName;
    private String[] clomnName;
    private Iterator<Row> rowIterator;
    private Map<String, Workbook> workbookMap = new HashMap<String, Workbook>();

    public ExcelDataproviderUtil(String path, String sheetName) {
        this.path = path;
        this.sheetName = sheetName;
    }

    private void sureInit() {
        if (workbookMap.get(path) == null)
            initReadExcel();
    }

    public void initReadExcel() {
        InputStream inp = ExcelDataproviderUtil.class.getResourceAsStream(path);
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        workbookMap.put(path, workbook);
        Sheet sheet = workbook.getSheet(this.sheetName);
        Row firstRow = sheet.getRow(0);
        short lastCellNum = firstRow.getLastCellNum();
        clomnName = new String[lastCellNum];
        //初始化列名数组
        for (int i = 0; i < clomnName.length; i++) {
            clomnName[i] = firstRow.getCell(i).getStringCellValue();
        }

        rowIterator = sheet.iterator();
        rowIterator.next();//直接将行迭代器移动到数据行上面
    }

    boolean flag = false;//默认假设没有下一个

    @Override
    public boolean hasNext() {
        sureInit();
        if (flag)
            return true;
        //判断，当有下一个的时候，将flag设置为true
        if (rowIterator.hasNext()) {
            flag = true;
            return true;
        } else {
            return false;
        }
    }


	@SuppressWarnings("deprecation")
	@Override
    public Object[] next() {
        sureInit();
        flag = false;
        //读取当前行数据
        Row currentRow = rowIterator.next();
        Map<String,String> currentRowData = new HashMap<>();
        for (int i = 0; i < clomnName.length; i++) {
            Cell cell = currentRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);
            currentRowData.put(clomnName[i],cell.getStringCellValue());

        }
        return new Object[]{currentRowData};
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("不支持删除操作！");

    }
}
