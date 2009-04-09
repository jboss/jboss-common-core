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

package org.jboss.test.util.test;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jboss.util.TimedCachePolicy;

import junit.framework.TestCase;

/**
 * Test for JBCOMMON-50.
 * 
 * @author Brian Stansberry
 */
public class TimedCachePolicyClassLoaderLeakTestCase extends TestCase
{
   protected void tearDown() throws Exception
   {
      System.clearProperty(TimedCachePolicy.TIMER_CLASSLOADER_PROPERTY);
   }
   
   public void testUseSystemClassLoader() throws Exception
   {
      classLoaderLeaktoTimerTest(TimedCachePolicy.TIMER_CLASSLOADER_SYSTEM, false);
   }
   
   public void testUseCurrentClassLoader() throws Exception
   {
      classLoaderLeaktoTimerTest(TimedCachePolicy.TIMER_CLASSLOADER_CURRENT, false);
   }
   
   public void testUseContextClassLoader() throws Exception
   {
      // Here we expect to leak the CL
      classLoaderLeaktoTimerTest(TimedCachePolicy.TIMER_CLASSLOADER_CONTEXT, true);
   }
   
   public void testUseBogusClassLoader() throws Exception
   {
      classLoaderLeaktoTimerTest("bogus", false);
   }
   
   private void classLoaderLeaktoTimerTest(String timerCL, boolean expectLeak) throws Exception
   {
      ClassLoader origClassLoader = Thread.currentThread().getContextClassLoader();
      ClassLoader isolated = new IsolatedTimedCachePolicyClassLoader(origClassLoader);
      ClassLoader cl = new ClassLoader(isolated){};
      WeakReference<ClassLoader> clRef = new WeakReference<ClassLoader>(cl);
      Thread.currentThread().setContextClassLoader(cl);
      Object policy = null;
      try
      {
         policy = createTimedCachePolicy(timerCL);
      }
      finally
      {
         Thread.currentThread().setContextClassLoader(origClassLoader);
      }
      
      cl = null;
      System.gc();
      if (expectLeak)
      {
         assertNotNull("ClassLoader not collected", clRef.get());
      }
      else
      {
         assertNull("ClassLoader collected", clRef.get());
      }
      
      if (policy != null)
      {
         destroyTimedCachePolicy(policy);
      }
   }
   
   private static Object createTimedCachePolicy(String timerCL) throws Exception
   {
      System.setProperty(TimedCachePolicy.TIMER_CLASSLOADER_PROPERTY, timerCL);
      Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass("org.jboss.util.TimedCachePolicy");
      Object obj = clazz.newInstance();
      Method create = clazz.getDeclaredMethod("create", new Class[0]);
      Method start = clazz.getDeclaredMethod("start", new Class[0]);
      Method insert = clazz.getDeclaredMethod("insert", new Class[]{Object.class, Object.class});
      create.invoke(obj, new Object[0]);
      start.invoke(obj, new Object[0]);
      insert.invoke(obj, new Object[]{new Object(), new Object()});
      return obj;
   }
   
   private static void destroyTimedCachePolicy(Object policy) throws Exception
   {
      Class<?> clazz = policy.getClass();
      Method stop = clazz.getDeclaredMethod("stop", new Class[0]);
      Method destroy = clazz.getDeclaredMethod("destroy", new Class[0]);
      stop.invoke(policy, new Object[0]);
      destroy.invoke(policy, new Object[0]);
      
   }
   
   private static class IsolatedTimedCachePolicyClassLoader extends ClassLoader
   {      
      private Map<String, Class<?>> clazzes = new HashMap<String, Class<?>>();

      public IsolatedTimedCachePolicyClassLoader()
      {
         this(Thread.currentThread().getContextClassLoader());
      }
      
      public IsolatedTimedCachePolicyClassLoader(ClassLoader parent)
      {
         super(parent);
      }
      
      @Override
      protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
      {
         if (name.startsWith("org.jboss.util.TimedCachePolicy"))
         {
            Class<?> c = findClass(name);
            if (resolve)
            {
               resolveClass(c);
            }
            return c;
         }
         else
         {
            return super.loadClass(name, resolve);
         }
      }

      @Override
      protected Class<?> findClass(String name) throws ClassNotFoundException
      {
         if (name.startsWith("org.jboss.util.TimedCachePolicy"))
         {
            Class<?> clazz = clazzes.get(name);
            if (clazz == null)
            {
               String path = name.replace('.', '/').concat(".class");
               InputStream stream = getParent().getResourceAsStream(path);
               ByteArrayOutputStream baos = new ByteArrayOutputStream();
               byte[] input = new byte[1024];
               int read = 0;
               try
               {
                  while ((read = stream.read(input)) > -1)
                  {
                     baos.write(input, 0, read);
                  }
               }
               catch (IOException e)
               {
                  throw new RuntimeException(e);
               }
               byte[] bytes = baos.toByteArray();
               clazz = defineClass(name, bytes, 0, bytes.length);
               clazzes.put(name, clazz);
            }
            return clazz;
         }
         else
         {
            return super.findClass(name);
         }
      }      
      
   }

}
