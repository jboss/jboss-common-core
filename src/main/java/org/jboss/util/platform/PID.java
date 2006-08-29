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
package org.jboss.util.platform;

import java.io.Serializable;

import java.util.Random;

/**
 * Provides access to the process identifier for this virtual machine.
 *
 * <p>Currently does not support native access and generates random numbers
 *    for the process id.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PID
   implements Serializable, Cloneable
{
   /** The <tt>int</tt> process identifier. */
   protected final int id;

   /**
    * Construct a new PID.
    *
    * @param id   Process identifier.
    */
   protected PID(final int id) {
      this.id = id;
   }

   /**
    * Get the <tt>int</tt> process identifier.
    *
    * @return  <tt>int</tt> process identifier.
    */
   public final int getID() {
      return id;
   }

   /**
    * Return a string representation of this PID.
    *
    * @return  A string representation of this PID.
    */
   public String toString() {
      return String.valueOf(id);
   }

   /**
    * Return a string representation of this PID.
    *
    * @return  A string representation of this PID.
    */
   public String toString(int radix) {
      return Integer.toString(id, radix);
   }

   /**
    * Return the hash code of this PID.
    *
    * @return  The hash code of this PID.
    */
   public int hashCode() {
      return id;
   }

   /**
    * Check if the given object is equal to this PID.
    *
    * @param obj     Object to test equality with.
    * @return        True if object is equals to this PID.
    */
   public boolean equals(final Object obj) {
      if (obj == this) return true;

      if (obj != null && obj.getClass() == getClass()) {
         PID pid = (PID)obj;
         return pid.id == id;
      }

      return false;
   }

   /**
    * Returns a copy of this PID.
    *
    * @return  A copy of this PID.
    */
   public Object clone() {
      try {
         return super.clone();
      }
      catch (CloneNotSupportedException e) {
         throw new InternalError();
      }
   }


   /////////////////////////////////////////////////////////////////////////
   //                            Instance Access                          //
   /////////////////////////////////////////////////////////////////////////

   /** The single instance of PID for the running Virtual Machine */
   private static PID instance = null;

   /**
    * Get the PID for the current virtual machine.
    *
    * @return  Process identifier.
    */
   public synchronized static PID getInstance() {
      if (instance == null) {
         instance = create();
      }
      return instance;
   }

   /**
    * Create the PID for the current virtual mahcine.
    *
    * @return  Process identifier.
    */
   private static PID create() {
      // for now just return a random integer.
      int random = Math.abs(new Random().nextInt());
      return new PID(random);
   }
}
