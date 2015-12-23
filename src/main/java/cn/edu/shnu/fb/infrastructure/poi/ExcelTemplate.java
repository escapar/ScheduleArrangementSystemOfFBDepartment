package cn.edu.shnu.fb.infrastructure.poi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;

/**
 *
 * 功能描述: Excel操纵类，可以根据Excel模板来生成Excel对象<br>
 * 版本信息：1.0 <br>
 * Copyright: Copyright (c) 2005<br>
 */
public class ExcelTemplate {
    private static Log logger = LogFactory.getLog(ExcelTemplate.class);
    private static final String DATAS = "datas";

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private HSSFRow currentRow;
    private Map styles = new HashMap(); //数据行的默认样式配置
    private Map confStyles = new HashMap(); //通过设置"#STYLE_XXX"来标识的样式配置
    private int initrow; //数据输出起始行
    private int initcol; //数据输出起始列
    private int num; //index number
    private int currentcol; //当前列
    private int currentRowIndex; //当前行index
    private int rowheight = 400; //行高
    private int lastLowNum = 0;
    private String cellStyle = null;

    private ExcelTemplate() {
    }

    /**
     * 使用默认模板创建ExcelTemplate对象
     * @return 根据模板已初始化完成的ExcelTemplate对象
     */
 /*   public static ExcelTemplate newInstance(){
        return newInstance("default.xls");
    }*/

    public static ExcelTemplate newInstance(InputStream is){
        try {
            ExcelTemplate excel = new ExcelTemplate();
            POIFSFileSystem fs = new POIFSFileSystem(is);
        //            Thread.currentThread().getContextClassLoader()
         //                   ExcelTemplate.class.getResourceAsStream(templates));

            excel.workbook = new HSSFWorkbook(fs);
            excel.sheet = excel.workbook.getSheetAt(0);

            //查找配置
            excel.initConfig();

            //查找其它样式配置

            //删除配置行
            //excel.sheet.removeRow( excel.sheet.getRow(excel.initrow) );

            return excel;
        } catch (Exception e) {
            e.printStackTrace();
            logger.trace("创建Excel对象出现异常",e);
            throw new RuntimeException("创建Excel对象出现异常");
        }
    }


    /**
     * 设置特定的单元格样式，此样式可以通过在模板文件中定义"#STYLE_XX"来得到，如：
     * #STYLE_1，传入的参数就是"STYLE_1"
     * @param style
     */
    public void setCellStyle(String style){
        cellStyle = style;
    }

    /**
     * 取消特定的单元格格式，恢复默认的配置值，即DATAS所在行的值
     */
    public void setCellDefaultStyle(){
        cellStyle = null;
    }


