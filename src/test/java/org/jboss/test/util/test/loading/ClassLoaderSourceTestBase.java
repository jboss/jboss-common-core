/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.test.util.test.loading;

import org.jboss.test.BaseTestCase;
import org.jboss.util.loading.ClassLoaderSource;

/**
 * Base class for testing {@link ClassLoaderSource} implementations.
 * 
 * @author Brian Stansberry
 */
public abstract class ClassLoaderSourceTestBase extends BaseTestCase
{
   protected ClassLoader origCl;
   
   /**
    * @param name
    */
   public ClassLoaderSourceTestBase(String name)
   {
      super(name);
   }
   
   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      
      origCl = Thread.currentThread().getContextClassLoader();
   }
   
   @Override
   protected void tearDown() throws Exception
   {
      try
      {
         super.tearDown();
      }
      finally
      {
         Thread.currentThread().setContextClassLoader(origCl);
      }
   }
   
   protected abstract ClassLoaderSource createClassLoaderSource();
   protected abstract ClassLoader getExpectedClassLoader(ClassLoader tccl);
   
   /**
    * Tests that the ClassLoaderSource returns the expected classloader
    * when the TCCL is null.
    */
   public void testNullTCCL()
   {
      checkCorrectClassLoaderSource(null);
   }
   
   /**
    * Tests that the ClassLoaderSource returns the expected classloader
    * when the TCCL is the basic one in place when this test is executed.
    */
   public void testOriginalTCCL()
   {
      checkCorrectClassLoaderSource(origCl);
   }
   
   /**
    * Tests that the ClassLoaderSource returns the expected classloader
    * when the TCCL is the CLS impl's own classloader.
    */
   public void testImplClassLoader()
   {
      checkCorrectClassLoaderSource(createClassLoaderSource().getClass().getClassLoader());
   }
   
   /**
    * Tests that the ClassLoaderSource returns the expected classloader
    * when the TCCL is a child classloader.
    */
   public void testDifferentTCCL()
   {
      checkCorrectClassLoaderSource(getExtendedClassLoader());
   }
   
   protected void checkCorrectClassLoaderSource(ClassLoader tccl)
   {
      Thread.currentThread().setContextClassLoader(tccl);
      ClassLoaderSource cls = createClassLoaderSource();
      assertSame("ClassLoaderSource returned expected cl", getExpectedClassLoader(tccl), cls.getClassLoader());
   }
   
   protected ClassLoader getExtendedClassLoader()
   {
      return new ExtendedClassLoader(origCl);
   }
   
   private class ExtendedClassLoader extends ClassLoader
   {
      ExtendedClassLoader(ClassLoader parent)
      {
         super(parent);
      }
   }

}
