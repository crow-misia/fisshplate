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
package org.seasar.fisshplate.interceptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author rokugen
 */
public class S2FisshplateInterceptorTest extends S2TestCase {
	private TestFisshplate fisshplate;
	
	protected void setUp()throws Exception{
		include("test.dicon");		
	}
	
	protected void tearDown(){
		
	}
	
	public void test�C���^�Z�v�^�̃e�X�g(){
		TestFisshplateDto dto =new TestFisshplateDto();
		dto.setTitle("�^�C�g���ł�");
		List<TestItem> itemList = new ArrayList<TestItem>();
		itemList.add(new TestItem("1�s��",10,new Date()));
		itemList.add(new TestItem("2�s��",20,new Date()));
		itemList.add(new TestItem("3�s��",30,new Date()));
		
		dto.setItemList(itemList);
		
		HSSFWorkbook wb = fisshplate.getTestWb(dto);
		
	}

}
