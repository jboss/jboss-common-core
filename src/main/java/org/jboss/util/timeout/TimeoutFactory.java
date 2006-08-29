/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.util.timeout;

import org.jboss.util.NestedRuntimeException;
import org.jboss.util.ThrowableHandler;
import org.jboss.util.threadpool.BasicThreadPool;
import org.jboss.util.threadpool.BlockingMode;
import org.jboss.util.threadpool.ThreadPool;

import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;

/**
 * The timeout factory.
 *
 * @author <a href="osh@sparre.dk">Ole Husgaard</a>
 * @author <a href="dimitris@jboss.org">Dimitris Andreadis</a>
 * @author <a href="genman@maison-otaku.net">Elias Ross</a>  
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class TimeoutFactory
{
   /** The priority queue property */
   private static final String priorityQueueProperty = TimeoutPriorityQueue.class.getName();

   /** The priority queue default */
   private static final String priorityQueueName = TimeoutPriorityQueueImpl.class.getName();
   
   /** Our singleton instance */
   private static TimeoutFactory singleton;
   
   /** Number of TimeoutFactories created */
   private static int timeoutFactoriesCount = 0;
   
   /** The priority queue class */
   private static Class priorityQueueClass;
   
   /** The default threadpool used to execute timeouts */
   private static BasicThreadPool DEFAULT_TP = new BasicThreadPool("Timeouts");
   static
   {
      DEFAULT_TP.setBlockingMode(BlockingMode.RUN);

      String priorityQueueClassName = priorityQueueName;
      ClassLoader cl = TimeoutFactory.class.getClassLoader();
      try
      {
         priorityQueueClassName = System.getProperty(priorityQueueProperty, priorityQueueName);
         cl = Thread.currentThread().getContextClassLoader();
      }
      catch (Exception ignored)
      {
      }
      
      try
      {
         priorityQueueClass = cl.loadClass(priorityQueueClassName);
      }
      catch (Exception e)
      {
         throw new NestedRuntimeException(e.toString(), e);
      }
   }

   /** Used for graceful exiting */
   private SynchronizedBoolean cancelled = new SynchronizedBoolean(false);
   
   /** The daemon thread that dequeues timeouts tasks and issues
       them for execution to the thread pool */ 
   private Thread workerThread;
   
   /** Per TimeoutFactory thread pool used to execute timeouts */
   private ThreadPool threadPool;
   
   /** The priority queue */
   private TimeoutPriorityQueue queue;

   /** Lazy constructions of the TimeoutFactory singleton */
   public synchronized static TimeoutFactory getSingleton()
   {
      if (singleton == null)
      {
         singleton = new TimeoutFactory(DEFAULT_TP);
      }
      return singleton;
   }
   
   /**
    *  Schedules a new timeout using the singleton TimeoutFactory
    */
   static public Timeout createTimeout(long time, TimeoutTarget target)
   {
      return getSingleton().schedule(time, target);
   }
   
   /**
    * Constructs a new TimeoutFactory that uses the provided ThreadPool
    */
   public TimeoutFactory(ThreadPool threadPool)
   {
      this.threadPool = threadPool;
      try
      {
         queue = (TimeoutPriorityQueue) priorityQueueClass.newInstance(); 
      }
      catch (Exception e)
      {
         throw new RuntimeException("Cannot instantiate " + priorityQueueClass,e);
      }
      
      // setup the workerThread
      workerThread = new Thread("TimeoutFactory-" + timeoutFactoriesCount++)
      {
         public void run()
         {
            doWork();
         }
      };
      workerThread.setDaemon(true);
      workerThread.start();
   }
   
   /**
    * Constructs a new TimeoutFactory that uses the default thread pool
    */
   public TimeoutFactory()
   {
      this(DEFAULT_TP);
   }
   
   /**
    * Schedules a new timeout.
    * 
    * @param time absolute time
    * @param target target to fire
    */
   public Timeout schedule(long time, TimeoutTarget target)
   {
      if (cancelled.get())
         throw new IllegalStateException("TimeoutFactory has been cancelled");      
      if (time < 0)
         throw new IllegalArgumentException("Negative time");
      if (target == null)
         throw new IllegalArgumentException("Null timeout target");

      return queue.offer(time, target);
   }
   
   /**
    * Schedules a new timeout.
    * 
    * @param time absolute time
    * @param run runnable to run
    */
   public Timeout schedule(long time, Runnable run)
   {
      return schedule(time, new TimeoutTargetImpl(run));
   }
   
   /**
    * Cancels all submitted tasks, stops the worker
    * thread and clean-ups everything except for the
    * thread pool. Scheduling new timeouts after cancel
    * is called results in a IllegalStateException.
    */
   public void cancel()
   {
      // obviously the singleton TimeoutFactory cannot
      // be cancelled since its reference is not accessible
      
      // let the worker thread cleanup
      if (cancelled.set(true) == false);
      {
         // Cancel the priority queue
         queue.cancel();
      }
   }
   
   /**
    * Returns true if the TimeoutFactory has been cancelled,
    * false if it is operational (i.e. accepts timeout schedules).
    */
   public boolean isCancelled()
   {
      return cancelled.get();
   }

   /**
    *  Timeout worker method.
    */
   private void doWork()
   {
      while (cancelled.get() == false)
      {
         TimeoutExt work = queue.take();
         // Do work, if any
         if (work != null)
         {
            // Wrap the TimeoutExt with a runnable that invokes the target callback
            TimeoutWorker worker = new TimeoutWorker(work);
            try
            {
               threadPool.run(worker);
            }
            catch (Throwable t)
            {
               // protect the worker thread from pool enqueue errors
               ThrowableHandler.add(ThrowableHandler.Type.ERROR, t);
            }
            synchronized (work)
            {
               work.done();
            }            
         }
      }
      
      // TimeoutFactory was cancelled
      queue.cancel();
   }
   
   /**
    *  A runnable that fires the timeout callback
    */
   private static class TimeoutWorker implements Runnable
   {
      private TimeoutExt work;

      /**
       *  Create a new instance.
       *
       *  @param work The timeout that should be fired.
       */
      TimeoutWorker(TimeoutExt work)
      {
         this.work = work;
      }

      /**
       *  Override to fire the timeout.
       */
      public void run()
      {
         try
         {
            work.getTimeoutTarget().timedOut(work);
         }
         catch (Throwable t)
         {
            // protect the thread pool thread from receiving this error
            ThrowableHandler.add(ThrowableHandler.Type.ERROR, t);
         }
         synchronized (work)
         {
            work.done();
         }
      }
   }
   
   /**
    * Simple TimeoutTarget implementation that wraps a Runnable
    */
   private static class TimeoutTargetImpl implements TimeoutTarget
   {
      Runnable runnable;
      
      TimeoutTargetImpl(Runnable runnable)
      {
         this.runnable = runnable;
      }
      
      public void timedOut(Timeout ignored)
      {
         runnable.run();
      }
   }
}