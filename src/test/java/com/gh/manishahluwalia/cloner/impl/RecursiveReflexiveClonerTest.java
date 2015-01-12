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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gh.manishahluwalia.cloner.annotations.Clone;
import com.gh.manishahluwalia.cloner.annotations.CopyFromClient;
import com.gh.manishahluwalia.cloner.annotations.DoNotClone;
import com.gh.manishahluwalia.cloner.annotations.Projection;
import com.gh.manishahluwalia.cloner.annotations.ReflexivelyClonable;
import com.gh.manishahluwalia.cloner.impl.CloningDisposition;
import com.gh.manishahluwalia.cloner.impl.CloningError;
import com.gh.manishahluwalia.cloner.impl.RecursiveReflexiveCloner;
import com.gh.manishahluwalia.cloner.impl.RecursiveReflexiveCloner.FieldAccessorClonerWrapper;


public class RecursiveReflexiveClonerTest {
    
    @ReflexivelyClonable
	public static class ClassA {
		@Clone public int field1;
		@Clone public Integer field2;
		@Clone public Date field3;
		@Clone @CopyFromClient public String field4;
		@Clone public int[] field5;
		public void setField1 (int field1)
        {
            this.field1 = field1;
        }
        public void setField2 (Integer field2)
        {
            this.field2 = field2;
        }
        public void setField3 (Date field3)
        {
            this.field3 = field3;
        }
        public void setField4 (String field4)
        {
            this.field4 = field4;
        }
        public void setField5 (int[] field5)
        {
            this.field5 = field5;
        }
        public int getField1() {
			return field1;
		}
		public Integer getField2() {
			return field2;
		}
		public Date getField3() {
			return field3;
		}
		public String getField4() {
			return field4;
		}
		public int[] getField5() {
			return field5;
		}
	}
	
    @ReflexivelyClonable
	public static class ClassB {
		@Clone public LinkedList<Integer> field1;
		@Clone public LinkedList<String>[] field2;
		@Clone public ClassA field3;
		public void setField1 (LinkedList<Integer> field1)
        {
            this.field1 = field1;
        }
        public void setField2 (LinkedList<String>[] field2)
        {
            this.field2 = field2;
        }
        public void setField3 (ClassA field3)
        {
            this.field3 = field3;
        }
        public LinkedList<Integer> getField1() {
			return field1;
		}
		public LinkedList<String>[] getField2() {
			return field2;
		}
		public ClassA getField3() {
			return field3;
		}
	}

	/**
	 * This simulates a class whose fields are initialized lazily, when the getter is
	 * called. This happens for instance when an ORM enhances entity classes.
	 * This tests that the cloner is using the getter method as opposed to
	 * accessing the field1 directly.
	 */
    @ReflexivelyClonable
	public static class LazyField {
		@Clone public LinkedList<Integer> field=null;
		public void setField (LinkedList<Integer> field)
        {
            this.field = field;
        }
        public LinkedList<Integer> getField() {
			field = new LinkedList<Integer>();
			return field;
		}
	}
    
    @ReflexivelyClonable
    public static class CopyableA {
        @CopyFromClient public int x;
        @CopyFromClient @Clone public Integer y;
        @CopyFromClient public CopyableB z;
        public void setX (int x)
        {
            this.x = x;
        }
        public void setY (Integer y)
        {
            this.y = y;
        }
        public void setZ (CopyableB z)
        {
            this.z = z;
        }
        public int getX ()
        {
            return x;
        }
        public Integer getY ()
        {
            return y;
        }
        public CopyableB getZ ()
        {
            return z;
        }
    }
    
    @ReflexivelyClonable
    public static class CopyableB {
        @CopyFromClient public int x;
        public int y;
        public void setX (int x)
        {
            this.x = x;
        }
        public void setY (int y)
        {
            this.y = y;
        }
        public int getX ()
        {
            return x;
        }
        public int getY ()
        {
            return y;
        }
    }

    /**
     *  This class helps test that non clonable fields (ones not marked with {@link Clone}
     *  are not cloned.
     */
    @ReflexivelyClonable
    public static class PartlyClonable {
        @DoNotClone public Integer field1;
        @Clone public Integer field2;
        public Integer field3;
        public void setField1 (Integer field1)
        {
            this.field1 = field1;
        }
        public Integer getField1 ()
        {
            return field1;
        }
        public Integer getField2 ()
        {
            return field2;
        }
        public void setField2 (Integer field2)
        {
            this.field2 = field2;
        }
        public void setField3 (Integer field3)
        {
            this.field3 = field3;
        }
        public Integer getField3 ()
        {
            return field3;
        }
    }
    
    /**
     *  This class helps test that non copyable fields (ones not marked with {@link CopyFromClient}
     *  are not copied from the client.
     */
    @ReflexivelyClonable
    public static class NoCopyableField {
        @Clone public int field;
        public void setField (int field)
        {
            this.field = field;
        }
        public int getField ()
        {
            return field;
        }
    }
	
	/*
	 * This class is not declared as clonable
	 */
	public static class NotClonableClass {
	    @Clone @CopyFromClient public int x;
        public void setX (int x)
        {
            this.x = x;
        }
        public int getX ()
        {
            return x;
        }
	}

    @ReflexivelyClonable
	public static class ClassC {
	    @Clone public int x;
	    @Clone ClassD y;
        public void setX (int x)
        {
            this.x = x;
        }
        public void setY (ClassD y)
        {
            this.y = y;
        }
        public int getX ()
        {
            return x;
        }
        public ClassD getY ()
        {
            return y;
        }
	}
	
