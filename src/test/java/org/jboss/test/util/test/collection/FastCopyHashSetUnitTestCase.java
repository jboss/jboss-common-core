package org.jboss.test.util.test.collection;

import org.jboss.util.collection.ConcurrentSet;
import org.jboss.util.collection.FastCopyHashSet;

import java.util.Set;

/**
 * Unit tests for FastCopyHashSet
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SuppressWarnings("unchecked")
public class FastCopyHashSetUnitTestCase extends AbstractSetUnitTest
{
   protected Set createSet()
   {
      return new FastCopyHashSet();
   }
}