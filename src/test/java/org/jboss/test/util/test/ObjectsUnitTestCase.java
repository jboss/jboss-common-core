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
package org.jboss.test.util.test;

import java.util.Date;

import junit.framework.TestCase;
import org.jboss.util.Objects;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ObjectsUnitTestCase extends TestCase
{
   public void testEquals() throws Exception
   {
      assertTrue(Objects.equals("123", "123"));

      Object first = new String[]{"123", "321"};
      Object second = new String[]{"123", "321"};
      assertTrue(Objects.equals(first, second));
      first = new String[][]{{"1", "2"}, {"1", "2"}};
      second = new String[][]{{"1", "2"}, {"1", "2"}};
      assertTrue(Objects.equals(first, second));

      assertFalse(Objects.equals("129", "123"));
      first = new String[]{"123", "324"};
      second = new String[]{"123", "321"};
      assertFalse(Objects.equals(first, second));
      first = new String[][]{{"1", "6"}, {"1", "2"}};
      second = new String[][]{{"1", "2"}, {"1", "2"}};
      assertFalse(Objects.equals(first, second));

      first = new int[]{1, 2, 3};
      second = new int[]{1, 2, 3};
      assertTrue(Objects.equals(first, second));
      first = new int[][]{{1, 2}, {1, 2}};
      second = new int[][]{{1, 2}, {1, 2}};
      assertTrue(Objects.equals(first, second));

      first = new int[]{1, 2, 4};
      second = new int[]{1, 2, 3};
      assertFalse(Objects.equals(first, second));
      first = new int[][]{{1, 6}, {1, 2}};
      second = new int[][]{{1, 2}, {1, 2}};
      assertFalse(Objects.equals(first, second));

      assertFalse(Objects.equals("123", new int[]{1, 2, 3}));
      assertFalse(Objects.equals(new int[]{1, 2, 3}, "123"));
      assertFalse(Objects.equals(new Date(), 123));
   }
}
