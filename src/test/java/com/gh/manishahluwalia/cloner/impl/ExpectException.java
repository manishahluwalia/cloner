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

import org.junit.Test;

/**
 * Used when you are writing a test where you expect a particular piece of code to throw a specific type of exception / error.
 * The test fails if this exact type of exception is not thrown. The test will continue successfully if the exception is thrown.
 * <p/>
 * Example:
 * <pre>
 * public badServiceArgumentTest() {
 *   // Set up mocked objects and the main class under test
 *   DbInterface db = mock(DbInterface.class); // mock object used by main test class
 *   final Service subject = new Service();  // class under test
 *   
 *   // Invoke method being tested
 *   String argument = new String("Bad Argument");
 *   new ExpectException(InvalidArgumentException.class) {
 *     protected void run() throws Throwable {
 *        service.processArgument(argument);
 *     }
 *   }
 *   
 *   // Check conditions
 *   verify(db.rollBack());
 * }
 * </pre>
 * This is better than the <code>expected</code> parameter of the {@link Test} annotation because the test can continue
 * after the exception has been thrown and can still do some more verification work, as shown in the example.
 */
public abstract class ExpectException {
	/**
	 * @param expect The class of the exception to be expected. This exception is swallowed silently
	 * @throws IllegalStateException If the expected exception is not thrown
	 */
	public ExpectException(Class<? extends Throwable> expect) {
		boolean noExceptions=false;
		try {
			run();
			noExceptions=true;
		} catch (Throwable thrown) {
			if (!expect.isInstance(thrown)) {
				throw new IllegalStateException("Unexpected exception of type " + thrown.getClass().getName(), thrown);
			}
		}
		if (noExceptions) {
			throw new IllegalStateException("Exception of type " + expect.getName() + " not thrown");
		}
	}
	
	/**
	 * The method that implements the part of the test code that is expected to throw an exception
	 */
	abstract protected void run() throws Throwable;
}
