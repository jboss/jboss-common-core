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

import org.jboss.util.loading.ClassLoaderSource;
import org.jboss.util.loading.ConstructorTCCLClassLoaderSource;

/**
 * Unit tests of {@link ConstructorTCCLClassLoaderSource}.
 *  
 * @author Brian Stansberry
 */
public class ConstructorTCCLClassLoaderSourceUnitTestCase extends ClassLoaderSourceTestBase
{

   public ConstructorTCCLClassLoaderSourceUnitTestCase(String name)
   {
      super(name);
   }

   @Override
   protected ClassLoaderSource createClassLoaderSource()
   {
      return new ConstructorTCCLClassLoaderSource();
   }

   @Override
   protected ClassLoader getExpectedClassLoader(ClassLoader tccl)
   {
      return tccl;
   }
   
   /**
    * Tests that the ClassLoaderSource does not prevent garbage collection of
    * the classloader.
    */
   public void testGarbageCollectedClassLoader()
   {
      ClassLoader cl = getExtendedClassLoader();
      Thread.currentThread().setContextClassLoader(cl);
      ClassLoaderSource cls = createClassLoaderSource();
      Thread.currentThread().setContextClassLoader(origCl);
      cl = null;
      
      System.gc();
      
      assertNull("ClassLoader garbage collected", cls.getClassLoader());
   }

}
