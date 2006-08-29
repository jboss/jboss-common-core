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
package org.jboss.util.threadpool;

import org.jboss.logging.Logger;

/**
 * Makes a runnable a task.
 *
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 * @version $Revision$
 */
public class RunnableTaskWrapper implements TaskWrapper
{
   // Constants -----------------------------------------------------

   /** The log */
   private static final Logger log = Logger.getLogger(RunnableTaskWrapper.class);

   // Attributes ----------------------------------------------------

   /** The runnable */
   private Runnable runnable;
   private boolean started;
   private Thread runThread;
   /** The start timeout */
   private long startTimeout;
   /** The completion timeout */
   private long completionTimeout;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   /**
    * Create a new RunnableTaskWrapper
    *
    * @param runnable the runnable
    * @throws IllegalArgumentException for a null runnable
    */
   public RunnableTaskWrapper(Runnable runnable)
   {
      this(runnable, 0, 0);
   }
   public RunnableTaskWrapper(Runnable runnable, long startTimeout, long completeTimeout)
   {
      if (runnable == null)
         throw new IllegalArgumentException("Null runnable");
      this.runnable = runnable;
      this.startTimeout = startTimeout;
      this.completionTimeout = completeTimeout;
   }

   // Public --------------------------------------------------------

   // TaskWrapper implementation ---------------------------------------

   public int getTaskWaitType()
   {
      return Task.WAIT_NONE;
   }

   public int getTaskPriority()
   {
      return Thread.NORM_PRIORITY;
   }

   public long getTaskStartTimeout()
   {
      return startTimeout;
   }

   public long getTaskCompletionTimeout()
   {
      return completionTimeout;
   }

   public void acceptTask()
   {
      // Nothing to do
   }

   public void rejectTask(RuntimeException t)
   {
      throw t;
   }

   public void stopTask()
   {
      boolean trace = log.isTraceEnabled();
      // Interrupt the run thread if its not null
      if( runThread != null && runThread.isInterrupted() == false )
      {
         runThread.interrupt();
         if( trace )
            log.trace("stopTask, interrupted thread="+runThread);
      }
      else if( runThread != null )
      {
         /* If the thread has not been returned after being interrupted, then
         use the deprecated stop method to try to force the thread abort.
         */
         runThread.stop();
         if( trace )
            log.trace("stopTask, stopped thread="+runThread);
      }
   }

   public void waitForTask()
   {
      // Nothing to do
   }

   public boolean isComplete()
   {
      return started == true && runThread == null;
   }
   // Runnable implementation ---------------------------------------

   public void run()
   {
      boolean trace = log.isTraceEnabled();
      try
      {
         if( trace )
            log.trace("Begin run, wrapper="+this);
         runThread = Thread.currentThread();
         started = true;
         runnable.run();
         runThread = null;
         if( trace )
            log.trace("End run, wrapper="+this);
      }
      catch (Throwable t)
      {
         log.warn("Unhandled throwable for runnable: " + runnable, t);
      }
   }

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------
}
