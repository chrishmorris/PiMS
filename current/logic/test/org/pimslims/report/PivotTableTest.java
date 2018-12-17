package org.pimslims.report;

import java.util.Iterator;

import junit.framework.TestCase;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.report.PivotTable.Cell;
import org.pimslims.report.PivotTable.Column;

public class PivotTableTest  extends TestCase {
	
	private static class TestCell implements Cell {

		private Object value = null;

		/**
		 * Constructor for TestCell
		 */
		public TestCell() {
			super();
		}

		@Override
		public void add(Object object) {
			this.value = object;
		}

		/**
		 * TestCell.getValue
		 * 
		 * @see org.pimslims.report.PivotTable.Cell#getValue()
		 */
		@Override
		public Object getValue() {
			return this.value;
		}


	}

	private static class Counter implements Cell {
		private int count = 0;;

		public Counter() {
			super();
		}

		/**
		 * Counter.add
		 * 
		 * @see org.pimslims.report.PivotTable.Cell#add(java.lang.Object)
		 */
		@Override
		public void add(Object object) {
			this.count = this.count + 1;
		}

		/**
		 * Counter.getValue
		 * 
		 * @see org.pimslims.report.PivotTable.Cell#getValue()
		 */
		@Override
		public Object getValue() {
			return this.count;
		}

	}

	private static final String UNIQUE=""+System.currentTimeMillis();
	
	
	/**
	 * Constructor for PivotTableTest 
	 * @param name
	 */
	public PivotTableTest(String method) {
		super(method);
	}

	private String getUnique() {return this.getClass().getSimpleName()+":"+this.getName()+UNIQUE;} 
	
	public void testNoCells() {
		PivotTable<Experiment> table = new PivotTable<Experiment>() {
	    	@Override
			protected Column getColumn(Experiment experiment) {return null;}

			@Override
			protected Row getRow(
					Experiment experiment) {
				return null;
			}

			@Override
			protected Class getCellClass(String column) {
				return TestCell.class;
			}

	    };
	    Iterator<PivotTable<Experiment>.Row> rows = table.getRowInterator();
	    assertFalse(rows.hasNext());
	    Iterator<Column> columns = table.getColumnInterator();
	    assertFalse(columns.hasNext());
	    Cell[][] cells = table.getCells();
	    assertEquals(0, cells.length);
	}
	
	public void testOneCell() {
		PivotTable<Experiment> table = new PivotTable<Experiment>() {
	    	private final Column column = new Column(getUnique()+"c");
			private final Row row = new Row(getUnique()+"r");

			@Override
			protected Column getColumn(Experiment experiment) {return this.column ;}

			@Override
			protected Row getRow(
					Experiment experiment) {
				return this.row ;
			}

			@Override
			protected Class getCellClass(String column) {
				return TestCell.class;
			}

	    };
	    table.add(null);
	    Iterator<PivotTable<Experiment>.Row> rows = table.getRowInterator();
	    assertTrue(rows.hasNext());
	    assertEquals(getUnique()+"r", rows.next().getName()); 
	    assertFalse(rows.hasNext());
	    Iterator<Column> columns = table.getColumnInterator();
	    assertTrue(columns.hasNext());
	    assertEquals(getUnique()+"c", columns.next().getName()); 
	    assertFalse(columns.hasNext());
	    Cell[][] cells = table.getCells();
	    assertEquals(1, cells.length);
	    assertEquals(1, cells[0].length);
	}
	

	public void testRows() {
		PivotTable<String> table = new PivotTable<String>() {
	    	private final Column column = new Column(getUnique()+"c");

			@Override
			protected Column getColumn(String string) {return this.column ;}

			@Override
			protected Row getRow(String string) {
				return new Row(string) ;
			}

			@Override
			protected Class getCellClass(String column) {
				return TestCell.class;
			}
			
			
	    };
	    table.add("one");
	    table.add("two");
	    Iterator<PivotTable<String>.Row> rows = table.getRowInterator();
	    assertTrue(rows.hasNext());
	    assertEquals("one", rows.next().getName()); 
	    assertTrue(rows.hasNext());
	    assertEquals("two", rows.next().getName()); 
	    assertFalse(rows.hasNext());
	    Iterator<Column> columns = table.getColumnInterator();
	    assertTrue(columns.hasNext());
	    assertEquals(getUnique()+"c", columns.next().getName()); 
	    assertFalse(columns.hasNext());
	    Cell[][] cells = table.getCells();
	    assertEquals(2, cells.length);
	    assertEquals(1, cells[0].length);
		assertEquals("one", cells[0][0].getValue());
	    assertEquals(1, cells[1].length);
		assertEquals("two", cells[1][0].getValue());
	}
	

