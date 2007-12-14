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
package org.seasar.fisshplate.exception;

/**
 * テンプレート解析時に投げられる例外クラスです。
 * @author rokugen
 *
 */
public class FPParseException extends FPException {
	private static final long serialVersionUID = 1L;

	/**
	 * メッセージIDを元にリソースバンドルからメッセージを取得します。
	 * @param messageId メッセージID
	 */
	public FPParseException(String messageId){
		super(messageId);
	}
	
	/**
	 * メッセージIDを元にリソースバンドルからメッセージを取得します。
	 * @param messageId メッセージID
	 * @param args 埋め込みパラメータ
	 */
	public FPParseException(String messageId, Object[]args){
		super(messageId,args);
	}

}
