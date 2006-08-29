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
package org.jboss.util.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A <em>prefix</em> based filename filter.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class FilenamePrefixFilter
   implements FilenameFilter
{
   /** The prefix which files must have to be accepted. */
   protected final String prefix;

   /** Flag to signal that we want to ignore the case. */
   protected final boolean ignoreCase;

   /**
    * Construct a <tt>FilenamePrefixFilter</tt>.
    *
    * @param prefix     The prefix which files must have to be accepted.
    * @param ignoreCase <tt>True</tt> if the filter should be case-insensitive.
    */
   public FilenamePrefixFilter(final String prefix,
                               final boolean ignoreCase)
   {
      this.ignoreCase = ignoreCase;
      this.prefix = (ignoreCase ? prefix.toLowerCase() : prefix);
   }

   /**
    * Construct a case sensitive <tt>FilenamePrefixFilter</tt>.
    *
    * @param prefix  The prefix which files must have to be accepted.
    */
   public FilenamePrefixFilter(final String prefix) {
      this(prefix, false);
   }

   /**
    * Check if a file is acceptible.
    *
    * @param dir  The directory the file resides in.
    * @param name The name of the file.
    * @return     <tt>true</tt> if the file is acceptable.
    */
   public boolean accept(final File dir, final String name) {
      if (ignoreCase) {
         return name.toLowerCase().startsWith(prefix);
      }
      else {
         return name.startsWith(prefix);
      }
   }
}
