package org.pimslims.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

public abstract class PivotTable<T extends Object> {

    public static interface Cell {

        public abstract void add(Object object);

        public abstract Object getValue();

    }

    public static class Column {

        private final String name;

        /**
         * Constructor for Column
         * 
         * @param name
         */
        public Column(String name) {
            super();
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }

    //TODO no, LinkedHashMap<String, Column>. Pull up from ThroughputReport.
    private final Collection<Column> columns = new LinkedHashSet();

    public class Row {

        private final String name;

        private final Map<Column, Cell> cells = new HashMap();

        /**
         * Constructor for Row
         * 
         * @param name
         */
        public Row(String name) {
            super();
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Iterator<Cell> getCellIterator() {
            final Iterator<Column> columns = PivotTable.this.columns.iterator();
            return new Iterator<Cell>() {

                @Override
                public boolean hasNext() {
                    return columns.hasNext();
                }

                @Override
                public Cell next() {
                    return Row.this.cells.get(columns.next());
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        public void add(T object, Column column) {
            try {
                if (!this.cells.containsKey(column)) {
                    this.cells.put(column, (Cell) PivotTable.this.getCellClass(column.getName())
                        .newInstance());
                }
                Cell cell = this.cells.get(column);
                cell.add(object);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private final Collection<PivotTable<T>.Row> rows = new LinkedHashSet();

    public PivotTable() {
        super();
    }

    public Iterator<PivotTable<T>.Row> getRowInterator() {
        //TODO sort
        return this.rows.iterator();
    }

    public Iterator<Column> getColumnInterator() {
        //TODO sort
        return this.columns.iterator();
    }

    /**
     * PivotTable.getColumn
     * 
     * @param object PiMS record with information to add to report
     * @return column the information goes in, or null if irrelevant
     */
    protected abstract Column getColumn(T object);

    /**
     * PivotTable.getRow
     * 
     * @param object PiMS record with information to add to report
     * @return row the information goes in, or null if irrelevant
     */
    protected abstract Row getRow(T object);

    public void add(T object) {
        Column column = getColumn(object);
        if (null == column) {
            return;
        }
        this.columns.add(column);
        Row row = getRow(object);
        this.rows.add(row);
        if (null == row) {
            return;
        }
        row.add(object, column);
    }

    public Cell[][] getCells() {
        Cell[][] ret = new Cell[rows.size()][columns.size()];
        int r = 0;
        for (Iterator<Row> iterator = rows.iterator(); iterator.hasNext();) {
            Row row = iterator.next();
            ret[r] = new Cell[columns.size()];
            int c = 0;
            for (Iterator<Cell> iterator2 = row.getCellIterator(); iterator2.hasNext();) {
                ret[r][c++] = iterator2.next();
            }
            r = r + 1;
        }
        return ret;
    }

    protected abstract Class getCellClass(String column);

}
