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
package org.jboss.net.protocol;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;

public class URLListerFactory {
   private static HashMap defaultClasses = new HashMap();
   static {
      defaultClasses.put("file", "org.jboss.net.protocol.file.FileURLLister");
      defaultClasses.put("http", "org.jboss.net.protocol.http.DavURLLister");
      defaultClasses.put("https", "org.jboss.net.protocol.http.DavURLLister");
   }

   private HashMap classes;

   /**
    * Create a URLLister with default listers defined for file and http
    * protocols.
    */
   public URLListerFactory() {
      classes = (HashMap) defaultClasses.clone();
   }

   /**
    * Create a URL lister using the protocol from the URL
    * @param url the url defining the protocol
    * @return a URLLister capable of listing URLs of that protocol
    * @throws MalformedURLException if no lister could be found for the protocol
    */
   public URLLister createURLLister(URL url) throws MalformedURLException  {
      return createURLLister(url.getProtocol());
   }

   /**
    * Create a URL lister for the supplied protocol
    * @param protocol the protocol
    * @return a URLLister capable of listing URLs of that protocol
    * @throws MalformedURLException if no lister could be found for the protocol
    */
   public URLLister createURLLister(String protocol) throws MalformedURLException {
      try {
         String className = (String) classes.get(protocol);
         if (className == null) {
            throw new MalformedURLException("No lister class defined for protocol "+protocol);
         }

         Class clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
         return (URLLister) clazz.newInstance();
      } catch (ClassNotFoundException e) {
         throw new MalformedURLException(e.getMessage());
      } catch (InstantiationException e) {
         throw new MalformedURLException(e.getMessage());
      } catch (IllegalAccessException e) {
         throw new MalformedURLException(e.getMessage());
      }
   }

   /**
    * Register a URLLister class for a given protocol
    * @param protocol the protocol this class will handle
    * @param className the URLLister implementation to instanciate
    */
   public void registerListener(String protocol, String className) {
      classes.put(protocol, className);
   }
}
