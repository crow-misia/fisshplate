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
package org.seasar.fisshplate.util;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.core.FPParser;
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
	 * <p>{@link FPParser}���g���āA�e���v���[�g�I�u�W�F�N�g�𐶐��A�f�[�^�𖄂ߍ��݂܂��B</p>
	 * <p>��O�����s����O�Ƀ��b�v���܂��B</p>
	 * @param templateWb �e���v���[�g�p���[�N�u�b�N
	 * @param data ���ߍ��݃f�[�^
	 * @return �o�͂��郏�[�N�u�b�N
	 */
	public static final HSSFWorkbook createTemplateAndProcess(HSSFWorkbook templateWb,Map<String, Object> data){		
		try {
			FPTemplate template = new FPTemplate(templateWb);		
			return template.process(data);
		} catch (FPParseException e1) {
			throw new FPParseRuntimeException(e1);
		} catch (FPMergeException e) {
			throw new FPMergeRuntimeException(e);
		}
	}

}
