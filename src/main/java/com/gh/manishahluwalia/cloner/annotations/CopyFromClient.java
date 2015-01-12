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
 * Indicates that this field should be copied over from
 * the client to the server when using
 * {@link RecursiveReflexiveCloner}. Not having this annotation on a
 * field means that the field in the server's object is left
 * untouched. Having this annotation means that the field in the
 * server's object is overwritten with the field on the client's
 * object. 
 * </p><p>
 * Additionally, you can specify that the annotation only applies to
 * certain {@link Projection}s. The annotation then does not apply
 * for any other projections (including the default, unspecified
 * projection). Not specifying any projections in the
 * annotation means it applies to all projections.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface CopyFromClient
{
    public Class<? extends Projection>[] value() default {};
}
