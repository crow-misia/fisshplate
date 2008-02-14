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
package org.seasar.fisshplate.core.element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.core.ElExpression;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.OgnlUtil;
import org.seasar.fisshplate.util.StringUtil;

/**
 * テンプレートのセルの値が評価式の場合の要素クラスです。OGNLで評価します。
 * @author rokugen
 *
 */
public class El implements TemplateElement{	
	private Map expressionMap = new HashMap();
	protected AbstractCell targetElement;	

	/**
	 * コンストラクタです。
	 * @param cell テンプレート側のセル
	 * @param expression 評価式
	 */
	public El(AbstractCell target) {
		this.targetElement = target;
		String originalCellValue = target.cell.getStringValue();
		Pattern patEl = Pattern.compile(FPConsts.REGEX_EL);
		Matcher mat = patEl.matcher(originalCellValue);
		while(mat.find()){
			expressionMap.put(mat.group(), null);
		}				
	}

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {		
		Object value = null;
		if(context.isSkipMerge()){
			value = "";
		}else{
			putValueToMap(context);
			value = buildValue();			
		}
		targetElement.setCellValue(value);
		targetElement.merge(context);
	}

	private Object buildValue(){
		
		String cellValue = targetElement.cell.getStringValue();		
		Set keySet = expressionMap.keySet();
		
		if(isSingleElOnly(cellValue)){
			return expressionMap.get(keySet.iterator().next());			
		}
		
		for(Iterator itr = keySet.iterator(); itr.hasNext();){			
			String key = (String) itr.next();
			cellValue = cellValue.replaceAll(StringUtil.escapeEl(key), expressionMap.get(key).toString());
		}
		return cellValue;
		
	}
	
	private boolean isSingleElOnly(String value){
		if(expressionMap.size() != 1){
			return false;
		}
		
		String exp = expressionMap.keySet().iterator().next().toString();				
		value = value.replaceAll(StringUtil.escapeEl(exp), "");
		
		return (value.trim().length() == 0);
	}
	
	
	private void putValueToMap(FPContext context) throws FPMergeException{				
		Map data = context.getData();		
		Set key = expressionMap.keySet();
		for(Iterator itr = key.iterator(); itr.hasNext();){
			String expString = (String) itr.next();
			ElExpression expression = new ElExpression(expString);
			Object value = getValue(data,expression);
			expressionMap.put(expString, value);
		}		
	}	
	
	private Object getValue(Map data, ElExpression expression) throws FPMergeException{		
		Object value = OgnlUtil.getValue(expression.getExpression(), data);		
		if(value == null){
			if(expression.isNullAllowed()){
				value = expression.getNullValue();
			}else{
				throw new FPMergeException(FPConsts.MESSAGE_ID_EL_EXPRESSION_UNDEFINED,
						new Object[]{expression.getExpression(),new Integer(targetElement.cell.getRow().getHSSFRow().getRowNum() + 1)});
			}		
		}
		return value;		
	}
	

}

