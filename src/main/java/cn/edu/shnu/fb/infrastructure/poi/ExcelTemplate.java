package cn.edu.shnu.fb.infrastructure.poi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

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

    /**
     * 指定模板创建ExcelTemplate对象
     * @param templates 模板名称
     * @return 根据模板已初始化完成的ExcelTemplate对象
     */
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
      //      excel.readCellStyles();

            //删除配置行
   //         excel.sheet.removeRow( excel.sheet.getRow(excel.initrow) );

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

    /**
     * 创建新行
     * @param index 从0开始计数
     */

    public void createRowByHashMap(ArrayList<Map> props , String keyword) {
        int index = findRowByKeyword(keyword);
        int tmpIndex = index + 1;
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
                            value = value.replaceAll(keyword+entry.getKey(),String.valueOf(entry.getValue()));
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
                if(i >= cellLength ){
                    sheet.addMergedRegion(new CellRangeAddress(tmpIndex, tmpIndex, mergeFlag, i-1));
                }
                mergeFlag = -1;
                tmpIndex++;
        }
        sheet.shiftRows(index + 1 , 100 , -1);

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
     * @param props
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
                String config = cell.getStringCellValue();
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
                copyRow(tmpRow,newRow,copyValueFlag);
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
            HSSFCellStyle newstyle=workbook.createCellStyle();
            copyCellStyle(srcCell.getCellStyle(), newstyle);
            //样式
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
}
