package org.pimslims.report;

import java.util.ArrayList;
import java.util.List;

import org.pimslims.model.people.Organisation;

/**
 * Table Represents the results from a tabular query We already have code that
 * represents a WHERE clause. Unfortunately, it assumes that the results will be
 * a list of model objects.
 * 
 */
public class TableResults {

	public static class Column {

		private final String name;

		private final Class type;

		/**
		 * Constructor for Column
		 * 
		 * @param name
		 * @param type
		 */
		public Column(String name, Class type) {
			super();
			this.name = name;
			this.type = type;
		}

		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return Returns the type.
		 */
		public Class getType() {
			return type;
		}


	}

	private final List<Column> columns = new ArrayList<Column>();

	public TableResults(Class<Organisation> class1) {
		// TODO Auto-generated constructor stub
	}

	public List<Column> getColumns() {
		return this.columns;
	}

	public void addColumn(String propName) {
		this.columns.add(new Column("name", String.class));
	}

}