	public void testColumns() {
		PivotTable<String> table = new PivotTable<String>() {
	    	private final Row row = new Row(getUnique()+"r");

			@Override
			protected Row getRow(String string) {return this.row ;}

			@Override
			protected Column getColumn(String string) {
				return new Column(string) ;
			}

			@Override
			protected Class getCellClass(String column) {
				return TestCell.class;
			}

	    };
	    table.add("one");
	    table.add("two");
	    Iterator<Column> columns = table.getColumnInterator();
	    assertTrue(columns.hasNext());
	    assertEquals("one", columns.next().getName()); 
	    assertTrue(columns.hasNext());
	    assertEquals("two", columns.next().getName()); 
	    assertFalse(columns.hasNext());
	    
	    Iterator<PivotTable<String>.Row> rows = table.getRowInterator();
	    assertTrue(rows.hasNext());
	    assertEquals(getUnique()+"r", rows.next().getName()); 
	    assertFalse(rows.hasNext());
	    Cell[][] cells = table.getCells();
	    assertEquals(1, cells.length);
	    assertEquals(2, cells[0].length);
		assertEquals("one", cells[0][0].getValue());
		assertEquals("two", cells[0][1].getValue());
	}

	public void testAggregate() {
		PivotTable<String> table = new PivotTable<String>() {
	    	private final Column column = new Column(getUnique()+"c");
			private final Row row = new Row(getUnique()+"r");

			@Override
			protected Column getColumn(String string) {return this.column ;}

			@Override
			protected Row getRow(String string) {
				return this.row ;
			}

			@Override
			protected Class getCellClass(String column) {
				return Counter.class;
			}
	    };
	    table.add("one");
	    table.add("two");
		Iterator<PivotTable<String>.Row> rows = table.getRowInterator();
	    assertTrue(rows.hasNext());
	    assertEquals(getUnique()+"r", rows.next().getName()); 
	    assertFalse(rows.hasNext());
	    Iterator<Column> columns = table.getColumnInterator();
	    assertTrue(columns.hasNext());
	    assertEquals(getUnique()+"c", columns.next().getName()); 
	    assertFalse(columns.hasNext());
	    Cell[][] cells = table.getCells();
	    assertEquals(1, cells.length);
	    assertEquals(1, cells[0].length);
	    assertEquals(new Integer(2), cells[0][0].getValue());
	}
	
	public void testDifferentColumns() {
		PivotTable<String> table = new PivotTable<String>() {
			private final Row row = new Row(getUnique() + "r");

			@Override
			protected Row getRow(String string) {
				return this.row;
			}

			@Override
			protected Column getColumn(String string) {
				return new Column(string);
			}

			@Override
			protected Class getCellClass(String string) {
				if ("one".equals(string)) {
					return TestCell.class;
				}
				return Counter.class;
			}

		};
		table.add("one");
		table.add("two");
		Iterator<Column> columns = table.getColumnInterator();
		assertTrue(columns.hasNext());
		assertEquals("one", columns.next().getName());
		assertTrue(columns.hasNext());
		assertEquals("two", columns.next().getName());
		assertFalse(columns.hasNext());

		Iterator<PivotTable<String>.Row> rows = table.getRowInterator();
		assertTrue(rows.hasNext());
		assertEquals(getUnique() + "r", rows.next().getName());
		assertFalse(rows.hasNext());
		Cell[][] cells = table.getCells();
		assertEquals(1, cells.length);
		assertEquals(2, cells[0].length);
		assertTrue(cells[0][0] instanceof TestCell);
		assertTrue(cells[0][1] instanceof Counter);
	}

}
