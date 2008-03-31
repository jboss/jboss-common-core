/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.test.util.test.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.jboss.util.threadpool.BasicThreadPool;

/**
 * Tests of thread pool with Tasks added to the pool
 *
 * @see org.jboss.util.threadpool.ThreadPool
 * @author <a href="adrian@jboss.org">Adrian.Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 2787 $
 */
public class ThreadPoolInterruptedThreadUnitTestCase extends TestCase
{
   public ThreadPoolInterruptedThreadUnitTestCase(String name)
   {
      super(name);
   }

   public static class TestRunnable implements Runnable
   {
      public CountDownLatch latch = new CountDownLatch(1);
      public void run()
      {
         latch.countDown();
      }
   }
   
   public void testInterruptedExecute() throws Exception
   {
      BasicThreadPool pool = new BasicThreadPool();
      TestRunnable runnable = new TestRunnable();
      
      Thread.currentThread().interrupt();
      try
      {
         pool.run(runnable);
      }
      finally
      {
         assertTrue(Thread.interrupted());
      }
      assertTrue(runnable.latch.await(10, TimeUnit.SECONDS));
   }
}
