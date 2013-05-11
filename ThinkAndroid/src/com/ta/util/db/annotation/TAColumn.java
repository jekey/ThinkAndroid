/*
 * Copyright (C) 2013  WhiteCat °×Ã¨ (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ta.util.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title TAColumn ×Ö¶ÎÅäÖÃ
 * @Package com.ta.util.db.annotation
 * @Description Êý¾Ý×Ö¶Î×¢½â
 * @author °×Ã¨
 * @date 2013-1-20
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface TAColumn
{
	/**
	 * ÉèÖÃ×Ö¶ÎÃû
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * ×Ö¶ÎÄ¬ÈÏÖµ
	 * 
	 * @return
	 */
	public String defaultValue() default "";
}
