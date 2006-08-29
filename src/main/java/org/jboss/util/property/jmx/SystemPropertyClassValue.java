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
package org.jboss.util.property.jmx;

import org.jboss.logging.Logger;

/**
 * A helper for setting system properties based on class availablity.<p>
 *
 * It has a static method and an MBean wrapper for dynamic configuration.<p>
 *
 * The class is first checked for availablity before setting the system
 * property.

 * @jmx.mbean
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:Adrian.Brock@HappeningTimes.com">Adrian Brock</a>
 */
public class SystemPropertyClassValue
   implements SystemPropertyClassValueMBean
{
   public static final Logger log = Logger.getLogger(SystemPropertyClassValue.class);

   /** Property name. */
   protected String property;

   /** Class Name. */
   protected String className;

   /**
    * Constructor.
    */
   public SystemPropertyClassValue()
   {
   }

   /**
    * The system property value
    *
    * @jmx.managed-attribute
    */
   public String getProperty()
   {
      return property;
   }

   /**
    * The system property value
    *
    * @jmx.managed-attribute
    */
   public void setProperty(String property)
   {
      this.property = property;
   }

   /**
    * The class name to use a value for the system property
    * when it is available
    *
    * @jmx.managed-attribute
    */
   public String getClassName()
   {
      return className;
   }

   /**
    * The class name to use a value for the system property
    * when it is available
    *
    * @jmx.managed-attribute
    */
   public void setClassName(String className)
   {
      this.className = className;
   }

   /**
    * JBoss lifecycle
    *
    * @jmx.managed-operation
    */
   public void create()
   {
      Throwable error = setSystemPropertyClassValue(property, className);
      if (error != null)
         log.trace("Error loading class " + className + " property " + property + " not set.", error);
   }

   /**
    * Sets the system property to a class when the class is available.
    *
    * @param property the property to set
    * @param className the class to set as the properties value
    * @return any error loading the class
    * @exception IllegalArgumentException for a null or empty parameter
    */
   public static Throwable setSystemPropertyClassValue(String property, String className)
   {
      // Validation
      if (property == null || property.trim().length() == 0)
         throw new IllegalArgumentException("Null or empty property");
      if (className == null || className.trim().length() == 0)
         throw new IllegalArgumentException("Null or empty class name");

      // Is the class available?
      try
      {
         Thread.currentThread().getContextClassLoader().loadClass(className);
      }
      catch (Throwable problem)
      {
         return problem;
      }

      // The class is there, set the property.
      System.setProperty(property, className);
      return null;
   }
}
