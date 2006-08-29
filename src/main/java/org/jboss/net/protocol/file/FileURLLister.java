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
package org.jboss.net.protocol.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.jboss.logging.Logger;
import org.jboss.net.protocol.URLListerBase;

/**
 * FileURLLister
 *
 * @author jboynes@users.sf.net
 * @author <a href="mailto:dimitris@jboss.org">Dimitris Andreadis</a>
 * @version $Revision$
 */
public class FileURLLister extends URLListerBase
{
   /** The Logger */
   private static final Logger log = Logger.getLogger(FileURLLister.class);
   
   // Public --------------------------------------------------------
   
   public Collection listMembers(URL baseUrl, URLFilter filter) throws IOException   
   {
      return listMembers(baseUrl, filter, false);
   }

   public Collection listMembers(URL baseUrl, URLFilter filter, boolean scanNonDottedSubDirs) throws IOException
   {
      // Make sure this is a directory URL
      String baseUrlString = baseUrl.toString();
      if (!baseUrlString.endsWith("/"))
      {
         throw new IOException("Does not end with '/', not a directory url: " + baseUrlString);
      }
      
      // Verify the directory actually exists
      File dir = new File(baseUrl.getPath());
      if (!dir.isDirectory())
      {
         throw new FileNotFoundException("Not pointing to a directory, url: " + baseUrlString);
      }
      
      // The list of URLs to return
      ArrayList resultList = new ArrayList();

      // Do the actual job
      listFiles(baseUrl, filter, scanNonDottedSubDirs, resultList);
      
      // Done
      return resultList;
   }
   
   // Private -------------------------------------------------------
   
   /**
    * Starting from baseUrl, that should point to a directory, populate the
    * resultList with the contents that pass the filter (in the form of URLs)
    * and possibly recurse into subdris not containing a '.' in their name.
    */
   private void listFiles(final URL baseUrl, final URLFilter filter, boolean scanNonDottedSubDirs, ArrayList resultList)
      throws IOException
   {      
      // List the files at the current dir level, using the provided filter
      final File baseDir = new File(baseUrl.getPath());
      String[] filenames = baseDir.list(new FilenameFilter()
      {
         public boolean accept(File dir, String name)
         {
            try
            {
               return filter.accept(baseUrl, name);
            }
            catch (Exception e)
            {
               log.debug("Unexpected exception filtering entry '" + name + "' in directory '" + baseDir + "'", e);
               return true;
            }
         }
      });
      
      if (filenames == null)
      {
         // This happens only when baseDir not a directory (but this is already
         // checked by the caller) or some unknown IOException happens internally
         // (e.g. run out of file descriptors?). Unfortunately the File API
         // doesn't provide a way to know.
         throw new IOException("Could not list directory '" + baseDir + "', reason unknown");
      }      
      else
      {
         String baseUrlString = baseUrl.toString();
         
         for (int i = 0; i < filenames.length; i++)
         {
            String filename = filenames[i];
            
            // Find out if this is a directory
            File file = new File(baseDir, filename);
            boolean isDir = file.isDirectory();
            
            // The subUrl
            URL subUrl = createURL(baseUrlString, filename, isDir);
            
            // If scanning subdirs and we have a directory, not containing a '.' in
            // the name, recurse into it. This is to allow recursing into grouping
            // dirs like ./deploy/jms, ./deploy/management, etc., avoiding
            // at the same time exploded packages, like .sar, .war, etc.
            if (scanNonDottedSubDirs && isDir && (filename.indexOf('.') == -1))
            {
               // recurse into it
               listFiles(subUrl, filter, scanNonDottedSubDirs, resultList);
            }
            else
            {
               // just add to the list
               resultList.add(subUrl);                
            }
         }
      }
   }  
   
   /**
    * Create a URL by concatenating the baseUrlString that should end at '/',
    * the filename, and a trailing slash, if it points to a directory
    */
   private URL createURL(String baseUrlString, String filename, boolean isDirectory)
   {
      try
      {
         return new URL(baseUrlString + filename + (isDirectory ? "/" : ""));
      } 
      catch (MalformedURLException e)
      {
         // shouldn't happen
         throw new IllegalStateException();
      }
   }
   
}
