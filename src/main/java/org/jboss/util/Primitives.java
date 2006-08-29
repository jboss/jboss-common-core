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
 * Primitive utilities.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public final class Primitives
{
   /**
    * Get a Boolean from a boolean, equivalent to the java 1.4 method Boolean.valueOf(boolean)
    *
    * @param value the boolean
    * @return the Boolean equivalent
    */
   public static Boolean valueOf(boolean value)
   {
      if (value)
         return Boolean.TRUE;
      else
         return Boolean.FALSE;
   }

   /**
    * Test the equality of two doubles by converting their values into
    * IEEE 754 floating-point "double format" long values.
    *
    * @param a    Double to check equality with.
    * @param b    Double to check equality with.
    * @return     True if a equals b.
    */
   public static boolean equals(final double a, final double b) {
      return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
   }

   /**
    * Test the equality of two doubles by converting their values into
    * IEEE 754 floating-point "single precision" bit layouts.
    *
    * @param a    Float to check equality with.
    * @param b    Float to check equality with.
    * @return     True if a equals b.
    */
   public static boolean equals(final float a, final float b) {
      return Float.floatToIntBits(a) == Float.floatToIntBits(b);
   }

   /**
    * Test the equality of a given sub-section of two byte arrays.
    *
    * @param a       The first byte array.
    * @param abegin  The begining index of the first byte array.
    * @param b       The second byte array.
    * @param bbegin  The begining index of the second byte array.
    * @param length  The length of the sub-section.
    * @return        True if sub-sections are equal.
    */
   public static boolean equals(final byte a[], final int abegin,
                                final byte b[], final int bbegin,
                                final int length)
   {
      try {
         int i=length;
         while (--i >= 0) {
            if (a[abegin + i] != b[bbegin + i]) {
               return false;
            }
         }
      }
      catch (ArrayIndexOutOfBoundsException e) {
         return false;
      }

      return true;
   }

   /**
    * Test the equality of two byte arrays.
    *
    * @param a    The first byte array.
    * @param b    The second byte array.
    * @return     True if the byte arrays are equal.
    */
   public static boolean equals(final byte a[], final byte b[]) {
      if (a == b) return true;
      if (a == null || b == null) return false;
      if (a.length != b.length) return false;

      try {
         for (int i=0; i<a.length; i++) {
            if (a[i] != b[i]) {
               return false;
            }
         }
      }
      catch (ArrayIndexOutOfBoundsException e) {
         return false;
      }

      return true;
   }

}
