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
package com.gh.manishahluwalia.cloner.impl;

import java.util.Map;
import java.util.Set;

import com.gh.manishahluwalia.cloner.annotations.Projection;

abstract class Cloner
{
    public abstract boolean cloneNeededForGwt (Object object, Class<? extends Projection> projection, Set<Object> alreadyChecked);

    public abstract Object deepClone (Object source, Class<? extends Projection> projection, Map<Object, Object> alreadyXlated);

    public void copyFromClient (Object serverDestinationObject, Object clientSourceObject, Class<? extends Projection> projection)
    {
        throw new CloningError("Method copyFromClient() not overridden in " + this.getClass().getName());
    }

}
