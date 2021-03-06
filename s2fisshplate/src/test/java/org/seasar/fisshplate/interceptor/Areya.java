/**
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
 *
 */

package org.seasar.fisshplate.interceptor;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.wrapper.CellWrapper;

public class Areya implements TemplateElement {
    private CellWrapper originalCell;

    public Areya(CellWrapper cell) {
        originalCell = cell;
    }

    public void merge(FPContext context) throws FPMergeException {
        Cell currentCell = context.getCurrentCell();
        currentCell.setCellStyle(originalCell.getHSSFCell().getCellStyle());
        currentCell.setCellValue(new HSSFRichTextString("独自タグテストです"));
        context.nextRow();
    }

}