    @ReflexivelyClonable
	public static class ClassD {
	    @Clone public long p;
	    @Clone ClassC q;
        public void setP (long p)
        {
            this.p = p;
        }
        public void setQ (ClassC q)
        {
            this.q = q;
        }
        public long getP ()
        {
            return p;
        }
        public ClassC getQ ()
        {
            return q;
        }
	}
    
    @ReflexivelyClonable
    public static class ClassE {
        @Clone public ClassE[] x;
        @Clone public LinkedList<Integer> y;
        public void setX (ClassE[] x)
        {
            this.x = x;
        }
        public void setY (LinkedList<Integer> y)
        {
            this.y = y;
        }
        public ClassE[] getX ()
        {
            return x;
        }
        public LinkedList<Integer> getY()
        {
            return y;
        }
    }
	
	@SuppressWarnings("serial")
	public static class SubLinkedList<E> extends LinkedList<E> {}
	
	private RecursiveReflexiveCloner cloner;
	
	@Before
	public void setUp() throws Exception {
		cloner = new RecursiveReflexiveCloner();
	}

	@Test
	public void nullTests() {
		Assert.assertNull(cloner.copyForGwtRpcIfNeeded(null));
		Assert.assertNull(cloner.deepClone(null));
        new ExpectException(NullPointerException.class) {
            @Override
            protected void run () throws Throwable
            {
                cloner.shallowCopyFieldsFromClient(null, new Object());
            }
        };
        new ExpectException(NullPointerException.class) {
            @Override
            protected void run () throws Throwable
            {
                cloner.shallowCopyFieldsFromClient(new Object(), null);
            }
        };
        new ExpectException(NullPointerException.class) {
            @Override
            protected void run () throws Throwable
            {
                cloner.shallowCopyFieldsFromClient(null, null);
            }
        };
	}

	@Test
	public void nonClonableClassThrowsException()
	{
	    final NotClonableClass source = new NotClonableClass();
	    source.x = 55;
	    
        new ExpectException(CloningError.class) {
            @Override
            protected void run () throws Throwable
            {
                cloner.deepClone(source);
            }
        };

        new ExpectException(CloningError.class) {
            @Override
            protected void run () throws Throwable
            {
                cloner.copyForGwtRpcIfNeeded(source);
            }
        };

        final NotClonableClass destination = new NotClonableClass();
        new ExpectException(CloningError.class) {
            @Override
            protected void run () throws Throwable
            {
                cloner.shallowCopyFieldsFromClient(destination, source);
            }
        };
	}
	
	@Test
	public void simpleFieldsTestsA() {
		ClassA orig = new ClassA();
		orig.field1=42;
		orig.field2=72;
		orig.field3=new Date(96);
		orig.field4 = "hello";
		orig.field5 = new int[] { 7, 13};
				
		ClassA gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
		ClassA deepClone = cloner.deepClone(orig);
		
		Assert.assertTrue(orig==gwtClone);
		Assert.assertFalse(orig==deepClone);
		
		Assert.assertTrue(orig.field1==deepClone.field1);
		Assert.assertTrue(orig.field2==deepClone.field2);
		Assert.assertFalse(orig.field3==deepClone.field3);
		Assert.assertEquals(orig.field3, deepClone.field3);
		Assert.assertTrue(orig.field4==deepClone.field4);
		Assert.assertFalse(orig.field5==deepClone.field5);
		Assert.assertTrue(Arrays.equals(orig.field5, deepClone.field5));
	}

	@Test
	public void simpleNullFieldsTestA() {
		ClassA orig = new ClassA();
		
		ClassA gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
		ClassA deepClone = cloner.deepClone(orig);
		
		Assert.assertTrue(orig==gwtClone);
		Assert.assertFalse(orig==deepClone);
		
		Assert.assertTrue(orig.field1==deepClone.field1);
		Assert.assertNull(deepClone.field2);
		Assert.assertNull(deepClone.field3);
		Assert.assertNull(deepClone.field4);
		Assert.assertNull(orig.field5);
	}

