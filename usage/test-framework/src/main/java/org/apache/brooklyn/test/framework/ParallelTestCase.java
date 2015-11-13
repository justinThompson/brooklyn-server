package org.apache.brooklyn.test.framework;

import org.apache.brooklyn.api.entity.Entity;
import org.apache.brooklyn.api.entity.ImplementedBy;
import org.apache.brooklyn.core.entity.trait.Startable;

/**
 * This implementation will start all child entities in parallel.
 *
 * @author Chris Burke
 */
@ImplementedBy(value = ParallelTestCaseImpl.class)
public interface ParallelTestCase extends Entity, Startable {
}