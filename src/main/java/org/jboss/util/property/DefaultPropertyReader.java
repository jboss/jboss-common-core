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

/**
 * Reads properties from files specified via a system property.
 *
 * <p>Unless otherwise specified, propertie filenames will be read from
 *    the <tt>org.jboss.properties</tt> singleton or array property.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public final class DefaultPropertyReader
   extends FilePropertyReader
{
   //
   // Might want to have a org.jboss.properties.property.name or something
   // property to determine what property name to read from.
   //
   // For now just use 'properties'
   //

   /** Default property name to read filenames from */
   public static final String DEFAULT_PROPERTY_NAME = "properties";

   /**
    * Construct a <tt>DefaultPropertyReader</tt> with a specified property 
    * name.
    *
    * @param propertyName    Property name.
    */
   public DefaultPropertyReader(final String propertyName) {
      super(getFilenames(propertyName));
   }

   /**
    * Construct a <tt>DefaultPropertyReader</tt>.
    */
   public DefaultPropertyReader() {
      this(DEFAULT_PROPERTY_NAME);
   }
   
   /**
    * Get an array of filenames to load.
    *
    * @param propertyName  Property to read filenames from.
    * @return              Array of filenames.
    */
   public static String[] getFilenames(final String propertyName)
      throws PropertyException
   {
      String filenames[];

      // check for singleton property first
      Object filename = PropertyManager.getProperty(propertyName);
      if (filename != null) {
         filenames = new String[] { String.valueOf(filename) };
      }
      else {
         // if no singleton property exists then look for array props
         filenames = PropertyManager.getArrayProperty(propertyName);
      }

      return filenames;
   }
}
