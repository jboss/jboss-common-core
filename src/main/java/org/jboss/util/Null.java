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

import java.io.Serializable;

/**
 * A class that represents <tt>null</tt>.
 *
 * <p>{@link Null#VALUE} is used to given an object variable a dual-mode
 *    nullified value, where <tt>null</tt> would indicate that the value is 
 *    empty, and {@link Null#VALUE} would idicate that the value has been 
 *    set to <tt>null</tt> (or something to that effect).
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public final class Null
   implements Serializable
{
   /** The primary instance of Null. */
   public static final Null VALUE = new Null();

   /** Do not allow public construction. */
   private Null() {}

   /**
    * Return a string representation.
    *
    * @return  Null
    */
   public String toString() {
      return null;
   }

   /**
    * Returns zero.
    *
    * @return  Zero.
    */
   public int hashCode() {
      return 0;
   }

   /**
    * Check if the given object is a Null instance or <tt>null</tt>.
    *
    * @param obj  Object to test.
    * @return     True if the given object is a Null instance or <tt>null</tt>.
    */
   public boolean equals(final Object obj) {
      if (obj == this) return true;
      return (obj == null || obj.getClass() == getClass());
   }
}

