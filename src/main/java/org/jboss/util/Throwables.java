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

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

/**
 * A collection of Throwable utilities.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public final class Throwables
{
   /**
    * Return a string that consists of the stack trace of the given 
    * <code>Throwable</code>.
    *
    * @param t    <code>Throwable</code> to get stack trace from.
    * @return     <code>Throwable</code> stack trace as a string.
    */
   public static String toString(final Throwable t) {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      PrintStream stream = new PrintStream(output);
      t.printStackTrace(stream);

      return output.toString();
   }
}
