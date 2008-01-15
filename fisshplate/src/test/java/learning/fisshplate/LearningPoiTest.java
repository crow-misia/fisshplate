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
package learning.fisshplate;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * @author a-conv
 */
public class LearningPoiTest extends TestCase {

	private HSSFWorkbook setupInputWorkbook(String filePath) throws Exception {
		FileInputStream fis = new FileInputStream(filePath);
		POIFSFileSystem poifs = new POIFSFileSystem(fis);
		fis.close();
		return new HSSFWorkbook(poifs);
	}

	public void testInithialize() throws Exception {
		String filePath = "src/test/resources/LearningPOITest.xls";
		HSSFWorkbook input = setupInputWorkbook(filePath);
		HSSFSheet inputSheet = input.getSheetAt(0);

		for (int rowNo = 0; rowNo <= inputSheet.getLastRowNum(); rowNo++) {
			HSSFRow row = inputSheet.getRow(rowNo);
			if (row == null) {
				continue;
			}
			for (int columnNo = 0; columnNo <= row.getLastCellNum(); columnNo++) {
				HSSFCell cell = row.getCell((short) columnNo);
				if (cell == null) {
					continue;
				}
				HSSFRichTextString richText = new HSSFRichTextString(null);
				cell.setCellValue(richText);
			}
		}

		FileOutputStream fos = new FileOutputStream("target/outLearningTest.xls");
		input.write(fos);
		fos.close();
	}
}
