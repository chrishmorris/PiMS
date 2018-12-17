package org.pimslims.spreadsheet;

public class Formula {

	private final String cellFormula;

	public Formula(String cellFormula) {
		this.cellFormula = cellFormula;
	}

	/**
	 * @return Returns the cellFormula.
	 */
	public String getCellFormula() {
		return cellFormula;
	}
	
	public Object evaluate() {
		throw new UnsupportedOperationException("Not implemented");
	}

}
