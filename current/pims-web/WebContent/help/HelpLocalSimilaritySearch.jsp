<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Guide to local sequence similarity searches in PiMS ' />
</jsp:include>

<div class="help">
 
<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#overview">Overview</a> </li>
  <li><a href="#setup">Setting up a search</a></li> 
  <li><a href="#summary">Search results hit list</a></li>
  <li><a href="#alignments">Search results alignments</a></li>
 </ul>
 <a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS<br />
</pimsWidget:box>

<div class="toplink"><a href="#">Back to top</a></div>


<pimsWidget:box initialState="fixed" title="Overview">
  <div class="textNoFloat">
Local sequence similarity search can be performed for any DNA or protein sequence. 
These include PiMS targets' protein and nucleotide sequences, expressed and final proteins, primers and constructs.<br />  

Search results are displayed in a summary table containing links to the individual 
&#39;Hits&#39; and sequence alignments. 
<br />

PIMS employs Smith-Waterman algorithm for local sequence similarity search which is slightly more sensitive than BLAST. 
<div class="techdetails">
The Smith-Waterman algorithm is a database search algorithm developed by T.F. Smith and M.S. Waterman.
The S-W Algorithm implements a technique called dynamic programming, which takes alignments of any length, 
at any location, in any sequence, and determines whether an optimal alignment can be found. 
Based on these calculations, scores or weights are assigned to each character-to-character comparison: positive 
for exact matches/substitutions, negative for insertions/deletions. In weight matrices, scores are added together 
and the highest scoring alignment is reported.
<p> To put it simply, dynamic programming finds solutions to smaller pieces of the problem and then puts them 
all together to form a complete and optimal final solution to the entire problem.</p>
<p>It is superior to the BLAST and FASTA algorithms because it searches a larger field of possibilities, 
making it a more sensitive technique; however, individual pair-wise comparisons between letter slows
the process down significantly. (copyright Stanford University)
<a href="http://cse.stanford.edu/class/sophomore-college/projects-00/computers-and-the-hgp/smith_waterman.html">read more</a>
</p>
</div>
The inclusion of gaps is controlled by the two penalties known as gapopen and gapextend. 
Given a novel protein sequence, this may be the choice to detect distantly related proteins. 
These proteins may align with long gapped regions, possibly in loops on protein surfaces that may not 
contain critical functional residues. If you want to detect weak candidate alignments it may be 
necessary to repeat the searches with a variety of gap penalties. 
</div>
<h2 id="setup">Setting up a search</h2>
  <div class="textNoFloat">
  
  First you need to supply PIMS with a sequence to searched against PIMS local database
  To do that click on the Target->Similarity Search on the menubar 
 <img class="imageNoFloat" src="../images/helpScreenshots/bioinf/LoadSequence.gif" alt="Load sequence" /><br />
   
<h5>Sequence</h5> 
    You can cut and paste or type a protein sequence into the large text window. 
    A free text (raw) sequence is simply a block of characters representing a protein or a nucleotide sequence. 
    Partially formatted sequences will not be accepted. 
		If you paste a sequence in FASTA format, it's name will be used in a hit list and alignments. 
		Please note that sequence name will be chopped to 13 characters to preserve alignment formatting.  

<h5>Matrix (scoring function)</h5> 
This is the scoring matrix used when comparing sequences. By default it PIMS uses 'BLOSUM62'
for both proteins and nucleotide sequences. However, you are welcome to change this according to the task.  
<div class="techdetails">
 The choice of a scoring function that reflects biological or statistical observations about 
 known sequences is important to producing good alignments. Protein sequences are frequently aligned 
 using substitution matrices that reflect the probabilities of given character-to-character substitutions. 
 A series of matrices called PAM matrices (Point Accepted Mutation matrices, originally defined by 
 Margaret Dayhoff and sometimes referred to as "Dayhoff matrices") explicitly encode evolutionary 
 approximations regarding the rates and probabilities of particular amino acid mutations. 
 Another common series of scoring matrices, known as BLOSUM (Blocks Substitution Matrix), 
 encodes empirically derived substitution probabilities. (courtesy of Wikipedia)
 <a href="http://www.ebi.ac.uk/help/matrix_frame.jsp">More about scoring matrices</a> 
 </div>

<h5>Gap</h5> 

Default in PIMS 10.0 for any sequence. The gap open penalty is the score taken away when a gap is created. 
The best value depends on the choice of comparison matrix. (Valid number from 0 to 100.0) 
 
<h5>Gap extend</h5> 

