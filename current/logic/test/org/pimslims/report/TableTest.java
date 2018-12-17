package org.pimslims.report;

import java.util.List;

import junit.framework.TestCase;

import org.pimslims.model.people.Organisation;

public class TableTest extends TestCase {

	public TableTest(String name) {
		super(name);
	}

	public void testStringAttribute() {
		TableResults table = new TableResults(Organisation.class);
		table.addColumn(Organisation.PROP_NAME);
		List<TableResults.Column> columns = table.getColumns();
		assertEquals(1, columns.size());
		TableResults.Column column = columns.iterator().next();
		assertEquals("name", column.getName());
		assertEquals(String.class, column.getType());
	}

}
