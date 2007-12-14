package org.seasar.fisshplate.core;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.seasar.fisshplate.context.FPContext;

/**
 * セル要素の基底抽象クラスです。
 * @author rokugen
 *
 */
public abstract class AbstractCell implements TemplateElement {
	/**
	 * テンプレート側のセル
	 */
	protected HSSFCell templateCell;
	private boolean isMergedCell;
	private short relativeMergedColumnTo;
	private int relativeMergedRowNumTo;
	

	/**
	 * コンストラクタです。
	 * 
	 * @param templateCell
	 */
	AbstractCell( HSSFSheet templateSheet,HSSFCell templateCell,int rowNum) {
		this.templateCell = templateCell;
		//マージ情報をなめて、スタート地点が合致すれば保存しておく。
		for(int i=0; i < templateSheet.getNumMergedRegions();i++){
			Region reg = templateSheet.getMergedRegionAt(i);
			setUpMergedCellInfo(templateCell.getCellNum(), rowNum, reg);
		}		
	}
	
	private void setUpMergedCellInfo(short cellNum, int rowNum, Region reg){
		if(reg.getColumnFrom() != cellNum || reg.getRowFrom() != rowNum){
			isMergedCell = false;
			return;
		}						
		isMergedCell = true;
		relativeMergedColumnTo = (short) (reg.getColumnTo() - reg.getColumnFrom());
		relativeMergedRowNumTo = reg.getRowTo() - reg.getRowFrom();
	}	
	

	/**
	 * テンプレート側のセルのスタイルを出力側へコピーします。フォントも反映されます。
	 * @param context コンテキスト
	 * @param outCell 出力するセル
	 */
	protected void copyCellStyle(FPContext context, HSSFCell outCell) {

		HSSFWorkbook outWb = context.getOutWorkBook();
		HSSFCellStyle outStyle = outWb.createCellStyle();
		copyProperties(outStyle, templateCell.getCellStyle());

		HSSFFont font = getCopiedFont(context, outWb);
		outStyle.setFont(font);
		outCell.setCellStyle(outStyle);
		if(isMergedCell){
			mergeCell(context, outWb);
		}
		
	}

	private HSSFFont getCopiedFont(FPContext context, HSSFWorkbook outWb) {
		short fontIndex = templateCell.getCellStyle().getFontIndex();
		HSSFFont font = outWb.createFont();
		copyProperties(font, context.getTemplate().getFontAt(fontIndex));
		return font;
	}

	private void copyProperties(Object dest, Object src) {
		try {
			BeanUtils.copyProperties(dest, src);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void mergeCell(FPContext context, HSSFWorkbook wb){		
		short columnFrom = context.getCurrentCellNum();
		int rowFrom = context.getCurrentRowNum();
		
		Region reg = new Region();
		reg.setColumnFrom(columnFrom);
		reg.setColumnTo((short) (columnFrom + relativeMergedColumnTo));
		reg.setRowFrom(rowFrom);
		reg.setRowTo(rowFrom + relativeMergedRowNumTo);
		wb.getSheetAt(0).addMergedRegion(reg);		
	}

}
