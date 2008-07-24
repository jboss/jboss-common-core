package org.jboss.test.util.test.collection;

import java.util.Set;

import org.jboss.util.collection.ConcurrentSet;

/**
 * Unit tests for ConcurrentSet
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SuppressWarnings("unchecked")
public class ConcurrentSetUnitTestCase extends AbstractSetUnitTest
{
   protected Set createSet()
   {
      return new ConcurrentSet();
   }
}