The PIMS default is 0.5 for any sequence. The gap extension penalty is added to the standard gap penalty for each 
base or residue in the gap. This is how long gaps are penalized. Usually you will expect a few long gaps 
rather than many short gaps, so the gap extension penalty should be lower than the gap penalty. An
exception is where one or both sequences are single reads with possible sequencing
errors in which case you would expect many single base gaps. You can get this result by
setting the gap open penalty to zero (or very low) and using the gap extension
penalty to control gap scoring. (Number from 0 to 10.0) 
</div>
<h3 id="summary" style="color: black;">Search results hit list</h3>
<div class="textNoFloat">
<p>Search results comprising information describing any entries in PIMS database (Hits) which match the search parameters.
At the top of a search results page a hit table is displayed. It is sorted by the score.
Example screenshots </p> 
<img class="imageNoFloat" src="../images/helpScreenshots/bioinf/hitList.gif" alt="Hit list" /><br />
<br/>
<img class="imageNoFloat" src="../images/helpScreenshots/bioinf/hitListConstruct.gif" alt="Hit list with construct" /><br />
Hit table columns form left to right: 
<h5>No - (Result Number)</h5>
This is the result number, results are ranked in descending order of score.
By clicking on this number you will be forwarded to the details of the alignment further down the page. 

<h5>Hit id</h5>
 The name of the molecule which sequence matches query sequence. By clicking on this name you can view 
other information related to it. 

<h5>Hit type</h5>
Either Target, Construct or Primer sequence. By howevering a mouse other the type you can see
its name, click on it and you'll be forwarded to the details.      

<h5>Length</h5>
Alignment sequence length.

<h5>Score</h5>
Alignment score, representing the likelihood that the described alignment is not random, 
providing an indication of its validity. It is calculated by totaling the scores for each matched pair of 
residues at each position in the alignment, plus unmatched residues are given the gap penalty, 
and the gap extension penalty.

<h5>Identity</h5>
The number of residues (either an amino acid or a nucleotide) which are identical between the query sequence and the Hit, over the region of the match.
<h5>Similarity</h5>
The number of identical or related residues over the region of the match.
<h5>Gaps</h5>
The number of gaps in the alignment
</div>

<h3 id="alignments" style="color: black;">Alignments</h3> 
<div class="textNoFloat">
For each Hit, PiMS displays the alignment between the Query and the Hit sequences in a table.
The alignment tables are located at the end of the Hit list table. You can view them by scrolling to the bottom of 
the Hit table or by clicking the "View Alignments" link at the top of it.
There is also a numbered link to each individual alignment table in the first column of the hit list.
	PIMS adopt Intelligenetics format which uses `|' to show identities and `:' to show conservative replacements 
and places these indicators between the two aligned sequences.
An example alignment is shown below. 
<img class="imageNoFloat" src="../images/helpScreenshots/bioinf/Alignment.gif" alt="Pairwise sequence alignment" /><br />

<ul> 
    <li style="padding: 1em 0 1em 0">The lengths of the Target and Hit sequences are shown, along with the length 
    and score for the alignment.  Also shown are the number of 
    identical amino acids, the number of identical and similar amino acids  
    and the number of gaps in the region of the alignment.</li>
  
    <li style="padding: 1em 0 1em 0">Below this are values for the &#037; of identical residues in the Hit, alignment 
    and Target and the length of the Hit as a &#037; of the Target length.</li>
  
    <li style="padding: 1em 0 1em 0">The sequence alignment is displayed in three rows.  The top and bottom rows are 
    labelled Query (or sequence name if the sequence was in FASTA format) and hit name 
    and these are separated by a pattern in red.</li>
	
		<li style="padding: 1em 0 1em 0">
		<strong>Markup Line</strong>
 		is placed between a pairwise alignment it shows where sequences 
		are mismatched, gapped, identical or similar. 
		<br/>In general the markup line uses a space for a mismatch or a gap, &quot;.&quot;
		for any small positive score, &quot;:&quot; for a similarity which scores more
		than 1.0, and &quot;|&quot; for an identity where both sequences have the same
		residue regardless of its score (&quot;W&quot; matching &quot;W&quot; scores much more
		than &quot;L&quot; matching &quot;L&quot; because a conserved tryptophan is more
		significant than a conserved leucine).
	</li>
	
	<li style="padding: 1em 0 1em 0">
	Any two residues or bases are defined as similar when they have
	positive comparisons (as defined by the comparison matrix being used in
	the alignment algorithm).
	</li>
	
	<li style="padding: 1em 0 1em 0">
	Note that the sum of identical and similar positions is greater than
	100%.  This is because the count of similar positions includes the count
	of identical positions; if residues are identical, they must also be
	similar.</li> 
 	<li style="padding: 1em 0 1em 0">
	<strong>Score</strong>
 	used to determine which is the best possible alignment to report. 
	Smith-Waterman score of an alignment is equal to the sum of the matches taken from the scoring matrix.
	</li>
</ul>
<br/>
</div>
  	
 <div class="toplink"><a href="#">Back to top</a></div>

 
 </pimsWidget:box>
</div> <!--end div help-->

<jsp:include page="/JSP/core/Footer.jsp" />
