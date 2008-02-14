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
package org.seasar.fisshplate.core.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.core.element.AbstractBlock;
import org.seasar.fisshplate.core.element.ElseBlock;
import org.seasar.fisshplate.core.element.ElseIfBlock;
import org.seasar.fisshplate.core.element.IfBlock;
import org.seasar.fisshplate.core.element.PageFooterBlock;
import org.seasar.fisshplate.core.element.PageHeaderBlock;
import org.seasar.fisshplate.core.element.Root;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.RowWrapper;

/**
 * endを解析するクラスです。
 * @author rokugen
 */
public class EndParser implements StatementParser {
	private static final Pattern patEnd = Pattern.compile("(^\\s*#end\\s*$|#pageHeaderEnd|#pageFooterEnd)");
	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.parser.StatementParser#process(org.seasar.fisshplate.wrapper.CellWrapper, org.seasar.fisshplate.core.parser.FPParser)
	 */
	public boolean process(CellWrapper cell, FPParser parser)	throws FPParseException {
		String value = cell.getStringValue();	
		Matcher mat  = patEnd.matcher(value);  
		if(!mat.find()){
			return false;
		}
		RowWrapper row = cell.getRow();
		Root root = parser.getRoot();
		if (parser.isBlockStackBlank()) {
			throw new FPParseException(FPConsts.MESSAGE_ID_END_ELEMENT,
					new Object[]{new Integer(row.getHSSFRow().getRowNum() + 1)});
		}
		AbstractBlock block = parser.popFromBlockStack();
		// elseとelse ifの場合、ifが出るまでpop継続する
		Class clazz = block.getClass();
		if ((clazz == ElseBlock.class) || clazz == ElseIfBlock.class) {
			while (block.getClass() != IfBlock.class) {
				block = parser.popFromBlockStack();
			}
		} else if ((clazz == PageHeaderBlock.class)) {
			root.setPageHeader(block);
			// Header自体はBodyに追加しない
			return true;
		} else if ((clazz == PageFooterBlock.class)) {
			root.setPageFooter(block);			
			// Footer自体はBodyに追加しない
			return true;
		}
		// ブロックのネストがルートまで戻ったらルートの要素リストに追加する。
		if (parser.isBlockStackBlank()) {
			root.addBody(block);			
		}

		return true;
	}

}