    public void replaceParameters(Map props){
        if(props == null || props.size() == 0){
            return;
        }
        Set propsets = props.entrySet();
        Iterator rowit = sheet.rowIterator();
        while(rowit.hasNext()){
            HSSFRow row = (HSSFRow)rowit.next();
            if(row == null)	continue;
            int cellLength = row.getLastCellNum();
            for(int i=0; i<cellLength; i++){
                HSSFCell cell = (HSSFCell)row.getCell((short)i);
                if(cell == null) continue;
                String value = poiGetCellStringValue(cell);
                if(value != null && value.indexOf("#") != -1){
                    for (Iterator iter = propsets.iterator(); iter.hasNext();) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        value = value.replaceAll("#"+entry.getKey(),String.valueOf(entry.getValue()));
                    }
                }
                // cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                cell.setCellValue(value);
            }
        }
    }

    public void createRowByHashMap(ArrayList<Map> props , String keyword,int type) {
        //type=0 not merge ,1 merge
        int index = findRowByKeyword(keyword);
        int tmpIndex = index+1;
        int mergeFlag = -1;
        HSSFRow src = sheet.getRow(index);
        for(Map map: props) {
            Set propsets = map.entrySet();
            createRow(tmpIndex);
            copyRow(src,currentRow,true);
                int cellLength = currentRow.getLastCellNum();
                int i;
                for(i=0; i<cellLength; i++){
                    HSSFCell cell = (HSSFCell)currentRow.getCell((short)i);
                    if(cell == null) continue;
                    String value = poiGetCellStringValue(cell);
                    if(value != null && value.indexOf(keyword) != -1){
                        for (Iterator iter = propsets.iterator(); iter.hasNext();) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            value = value.replaceAll(keyword+entry.getKey(),subZeroAndDot(String.valueOf(entry.getValue())));
                            if(type==0){
                                if(value.equals("0")) value=value.replaceAll("0","");
                                if(value.equals("-0")) value=value.replaceAll("-0","");
                            }
                        }
                        if(mergeFlag >= 0) {
                            sheet.addMergedRegion(new CellRangeAddress(tmpIndex, tmpIndex, mergeFlag, i-1));
                            mergeFlag = -1;
                        }
                    }else if (value == null || StringUtils.isEmpty(value)){
                        if(mergeFlag < 0) { // not merged
                            mergeFlag = i - 1;  // set merge flag
                        }
                    }
                    // cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                    cell.setCellValue(value);
                }
            if(type==1){
                if(i >= cellLength){
                    sheet.addMergedRegion(new CellRangeAddress(tmpIndex, tmpIndex, mergeFlag, i-1));
                }
            }

                mergeFlag = -1;
                tmpIndex++;
        }
        sheet.shiftRows(index + 1 , 10000 , -1);

    }

    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public void createRow(int index){
        //如果在当前插入数据的区域有后续行，则将其后面的行往后移动
        if(lastLowNum > initrow && index > 0){
            sheet.shiftRows(index + initrow ,lastLowNum + index,1,true,true);
        }
        currentRow = sheet.createRow(index + initrow);
        currentRow.setHeight((short)rowheight);
        currentRowIndex = index;
        currentcol = initcol;
    }

    /**
     * 根据传入的字符串值，在当前行上创建新列
     * @param value 列的值（字符串）
     */
    public void createCell(String value){
        HSSFCell cell = createCell();
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(value);
    }

    /**
     * 根据传入的日期值，在当前行上创建新列
     * 在这种情况下（传入日期），你可以在模板中定义对应列
     * 的日期格式，这样可以灵活通过模板来控制输出的日期格式
     * @param value 日期
     */
    public void createCell(Date value){
        HSSFCell cell = createCell();
        cell.setCellValue(value);
    }

    /**
     * 创建当前行的序列号列，通常在一行的开头便会创建
     * 注意要使用这个方法，你必需在创建行之前调用initPageNumber方法
     */
    public void createSerialNumCell(){
        HSSFCell cell = createCell();
        cell.setCellValue(currentRowIndex + num);
    }

    private HSSFCell createCell(){
        HSSFCell cell = currentRow.createCell((short) currentcol++);
        //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
        HSSFCellStyle style = (HSSFCellStyle)styles.get(new Integer(cell.getCellNum()));
        if(style != null){
            cell.setCellStyle(style);
        }

        //设置了特定格式
        if(cellStyle != null){
            HSSFCellStyle ts = (HSSFCellStyle)confStyles.get(cellStyle);
            if(ts != null){
                cell.setCellStyle(ts);
            }
        }
        return cell;
    }

    /**
     * 获取当前HSSFWorkbook的实例
     * @return
     */
    public HSSFWorkbook getWorkbook(){
        return workbook;
    }

    /**
     * 获取模板中定义的单元格样式，如果没有定义，则返回空
     * @param style 模板定义的样式名称
     * @return 模板定义的单元格的样式，如果没有定义则返回空
     */
    public HSSFCellStyle getTemplateStyle(String style){
        return (HSSFCellStyle)confStyles.get(style);
    }

    /**
     * 替换模板中的文本参数
     * 参数以“#”开始
     * @param
     */
    public int findRowByKeyword(String keyword){
        Iterator rowit = sheet.rowIterator();
        while(rowit.hasNext()){
            HSSFRow row = (HSSFRow)rowit.next();
            if(row == null)	continue;
            int cellLength = row.getLastCellNum();
            for(int i=0; i<cellLength; i++){
                HSSFCell cell = (HSSFCell)row.getCell((short)i);
                if(cell == null) continue;
                String value = poiGetCellStringValue(cell);
                if(value.contains(keyword)){
                    return cell.getRowIndex();
                }
                // cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                cell.setCellValue(value);
            }
        }
        return 0;
    }

    /**
     * 替换模板中的文本参数
     * 参数以“#”开始
     * @param props
     */

    public void replaceParametersBykeyword(Map props,String keyword){
        if(props == null || props.size() == 0){
            return;
        }
        Set propsets = props.entrySet();
        Iterator rowit = sheet.rowIterator();
        while(rowit.hasNext()){
            HSSFRow row = (HSSFRow)rowit.next();
            if(row == null)	continue;
            int cellLength = row.getLastCellNum();
            for(int i=0; i<cellLength; i++){
                HSSFCell cell = (HSSFCell)row.getCell((short)i);
                if(cell == null) continue;
                String value = poiGetCellStringValue(cell);
                if(value != null && value.indexOf("") != -1 && value.contains(keyword)){
                    for (Iterator iter = propsets.iterator(); iter.hasNext();) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        value = value.replaceAll(keyword + entry.getKey(), subZeroAndDot(String.valueOf(entry.getValue())));
                        cell.setCellValue(value);
                    }
                }
                // cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            }
        }
    }

    public void replaceParametersByKeywordRichString(Map props,String keyword){
        if(props == null || props.size() == 0){
            return;
        }
        Set propsets = props.entrySet();
        Iterator rowit = sheet.rowIterator();
        Font underLine = getFont();
        while(rowit.hasNext()){
            HSSFRow row = (HSSFRow)rowit.next();
            if(row == null)	continue;
            int cellLength = row.getLastCellNum();
            for(int i=0; i<cellLength; i++){
                HSSFCell cell = (HSSFCell)row.getCell((short)i);
                if(cell == null) continue;
                HSSFRichTextString value = poiGetCellRichStringValue(cell);
                if(value != null && value.getString().indexOf("") != -1 && value.getString().contains(keyword)){
                    String strValue = value.getString();
                    for (Iterator iter = propsets.iterator(); iter.hasNext();) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String replaceValue = String.valueOf(entry.getValue());
                        strValue = strValue.replaceAll(keyword + entry.getKey(), replaceValue);
                       // value.applyFont(strValue.indexOf(replaceValue), strValue.indexOf(replaceValue) + replaceValue.length(), underLine);
                    }
                    value = new HSSFRichTextString(strValue);
                    for (Iterator iter = propsets.iterator(); iter.hasNext();) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String replaceValue = String.valueOf(entry.getValue());
                        value.applyFont(strValue.indexOf(replaceValue)-2, strValue.indexOf(replaceValue)+2 + replaceValue.length(), underLine);
                    }
                    value.applyFont(strValue.indexOf("商学院")-2, strValue.indexOf("商学院")+2 + "商学院".length(), underLine);

                    cell.setCellValue(value);
                }
                // cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            }
        }
    }

    public Font getFont() {
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)12);
        font.setUnderline(Font.U_SINGLE); //下划线
        return font;
    }

    private String poiGetCellStringValue(HSSFCell cell){
        String x;
        if(cell.getCellType() == 1)
            x = cell.getStringCellValue();
        else if(cell.getCellType() == 0)
            x = String.valueOf(cell.getNumericCellValue());
        else
            x = "";
        return x;
    }

    private HSSFRichTextString poiGetCellRichStringValue(HSSFCell cell){
        if(cell.getCellType() == 1)
            return cell.getRichStringCellValue();
        else
            return null;
    }

    private long poiGetCellNumValue(HSSFCell cell){
        long x;
        if(cell.getCellType() == 0)
            x = (long)cell.getNumericCellValue();
        else if(cell.getCellType() == 1)
            x = Long.parseLong(cell.getStringCellValue());
        else
            x = -1;
        return x;
    }

    /**
     * 初始化Excel配置
     */
    private void initConfig(){
        lastLowNum = sheet.getLastRowNum();
        Iterator rowit = sheet.rowIterator();
        boolean configFinish = false;
        while(rowit.hasNext()){
            if(configFinish){
                break;
            }
            HSSFRow row = (HSSFRow)rowit.next();
            if(row == null)	continue;
            int cellLength = row.getLastCellNum();
            int rownum = row.getRowNum();
            for(int i=0; i<cellLength; i++){
                HSSFCell cell = (HSSFCell)row.getCell((short)i);
                if(cell == null) continue;
                String config = getCellValue(cell);
                if(DATAS.equalsIgnoreCase(config)){
                    //本行是数据开始行和样式配置行，需要读取相应的配置信息
                    initrow = row.getRowNum();
                    rowheight = row.getHeight();
                    initcol = cell.getCellNum();
                    configFinish = true;
                }
                if(configFinish){
                    readCellStyle(cell);
                }
            }
        }
    }

    /**
     * 读取cell的样式
     * @param cell
     */
    private void readCellStyle(HSSFCell cell){
        HSSFCellStyle style = cell.getCellStyle();
        if(style == null) return;
        styles.put(new Integer(cell.getCellNum()),style);
    }

    /**
     * 读取模板中其它单元格的样式配置
     */
    private void readCellStyles(){
        Iterator rowit = sheet.rowIterator();
        while(rowit.hasNext()){
            HSSFRow row = (HSSFRow)rowit.next();
            if(row == null)	continue;
            int cellLength = row.getLastCellNum();
            for(int i=0; i<cellLength; i++){
                HSSFCell cell = (HSSFCell)row.getCell((short)i);
                if(cell == null) continue;
                String value = cell.getStringCellValue();
                if(value != null && value.indexOf("#STYLE_") != -1){
                    HSSFCellStyle style = cell.getCellStyle();
                    if(style == null) continue;
                    confStyles.put(value.substring(1),style);

                    //remove it
                    row.removeCell(cell);
                }
            }
        }
    }

        public  void copyCellStyle(HSSFCellStyle fromStyle,
                HSSFCellStyle toStyle) {
            toStyle.setAlignment(fromStyle.getAlignment());
            //边框和边框颜色
            toStyle.setBorderBottom(fromStyle.getBorderBottom());
            toStyle.setBorderLeft(fromStyle.getBorderLeft());
            toStyle.setBorderRight(fromStyle.getBorderRight());
            toStyle.setBorderTop(fromStyle.getBorderTop());
            toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
            toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
            toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
            toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

            //背景和前景
            toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
            toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

            toStyle.setDataFormat(fromStyle.getDataFormat());
            toStyle.setFillPattern(fromStyle.getFillPattern());
            //		toStyle.setFont(fromStyle.getFont(null));
            toStyle.setHidden(fromStyle.getHidden());
            toStyle.setIndention(fromStyle.getIndention());//首行缩进
            toStyle.setLocked(fromStyle.getLocked());
            toStyle.setRotation(fromStyle.getRotation());//旋转
            toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
            toStyle.setWrapText(fromStyle.getWrapText());

        }
        /**
         * Sheet复制
         * @param fromSheet
         * @param toSheet
         * @param copyValueFlag
         */
        public  void copySheet(HSSFWorkbook wb,HSSFSheet fromSheet, HSSFSheet toSheet,
                boolean copyValueFlag) {
            //合并区域处理
            mergerRegion(fromSheet, toSheet);
            for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
                HSSFRow tmpRow = (HSSFRow) rowIt.next();
                HSSFRow newRow = toSheet.createRow(tmpRow.getRowNum());
                //行复制
                copyRow(tmpRow, newRow, copyValueFlag);
            }
        }
        /**
         * 行复制功能
         * @param fromRow
         * @param toRow
         */
        public void copyRow(HSSFRow fromRow,HSSFRow toRow,boolean copyValueFlag){
            for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
                HSSFCell tmpCell = (HSSFCell) cellIt.next();
                HSSFCell newCell = toRow.createCell(tmpCell.getCellNum());
                copyCell(tmpCell, newCell, copyValueFlag);
            }
        }
        /**
         * 复制原有sheet的合并单元格到新创建的sheet
         *
         * @param sheetCreat 新创建sheet
         * @param sheet      原有的sheet
         */
        public void mergerRegion(HSSFSheet fromSheet, HSSFSheet toSheet) {
            int sheetMergerCount = fromSheet.getNumMergedRegions();
            for (int i = 0; i < sheetMergerCount; i++) {
                Region mergedRegionAt = fromSheet.getMergedRegionAt(i);
                toSheet.addMergedRegion(mergedRegionAt);
            }
        }
        /**
         * 复制单元格
         *
         * @param srcCell
         * @param distCell
         * @param copyValueFlag
         *            true则连同cell的内容一起复制
         */
        public void copyCell(HSSFCell srcCell, HSSFCell distCell,
                boolean copyValueFlag) {
//            HSSFCellStyle newstyle=workbook.createCellStyle();
//            copyCellStyle(srcCell.getCellStyle(), newstyle);
//            //样式
//            distCell.setCellStyle(newstyle);
            HSSFCellStyle newstyle=srcCell.getCellStyle();
            distCell.setCellStyle(newstyle);
            //评论
            if (srcCell.getCellComment() != null) {
                distCell.setCellComment(srcCell.getCellComment());
            }
            // 不同数据类型处理
            int srcCellType = srcCell.getCellType();
            distCell.setCellType(srcCellType);
            if (copyValueFlag) {
                if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) {
                    if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
                        distCell.setCellValue(srcCell.getDateCellValue());
                    } else {
                        distCell.setCellValue(srcCell.getNumericCellValue());
                    }
                } else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {
                    distCell.setCellValue(srcCell.getRichStringCellValue());
                } else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {
                    // nothing21
                } else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {
                    distCell.setCellValue(srcCell.getBooleanCellValue());
                } else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {
                    distCell.setCellErrorValue(srcCell.getErrorCellValue());
                } else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {
                    distCell.setCellFormula(srcCell.getCellFormula());
                } else { // nothing29
                }
            }
        }

    public List<GridEntityDTO> getCourseGridEntity(){
        List<GridEntityDTO> geDTOs = new ArrayList<>();
        sheet = workbook.getSheetAt(1);
        int allRows = sheet.getPhysicalNumberOfRows();
        int cellNum ;
        String title ;
        for(int j=4;j < allRows;j++){
            HSSFRow row = sheet.getRow(j);
            cellNum = row.getPhysicalNumberOfCells();
            GridEntityDTO geDTO = new GridEntityDTO();
            float[] period = new float[9];
            float[] credits = new float[9];
            if (cellNum >= 3 && getCellValue(row.getCell(3)).contains("基础法语")){
                geDTO.setCourseClass("专业必修课");
                geDTO.setTitle("基础法语");
                geDTO.setCourseExamId(1);
                geDTO.setCode("4720037");
                float[] tmpPeriod = {5,5,5,5,0,0,0,0,0};
                float[] tmpCredits = {6,6,6,6,0,0,0,0,0};

                geDTO.setPeriod(tmpPeriod);
                geDTO.setCredits(tmpCredits);
                geDTOs.add(geDTO);
                geDTO = new GridEntityDTO();
                geDTO.setCourseClass("专业必修课");
                geDTO.setTitle("基础法语（听说）");
                geDTO.setCourseExamId(1);
                geDTO.setCode("4720037");

                geDTO.setPeriod(new float[]{4,4,4,4,0,0,0,0,0});
                geDTO.setCredits(new float[]{0,0,0,0,0,0,0,0,0});
                geDTOs.add(geDTO);
                continue;
            }else {
                for (int i = 0; i < cellNum; i++) {
                    if (i == 0) {
                        title = getMergedRegionValue(j, 0);
                        geDTO.setCourseClass(title);
                    } else {
                        HSSFCell cell = row.getCell(i);
                        if (i == 2) {
                            String code = getCellValue(cell);
                            if (code.contains(".")) {
                                String[] codes = code.split("\\.");
                                code = codes[0];
                            }
                            geDTO.setCode(code);
                        } else if (i == 3) {
                            geDTO.setTitle(getCellValue(cell));
                        } else if (i >= 4 && i <= 19) {
                            // 5-1 6-1 7-2 8-2
                            if (i % 2 == 0) {
                                if (getCellValue(cell) != "" && !getCellValue(cell).contains("周") && !getCellValue(cell).contains("SUM")) {
                                    period[i / 2 - 2] = Float.valueOf(getCellValue(cell));
                                }
                            } else {
                                if (getCellValue(cell) != "" && !getCellValue(cell).contains("周") && !getCellValue(cell).contains("SUM")) {
                                    credits[i / 2 - 2] = Float.valueOf(getCellValue(cell));
                                }
                            }
                        } else if (i == 21) {
                            if (getCellValue(cell) != "" && !getCellValue(cell).contains("+") && !getCellValue(cell).contains("SUM")) {
                                credits[8] = Float.valueOf(getCellValue(cell));
                            }
                        } else if (i == 22 && getCellValue(cell).equals("√")) {
                            geDTO.setCourseExamId(1);
                        } else if (i == 23 && getCellValue(cell).equals("√")) {
                            geDTO.setCourseExamId(2);
                        }
                    }
                }
                geDTO.setPeriod(period);
                geDTO.setCredits(credits);
                geDTOs.add(geDTO);
            }
        }
        return geDTOs;
    }

    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public String getMergedRegionValue(int row , int column){
        int sheetMergeCount = sheet.getNumMergedRegions();

        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if(row >= firstRow && row <= lastRow){

                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell) ;
                }
            }
        }

        return null ;
    }

    /**
     * 判断合并了行
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private boolean isMergedRow(int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row == firstRow && row == lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    private boolean isMergedRegion(int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断sheet页中是否含有合并单元格
     * @param sheet
     * @return
     */
    private boolean hasMerged() {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }

    /**
     * 合并单元格
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow 结束行
     * @param firstCol 开始列
     * @param lastCol 结束列
     */
    private void mergeRegion(int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public String getCellValue(Cell cell){

        if(cell == null) return "";

        if(cell.getCellType() == Cell.CELL_TYPE_STRING){

            return cell.getStringCellValue().replace(" ", "");

        }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){

            return String.valueOf(cell.getBooleanCellValue());

        }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){

            return cell.getCellFormula() ;

        }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){

            return String.valueOf(cell.getNumericCellValue());

        }
        return "";
    }


}
