package cn.edu.shnu.fb.infrastructure.poi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Bookmark;
import org.apache.poi.hwpf.usermodel.Bookmarks;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;

import cn.edu.shnu.fb.interfaces.dto.GridEntityDTO;
import javafx.scene.control.Cell;

/**
 * Created by bytenoob on 15/11/17.
 */
public class WordTemplate {
    HWPFDocument doc ;

    public static WordTemplate newInstance(InputStream is) {
        WordTemplate word = new WordTemplate();

        try{
            word.doc = new HWPFDocument(is);
        }catch (Exception e){

        }

        /*
        word.doc.write(new FileOutputStream("D:\\test.doc"));
        word.closeStream(is);*/

        return word;
    }
        /**
         * 关闭输入流
         * @param is
         */
        private void closeStream(InputStream is) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 读表格
         * 每一个回车符代表一个段落，所以对于表格而言，每一个单元格至少包含一个段落，每行结束都是一个段落。
         */
        public List<GridEntityDTO> getCourseGridEntity(){
            Range range = doc.getRange();
            return readTable(range);
        }
        private List<GridEntityDTO> readTable(Range range) {
            //遍历range范围内的table。
            TableIterator tableIter = new TableIterator(range);
            Table table;
            TableRow row;
            TableCell cell;
            List<GridEntityDTO> geDTOs = new ArrayList<>();
            String courseClass = "";
            int tableCount=0;
            while (tableIter.hasNext()) {
                tableCount++;
                table = tableIter.next();

                row = table.getRow(0);
                cell = row.getCell(0);
                if(cell!=null && cell.text().contains("系列")) {
                    int rowNum = table.numRows();
                    for (int j = 1; j < rowNum; j++) {
                        row = table.getRow(j);

                        if(row.numCells()<3 || toStr(row.getCell(1)).isEmpty() || row.getCell(1).text().contains("以上修满") || row.getCell(1).text().contains("课程代码")){
                            continue;
                        }
                        GridEntityDTO geDTO = new GridEntityDTO();
                        if(tableCount == 3){
                            courseClass = "任选";
                        }
                        else if(!toStr(row.getCell(0)).isEmpty() && !courseClass.equals(toStr(row.getCell(0)))){
                            courseClass = toStr(row.getCell(0));
                        }
                        geDTO.setCourseClass(courseClass);
                        geDTO.setCode(toStr(row.getCell(1)));
                        geDTO.setTitle(toStr(row.getCell(2)));
                        geDTO.setOnePeriod(Float.valueOf(toStr(row.getCell(3))));
                        geDTO.setOneCredits(Float.valueOf(toStr(row.getCell(4))));

                        if(row.numCells()>5) {
                            if (row.getCell(5).text().contains("√")) {
                                geDTO.setCourseExamId(1);
                            } else {
                                geDTO.setCourseExamId(2);
                            }
                        }
                        geDTOs.add(geDTO);
                    }
                }

                /*else if(table.getRow(0).getCell(0).equals("课程代码")){
                    int rowNum = table.numRows();
                    for (int j = 0; j < rowNum; j++) {
                        row = table.getRow(j);
                        GridEntityDTO geDTO = new GridEntityDTO();
                        int cellNum = row.numCells();

                        geDTOs.add(geDTO);
                    }
                }*/

            }
            return geDTOs;
        }


        private String toStr(TableCell cell){
            return cell.text().replaceAll("[\u0000-\u0007]", "").replace("#","").replace(" ","");
        }
        /**
         * 读列表
         * @param range
         */
        private void readList(Range range) {
            int num = range.numParagraphs();
            Paragraph para;
            for (int i=0; i<num; i++) {
                para = range.getParagraph(i);
                if (para.isInList()) {
                    System.out.println("list: " + para.text());
                }
            }
        }

        /**
         * 输出Range
         * @param range
         */
        private void printInfo(Range range) {
            //获取段落数
            int paraNum = range.numParagraphs();
            System.out.println(paraNum);
            for (int i=0; i<paraNum; i++) {
                //       this.insertInfo(range.getParagraph(i));
                System.out.println("段落" + (i+1) + "：" + range.getParagraph(i).text());
                if (i == (paraNum-1)) {
                    this.insertInfo(range.getParagraph(i));
                }
            }
            int secNum = range.numSections();
            System.out.println(secNum);
            Section section;
            for (int i=0; i<secNum; i++) {
                section = range.getSection(i);
                System.out.println(section.getMarginLeft());
                System.out.println(section.getMarginRight());
                System.out.println(section.getMarginTop());
                System.out.println(section.getMarginBottom());
                System.out.println(section.getPageHeight());
                System.out.println(section.text());
            }
        }

        /**
         * 插入内容到Range，这里只会写到内存中
         * @param range
         */
        private void insertInfo(Range range) {
            range.insertAfter("Hello");
        }

}
