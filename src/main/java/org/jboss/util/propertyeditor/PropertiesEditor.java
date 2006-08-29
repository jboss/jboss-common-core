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
package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Properties;

import org.jboss.util.NestedRuntimeException;
import org.jboss.util.StringPropertyReplacer;

/**
 * A property editor for {@link java.util.Properties}.
 *
 * @author Jason Dillon
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class PropertiesEditor extends PropertyEditorSupport
{

   /**
    * To be consistent with the Properties.load and .store methods.
    */
   private static final String ENC = "ISO-8859-1";

   /**
    * Initializes a Properties object with text value
    * interpretted as a .properties file contents. This replaces any
    * references of the form ${x} with the corresponding system property. 
    *
    * @return a Properties object
    *
    * @throws NestedRuntimeException  An IOException occured.
    */
   public void setAsText(String propsText)
   {
      try
      {
         // Load the current key=value properties into a Properties object
         Properties rawProps = new Properties(System.getProperties());
         ByteArrayInputStream bais = new ByteArrayInputStream(propsText.getBytes(ENC));
         rawProps.load(bais);
         // Now go through the rawProps and replace any ${x} refs
         Properties props = new Properties();
         Iterator keys = rawProps.keySet().iterator();
         while( keys.hasNext() )
         {
            String key = (String) keys.next();
            String value = rawProps.getProperty(key);
            String value2 = StringPropertyReplacer.replaceProperties(value, rawProps);
            props.setProperty(key, value2);
         }
         rawProps.clear();

         setValue(props);
      }
      catch (IOException e)
      {
         throw new NestedRuntimeException(e);
      }
   }

   /**
    * Returns the properties as text.
    */
   public String getAsText()
   {
      try
      {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         Properties p = (Properties)getValue();
         p.store(baos, null);
         return baos.toString(ENC);
      }
      catch (IOException e)
      {
         throw new NestedRuntimeException(e);
      }
   }

}
