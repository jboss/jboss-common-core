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
 * Wait exclusive semaphore with wait - notify primitives
 *
 * @author <a href="mailto:simone.bordet@compaq.com">Simone Bordet</a>
 * @version $Revision$
 */
public class WaitSemaphore
   extends Semaphore
   implements WaitSync
{
   // Constants -----------------------------------------------------
   private final static int MAX_USERS_ALLOWED = 1;

   // Attributes ----------------------------------------------------
   private int m_waiters;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------
   public WaitSemaphore()
   {
      super(MAX_USERS_ALLOWED);
   }

   // Public --------------------------------------------------------
   public void doWait() throws InterruptedException
   {
      synchronized (this)
      {
         release();
         ++m_waiters;
         waitImpl(this);
         --m_waiters;
         acquire();
      }
   }

   public void doNotify() throws InterruptedException
   {
      synchronized (this)
      {
         if (getWaiters() > 0)
         {
            acquire();
            notify();
            release();
         }
      }
   }

   public int getWaiters()
   {
      synchronized (this)
      {
         return m_waiters;
      }
   }

   // Object overrides ---------------------------------------------------
   public String toString()
   {
      return super.toString() + " - " + m_waiters;
   }

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------
}
