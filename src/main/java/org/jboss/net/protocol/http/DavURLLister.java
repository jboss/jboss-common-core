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
package org.jboss.net.protocol.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpURL;
import org.apache.webdav.lib.WebdavResource;
import org.jboss.net.protocol.URLListerBase;

public class DavURLLister extends URLListerBase
{
   public Collection listMembers (URL baseUrl, URLFilter filter) throws IOException
   {
      return listMembers (baseUrl, filter, false);
   }

   public Collection listMembers (URL baseUrl, URLFilter filter, boolean scanNonDottedSubDirs) throws IOException
   {
      WebdavResource resource = null;
      try
      {
         resource = new WebdavResource (baseUrl.toString ());
         WebdavResource[] resources = resource.listWebdavResources ();
         List urls = new ArrayList (resources.length);
         for (int i = 0; i < resources.length; i++)
         {
            WebdavResource member = resources[i];
            HttpURL httpURL = member.getHttpURL ();
            if (filter.accept (baseUrl, httpURL.getName ()))
            {
               String uri = httpURL.getURI();
               if (member.isCollection ())
               {
                  if (! uri.endsWith ("/"))
                     uri += "/";

                  // it is a directory: do we have to recursively list its content?
                  String path = httpURL.getPath();
                  if (scanNonDottedSubDirs && getFilePartFromUrl(path).indexOf (".") == -1)
                  {
                     URL subUrl = new URL (uri) ;
                     urls.addAll (listMembers (subUrl, filter, scanNonDottedSubDirs));
                  }
                  else
                  {
                     urls.add (new URL (uri));
                  }
               }
               else
               {
                  urls.add (new URL (uri));
               }
               
            }
         }
         return urls;
      } catch (HttpException e)
      {
         throw new IOException (e.getMessage ());
      } catch (MalformedURLException e)
      {
         // should not happen
         throw new IllegalStateException (e.getMessage ());
      } finally
      {
         if (resource != null)
         {
            resource.close ();
         }
      }
   }
   
   protected static final String getFilePartFromUrl (String name)
   {
      int length = name.length ();
      
      if (name.charAt (length - 1) == '/')
      {
         int start = name.lastIndexOf ("/", length - 2);
         return name.substring (start, length -2);
      }
      else
      {
         int start = name.lastIndexOf ("/");
         return name.substring (start);         
      }
   }
}
