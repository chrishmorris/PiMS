package org.pimslims.diamond.userrights;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;


public class ParserTest {
	
	private static final String EXAMPLE = "<ns1:buildSQLResponse xmlns:ns1=\"http://www.diamond.ac.uk/services/genericws\">" +
			"<ns1:numberColumns>8</ns1:numberColumns><ns1:columnNames>RNUM</ns1:columnNames><ns1:columnNames>VISIT_NUMBER</ns1:columnNames>" +
			"<ns1:columnNames>PROJECTCODE</ns1:columnNames><ns1:columnNames>BEAMLINEOPERATOR</ns1:columnNames><ns1:columnNames>BEAMLINENAME</ns1:columnNames>" +
			"<ns1:columnNames>STARTDATE</ns1:columnNames><ns1:columnNames>PROPOSALID</ns1:columnNames><ns1:columnNames>SESSIONID</ns1:columnNames>" +
			"<ns1:numberRows>1</ns1:numberRows><ns1:row>1#SEP#1#SEP#null#SEP#null#SEP#p45#SEP#2014-09-21 09:00:00#SEP#26166#SEP#31381#SEP#</ns1:row>" +
			"<ns1:status>OK</ns1:status><ns1:message>success</ns1:message></ns1:buildSQLResponse>";

	@Test
	public void testEmpty() {
		Map<String, Object>[] maps = new DiamondResponseParser("<ns1:buildSQLResponse xmlns:ns1=\"http://www.diamond.ac.uk/services/genericws\">" +
				"<ns1:numberColumns>0</ns1:numberColumns><ns1:numberRows>0</ns1:numberRows></ns1:buildSQLResponse>").getMap();
		assertTrue(0==maps.length);
	}

	@Test
	public void test1Row() {
		Map<String, Object>[] maps = new DiamondResponseParser("<ns1:buildSQLResponse xmlns:ns1=\"http://www.diamond.ac.uk/services/genericws\">" +
				"<ns1:numberRows>1</ns1:numberRows><ns1:row>1#SEP#1#SEP#null#SEP#null#SEP#p45#SEP#2014-09-21 09:00:00#SEP#26166#SEP#31381#SEP#</ns1:row>" +
				"<ns1:status>OK</ns1:status><ns1:message>success</ns1:message></ns1:buildSQLResponse>").getMap();
		assertTrue(1==maps.length);
	}

    @Test
	public void test1Column() {
		DiamondResponseParser parser = new DiamondResponseParser("<ns1:buildSQLResponse xmlns:ns1=\"http://www.diamond.ac.uk/services/genericws\">" +
				"<ns1:numberColumns>1</ns1:numberColumns><ns1:columnNames>RNUM</ns1:columnNames>" +
				"<ns1:numberRows>0</ns1:numberRows>" +
				"<ns1:status>OK</ns1:status><ns1:message>success</ns1:message></ns1:buildSQLResponse>");
		String[] keywords = parser.getKeywords();
		assertEquals(1, keywords.length);
		assertEquals("RNUM", keywords[0]);
	}
	
	@Test
	public void testStringColumn() {
		Map<String, Object>[] maps = new DiamondResponseParser(EXAMPLE).getMap();
		assertTrue(1==maps.length);
		assertEquals(8, maps[0].size());
		assertEquals("26166", maps[0].get("PROPOSALID"));
	}
	
	//TODO test2Rows
	
	//TODO testDateColumn
	
	//TODO testIntegerColumn
}

