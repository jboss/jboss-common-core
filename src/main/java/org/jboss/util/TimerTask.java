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
package org.jboss.util;

/**
 * A class that represent a task that can be scheduled for one-shot or
 * repeated execution by a {@link TimerQueue}. <p>
 * A similar class is present in java.util package of jdk version >= 1.3;
 * for compatibility with jdk 1.2 we reimplemented it.
 *
 * @see TimerQueue
 * 
 * @author <a href="mailto:simone.bordet@compaq.com">Simone Bordet</a>
 * @version $Revision$
 */
public abstract class TimerTask 
   implements Executable, Comparable
{
   /** The state before the first execution */
   static final int NEW = 1;
   /** The state after first execution if the TimerTask is repeating */
   static final int SCHEDULED = 2;
   /** The state after first execution if the TimerTask is not repeating */
   static final int EXECUTED = 3;
   /** The state when cancelled */
   static final int CANCELLED = 4;

   // Attributes ----------------------------------------------------
   private final Object m_lock = new Object();
   private int m_state;
   // this is a constant, and need not be locked
   private final long m_period;
   private long m_nextExecutionTime;

   /**
    * Creates a TimerTask object that will be executed once.
    */
   protected TimerTask() 
   {
      m_state = NEW;
      m_period = 0;
   }
   
   /**
    * Creates a TimerTask object that will be executed every <code>period</code>
    * milliseconds. <p>
    * @param period the execution period; if zero, will be executed only once.
    */
   protected TimerTask(long period) 
   {
      m_state = NEW;
      if (period < 0) throw new IllegalArgumentException("Period can't be negative");
      m_period = period;
   }

   /**
    * Cancels the next execution of this TimerTask (if any). <br>
    * If the TimerTask is executing it will prevent the next execution (if any).
    * @returns true if one or more scheduled execution will not take place,
    * false otherwise.
    */
   public boolean cancel() 
   {
      synchronized (getLock()) 
      {
         boolean ret = (m_state == SCHEDULED);
         m_state = CANCELLED;
         return ret;
      }
   }

   // Executable implementation ---------------------------------------

   /**
    * The task to be executed, to be implemented in subclasses.
    */
   public abstract void execute() throws Exception;

   // Comparable implementation ---------------------------------------
   
   /**
    * A TimerTask is less than another if it will be scheduled before.
    * @throws ClassCastException if other is not a TimerTask, according to the Comparable contract
    */
   public int compareTo(Object other)
   {
      if (other == this) return 0;
      TimerTask t = (TimerTask) other;
      long diff = getNextExecutionTime() - t.getNextExecutionTime();
      return (int) diff;
   }

   /** Returns the mutex that syncs the access to this object */
   Object getLock() 
   {
      return m_lock;
   }
   
   /** Sets the state of execution of this TimerTask */
   void setState(int state) 
   {
      synchronized (getLock())
      {
         m_state = state;
      }
   }
   
   /** Returns the state of execution of this TimerTask */
   int getState() 
   {
      synchronized (getLock())
      {
         return m_state;
      }
   }
   
   /** Returns whether this TimerTask is periodic */
   boolean isPeriodic() 
   {
      return m_period > 0;
   }
   
   /** Returns the next execution time for this TimerTask */
   long getNextExecutionTime() 
   {
      synchronized (getLock())
      {
         return m_nextExecutionTime;
      }
   }
   
   /** Sets the next execution time for this TimerTask */
   void setNextExecutionTime(long time) 
   {
      synchronized (getLock())
      {
         m_nextExecutionTime = time;
      }
   }

   /** Returns the period of this TimerTask */
   protected long getPeriod() 
   {
      return m_period;
   }
}
