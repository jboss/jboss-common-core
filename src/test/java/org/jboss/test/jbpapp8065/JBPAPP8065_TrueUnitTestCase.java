/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.test.jbpapp8065;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.jboss.net.protocol.file.FileURLConnection;

import junit.framework.TestCase;

/**
 * Unit tests for JBPAPP-8065
 *
 * @see https://issues.jboss.org/browse/JBPAPP-8065
 * 
 * @author bmaxwell
 */
public class JBPAPP8065_TrueUnitTestCase extends TestCase
{
   public JBPAPP8065_TrueUnitTestCase(String name)
   {
      super(name);
   }
 
   public void testJBPAPP806_UseURI_True()
   {
      System.setProperty("org.jboss.net.protocol.file.useURI", "true");
      URL url = null;
      try
      {
         url = new URL("file:non-exisitant-file-" + new Date().getTime());
         try
         {            
            URLConnection urlConnection = new FileURLConnection(url);
            urlConnection.connect();
         }
         catch (IOException e)
         {
            // this will catch a FileNotFoundException
            // This is NOT the expected result when -Dorg.jboss.net.protocol.file.useURI=true
            fail("URL: " + url + " should have thrown an IllegalArgumentException when -Dorg.jboss.net.protocol.file.useURI=true see JBPAPP-8065");
         }
         catch(IllegalArgumentException iae)
         {
            // This is the expected results the url.toURI() will throw given file:non... is not a valid URI
            return;   
         }
         fail("URL: " + url + " should have thrown an IllegalArgumentException when -Dorg.jboss.net.protocol.file.useURI=true see JBPAPP-8065");
      }
      catch (MalformedURLException e)
      {
         e.printStackTrace();
         fail("URL: " + url + " should not be malformed");
      }
   }
}
