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

import java.util.Properties;

/**
 * Provides helper methods for working with instance or class properties.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class PropertyContainer
   extends PropertyMap
{
   /** The group name for this container. */
   protected String groupname = "<unknown>";

   /**
    * Initialize the container with a property group.
    *
    * @param group  Properties.
    */
   public PropertyContainer(final Properties props) {
      super(props);
   }

   /**
    * Initialize the container with a property group of the given name.
    *
    * @param groupname  Property group name.
    */
   public PropertyContainer(final String groupname) {
      this(Property.getGroup(groupname));
      this.groupname = groupname;
   }

   /**
    * Initialize the container with a property group of the given class name.
    *
    * @param type    The class whos name will be the property group name.
    */
   public PropertyContainer(final Class type) {
      this(type.getName());
   }
   
   /**
    * Creates a {@link FieldBoundPropertyListener} for the field and
    * property name and adds it the underlying property group.
    *
    * @param name          The field name to bind values to.
    * @param propertyName  The property name to bind to.
    *
    * @throws IllegalArgumentException    Field of property name is null or 
    *                                     empty.
    */
   protected void bindField(final String name, final String propertyName) {
      if (name == null || name.equals(""))
         throw new IllegalArgumentException("name");
      if (propertyName == null || propertyName.equals(""))
         throw new IllegalArgumentException("propertyName");

      addPropertyListener
         (new FieldBoundPropertyListener(this, name, propertyName));
   }

   /**
    * Creates a {@link FieldBoundPropertyListener} for the field and
    * property name and adds it the underlying property group.
    *
    * @param name    The field name and property to bind values to.
    *
    * @throws IllegalArgumentException    Field of property name is null or 
    *                                     empty.
    */
   protected void bindField(final String name) {
      bindField(name, name);
   }

   /**
    * Creates a {@link MethodBoundPropertyListener} for the method and
    * property name and adds it the underlying property group.
    *
    * @param name          The method name to bind values to.
    * @param propertyName  The property name to bind to.
    *
    * @throws IllegalArgumentException    Method of property name is null or 
    *                                     empty.
    */
   protected void bindMethod(final String name, final String propertyName) {
      if (name == null || name.equals(""))
         throw new IllegalArgumentException("name");
      if (propertyName == null || propertyName.equals(""))
         throw new IllegalArgumentException("propertyName");

      addPropertyListener //                opposite of field bound =(
         (new MethodBoundPropertyListener(this, propertyName, name));
   }

   /**
    * Creates a {@link MethodBoundPropertyListener} for the method and
    * property name and adds it the underlying property group.
    *
    * @param name    The method name and property to bind values to.
    *
    * @throws IllegalArgumentException    Method of property name is null or 
    *                                     empty.
    */
   protected void bindMethod(final String name) {
      bindMethod(name, name);
   }

   private String makeName(final String name) {
      return groupname + "." + name;
   }

   protected void throwException(final String name) 
      throws PropertyException
   {
      throw new PropertyException(makeName(name));
   }

   protected void throwException(final String name, final String msg) 
      throws PropertyException
   {
      throw new PropertyException(makeName(name) + ": " + msg);
   }

   protected void throwException(final String name, final String msg, final Throwable nested)
      throws PropertyException
   {
      throw new PropertyException(makeName(name) + ": " + msg, nested);
   }

   protected void throwException(final String name, final Throwable nested)
      throws PropertyException
   {
      throw new PropertyException(makeName(name), nested);
   }
}
