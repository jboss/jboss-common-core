package org.jboss.test.util.test.collection;

import java.util.Set;

import org.jboss.util.collection.ConcurrentReferenceHashSet;

/**
 * Unit tests for ConcurrentReferenceSet
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SuppressWarnings("unchecked")
public class ConcurrentRefSetUnitTestCase extends AbstractSetUnitTest
{
   protected Set createSet()
   {
      return new ConcurrentReferenceHashSet();
   }
}