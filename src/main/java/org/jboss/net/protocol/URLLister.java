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

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

/**
 * Interface defining methods that can be used to list the contents of a URL
 * collection irrespective of the protocol.
 */
public interface URLLister {
   /**
    * List the members of the given collection URL that match the patterns
    * supplied and, if it contains directory that contains NO dot in the name and
    * scanNonDottedSubDirs is true, recursively finds URL in these directories.
    * @param baseUrl the URL to list; must end in "/"
    * @param patterns the patterns to match (separated by ',')
    * @param scanNonDottedSubDirs enables recursive search for directories containing no dots
    * @return a Collection of URLs that match
    * @throws IOException if there was a problem getting the list
    */
   Collection listMembers(URL baseUrl, String patterns, boolean scanNonDottedSubDirs) throws IOException;
   
   /**
    * List the members of the given collection URL that match the patterns
    * supplied. Doesn't recursively list files contained in directories.
    * @param baseUrl the URL to list; must end in "/"
    * @param patterns the patterns to match (separated by ',')
    * @return a Collection of URLs that match
    * @throws IOException if there was a problem getting the list
    */
   Collection listMembers(URL baseUrl, String patterns) throws IOException;

   /**
    * List the members of the given collection that are accepted by the filter
    * @param baseUrl the URL to list; must end in "/"
    * @param filter a filter that is called to determine if a member should
    *               be returned
    * @param scanNonDottedSubDirs enables recursive search for directories containing no dots
    * @return a Collection of URLs that match
    * @throws IOException if there was a problem getting the list
    */
   Collection listMembers(URL baseUrl, URLFilter filter, boolean scanNonDottedSubDirs) throws IOException;

   /**
    * List the members of the given collection that are accepted by the filter
    * @param baseUrl the URL to list; must end in "/"
    * @param filter a filter that is called to determine if a member should
    *               be returned
    * @return a Collection of URLs that match
    * @throws IOException if there was a problem getting the list
    */
   Collection listMembers(URL baseUrl, URLFilter filter) throws IOException;

   /**
    * Interface defining a filter for listed members.
    */
   public interface URLFilter {
      /**
       * Determine whether the supplied memberName should be accepted
       * @param baseURL the URL of the collection
       * @param memberName the member of the collection
       * @return
       */
      boolean accept(URL baseURL, String memberName);
   }
}
