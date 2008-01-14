/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.fisshplate.core;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.FileInputStreamUtil;
import org.seasar.fisshplate.util.ImageIOUtil;
import org.seasar.fisshplate.util.OgnlUtil;
import org.seasar.fisshplate.util.StringUtil;

/**
 * 画像処理用の要素クラスです
 * 
 * @author a-conv
 */
public class Picture extends AbstractCell {
	private ElExpression expression;

	private HSSFPatriarch patriarch;

	/**
	 * コンストラクタです。
	 * 
	 * @param sheet
	 *            テンプレート側のシート
	 * @param cell
	 *            テンプレート側のセル
	 * @param rowNum
	 *            行番号
	 * @param expression
	 *            評価式
	 */
	Picture(HSSFSheet sheet, HSSFCell cell, int rowNum, String expression) {
		super(sheet, cell, rowNum);
		this.expression = new ElExpression(expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {
		HSSFCell out = context.getCurrentCell();
		copyCellStyle(context, out);
		Object value = getValue(context);
		String picturepath = (String) value;
		System.out.println(picturepath);
		if (!picturepath.equals("") && picturepath.length() > 0) {
			System.out.println("処理開始");
			writePicture(picturepath, context);
			System.out.println("処理終了");
		}
		context.nextCell();
	}

	/**
	 * 画像用オブジェクトを作成する
	 * 
	 * @param width
	 * @param height
	 * @param cellNo
	 * @param rowNo
	 * @return
	 */
	private HSSFClientAnchor createAnchor(int width, int height, int cellNo, int rowNo) {
		HSSFClientAnchor anchor = new HSSFClientAnchor();
		// TODO サイズを指定が利かないので最大値で初期化
		anchor.setDx1(0);
		anchor.setDx2(0);
		anchor.setDy1(0);
		anchor.setDy2(255);
		// TODO カラムサイズの指定方法を検討する
		int fromCellNo = cellNo;
		int toCellNo = cellNo + 1;
		int fromRowNo = rowNo;
		int toRowNo = rowNo + 5;
		//
		anchor.setCol1((short) fromCellNo);
		anchor.setCol2((short) toCellNo);
		anchor.setRow1(fromRowNo);
		anchor.setRow2(toRowNo);
		anchor.setAnchorType(2);
		return anchor;
	}

	/**
	 * 画像タイプ取得
	 * 
	 * @param suffix
	 * @return
	 * @throws FPMergeException
	 */
	private int setupPictureType(String suffix) throws FPMergeException {
		if (suffix.toLowerCase().equals("jpg")) {
			return HSSFWorkbook.PICTURE_TYPE_JPEG;
		}
		if (suffix.toLowerCase().equals("png")) {
			return HSSFWorkbook.PICTURE_TYPE_PNG;
		}
		throw new FPMergeException(FPConsts.MESSAGE_PICTURE_TYPE_INVALID);
	}

	/**
	 * 画像貼り付け
	 * 
	 * @param picturepath
	 * @param context
	 * @throws FPMergeException
	 */
	private void writePicture(String picturepath, FPContext context) throws FPMergeException {

		FileInputStream imgFis = FileInputStreamUtil.createFileInputStream(picturepath);
		BufferedImage img = ImageIOUtil.read(imgFis);
		FileInputStreamUtil.close(imgFis);

		HSSFWorkbook workbook = context.getOutWorkBook();
		HSSFSheet worksheet = context.getOutSheet();
		if (patriarch == null) {
			patriarch = worksheet.createDrawingPatriarch();
		}
		HSSFClientAnchor anchor = createAnchor(img.getWidth(), img.getHeight(), context.getCurrentCellNum(), context.getCurrentRowNum());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String suffix = StringUtil.parseSuffix(picturepath);
		ImageIOUtil.write(img, suffix, baos);

		byte[] pictureData = baos.toByteArray();
		int pictureType = setupPictureType(suffix);
		int pictureIndex = workbook.addPicture(pictureData, pictureType);

		System.out.println("*[Anchor]=========================");
		System.out.println("cellNo:=" + context.getCurrentCellNum());
		System.out.println("rowNo:=" + context.getCurrentRowNum());
		System.out.println("PictureType:=" + pictureType);
		System.out.println("PictureIndex:=" + pictureIndex);

		patriarch.createPicture(anchor, pictureIndex);

		ImageIOUtil.close(baos);
	}

	private Object getValue(FPContext context) throws FPMergeException {
		Map data = context.getData();
		Object value = OgnlUtil.getValue(expression.getExpression(), data);
		if (value == null) {
			if (expression.isNullAllowed()) {
				return expression.getNullValue();
			} else {
				throw new FPMergeException(FPConsts.MESSAGE_ID_EL_EXPRESSION_UNDEFINED, new Object[] { expression.getExpression() });
			}
		}
		return value;
	}

}
