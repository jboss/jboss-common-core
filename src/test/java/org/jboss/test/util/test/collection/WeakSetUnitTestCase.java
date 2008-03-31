package org.jboss.test.util.test.collection;

import java.util.Iterator;

import org.jboss.util.collection.WeakSet;

import junit.framework.TestCase;

/**
 * Unit tests for WeakSet
 * 
 * @author <a href="mailto:sven@meiers.net">Sven Meier</a> 
 * @version $Revision: 43534 $
 */
@SuppressWarnings("unchecked")
public class WeakSetUnitTestCase extends TestCase
{
   public void testNullElement()
   {
      WeakSet set = new WeakSet();
        
      set.add(null);
        
      assertEquals(1, set.size());
        
      Iterator iterator = set.iterator();
      assertTrue(iterator.hasNext());     
      iterator.next();
      assertFalse(iterator.hasNext());
   }
    
   public void testMultipleHasNext()
   {
      WeakSet set = new WeakSet();
        
      set.add(new Object());
        
      Iterator iterator = set.iterator();
      assertTrue(iterator.hasNext());
      assertTrue(iterator.hasNext());
   }
}
