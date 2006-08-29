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

import java.net.URI;
import java.net.URISyntaxException;

import org.jboss.util.NestedRuntimeException;

/**
 * A property editor for {@link java.net.URI}.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:dimitris@jboss.org">Dimitris Andreadis</a>
 */
public class URIEditor extends TextPropertyEditorSupport
{
   /**
    * Returns a URI for the input object converted to a string.
    *
    * @return a URI object
    *
    * @throws NestedRuntimeException   An MalformedURLException occured.
    */
   public Object getValue()
   {
      try
      {
         // TODO - more strict checking, like URLEditor
         return new URI(getAsText());
      }
      catch (URISyntaxException e)
      {
         throw new NestedRuntimeException(e);
      }
   }
}
