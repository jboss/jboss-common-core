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
import java.io.FileFilter;

/**
 * A <em>suffix</em> based file filter.
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class FileSuffixFilter
   implements FileFilter
{
   /** A list of suffixes which files must have to be accepted. */
   protected final String suffixes[];

   /** Flag to signal that we want to ignore the case. */
   protected final boolean ignoreCase;

   /**
    * Construct a <tt>FileSuffixFilter</tt>.
    *
    * @param suffixes   A list of suffixes which files mut have to be accepted.
    * @param ignoreCase <tt>True</tt> if the filter should be case-insensitive.
    */
   public FileSuffixFilter(final String suffixes[],
                           final boolean ignoreCase)
   {
      this.ignoreCase = ignoreCase;
      if (ignoreCase) {
         this.suffixes = new String[suffixes.length];
         for (int i=0; i<suffixes.length; i++) {
            this.suffixes[i] = suffixes[i].toLowerCase();
         }
      }
      else {
         this.suffixes = suffixes;
      }
   }

   /**
    * Construct a <tt>FileSuffixFilter</tt>.
    *
    * @param suffixes   A list of suffixes which files mut have to be accepted.
    */
   public FileSuffixFilter(final String suffixes[])
   {
      this(suffixes, false);
   }

   /**
    * Construct a <tt>FileSuffixFilter</tt>.
    *
    * @param suffix     The suffix which files must have to be accepted.
    * @param ignoreCase <tt>True</tt> if the filter should be case-insensitive.
    */
   public FileSuffixFilter(final String suffix,
                           final boolean ignoreCase)
   {
      this(new String[] { suffix }, ignoreCase);
   }

   /**
    * Construct a case sensitive <tt>FileSuffixFilter</tt>.
    *
    * @param suffix  The suffix which files must have to be accepted.
    */
   public FileSuffixFilter(final String suffix) {
      this(suffix, false);
   }

   /**
    * Check if a file is acceptible.
    *
    * @param file    The file to check.
    * @return        <tt>true</tt> if the file is acceptable.
    */
   public boolean accept(final File file) {
      boolean success = false;

      for (int i=0; i<suffixes.length && !success; i++) {
         if (ignoreCase)
            success = file.getName().toLowerCase().endsWith(suffixes[i]);
         else
            success = file.getName().endsWith(suffixes[i]);
      }

      return success;
   }
}
