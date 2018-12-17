package org.pimslims.crystallization.servlet;

import org.pimslims.crystallization.servlet.view.ViewByName;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;

public class ViewByNameTest extends AbstractTestCase {
	public void testProcess() throws AccessException, ConstraintException {
		wv = getWV();
		try {
			final Target t = create(Target.class);
			final String hook = ViewByName.process(wv, Target.class.getName()
					+ ":" + t.getName());
			assertEquals(t.get_Hook(), hook);

		} finally {
			wv.abort();
		}
	}
}
