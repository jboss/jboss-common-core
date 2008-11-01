package org.jboss.test.util.test.collection;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Unit tests for custom maps.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractMapUnitTest extends TestCase
{
   protected abstract Map createEmptyMap();

   @SuppressWarnings("unchecked")
   public void testBasicOperations() throws Exception
   {
      Map map = createEmptyMap();
      assertTrue(map.isEmpty());

      String key = "date1";
      Date value = new Date();
      map.put(key, value);

      assertTrue(map.containsKey(key));
      assertTrue(map.containsValue(value));
      assertEquals(1, map.size());

      map.clear();
      assertTrue(map.isEmpty());

      key = "date1";
      value = new Date();
      map.put(key, value);

      map.remove(key);
      assertTrue(map.isEmpty());

      map.putAll(Collections.singletonMap(key, value));

      assertEquals(value, map.get(key));
      assertEquals(map, Collections.singletonMap(key, value));
      
      // iterables
      Iterable<String> keys = map.keySet();
      assertIterable(keys, String.class);
      Iterable<Date> values = map.values();
      assertIterable(values, Date.class);
      Iterable<Map.Entry> entries = map.entrySet();
      Map.Entry entry = assertIterable(entries, Map.Entry.class);
      assertEquals(key, entry.getKey());
      assertEquals(value, entry.getValue());
   }

   protected <T> T assertIterable(Iterable<T> iter, Class<T> clazz)
   {
      assertTrue(iter.iterator().hasNext());
      T next = iter.iterator().next();
      assertTrue("Next " + next + " is not instance of " + clazz.getName(), clazz.isInstance(next));
      assertNotNull(next);
      return next;
   }
}