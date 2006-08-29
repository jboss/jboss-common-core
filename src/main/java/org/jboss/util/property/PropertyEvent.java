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
package org.jboss.util.property;

import java.util.EventObject;

import org.jboss.util.NullArgumentException;

/**
 * A property event.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PropertyEvent
   extends EventObject
{
   /** Property name. */
   protected final String name;

   /** Property value. */
   protected final String value;

   /**
    * Construct a new <tt>PropertyEvent</tt>.
    *
    * @param source  The source of the event.
    * @param name    The property name effected.
    * @param value   The value of the property effected.
    *
    * @throws NullArgumentException    Name or source is <tt>null</tt>.
    */
   public PropertyEvent(final Object source,
                        final String name,
                        final String value)
   {
      super(source);

      if (name == null)
         throw new NullArgumentException("name");
      // value can be null

      this.name = name;
      this.value = value;
   }

   /**
    * Construct a new <tt>PropertyEvent</tt>.
    *
    * @param source  The source of the event.
    * @param name    The property name effected.
    *
    * @throws NullArgumentException    Name or source is <tt>null</tt>.
    */
   public PropertyEvent(Object source, String name) {
      this(source, name, null);
   }

   /**
    * Get the name of the property that is effected.
    *
    * @return     Property name.
    */
   public final String getPropertyName() {
      return name;
   }

   /**
    * Get the value of the property that is effected.
    *
    * @return  The value of the property that is effected or <tt>null</tt>.
    */
   public final String getPropertyValue() {
      return value;
   }

   /**
    * Return a string representation of this event.
    *
    * @return  A string representation of this event.
    */
   public String toString() {
      return super.toString() + 
         "{ name=" + name +
         ", value=" + value +
         " }";
   }
}
