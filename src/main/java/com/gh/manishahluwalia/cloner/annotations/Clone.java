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
 * Indicates that this field should be cloned when using
 * {@link RecursiveReflexiveCloner}. Not having this annotation on a
 * field means that it may or may not be copied over. If you DO NOT
 * want a field copied over, use {@link DoNotClone}.
 * </p><p>
 * The following section applies to both {@link Clone} and {@link DoNotClone}:
 * </p><p>
 * Additionally, you can specify that the annotation only applies to
 * certain {@link Projection}s. The annotation then does not apply
 * for any other projections. Not specifying any projections in the
 * annotation means it applies to all projections not explicitly
 * specified in the dual annotation. The same projection cannot
 * be specified in both {@link Clone} and {@link DoNotClone}.
 * </p><p>
 * As a corollary to the above, it is illegal
 * to annotate a field both
 * {@link Clone} and {@link DoNotClone} without any projections.
 * </p><p>
 * The following examples may make this clearer:
 * 
 * <pre>
 * public interface ProjectionA extends Projection {}
 * public interface ProjectionB extends Projection {}
 * public interface ProjectionC extends Projection {}
 * 
 * &#064;ReflexivelyClonable
 * public class Model {
 *   &#064;Clone({ProjectionA.class, ProjectionB.class})
 *   int x;
 *   
 *   &#064;Clone(ProjectionA.class)
 *   &#064;DoNotClone(ProjectionB.class)
 *   int y;
 * }
 * </pre>
 * <table border="1">
 * <caption>What is cloned</caption>
 * <tr>
 * <th>Field</th>
 * <th>ProjectionA</th>
 * <th>ProjectionB</th>
 * <th>ProjectionC</th>
 * </tr>
 * <tr>
 * <td>x</td>
 * <td>cloned</td>
 * <td>cloned</td>
 * <td>don't care</td>
 * </tr>
 * <tr>
 * <td>x</td>
 * <td>cloned</td>
 * <td>not cloned (is 0 in client copy)</td>
 * <td>don't care</td>
 * </tr>
 * </table>
 * <p>
 * Note: that projection checking is done by using "==", so the exact
 * projection class must be used, not a derived class.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface Clone
{
    public Class<? extends Projection>[] value() default {};
}
