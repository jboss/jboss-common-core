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

import java.lang.reflect.Constructor;
import java.lang.reflect.Array;

import java.lang.ref.Reference;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Serializable;

import org.jboss.util.stream.Streams;

/**
 * A collection of <code>Object</code> utilities.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public final class Objects
{
   /////////////////////////////////////////////////////////////////////////
   //                           Coercion Methods                          //
   /////////////////////////////////////////////////////////////////////////

   /**
    * Get a compatible constructor for the given value type
    *
    * @param type       Class to look for constructor in
    * @param valueType  Argument type for constructor
    * @return           Constructor or null
    */
   public static Constructor getCompatibleConstructor(final Class type,
                                                      final Class valueType)
   {
      // first try and find a constructor with the exact argument type
      try {
         return type.getConstructor(new Class[] { valueType });
      }
      catch (Exception ignore) {
         // if the above failed, then try and find a constructor with
         // an compatible argument type

         // get an array of compatible types
         Class[] types = type.getClasses();

         for (int i=0; i<types.length; i++) {
            try {
               return type.getConstructor(new Class[] { types[i] });
            }
            catch (Exception ignore2) {}
         }
      }

      // if we get this far, then we can't find a compatible constructor
      return null;
   }

   /////////////////////////////////////////////////////////////////////////
   //                            Cloning Methods                          //
   /////////////////////////////////////////////////////////////////////////

   /**
    * Copy an serializable object deeply.
    *
    * @param obj  Object to copy.
    * @return     Copied object.
    *
    * @throws IOException
    * @throws ClassCastException
    */
   public static Object copy(final Serializable obj)
      throws IOException, ClassNotFoundException
   {
      ObjectOutputStream out = null;
      ObjectInputStream in = null;
      Object copy = null;
      
      try {
         // write the object
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         out = new ObjectOutputStream(baos);
         out.writeObject(obj);
         out.flush();

         // read in the copy
         byte data[] = baos.toByteArray();
         ByteArrayInputStream bais = new ByteArrayInputStream(data);
         in = new ObjectInputStream(bais);
         copy = in.readObject();
      }
      finally {
         Streams.close(out);
         Streams.close(in);
      }

      return copy;
   }
   

   /////////////////////////////////////////////////////////////////////////
   //                              Misc Methods                           //
   /////////////////////////////////////////////////////////////////////////

   /**
    * Dereference the given object if it is <i>non-null</i> and is an
    * instance of <code>Reference</code>.  If the object is <i>null</i>
    * then <i>null</i> is returned.  If the object is not an instance of
    * <code>Reference</code>, then the object is returned.
    *
    * @param obj  Object to dereference.
    * @return     Dereferenced object.
    */
   public static Object deref(final Object obj) {
      if (obj != null && obj instanceof Reference) {
         Reference ref = (Reference)obj;
         return ref.get();
      }

      return obj;
   }
      
   /**
    * Check if the given object is an array (primitve or native).
    *
    * @param obj  Object to test.
    * @return     True of the object is an array.
    */
   public static boolean isArray(final Object obj) {
      if (obj != null)
         return obj.getClass().isArray();
      return false;
   }

   /**
    * Return an Object array for the given object.
    *
    * @param obj  Object to convert to an array.  Converts primitive
    *             arrays to Object arrays consisting of their wrapper
    *             classes.  If the object is not an array (object or primitve)
    *             then a new array of the given type is created and the
    *             object is set as the sole element.
    */
   public static Object[] toArray(final Object obj) {
      // if the object is an array, the cast and return it.
      if (obj instanceof Object[]) {
         return (Object[])obj;
      }

      // if the object is an array of primitives then wrap the array
      Class type = obj.getClass();
      Object array; 
      if (type.isArray()) {
         int length = Array.getLength(obj);
         Class componentType = type.getComponentType();
         array = Array.newInstance(componentType, length);
         for (int i=0; i<length; i++) {
            Array.set(array, i, Array.get(obj, i));
         }
      }
      else {
         array = Array.newInstance(type, 1);
         Array.set(array, 0, obj);
      }

      return (Object[])array;
   }

   /**
    * Test the equality of two object arrays.
    *
    * @param a       The first array.
    * @param b       The second array.
    * @param deep    True to traverse elements which are arrays.
    * @return        True if arrays are equal.
    */
   public static boolean equals(final Object[] a, final Object[] b,
                                final boolean deep)
   {
      if (a == b) return true;
      if (a == null || b == null) return false;
      if (a.length != b.length) return false;

      for (int i=0; i<a.length; i++) {
         Object x = a[i];
         Object y = b[i];

         if (x != y) return false;
         if (x == null || y == null) return false;
         if (deep) {
            if (x instanceof Object[] && y instanceof Object[]) {
               if (! equals((Object[])x, (Object[])y, true)) return false;
            }
            else {
               return false;
            }
         }
         if (! x.equals(y)) return false;
      }

      return true;
   }

   /**
    * Test the equality of two object arrays.
    *
    * @param a    The first array.
    * @param b    The second array.
    * @return     True if arrays are equal.
    */
   public static boolean equals(final Object[] a, final Object[] b) {
      return equals(a, b, true);
   }
}
