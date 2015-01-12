/*
* Copyright 2014 Manish Ahluwalia
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.gh.manishahluwalia.cloner.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gh.manishahluwalia.cloner.impl.RecursiveReflexiveCloner;


/**
 * <p>
 * Indicates that this field should NOT be cloned when using
 * {@link RecursiveReflexiveCloner}. Not having this annotation on a
 * field means that it may or may not be copied over. If you
 * want a field copied over, use {@link Clone}.
 * </p><p>
 * See {@link Clone} for full rules.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface DoNotClone
{
    public Class<? extends Projection>[] value() default {};
}
