package org.jboss.test.util.test.collection;

import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

/**
 * Unit tests for WeakSet
 *
 * @author <a href="mailto:sven@meiers.net">Sven Meier</a>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @version $Revision: 43534 $
 */
@SuppressWarnings("unchecked")
public abstract class AbstractSetUnitTest extends TestCase
{
   protected abstract Set createSet();

   public void testMultipleHasNext()
   {
      Set set = createSet();

      set.add(new Object());

      Iterator iterator = set.iterator();
      assertTrue(iterator.hasNext());
      assertTrue(iterator.hasNext());
   }
}