	@Test
	public void simpleNullFieldsTestB() {
		ClassB orig = new ClassB();
		
		ClassB gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
		ClassB deepClone = cloner.deepClone(orig);
		
		Assert.assertTrue(orig==gwtClone);
		Assert.assertFalse(orig==deepClone);
		
		Assert.assertNull(deepClone.field1);
		Assert.assertNull(deepClone.field2);
		Assert.assertNull(deepClone.field3);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void simpleFieldsTestsB() {
		ClassB orig = new ClassB();
		orig.field1 = new LinkedList<Integer>();
		orig.field1.add(42);
		orig.field1.add(null);
		orig.field1.add(Integer.MIN_VALUE);
		orig.field2 = new LinkedList[]{new LinkedList<String>(), null};
		orig.field2[0].add("hola");
		orig.field2[0].add(null);
		orig.field2[0].add("hello");
		orig.field3 = new ClassA();
		orig.field3.field1=42;
		orig.field3.field2=72;
		orig.field3.field3=new Date(96);
		orig.field3.field4 = "hello";
		orig.field3.field5 = new int[] { 7, 13};
		
		ClassB gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
		ClassB deepClone = cloner.deepClone(orig);
		
		Assert.assertTrue(orig==gwtClone);
		Assert.assertFalse(orig==deepClone);
		
		Assert.assertFalse(orig.field1==deepClone.field1);
		Assert.assertTrue(orig.field1.equals(deepClone.field1));
		Assert.assertFalse(orig.field2==deepClone.field2);
		Assert.assertTrue(Arrays.equals(orig.field2,deepClone.field2));
		
		Assert.assertFalse(orig.field3==deepClone.field3);
		Assert.assertTrue(orig.field3.field1==deepClone.field3.field1);
		Assert.assertTrue(orig.field3.field2==deepClone.field3.field2);
		Assert.assertFalse(orig.field3.field3==deepClone.field3.field3);
		Assert.assertEquals(orig.field3.field3, deepClone.field3.field3);
		Assert.assertTrue(orig.field3.field4==deepClone.field3.field4);
		Assert.assertFalse(orig.field3.field5==deepClone.field3.field5);
		Assert.assertTrue(Arrays.equals(orig.field3.field5, deepClone.field3.field5));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void subClassedLinkedList() {
		ClassB orig = new ClassB();
		orig.field1 = new SubLinkedList<Integer>();
		orig.field1.add(42);
		orig.field1.add(null);
		orig.field1.add(Integer.MIN_VALUE);
		orig.field2 = new LinkedList[]{new LinkedList<String>(), null};
		orig.field2[0].add("hola");
		orig.field2[0].add(null);
		orig.field2[0].add("hello");
		
		ClassB gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
		ClassB deepClone = cloner.deepClone(orig);
		
		Assert.assertNotSame(orig,gwtClone);
		Assert.assertNotSame(orig,deepClone);
		
		Assert.assertNotSame(orig.field1,gwtClone.field1);
		Assert.assertTrue(orig.field1.equals(gwtClone.field1));
		Assert.assertNotSame(orig.field1,deepClone.field1);
		Assert.assertTrue(orig.field1.equals(deepClone.field1));

		Assert.assertNotSame(orig.field2,gwtClone.field2);
		Assert.assertNotSame(orig.field2,deepClone.field2);
        Assert.assertTrue(Arrays.equals(orig.field2,gwtClone.field2));
        Assert.assertTrue(Arrays.equals(orig.field2,deepClone.field2));
	}
	
	@Test
	public void lazyFieldGwtCloneTest() {
		LazyField orig = new LazyField();
		
		LazyField gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
		
		Assert.assertNotNull(orig.field);
		Assert.assertSame(orig, gwtClone);
	}
	
	@Test
	public void lazyFieldDeepCloneTest() {
		LazyField orig = new LazyField();
		
		LazyField deepClone = cloner.deepClone(orig);
		
		Assert.assertNotNull(orig.field);
		Assert.assertNotSame(orig, deepClone);
		Assert.assertNotNull(deepClone.field);
	}
    
    @Test
    public void simpleCopyTest() {
        CopyableA source = new CopyableA();
        source.x = 17;
        source.y = 22;
        source.z = null;
        CopyableA dest = new CopyableA();
        dest.x = 32;
        dest.y = 980;
        dest.z = new CopyableB();
        
        cloner.shallowCopyFieldsFromClient(dest, source);
        
        Assert.assertSame(17, dest.x);
        Assert.assertSame(22, dest.y);
        Assert.assertNull(dest.z);
    }
    
    @Test
    public void twoLevelSourceCopyTest() {
        CopyableA source = new CopyableA();
        CopyableB source2 = new CopyableB();
        source.x = 17;
        source.y = 22;
        source.z = source2;
        source2.x = 78;
        source2.y = 98;
        CopyableA dest = new CopyableA();
        dest.x = 32;
        dest.y = 980;
        dest.z = new CopyableB();
        dest.z.x = 234;
        dest.z.y = 123;
        
        cloner.shallowCopyFieldsFromClient(dest, source);
        
        Assert.assertSame(17, dest.x);
        Assert.assertSame(22, dest.y);
        Assert.assertSame(78, dest.z.x);
        Assert.assertSame(98, dest.z.y);
    }
    
    @Test
    public void nullNonClonableFieldsNotTouched() {
        PartlyClonable orig = new PartlyClonable();
        orig.field2 = 42;
        orig.field3 = 93;
        
        PartlyClonable gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
        PartlyClonable deepClone = cloner.deepClone(orig);
        
        Assert.assertSame(orig, gwtClone);
        Assert.assertNotSame(orig, deepClone);
        
        Assert.assertSame(orig.field2, deepClone.field2);
        Assert.assertNull(deepClone.field3);
    }
    
    @Test
    public void nonNullNonClonableFieldsNotCloned() {
        PartlyClonable orig = new PartlyClonable();
        orig.field1 = 17;
        orig.field2 = 42;
        orig.field3 = 93;
        
        PartlyClonable gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
        PartlyClonable deepClone = cloner.deepClone(orig);
        
        Assert.assertNotSame(orig, gwtClone);
        Assert.assertNotSame(orig, deepClone);
        
        Assert.assertNotNull(orig.field1);
        Assert.assertNull(gwtClone.field1);
        Assert.assertNull(deepClone.field1);
        
        Assert.assertSame(orig.field2, gwtClone.field2);
        Assert.assertSame(orig.field2, deepClone.field2);

        Assert.assertNull(gwtClone.field3);
        Assert.assertNull(deepClone.field3);
        
}
    
    @Test
    public void nonCopyableFieldsNotTouched() {
        NoCopyableField source = new NoCopyableField();
        source.field = 7;
        NoCopyableField dest = new NoCopyableField();
        dest.field = 9;

        cloner.shallowCopyFieldsFromClient(dest, source);
        
        Assert.assertEquals(7, source.field);
        Assert.assertEquals(9, dest.field);
    }
    
	/*
	 * We make a linked list of a simple type and try to gwt and deep clone it
	 */
    @Test
    public void linkedListOfClonableTypeNoDeepCopyNeeded()
    {
        List<ClassA> orig = new LinkedList<ClassA>();
        ClassA first = new ClassA();
        ClassA second = new ClassA();
        first.field1 = 36;
        second.field1 = 48;
        orig.add(first);
        orig.add(second);
        
        List<ClassA> gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
        List<ClassA> deepClone = cloner.deepClone(orig);
        
        Assert.assertSame(orig, gwtClone);
        Assert.assertNotSame(orig, deepClone);
        
        Assert.assertNotSame(orig.get(0), deepClone.get(0));
        Assert.assertNotSame(orig.get(1), deepClone.get(1));

        Assert.assertTrue(orig.get(0).field1==deepClone.get(0).field1);
        Assert.assertTrue(orig.get(1).field1==deepClone.get(1).field1);
    }
    
    /*
     * We make a linked list where the 2nd element in the list cannot be gwtCloned.
     * Verify that gwtClone is a deepClone
     */
    @Test
    public void linkedListOfClonableTypeDeepCopyNeeded()
    {
        List<ClassB> orig = new LinkedList<ClassB>();
        ClassB first = new ClassB();
        ClassB second = new ClassB();
        LinkedList<Integer> list = new SubLinkedList<Integer>();
        list.add(48);
        second.field1 = list;
        orig.add(first);
        orig.add(second);
        
        List<ClassB> gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
        List<ClassB> deepClone = cloner.deepClone(orig);
        
        Assert.assertNotSame(orig, gwtClone);
        Assert.assertNotSame(orig, deepClone);
        
        Assert.assertNotSame(orig.get(0), gwtClone.get(0));
        Assert.assertNotSame(orig.get(1), gwtClone.get(1));

        Assert.assertNotSame(orig.get(0), deepClone.get(0));
        Assert.assertNotSame(orig.get(1), deepClone.get(1));
        
        Assert.assertTrue(orig.get(1).field1.get(0) == gwtClone.get(1).field1.get(0));
        
        Assert.assertTrue(orig.get(1).field1.get(0) == deepClone.get(1).field1.get(0));
    }
    
    @Test
    public void cycleTest()
    {
        ClassC innerC = new ClassC();
        ClassC outerC = new ClassC();
        ClassD classD = new ClassD();
        innerC.x = 5;
        innerC.y = classD;
        classD.p = 6;
        classD.q = innerC;
        outerC.x = 7;
        outerC.y = classD;
        
        ClassC gwtClone = cloner.copyForGwtRpcIfNeeded(outerC);
        ClassC deepClone = cloner.deepClone(outerC);
        
        Assert.assertSame(outerC, gwtClone);
        Assert.assertNotSame(outerC, deepClone);
        
        Assert.assertNotSame(deepClone, deepClone.y.q);
        Assert.assertSame(deepClone.y, deepClone.y.q.y);
        
        Assert.assertTrue(outerC.x==deepClone.x);
        Assert.assertTrue(outerC.y.p==deepClone.y.p);
        Assert.assertTrue(outerC.y.q.x==deepClone.y.q.x);
    }
    
    @Test
    public void complexCyclesTest()
    {
        ClassE e1 = new ClassE();
        ClassE e2 = new ClassE();
        ClassE e3 = new ClassE();
        ClassE[] orig = new ClassE[]{e1, e2, e3};
        e1.x = orig;
        e2.y = new SubLinkedList<Integer>();
        e3.x = new ClassE[]{null, null};
        
        ClassE[] gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
        ClassE[] deepClone = cloner.deepClone(orig);
        
        Assert.assertNotSame(orig, gwtClone);
        Assert.assertNotSame(orig, deepClone);
        Assert.assertEquals(orig.length, gwtClone.length);
        Assert.assertEquals(orig.length, deepClone.length);
        
        Assert.assertNotSame(orig[0], gwtClone[0]);
        Assert.assertNotSame(orig[0], deepClone[0]);        
        Assert.assertSame(gwtClone, gwtClone[0].x);
        Assert.assertSame(deepClone, deepClone[0].x);
        
        Assert.assertNotSame(orig[1], gwtClone[1]);
        Assert.assertNotSame(orig[1], deepClone[1]);
        
        Assert.assertNotSame(orig[2], gwtClone[2]);
        Assert.assertNotSame(orig[2], deepClone[2]);
        Assert.assertTrue(Arrays.equals(orig[2].x, gwtClone[2].x));
        Assert.assertTrue(Arrays.equals(orig[2].x, deepClone[2].x));
    }
    
    @ReflexivelyClonable
    static class ClassWithStaticField {
        @Clone public static int x;
        public static int getX ()
        {
            return x;
        }
    }    
    @Test
    public void classWithStaticFieldNotClonable()
    {
        final ClassWithStaticField c = new ClassWithStaticField();

        new ExpectException(CloningError.class) {
            
            @Override
            protected void run () throws Throwable
            {
                cloner.copyForGwtRpcIfNeeded(c);
            }
        };

        new ExpectException(CloningError.class) {
            
            @Override
            protected void run () throws Throwable
            {
                cloner.deepClone(c);
            }
        };
    }
    
    @ReflexivelyClonable
    static class ClassWithFinalField {
        @Clone public final int x=5;
        public final int getX ()
        {
            return x;
        }
    }    
    @Test
    public void classWithFinalFieldNotClonable()
    {
        final ClassWithFinalField c = new ClassWithFinalField();

        new ExpectException(CloningError.class) {
            
            @Override
            protected void run () throws Throwable
            {
                cloner.copyForGwtRpcIfNeeded(c);
            }
        };

        new ExpectException(CloningError.class) {
            
            @Override
            protected void run () throws Throwable
            {
                cloner.deepClone(c);
            }
        };
    }
    
    static class SuperClass {
        @Clone public int x;
        public int z;
        public void setX (int x)
        {
            this.x = x;
        }
        public void setZ (int z)
        {
            this.z = z;
        }
        public int getX ()
        {
            return x;
        }
        public int getZ ()
        {
            return z;
        }
    }
    @ReflexivelyClonable
    static class SubClass extends SuperClass {
        @Clone public int y;
        public void setY (int y)
        {
            this.y = y;
        }
        public int getY ()
        {
            return y;
        }
    }
    @Test
    public void inheritedClonableFieldsAreCloned()
    {
        SubClass orig = new SubClass();
        orig.x=7;
        orig.y=9;
        orig.z=11;
        
        SubClass gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
        SubClass deepClone = cloner.deepClone(orig);
        
        Assert.assertSame(orig, gwtClone);
        Assert.assertNotSame(orig, deepClone);
        
        Assert.assertEquals(orig.x, deepClone.x);
        Assert.assertEquals(orig.y, deepClone.y);
        Assert.assertFalse(orig.z==deepClone.z);
    }
    
    @ReflexivelyClonable
    static class ClassF {
        @Clone LinkedList<Integer> list1;
        @Clone LinkedList<Integer> list2;
        public void setList1 (LinkedList<Integer> list1)
        {
            this.list1 = list1;
        }
        public void setList2 (LinkedList<Integer> list2)
        {
            this.list2 = list2;
        }
        public LinkedList<Integer> getList1 ()
        {
            return list1;
        }
        public LinkedList<Integer> getList2 ()
        {
            return list2;
        }
    }
    @Test
    public void sameLinkedListIsClonedToSame()
    {
        ClassF orig = new ClassF();
        orig.list1 = new LinkedList<Integer>();
        orig.list2 = orig.list1;
        
        ClassF gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
        ClassF deepClone = cloner.deepClone(orig);
        
        Assert.assertSame(orig, gwtClone);
        Assert.assertNotSame(orig, deepClone);
        
        Assert.assertSame(deepClone.list1,deepClone.list2);
    }

    @Test
    public void differentLinkedListIsClonedToDifferent()
    {
        ClassF orig = new ClassF();
        orig.list1 = new LinkedList<Integer>();
        orig.list2 = new LinkedList<Integer>();
        
        ClassF gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
        ClassF deepClone = cloner.deepClone(orig);
        
        Assert.assertSame(orig, gwtClone);
        Assert.assertNotSame(orig, deepClone);
        
        Assert.assertNotSame(deepClone.list1,deepClone.list2);
    }
    
    @ReflexivelyClonable
    static class ClassG {
        @Clone LinkedList<Integer> list; // To help make this non-gwtclonable
        public void setList (LinkedList<Integer> list)
        {
            this.list = list;
        }
        public LinkedList<Integer> getList ()
        {
            return list;
        }
        // All instances of ClassG are "equal" to one another
        @Override public int hashCode() { return 5; }
        @Override public boolean equals(Object other) { return ClassG.class.isInstance(other); }
    }
    @Test
    public void gwtClonableChecksForObjectIdentityNotEquals()
    {
        ClassG g1 = new ClassG();
        g1.list = null; // g1 is GWT clonable
        ClassG g2 = new ClassG();
        g2.list = new SubLinkedList<Integer>(); // g2 is not GWT clonable, needs a deep clone
        ClassG[] orig = new ClassG[]{g1,g2};
        // Now orig has 2 items which are "equal". Since the 2nd is not GWT clonable,
        // the array is not GWT clonable. We want to check that the gwtClone is a deep clone
        
        ClassG[] gwtClone = cloner.copyForGwtRpcIfNeeded(orig);
        
        Assert.assertNotSame(orig, gwtClone);
    }
    
    public interface ProjectionA extends Projection {}
    public interface ProjectionB extends Projection {}
    public interface ProjectionC extends Projection {}
    
    @ReflexivelyClonable
    public static class BadProjectionTest1
    {
        @Clone @DoNotClone
        public Integer field;

        public Integer getField ()
        {
            return field;
        }

        public void setField (Integer field)
        {
            this.field = field;
        }
    }
    
    @ReflexivelyClonable
    public static class BadProjectionTest2
    {
        @Clone(ProjectionA.class) @DoNotClone({ProjectionB.class, ProjectionA.class})
        public Integer field;

        public Integer getField ()
        {
            return field;
        }

        public void setField (Integer field)
        {
            this.field = field;
        }
    }
    
    @Test
    public void badProjectionTest()
    {
        new ExpectException(CloningError.class) {
            @Override
            protected void run() throws Throwable {
                cloner.copyForGwtRpcIfNeeded(new BadProjectionTest1());
            }
        };
        new ExpectException(CloningError.class) {
            @Override
            protected void run() throws Throwable {
                cloner.deepClone(new BadProjectionTest2());
            }
        };
    }
    
    @ReflexivelyClonable
    public static class ProjectionsTest
    {
        @Clone(ProjectionA.class) @DoNotClone
        public Integer field1;
        @Clone(ProjectionB.class) @DoNotClone(ProjectionA.class)
        public Integer field2;
        @Clone({ProjectionA.class, ProjectionB.class})
        public Integer field3;
        
        public Integer getField1 ()
        {
            return field1;
        }
        public void setField1 (Integer field1)
        {
            this.field1 = field1;
        }
        public Integer getField2 ()
        {
            return field2;
        }
        public void setField2 (Integer field2)
        {
            this.field2 = field2;
        }
        public Integer getField3 ()
        {
            return field3;
        }
        public void setField3 (Integer field3)
        {
            this.field3 = field3;
        }
    }
    
    @Test
    public void projectionsAreRespectedTest1()
    {
        ProjectionsTest orig = new ProjectionsTest();
        orig.field1 = 14;
        orig.field2 = 97;
        orig.field3 = 1212;
        
        ProjectionsTest test1gwtClone = cloner.copyForGwtRpcIfNeeded(orig, ProjectionA.class);
        ProjectionsTest test1deepClone = cloner.deepClone(orig, ProjectionA.class);
        
        Assert.assertSame(orig.field1, test1gwtClone.field1);
        Assert.assertSame(orig.field1, test1deepClone.field1);
        Assert.assertNull(test1gwtClone.field2);
        Assert.assertNull(test1deepClone.field2);
        Assert.assertSame(orig.field3, test1gwtClone.field3);
        Assert.assertSame(orig.field3, test1deepClone.field3);
        
        ProjectionsTest test2gwtClone = cloner.copyForGwtRpcIfNeeded(orig, ProjectionB.class);
        ProjectionsTest test2deepClone = cloner.deepClone(orig, ProjectionB.class);
        
        Assert.assertNull(test2deepClone.field1);
        Assert.assertNull(test2gwtClone.field1);
        Assert.assertSame(orig.field2, test2deepClone.field2);
        Assert.assertSame(orig.field3, test2deepClone.field3);
    }
    
    @Test
    public void projectionsAreRespectedTest2()
    {
        ProjectionsTest orig = new ProjectionsTest();
        orig.field1 = 14;
        orig.field3 = 1212;
        
        ProjectionsTest test1gwtClone = cloner.copyForGwtRpcIfNeeded(orig, ProjectionA.class);
        ProjectionsTest test1deepClone = cloner.deepClone(orig, ProjectionA.class);

        Assert.assertSame(orig, test1gwtClone);
        Assert.assertNotSame(orig, test1deepClone);
        Assert.assertSame(orig.field1, test1gwtClone.field1);
        Assert.assertSame(orig.field1, test1deepClone.field1);
        Assert.assertNull(test1gwtClone.field2);
        Assert.assertNull(test1deepClone.field2);
        Assert.assertSame(orig.field3, test1gwtClone.field3);
        Assert.assertSame(orig.field3, test1deepClone.field3);
        
        ProjectionsTest test2gwtClone = cloner.copyForGwtRpcIfNeeded(orig, ProjectionB.class);
        ProjectionsTest test2deepClone = cloner.deepClone(orig, ProjectionB.class);
        
        Assert.assertNull(test2gwtClone.field1);
        Assert.assertNull(test2deepClone.field1);
        Assert.assertSame(orig.field2, test2deepClone.field2);
        Assert.assertSame(orig.field3, test2deepClone.field3);
    }

    static class CloningDispositionTest {
        public Integer noAnnotations;
        
        @Clone
        public Integer cloned;
        
        @DoNotClone
        public Integer notCloned;
        
        @Clone(ProjectionA.class)
        public Integer clonedOnA;
        
        @DoNotClone(ProjectionA.class)
        public Integer notClonedOnA;
        
        @Clone(ProjectionA.class) @DoNotClone
        public Integer clonedOnAOnly;
        
        @DoNotClone(ProjectionA.class) @Clone
        public Integer notClonedOnAOnly;
        
        @Clone(ProjectionA.class) @DoNotClone(ProjectionB.class)
        public Integer clonedOnAButNotB;

        public Integer getNoAnnotations ()
        {
            return noAnnotations;
        }

        public void setNoAnnotations (Integer noAnnotations)
        {
            this.noAnnotations = noAnnotations;
        }

        public Integer getCloned ()
        {
            return cloned;
        }

        public void setCloned (Integer cloned)
        {
            this.cloned = cloned;
        }

        public Integer getNotCloned ()
        {
            return notCloned;
        }

        public void setNotCloned (Integer notCloned)
        {
            this.notCloned = notCloned;
        }

        public Integer getClonedOnA ()
        {
            return clonedOnA;
        }

        public void setClonedOnA (Integer clonedOnA)
        {
            this.clonedOnA = clonedOnA;
        }

        public Integer getNotClonedOnA ()
        {
            return notClonedOnA;
        }

        public void setNotClonedOnA (Integer notClonedOnA)
        {
            this.notClonedOnA = notClonedOnA;
        }

        public Integer getClonedOnAOnly ()
        {
            return clonedOnAOnly;
        }

        public void setClonedOnAOnly (Integer clonedOnAOnly)
        {
            this.clonedOnAOnly = clonedOnAOnly;
        }

        public Integer getNotClonedOnAOnly ()
        {
            return notClonedOnAOnly;
        }

        public void setNotClonedOnAOnly (Integer notClonedOnAOnly)
        {
            this.notClonedOnAOnly = notClonedOnAOnly;
        }

        public Integer getClonedOnAButNotB ()
        {
            return clonedOnAButNotB;
        }

        public void setClonedOnAButNotB (Integer clonedOnAButNotB)
        {
            this.clonedOnAButNotB = clonedOnAButNotB;
        }
    }
    private void testExpected(
            String name,
            CloningDisposition expectOnNoting,
            CloningDisposition expectOnA,
            CloningDisposition expectOnB,
            CloningDisposition expectOnC) throws Exception {
        Field f = CloningDispositionTest.class.getField(name);
        FieldAccessorClonerWrapper subject = cloner.new FieldAccessorClonerWrapper(f, f.getAnnotation(Clone.class), f.getAnnotation(DoNotClone.class), null);
        Assert.assertEquals(expectOnNoting, subject.getCloningDisposition(null));
        Assert.assertEquals(expectOnA, subject.getCloningDisposition(ProjectionA.class));
        Assert.assertEquals(expectOnB, subject.getCloningDisposition(ProjectionB.class));
        Assert.assertEquals(expectOnC, subject.getCloningDisposition(ProjectionC.class));
    }
    @Test
    public void cloningDispositionUnitTest() throws Exception
    {
        testExpected("noAnnotations",
                CloningDisposition.DONT_CARE,
                CloningDisposition.DONT_CARE,
                CloningDisposition.DONT_CARE,
                CloningDisposition.DONT_CARE);
        testExpected("cloned",
                CloningDisposition.CLONE,
                CloningDisposition.CLONE,
                CloningDisposition.CLONE,
                CloningDisposition.CLONE);
        testExpected("notCloned",
                CloningDisposition.DONT_CLONE,
                CloningDisposition.DONT_CLONE,
                CloningDisposition.DONT_CLONE,
                CloningDisposition.DONT_CLONE);
        testExpected("clonedOnA",
                CloningDisposition.CLONE,
                CloningDisposition.CLONE,
                CloningDisposition.DONT_CARE,
                CloningDisposition.DONT_CARE);
        testExpected("notClonedOnA",
                CloningDisposition.DONT_CLONE,
                CloningDisposition.DONT_CLONE,
                CloningDisposition.DONT_CARE,
                CloningDisposition.DONT_CARE);
        testExpected("clonedOnAButNotB",
                CloningDisposition.DONT_CARE,
                CloningDisposition.CLONE,
                CloningDisposition.DONT_CLONE,
                CloningDisposition.DONT_CARE);
        testExpected("notClonedOnAOnly",
                CloningDisposition.CLONE,
                CloningDisposition.DONT_CLONE,
                CloningDisposition.CLONE,
                CloningDisposition.CLONE);
        testExpected("clonedOnAOnly",
                CloningDisposition.DONT_CLONE,
                CloningDisposition.CLONE,
                CloningDisposition.DONT_CLONE,
                CloningDisposition.DONT_CLONE);
    }
    
    @ReflexivelyClonable
    static class DefaultValuesTest
    {
        @DoNotClone String s;
        @DoNotClone boolean b;
        @DoNotClone int i;
        @DoNotClone float f;
        @DoNotClone Set<Integer> set;
        @DoNotClone long[] arr;
        public String getS ()
        {
            return s;
        }
        public void setS (String s)
        {
            this.s = s;
        }
        public boolean isB ()
        {
            return b;
        }
        public void setB (boolean b)
        {
            this.b = b;
        }
        public int getI ()
        {
            return i;
        }
        public void setI (int i)
        {
            this.i = i;
        }
        public float getF ()
        {
            return f;
        }
        public void setF (float f)
        {
            this.f = f;
        }
        public Set<Integer> getSet ()
        {
            return set;
        }
        public void setSet (Set<Integer> set)
        {
            this.set = set;
        }
        public long[] getArr ()
        {
            return arr;
        }
        public void setArr (long[] arr)
        {
            this.arr = arr;
        }
    }
    @Test
    public void allNonClonableFieldsHaveDefaultValues()
    {
        DefaultValuesTest orig = new DefaultValuesTest();
        orig.s = "hey nonny no";
        orig.i = 42;
        orig.b = true;
        orig.f = 3.14f;
        orig.set = new HashSet<Integer>(Arrays.asList(4,2));
        orig.arr = new long[]{9,6};
        
        DefaultValuesTest clone = cloner.copyForGwtRpcIfNeeded(orig);
        
        Assert.assertNull(clone.s);
        Assert.assertEquals(0, clone.i);
        Assert.assertFalse(clone.b);
        Assert.assertEquals(0.0f, clone.f, 0);
        Assert.assertNull(clone.set);
        Assert.assertNull(clone.arr);
    }
    
    @ReflexivelyClonable
    static class ProjectedCopy
    {
        @CopyFromClient String x;
        @CopyFromClient(ProjectionC.class) int y;
        @CopyFromClient({ProjectionA.class, ProjectionB.class}) Integer z;
        public String getX ()
        {
            return x;
        }
        public void setX (String x)
        {
            this.x = x;
        }
        public int getY ()
        {
            return y;
        }
        public void setY (int y)
        {
            this.y = y;
        }
        public Integer getZ ()
        {
            return z;
        }
        public void setZ (Integer z)
        {
            this.z = z;
        }
    }
    @Test
    public void projectedCopyTest() {
        ProjectedCopy source = new ProjectedCopy();
        source.x = "abba";
        source.y = 22;
        source.z = 37;
        ProjectedCopy dest = new ProjectedCopy();

        dest.x = "hulahoop";
        dest.y = -23;
        dest.z = -787;
        
        cloner.shallowCopyFieldsFromClient(dest, source);
        
        Assert.assertEquals("abba", dest.x);
        Assert.assertEquals(-23, dest.y);
        Assert.assertEquals(Integer.valueOf(-787), dest.z);
        
        dest.x = "hulahoop";
        dest.y = -23;
        dest.z = -787;

        cloner.shallowCopyFieldsFromClient(dest, source, ProjectionC.class);
        
        Assert.assertEquals("abba", dest.x);
        Assert.assertEquals(22, dest.y);
        Assert.assertEquals(Integer.valueOf(-787), dest.z);
        
        dest.x = "hulahoop";
        dest.y = -23;
        dest.z = -787;

        cloner.shallowCopyFieldsFromClient(dest, source, ProjectionA.class);
        
        Assert.assertEquals("abba", dest.x);
        Assert.assertEquals(-23, dest.y);
        Assert.assertEquals(Integer.valueOf(37), dest.z);        
    }
    
    @ReflexivelyClonable
    static class Interface {}
    @ReflexivelyClonable
    static class Implementation extends Interface {
        @Clone private int x;
        public void setX (int x)
        {
            this.x = x;
        }
        public int getX ()
        {
            return x;
        }
    }
    @ReflexivelyClonable
    static class InterfaceContainer {
        @Clone private Interface contained;
        public void setContained (Interface contained)
        {
            this.contained = contained;
        }
        public Interface getContained ()
        {
            return contained;
        }
    }
    @Test
    public void derivedInterfacesAreCopiedCorrectly() {
        InterfaceContainer container = new InterfaceContainer();
        Implementation containee = new Implementation();
        containee.setX(42);
        container.setContained(containee);
        
        InterfaceContainer gwtClone = cloner.copyForGwtRpcIfNeeded(container);
        InterfaceContainer deepClone = cloner.deepClone(container);
        
        Assert.assertNotSame(deepClone, container);
        Assert.assertSame(gwtClone, container);
        
        Assert.assertEquals(42, ((Implementation)deepClone.getContained()).getX());
    }
    
    @ReflexivelyClonable
    static class SuperContainee {}
    @ReflexivelyClonable
    static class Containee extends SuperContainee {
        @Clone private int x;
        public void setX (int x)
        {
            this.x = x;
        }
        public int getX ()
        {
            return x;
        }
    }
    @ReflexivelyClonable
    static class Container {
        @Clone private Containee contained;
        public void setContained (Containee contained)
        {
            this.contained = contained;
        }
        public Containee getContained ()
        {
            return contained;
        }
    }
    @Test
    public void derivedClassesAreCopiedCorrectly() {
        Container container = new Container();
        Containee containee = new Containee();
        containee.setX(42);
        container.setContained(containee);
        
        Container gwtClone = cloner.copyForGwtRpcIfNeeded(container);
        Container deepClone = cloner.deepClone(container);
        
        Assert.assertNotSame(deepClone, container);
        Assert.assertSame(gwtClone, container);
        
        Assert.assertEquals(42, ((Containee)deepClone.getContained()).getX());
    }
    
    @ReflexivelyClonable
    static class MyObjectContainer {
        @Clone private Object o;
        public void setO (Object o)
        {
            this.o = o;
        }
        public Object getO ()
        {
            return o;
        }
    }
    @Test
    public void incorrectlyTypedButCorrectlyInstantiatedObjectIsClonable() {
        MyObjectContainer c = new MyObjectContainer();
        c.setO(new Long(42));
        
        MyObjectContainer gwtClone = cloner.copyForGwtRpcIfNeeded(c);
        MyObjectContainer deepClone = cloner.deepClone(c);
        
        Assert.assertSame(c, gwtClone);
        Assert.assertNotSame(c, deepClone);
        
        Assert.assertEquals(42l, deepClone.getO());
    }
    
    @ReflexivelyClonable
    static class MyImmutableClass {
        @Clone private Integer x;
        public void setX (Integer x)
        {
            this.x = x;
        }
        public Integer getX ()
        {
            return x;
        }
    }
    @ReflexivelyClonable
    static class ImmutableContainer {
        @Clone private MyImmutableClass x;
        public void setX (MyImmutableClass x)
        {
            this.x = x;
        }
        public MyImmutableClass getX ()
        {
            return x;
        }
    }
    @Test
    public void specifiedTypeIsTreatedAsImmutable() {
        ImmutableContainer orig = new ImmutableContainer();
        orig.setX(new MyImmutableClass());
        
        HashSet<Class<?>> immutableTypes = new HashSet<Class<?>>();
        immutableTypes.add(MyImmutableClass.class);
        RecursiveReflexiveCloner cloner = new RecursiveReflexiveCloner(immutableTypes);
        ImmutableContainer deepClone = cloner.deepClone(orig);
        
        Assert.assertSame(orig.getX(), deepClone.getX());
    }
}
