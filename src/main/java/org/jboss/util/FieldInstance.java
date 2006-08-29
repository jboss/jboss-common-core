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

import java.lang.reflect.Field;

/**
 * A <tt>FieldInstance</tt> refers to a specific reflected field on an object.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class FieldInstance
{
   /** Field */
   protected final Field field;

   /** Instance */
   protected final Object instance;

   /**
    * Construct a new field instance.
    *
    * @param instance      The instance object the given field belongs to.
    * @param fieldName     The name of the field to find in the instance.
    *
    * @throws NullArgumentException    Instance or fieldName is <tt>null</tt>.
    * @throws NoSuchFieldException
    */
   public FieldInstance(final Object instance, final String fieldName)
      throws NoSuchFieldException
   {
      if (instance == null)
         throw new NullArgumentException("instance");
      if (fieldName == null)
         throw new NullArgumentException("fieldName");

      // Get the field object
      field = instance.getClass().getField(fieldName);

      // Check if the field is assignable ?
      if (! field.getDeclaringClass().isAssignableFrom(instance.getClass()))
         throw new IllegalArgumentException
            ("field does not belong to instance class");

      this.instance = instance;
   }

   /**
    * Get the field.
    *
    * @return  Field.
    */
   public final Field getField() {
      return field;
   }

   /**
    * Get the instance.
    *
    * @return  Instance.
    */
   public final Object getInstance() {
      return instance;
   }

   /**
    * Get the value of the field instance.
    *
    * @return  Field value.
    *
    * @throws IllegalAccessException      Failed to get field value.
    */ 
   public final Object get() throws IllegalAccessException {
      return field.get(instance);
   }

   /**
    * Set the value of the field instance
    *
    * @param value   Field value.
    *
    * @throws IllegalAccessException      Failed to set field value.
    */
   public final void set(final Object value) throws IllegalAccessException {
      field.set(instance, value);
   }
}
