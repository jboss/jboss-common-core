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

package org.jboss.test.util.test.threadpool;

import junit.framework.TestCase;
import org.jboss.util.loading.ClassLoaderSource;
import org.jboss.util.threadpool.BasicThreadPool;

/**
 * Tests thread context classloader management by BasicThreadPool.
 * 
 * @author Brian Stansberry
 */
public class BasicThreadPoolTCCLTestCase extends TestCase
{
   protected ClassLoader origCl;
   
   /**
    * @param name
    */
   public BasicThreadPoolTCCLTestCase(String name)
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
   
   public void testNullClassLoaderSource() throws Exception
   {
      ClassLoader existing = origCl == null ? getExtendedClassLoader() : origCl;
      Thread.currentThread().setContextClassLoader(existing);
      runClassLoaderSourceTest(null, existing);
      
      Thread.currentThread().setContextClassLoader(null);
      runClassLoaderSourceTest(null, null);      
   }
   
   public void testConfiguredClassLoaderSource() throws Exception
   {
      ClassLoaderSource source = new TestClassLoaderSource();
      runClassLoaderSourceTest(source, source.getClassLoader());
   }
   
   private void runClassLoaderSourceTest(ClassLoaderSource source, ClassLoader expected) throws Exception
   {
      ThreadGroup group = new ThreadGroup("Test");
      BasicThreadPool pool = new BasicThreadPool("Test", group);
      // Only one thread so we can use it twice to confirm 
      // it gets cleaned after the first task
      pool.setMaximumPoolSize(1);
      pool.setClassLoaderSource(source);
      
      Task task = new Task();
      pool.run(task);
      
      // Wait for task completion
      synchronized (task)
      {
         if (!task.done)
         {
            task.wait();
         }
      }
      
//      ClassLoader expected = source == null ? null : source.getClassLoader();
      assertEquals(expected, task.incomingCl);
      
      Task task2 = new Task();
      pool.run(task2);
      
      // Wait for task completion
      synchronized (task2)
      {
         if (!task2.done)
         {
            task2.wait();
         }
      }
      
      assertSame(expected, task.incomingCl);
      
      assertEquals("Pool size limited", 1, pool.getPoolSize());
   }
   
   private ClassLoader getExtendedClassLoader()
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
   
   private class Task implements Runnable
   {
      private ClassLoader incomingCl;
      private boolean done;
      
      public void run()
      {
         incomingCl = Thread.currentThread().getContextClassLoader();
         
         // Deliberately pollute the TCCL
         ClassLoader leakedCl = BasicThreadPoolTCCLTestCase.this.getExtendedClassLoader();
         Thread.currentThread().setContextClassLoader(leakedCl);
         
         // Wake up the test thread
         synchronized (this)
         {
            done = true;         
            notifyAll();
         }
      }    
   }
   
   private class TestClassLoaderSource implements ClassLoaderSource
   {
      private ClassLoader cl;
      
      TestClassLoaderSource()
      {
         cl = BasicThreadPoolTCCLTestCase.this.getExtendedClassLoader();
      }

      public ClassLoader getClassLoader()
      {
         return cl;
      }
   }

}
