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
package org.jboss.util.deadlock;

/**
 * This exception class is thrown when application deadlock is detected when trying to lock an entity bean
 * This is probably NOT a result of a jboss bug, but rather that the application is access the same entity
 * beans within 2 different transaction in a different order.  Remember, with a PessimisticEJBLock, 
 * Entity beans are locked until the transaction commits or is rolled back.
 *
 * @author <a href="bill@burkecentral.com">Bill Burke</a>
 *
 * @version $Revision$
 *
 * <p><b>Revisions:</b><br>
 * <p><b>2002/02/13: billb</b>
 *  <ol>
 *  <li>Initial revision
 *  </ol>
 */
public class ApplicationDeadlockException extends RuntimeException
{
   protected boolean retry = false;


   public ApplicationDeadlockException()
   {
      super();
   }

   public ApplicationDeadlockException(String msg, boolean retry)
   {
      super(msg);
      this.retry = retry;
   }

   public boolean retryable()
   {
      return retry;
   }

   
   /**
    * Detects exception contains is or a ApplicationDeadlockException.
    */
   public static ApplicationDeadlockException isADE(Throwable t)
   {
      while (t!=null)
      {
         if (t instanceof ApplicationDeadlockException)
         {
            return (ApplicationDeadlockException)t;
         }
         else
         {
            t = t.getCause();
         }
      }
      return null;
   }

}

