package org.jboss.test.util.test.collection;

import java.util.Iterator;
import java.util.Set;

/**
 * Unit tests for WeakSet
 *
 * @author <a href="mailto:sven@meiers.net">Sven Meier</a>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @version $Revision: 43534 $
 */
@SuppressWarnings("unchecked")
public abstract class AbstractNullableSetUnitTest extends AbstractSetUnitTest
{
   protected abstract Set createSet();

   public void testNullElement()
   {
      Set set = createSet();

      set.add(null);

      assertEquals(1, set.size());

      Iterator iterator = set.iterator();
      assertTrue(iterator.hasNext());
      iterator.next();
      assertFalse(iterator.hasNext());
   }
}