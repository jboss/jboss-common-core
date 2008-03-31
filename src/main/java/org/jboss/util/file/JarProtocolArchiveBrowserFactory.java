/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * comment
 *
 * @author <a href="bill@jboss.com">Bill Burke</a>
 * @version $Revision: 1.1 $
 */
@SuppressWarnings("unchecked")
public class JarProtocolArchiveBrowserFactory implements ArchiveBrowserFactory
{

   @SuppressWarnings("deprecation")
   public Iterator create(URL url, ArchiveBrowser.Filter filter)
   {
      if (url.toString().endsWith("!/"))
      {
         try
         {
            return new JarArchiveBrowser((JarURLConnection) url.openConnection(), filter);
         }
         catch (IOException e)
         {
            throw new RuntimeException("Unable to browse url: " + url, e);
         }
      }
      else
      {
         try
         {
            return new JarStreamBrowser(url.openStream(), filter);
         }
         catch (IOException e)
         {
            throw new RuntimeException("Unable to browse url: " + url, e);
         }
      }
   }
}
