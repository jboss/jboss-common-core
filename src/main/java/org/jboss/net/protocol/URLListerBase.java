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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Collection;
import java.util.StringTokenizer;
import java.net.URL;
import java.io.IOException;

/**
 * Support class for URLLister's providing protocol independent functionality.
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public abstract class URLListerBase implements URLLister
{
   public Collection listMembers (URL baseUrl, String patterns,
      boolean scanNonDottedSubDirs) throws IOException
   {
      // @todo, externalize the separator?
      StringTokenizer tokens = new StringTokenizer (patterns, ",");
      String[] members = new String[tokens.countTokens ()];
      for (int i=0; tokens.hasMoreTokens (); i++)
      {
         String token = tokens.nextToken ();
         // Trim leading/trailing spaces as its unlikely they are meaningful
         members[i] = token.trim();
      }
      URLFilter filter = new URLFilterImpl (members);
      return listMembers (baseUrl, filter, scanNonDottedSubDirs);
   }

   public Collection listMembers (URL baseUrl, String patterns) throws IOException
   {
      return listMembers (baseUrl, patterns, false);
   }
   
   /**
    * Inner class representing Filter criteria to be applied to the members
    * of the returned Collection
    */
   public static class URLFilterImpl implements URLFilter
   {
      protected boolean allowAll;
      protected HashSet constants;
      
      public URLFilterImpl (String[] patterns)
      {
         constants = new HashSet (Arrays.asList (patterns));
         allowAll = constants.contains ("*");
      }
      
      public boolean accept (URL baseUrl, String name)
      {
         if (allowAll)
         {
            return true;
         }
         if (constants.contains (name))
         {
            return true;
         }
         return false;
      }
   }
   
   protected static final URLFilter acceptAllFilter = new URLFilter ()
   {
      public boolean accept (URL baseURL, String memberName)
      {
         return true;
      }
   };
}
