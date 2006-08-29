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
 * A simple interface for objects that can return pretty (ie.
 * prefixed) string representations of themselves.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public interface PrettyString
{
   /**
    * Returns a pretty representation of the object.
    *
    * @param prefix  The string which all lines of the output must be prefixed with.
    * @return        A pretty representation of the object.
    */
   String toPrettyString(String prefix);

   /**
    * Interface for appending the objects pretty string onto a buffer.
    */
   interface Appendable
   {
      /**
       * Appends a pretty representation of the object to the given buffer.
       *
       * @param buff    The buffer to use while making pretty.
       * @param prefix  The string which all lines of the output must be prefixed with.
       * @return        The buffer.
       */
      StringBuffer appendPrettyString(StringBuffer buff, String prefix);
   }
}
