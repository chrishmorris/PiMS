<%--

Author: Petr Troshin
Date: June 2006
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<select name="database" style="width: 10em; margin-bottom: 5px;">
  <optgroup label="Recommended databases:">
    <option value="embl">EMBL</option> <!-- embl -->
    <option value="GenBank">GENBANK</option> <!-- Genbank -->
    <option value="uniprot" selected="selected">UNIPROT</option>
<c:if test="${param.forTarget != 'yes'}">
    <option value="pdb">PDB</option>
</c:if>
  </optgroup>
  <optgroup label="Other databases:">
	<option value="JCVI">TIGR (JCVI-CMR) locus</option> <!-- Genbank -->
	<option value="jpo_prt">JPO_PRT</option><!-- swissport -->
	<option value="epo_prt">EPO_PRT</option><!-- swissport  -->
	<option value="uspto_prt">USPTO_PRT</option> <!-- swissport  -->
	<option value="emblsva">EMBL_SVA</option>  <!-- embl -->
	<option value="emblcon">EMBL_CON</option> <!-- embl -->
	<option value="emblanncon">EMBL_ANNCON</option> <!-- embl -->
	<option value="emblcds">EMBL_CDS</option> <!-- embl -->
    <option value="SwissProt" >SWISSPROT</option> <!-- swissport -->
<c:if test="${param.forTarget != 'yes'}">
	<option value="medline">Medline</option>
	<option value="refseq">REFSEQ</option><!-- Genbank -->
	<option value="refseqp">REFSEQP</option>
	<option value="hgbase">HGBASE</option>
	<option value="interpro">INTERPRO</option>
	<option value="uniparc">UNIPARC</option>
	<option value="uniref100">UNIREF100</option>
	<option value="uniref90">UNIREF90</option>
	<option value="uniref50">UNIREF50</option>
	<option value="ipi">IPI</option>
  </optgroup>
</c:if>

</select>
