package org.jboss.test.util.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jboss.util.Classes;

/**
 * Unit tests for org.jboss.util.Classes utility
 * 
 * @author Dimitris.Andreadis@jboss.org
 * @version $Revision: 43534 $
 */
public class ClassesUnitTestCase extends TestCase
{
   public void testGetAllInterfaces()
   {
      List list = new ArrayList();
      Classes.getAllInterfaces(list, ExtendedClass.class);
      assertEquals(3, list.size());
      assertEquals(Interface1.class, (java.lang.Class)list.get(0));
      assertEquals(Interface1.class, (java.lang.Class)list.get(1));
      assertEquals(Interface2.class, (java.lang.Class)list.get(2));
   }
   
   public void testGetAllUniqueInterfaces()
   {
      Class[] interfaces = Classes.getAllUniqueInterfaces(ExtendedClass.class);
      assertEquals(2, interfaces.length);
   }
   
   public interface Interface1 {}
   public interface Interface2 {}

   public static class BaseClass implements Interface1, Interface2 {}
   public static class ExtendedClass extends BaseClass implements Interface1 {}
} 