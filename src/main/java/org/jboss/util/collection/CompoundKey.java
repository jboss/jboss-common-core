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
package org.jboss.util.collection;

import java.io.Serializable;

import org.jboss.util.NullArgumentException;
import org.jboss.util.Objects;
import org.jboss.util.HashCode;
import org.jboss.util.Strings;

/**
 * An immutable compound key class.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class CompoundKey
   implements Serializable, Cloneable
{
   /** The elements of the key */
   private final Object elements[];

   /**
    * Construct a CompoundKey.
    *
    * @param elements   Elements of the key.
    */
   public CompoundKey(final Object elements[]) {
      if (elements == null)
         throw new NullArgumentException("elements");

      this.elements = elements;
   }

   /**
    * Construct a CompoundKey.
    *
    * @param a    Element.
    * @param b    Element.
    */
   public CompoundKey(final Object a, final Object b) {
      this(new Object[] { a, b });
   }

   /**
    * Construct a CompoundKey.
    *
    * @param a    Element.
    * @param b    Element.
    * @param c    Element.
    */
   public CompoundKey(final Object a, final Object b, final Object c) {
      this(new Object[] { a, b, c });
   }

   /**
    * Test the equality of an object with this.
    *
    * @param obj  Object to test equality with.
    * @return     True if object is equal.
    */
   public boolean equals(final Object obj) {
      if (obj == this) return true;

      if (obj != null && obj.getClass() == getClass()) {
         CompoundKey key = (CompoundKey)obj;

         return Objects.equals(key.elements, elements);
      }

      return false;
   }

   /**
    * Get the hash code of this object.
    *
    * @return  Hash code.
    */
   public int hashCode() {
      return HashCode.generate(elements);
   }

   /**
    * Return a string representation of this object.
    *
    * @return  A string representation of this object.
    */
   public String toString() {
      return super.toString() + Strings.join(elements, "[", ",", "]");
   }

   /**
    * Return a shallow cloned copy of this object.
    *
    * @return   Shallow cloned copy of this object.
    */
   public Object clone() {
      try {
         return super.clone();
      }
      catch (CloneNotSupportedException e) {
         throw new InternalError();
      }
   }
}
