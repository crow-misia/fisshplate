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

package org.seasar.fisshplate.util;

import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.seasar.fisshplate.core.parser.RowParser;
import org.seasar.fisshplate.core.parser.container.AddOnParserContainer;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.exception.FPMergeRuntimeException;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.exception.FPParseRuntimeException;
import org.seasar.fisshplate.template.FPTemplate;

/**
 * @author rokugen
 */
public class FisshplateUtil {
    private FisshplateUtil(){}

    /**
     * テンプレートにデータを埋め込みます。
     * @param workbook テンプレートオブジェクト
     * @param data 埋め込み用データ
     * @param addOnParserContainer 独自のパーサを保持するコンテナ
     * @return 出力するワークブック
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
	public static final Workbook process(Workbook workbook, Map data, AddOnParserContainer addOnParserContainer){
        try {
            FPTemplate template = new FPTemplate();
            addRowParsersToTemplate(template, addOnParserContainer);
            return template.process(workbook,data);
        } catch (FPMergeException e) {
            throw new FPMergeRuntimeException(e);
        }catch(FPParseException e){
            throw new FPParseRuntimeException(e);
        }
    }

    private static final void addRowParsersToTemplate(FPTemplate template, AddOnParserContainer rowParserContainer){
        int parsersCount = rowParserContainer.rowParserCount();
        for (int i = 0; i < parsersCount; i++) {
            RowParser rp = rowParserContainer.getRowParser(i);
            template.addRowParser(rp);
        }
    }

}
