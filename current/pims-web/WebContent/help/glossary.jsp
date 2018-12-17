<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='PiMS glossary' />
</jsp:include>

<div class="help">
<div class="glossary">
<!-- Note, the first item for each letter must include the <a> tag -->
 <h2>PiMS Terms</h2>

 <p>This is a glossary of terms used in PiMS.</p>

 <div>Jump to: <a href="#A">A</a> | <a href="#B">B</a> | <a
 href="#C">C</a> | <a href="#D">D</a> | <a href="#E">E</a> | <a
 href="#F">F</a> | <a href="#G">G</a> | <a href="#H">H</a> | <a
 href="#I">I</a> | <a href="#J">J</a> | <a href="#K">K</a> | <a
 href="#L">L</a> | <a href="#M">M</a> | <a href="#N">N</a> | <a
 href="#O">O</a> | <a href="#P">P</a> | <a href="#Q">Q</a> | <a
 href="#R">R</a> | <a href="#S">S</a> | <a href="#T">T</a> | <a
 href="#U">U</a> | <a href="#V">V</a> | <a href="#W">W</a> | <a
 href="#X">X</a> | <a href="#Y">Y</a> | <a href="#Z">Z</a> | <a
 href="#Other">Other</a> </div>

 <dl>
 
    <dt id="administrator"><a name="A"></a>Administrator</dt>
    <dd>The Administrator is a special <a href="#user">User</a> and is the only User defined when you 
    first install PiMS.  The Administrator has the right to create new Users, <a href="#userGroups">User 
    groups</a>, and <a href="#labNotebook">Lab notebooks</a> and also to specify the Access rights that apply 
    for the User groups and Lab notebooks i.e. the permissions to record, edit, view and delete information in one or more 
    Lab notebooks. 
    </dd>

    <dt id="attachment">Attachment</dt>
    <dd>In PiMS, Files can be uploaded and attached to most records (e.g. Targets, Constructs, Protocols, 
    Samples and Experiments). When this is possible, the page will contain a <a href="#box">Box</a> labelled 
    <strong>Attachments</strong>.
    </dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>

    <dt id="barcode"><a name="B"></a>Barcode</dt>
    <dd>Barcodes can be used as identifiers for various items. Provided that a Barcode is unique it can be used to 
    identify or name items in PiMS. If a barcode is used to identify a <a href="#plateExperiment">Plate Experiment</a> in PiMS, it can be 
    searched for from the PiMS Homepage.
    <%-- 
    PIMS will generate barcodes to use as unique identifiers for samples, 
    reagents etc., which can be affixed to sample containers.<br />
    A scanning device (barcode reader), linked to PIMS, will enable the user to 
    identify the sample and hence simplify the completion of a data-input form.
    <br />
    If a  sample or reagent arrives in the laboratory with an existing bar code 
    (the supplier's product code), this should be replaced with a PIMS barcode 
    which will identify the sample as unique.<br />
    However, where a supplier's barcode is a unique identifier e.g. a barcode on a 
    certain types of 96-well plates or on a plate of user-defined PCR primers, 
    this barcode can be used.--%></dd>
    
    <dt id="blastSearch">Blast searches</dt>
    <dd>Blast (Basic Local Alignment Search Tool) searches of the PDB (Protein Structure sequence database) and TargetDB (Structural Genomics 
    Targets database) can be performed for PiMS Targets.<br />
    For DNA Targets PiMS performs Blast searches against the EMBL nucleotide sequence database.<br />
    Blast results are presented in a summary table containing links to the individual 'Hit' records and sequence 
    alignments.<br />
    PiMS Blast searches are available via web-service access to the EBI-hosted NCBIBlast program.</dd>
    
    <dt id="box">Box</dt>
    <dd>PiMS records (Targets, Constructs, Experiments etc.) are displayed in Pages or Views which contain a series of labelled Boxes.  
    When you visit a page in PiMS one Box (often labelled &quot;Basic details&quot;) is open.
    The remainder are closed and appear as horizontal blue bands.  A closed Box can be opened to reveal its contents by clicking on the left 
    hand end.  The number of Boxes varies for different types of records.  Most PiMS pages contain Boxes labelled <a href="#images">Images</a>, 
    <a href="#attachments">Attachments</a> and <a href="#notes">Notes</a></dd>

    <dt id="brick">Brick</dt>
    <dd>The PiMS Homepage contains a number of labelled <a href="#boxes">Boxes</a> called Bricks.
    These are similar to Boxes but are smaller and are fixed open. Bricks provide shortcuts to some PiMS functions and to 
    some recently viewed records.
    As you use PiMS the contents of The &quot;History&quot; and &quot;My Targets&quot; Bricks will be updated.<br />
    Bricks are also found on <a href="#functionPages">Functions</a> pages to group related operations together.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>

<%--IS THIS USED?    <dt id="citation"><a name="C"></a>Citation</dt>
    <dd>In PIMS a citation is a record of the details which describe a literature 
    reference such as a book, conference, journal or thesis.  
    The details might include a title, author(s), year of publication, page numbers, 
    PubMed Id., etc.  Some details might be specific to the type of citation, 
    for example a book citation might also include the publisher and ISBN.  
    Where the literature reference is stored electronically, a link to the file 
    can also be recorded with the citation.</dd>
--%>    

    <dt id="calendar"><a name="C"></a>Calendar</dt>
    <dd>A Calendar is displayed on the majority of the pages in PiMS with the current date highlighted in yellow.<br />
    Clicking on a day in the Calendar allows you to see the information recorded in PiMS on that day -The Day View.<br />
    You can also view the information recorded for an entire Week.</dd>    
    
    <dt id="cloningVector">Cloning Vector</dt>
    <dd>A <a href="#vector">Vector</a> which is suitable for making an <a href="#entryClone">Entry Clone</a>.</dd>    

    <dt id="chort">Cohort</dt>
    <dd>24 or 96 constructs in a plate.</dd>
    <dd>A group of targets which the laboratory is working on in parallel.</dd>
    <dd>Used to manage a high throughput laboratory. PiMS can report on the progress of a cohort.</dd>

    <dt id="complex">Complex</dt>
    <dd>PiMS Complexes are intended to represent biological complexes. 
    Thus, Complexes are created from two (or more) PiMS <a href="#target">Targets</a>. Consequently, PiMS considers work on any relevant 
    <a href="#construct">Construct</a> to be part of work toward a biological Complex.</dd>
    
    <dt id="construct">Construct</dt>
    <dd>In the laboratory, a Construct is a <a href="#plasmid">Plasmid</a> created from fragments, typically a plasmid or vector 
    with one or more <a href="#insert">Inserts</a> of cloned target DNA.  
    Constructs are either <a href="#entryClone">Entry Clones</a> or <a href="#expressionClone">Expression Clones</a>.
    The Insert is often generated by 
    the polymerase chain reaction (PCR) but can also be produced by restriction enzyme digestion of a larger piece of DNA.</dd>
    <dd>In PiMS a Construct is linked to a <a href="#target">Target</a> and the Construct details are stored as a 
    <a href="#constructDesign">Construct design</a> Experiment.<br />
    Information recorded for Constructs in PiMS can include details of PCR primers, the predicted <a href="#pcrProduct">PCR product</a> or 
    <a href="#insert">Insert</a>, <a href="#proteinTag">protein tags</a>, the expression product -<a href="#expressedProtein">Expressed protein</a> 
    (if appropriate) and its relationship to the <a href="#target">Target</a> (domain, full length etc.).<br />
    In addition, PiMS calculates values for relevant DNA and protein sequences: the length and <a href="#percentGC">&#037;GC</a> for DNA sequences
    and the length, wieght(Da), <a href="#extinctionCoeff">Extinction coefficient</a> and <a href="#pI">Isoelectric point</a> for proteins.</dd>
    <dd>PiMS also supports recording <a href="#primerless">Primerless</a> Constructs such as those created using traditional cloning methods or 
    with synthetic genes.<br />
    Support for <a href="#sdm">SDM:</a> -site-directed mutagenesis is also provided which results in a &quot;mutated&quot; Construct
    and Primers.</dd>
    
    <dt id="constructDesign">Construct design</dt>
    <dd>PiMS Construct design is a series of steps to record the details for a <a href="#construct">Construct</a>.<br />
    The steps involve the definition of the region of the <a href="#target">Target</a> to be cloned (start and end amino acids) 
    and may include a <a href="#primerDesign">Primer design</a> step with the selection of suitable PCR Primers (calculated by PiMS), appropriate 
    <a href="#extension">5'-extensions</a> and <a href="#proteinTag">protein tags</a>.</dd>
    <dd>Construct design is a special type of <a href="#experiment">Experiment</a> with no <a href="#inputSample">Input Samples</a> and 
    3 <a href="#outputSample">Output Samples</a>. These represent the <a href="#forwardPrimer">Forward</a> and <a href="#reversePrimer">Reverse</a> 
    Primers and the <a href="#template">Template</a> -the selected region of the Target DNA sequence.</dd>

    <dt id="container">Container</dt>
    <dd>A <a href="#sample">Sample</a> or reagent will be contained in a Container e.g. a tube, plate, pin 
    etc.<br />
    A Container may also hold other Containers e.g. a rack of plates.<br />
    Commonly used Containers are part of the <a href="#refData">Reference data</a> which is provided when you install PiMS.<br />
    You can add extra Containers to suit your normal working practice.</dd>    

    <dt id="contextMenu">Context menu</dt>
    <dd>PiMS records displayed on the Home page and in lists are labelled with a black triangle <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/pulldown.gif"/><br />
    This is a context menu icon.<br />
    Clicking this icon will display a short menu of actions you can perform with the record.<br />
    These include links to 
    <img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/view.gif" alt="View icon" /> View and
    <img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/delete.gif" alt="Delete icon" /> Delete the record.
    </dd>
        
    <dt id="creator">Creator</dt>
    <dd>The person e.g. the Scientist, who is responsible for, or who 
    &quot;created&quot; a particular item in PIMS.  Typically relates to 
    Experiments, Protocols and Targets. Usually displayed as the &quot;Scientist&quot; on View pages.</dd>

<%--?NOT A PIMS TERM    <dt id="crystallisationSpace">Crystallisation space</dt>
    <dd>The physico-chemical and protein-specific parameters which define the 
    conditions in a crystallisation well<br />may include:
    <ul>
     <li>pH</li>
     <li>temperature</li>
     <li>concentration of various salts, precipitants, buffers, additives etc.</li>
     <li>drop size</li>
     <li>protein concentration</li>
     <li>presence/absence of co-factors or ligands</li>
    </ul>

    These can be thought of as the co-ordinates in a multi-dimensional space.
    <br />
    In a typical crystallisation experiment, the number of dimensions will 
    be approximately 100.<br />
    The probability of a crystallisation well yielding crystals, for a particular 
    protein will vary with movement through this space. </dd>
--%>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>

    <dt id="dbName"><a name="D"></a>Database name</dt>
    <dd>PiMS provides details of commonly used Databases which can be used to create links to records in remote databases.
    These <a href="#extDbLink">External Database Links</a> can be recorded for <a href="#target">Target</a> and 
    <a href="#experiment">Experiment</a> records and are created automatically for new Targets created by download.</dd>

    <dt id="diagram">Diagram</dt>
    <dd>Diagrams are interactive <strong>Graphical</strong> views of the relationships between certain records and can be 
    used as a form of navigation.<br />
    Each major PiMS entity is represented by a different shape and arrows show how entities relate to each other.
    <br /> PiMS Diagrams can be accessed by clicking the  
            <pimsWidget:linkWithIcon 
                icon="actions/viewdiagram.gif" 
                url="JavaScript:void(0)"
                text="Diagram" />link on a <a href="#construct">Construct</a>, <a href="#experiment">Experiment</a>, <a href="#sample">Sample</a> or 
    <a href="#target">Target</a> page.</dd>
    <dd>Diagrams are interactive: clicking a shape goes to the page that describes it in detail.</dd>
    <dd>The Diagrams for Experiments and Samples are also referred to as <a href="#workflow">Workflow</a> diagrams and 
    illustrate how Samples feed into Experiments which in turn produce more Samples.</dd>
   
    <dt id="entryClone">Entry Clone</dt>
    <dd>A <a href="#construct">Construct</a> which was not designed for transformation or transfection, 
    but which will be used for subcloning with an <a href="#expressionVector">Expression Vector</a>,
    in order to make an <a href="#expressionClone">Expression Clone</a>.</dd>

    
    <dt id="experiment"><a name="E"></a>Experiment</dt>
    <dd>A record in PiMS of the details of an Experiment.<br />
    PiMS can record &quot;Single&quot; or individual Experiments, <a href="#experimentGroup">Groups</a> of Experiments 
    and Experiments in <a href="#plateExperiment">Plates</a>.</dd>
    <dd>PiMS Experiments use a <a href="#protocol">Protocol</a> (Experiment template) to define the <a href="#expType">Experiment type</a>, 
    the <a href="#inputSample">Input</a> and <a href="#outputSample">Output</a> Samples. Other experimental details you might need to record 
    are defined as <a href="#parameter">Parameters</a>.</dd>

    <dt id="experimentGroup">Experiment Group</dt>
    <dd>A mechanism for recording and displaying a group of <a href="#experiment">Experiments</a> simultaneously in PiMS.<br />
    All Experiments in the group use the same <a href="#protocol">Protocol</a></dd>

    <dt id="expType">Experiment type</dt>
    <dd>PiMS uses an Experiment type to group together related experiments. The Experiment type is defined in the 
    <a href="#protocol">Protocol</a><br />
    This allows Experiments in PiMS to be searched for by Protocol or by Experiment type.</dd>
    <dd>A list of Experiment types relevant to Molecular biology and protein production labs is provided as part of the 
    PiMS <a href="#refData">Reference data</a></dd>
        
    <dt id="expertPerspective">Expert perspective</dt>
    <dd>One of the <a href="#pimsperspective">Perspectives</a> in PiMS for changing the menu items available and the way some of the records in PiMS 
    are displayed.<br />
    The expert perspective is only available to the PiMS <a href="#administrator">Administrator</a> and can be used
    to recover from some problems.</dd>
    
    <dt id="expressedProtein">Expressed protein</dt>
    <dd>The sequence of one of the proteins recorded as part of a <a href="#construct">Construct</a> in PiMS.</dd>
    <dd>Represents the translated region of the <a href="#target">Target</a> DNA sequence, as defined by the start
    and end positions during <a href="#constructDesign">
    Construct design</a>  in PiMS, plus any added <a href="#proteinTag">Protein tags</a>.</dd>
    
    
    
    <dt id="expressionClone">Expression Clone</dt>
    <dd>A <a href="#construct">Construct</a> which is suitable for transformation or transfection of a competent cell, 
    so producing cells that can express the <a href="#target">Target</a> protein(s).</dd>

    
    <dt id="expressionVector">Expression Vector</dt>
    <dd>A <a href="#vector">Vector</a> which is suitable for making an <a href="#expressionClone">Expression Clone</a>,
    usually one containing one or more promoter sequences and antibiotic resistance genes.</dd>

    
    
    
    <dt id="extension">Extension</dt>
    <dd>DNA sequence additions to the 5'-end of a <a href="#forwardPrimer">Forward</a> or <a href="#reversePrimer">Reverse</a> 
    Primer.<br />
    PiMS provides a list of Extensions which can be used during <a href="#constructDesign">Construct design</a> but
    you can record your own.<br />
    Extensions can include details of related <a href="#proteinTag">Protein tags</a> which are automatically added to the 
    <a href="#expressedProtein">Expressed</a> and <a href="#finalProtein">Final</a> proteins</dd>

    <dt id="extDbLink">External Database Link (formerly DbRef -Database reference</dt>
    <dd>In PIMS a reference to information stored in a remote database is called an External Database Link.</dd>
    <dd>When you record a New <a href="#target">Target</a> in PiMS by down-loading the details, PiMS will create External Database links
    corresponding to any cross references in the record. These will be displayed in the External Database links <a href="#box">Box</a>
    on the Target page.</dd>
    <dd>You can also record new External Database links for Targets and <a href="#target">Experiments</a>
    in PiMS. For example, you may wish to create a link to experimental data stored on a remote server.<br />
    To do this you need a URL for the record.  You can also add a unique identifier for the record and a <a href="#dbName">Database name</a> 
    (PiMS provides a list of Database names as part of the <a href="#refData">Reference data</a> but you can add to this).<br />
    PiMS will then create a link to record in the remote database from the Target or Experiment.</dd>

    <dt id="extinctionCoeff"> Extinction coefficient</dt>
    <dd>The extinction coefficient (E) of a protein is an indicator of how  much light it absorbs at a particular wavelength.<br /> 
    The molar extinction coefficient is a measure of the amount of light absorbed by a 1M solution in a pathlength of 1cm at a given wavelength.
    <br />This is a useful parameter for determining the concentration of a protein  for example, during its purification.</dd>
    <dd>The (molar) extinction coefficient of a protein can be estimated from a knowledge of its amino acid composition.  The calculation is based on the 
    number of tyrosine, tryptophan and cystine residues it contains using the known molar extinction coefficients of these residues.<br />
    PiMS uses the following formula to calculate the Extinction coefficient (E) for a protein:<br /> 
    <br />
    E (protein) = no. Tyr x E (Tyr) + no. Trp x E(Trp) + no. Cys x E(Cys)<br />
    The absorbance of the protein can then be calclated: 
    Absorbance(protein) = E(protein) / Molecular weight</dd>

    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>

    <dt id="finalProtein">Final protein<a name="F"></a></dt>
    <dd>The sequence of one of the proteins recorded as part of a <a href="#construct">Construct</a> in PiMS.</dd>
    <dd>As for the <a href="#expressedProtein">Expressed protein</a> but also defines any processing (e.g. cleavage) of 
    <a href="#proteinTag">Protein tags</a>.</dd>
    
    <dt id="forwardPrimer">Forward Primer</dt>
    <dd>a.k.a. the Sense primer. A short single stranded DNA fragment designed to anneal to a larger complementary (antisense) 
    sequence to initiate DNA synthesis during PCR.<br /></dd>
    <dd>The sequence of suitable Forward and <a href="#reversePrimer">Reverse</a> primers are calculated as part of 
    <a href="#constructDesign">Construct design</a> in PiMS.<br />
    These can be modified by the addition of <a href="#extension">Extensions</a> to the 5'-end of the Primer sequence.<br />
    PiMS also creates a Forward Primer <a href="#sample">Sample</a> which can be used as an <a href="#inputSample">Input Sample</a> in
    a suitable <a href="#experiment">Experiment</a>.</dd>
    
    <dt id="functionPages">Functions pages</dt>
    <dd>Functions pages are accessed by clicking the "More..." item in PiMS a <a href="#pimsmenu">Menu</a>.<br />
    They contain the links which are listed in the appropriate Menu, along with additional links to Search for and Create New related records.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>

    <%--?NOT PIMS TERMS
    <dt id="GMO"><a name="G"></a>GMO</dt>
    <dd>Genetically Modified Organism. A target protein is produced by adding the 
    gene for it to a suitable organism, often E.Coli.</dd>

    
    --%> 
    
    <dt id="percentGC"><a name="G"></a>&#037;GC</dt>
    <dd>The percentage of G and C nucleotides in a DNA sequence<br />
    Used in the calculation of Primers for a <a href="#construct">Construct</a> in PiMS.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>
    
    <dt id="holder"><a name="H"></a>Holder</dt>
    <dd>See <a href="#container">Container</a></dd>    

    <%--?NOT PIMS TERMS
    <dt id="homologousSequence">Homologous sequence</dt>
    <dd>Protein, or nucleotide sequences are likely to be homologous if they show
    a &quot;significant&quot; level of sequence similarity.  Truely homologous
    sequences are related by divergence from a common ancestor gene.  Sequence
    homologues can be of two types:<br />
    (i) where homologues exist in different species they are known as orthologues.
    e.g. the &alpha;-globin genes in mouse and human are orthologues.<br />
    (ii) paralogues are homologous genes in within a single species. e.g. the
    &alpha;- and &beta;- globin genes in mouse are paralogues</dd>
    --%>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>
    
    <dt id="image"><a name="I"></a>Image</dt>
    <dd>Most of the records in PiMS have a <a href="#box">Box</a> labelled Images which is to allow you to 
    upload and associate Image files (.jpg, .png etc.) with the record.<br />
    A thumbnail version of each uploaded image is displayed in the box, as a clickable link to the full size image.<br />
    You can also record a legend to display alongside the image.</dd>

    <dt id="inputSample">Input Sample (Input)</dt>
    <dd>A material which is utilised in an experiment, may be one of a list of materials or ingredients.</dd>
    <dd>In PiMS the Input Samples for an <a href="#experiment">Experiment</a> are defined in a <a href="#protocol">Protocol</a> and 
    have one or more <a href="#sampleCategory">Sample categories</a> or Types e.g. Buffer, Cell, Plasmid etc.<br /> 
    An Input Sample may be a reagent or an <a href="#outputSample">Output Sample</a> from a previous Experiment.<br />
    Experiments can be linked together if an Output Sample from the first Experiment has the same Sample category 
    as the Input Sample for the next Experiment.</dd>
   
    <dt id="insert">Insert</dt>
    <dd>A fragment of DNA, such as the product from a PCR reaction or one of the 
    products from a restriction enzyme digest, which is &#39;inserted&#39; into a 
    <a href="#vector">Vector</a>.</dd>
    <dd>PiMS records an Insert as part of the <a href="#constructDesign">Construct design</a>. It represents the region of the 
    <a href="#target">Target</a> DNA sequence defined by the selected start and end positions.  If the Construct design included the 
    <a href="#primerDesign">Primer design</a> step, then any added <a href="#extension">5'-Extensions</a> will also be included in 
    the Insert sequence to represent the predicted <a href="#pcrProduct">PCR product</a>.</dd>
   
     
    <dt id="instrument">Instrument</dt>
    <dd>Details of the type(s) of laboratory equipment required for a protocol.  
    Can include name, model, serial number etc.</dd>
    
    <dt id="pI">Isoelectric point</dt>
    <dd>The Isoelectric point or pI of a protein is the pH at which the net charge 
    of the protein is neutral.  i.e. the protein carries no net charge since it has 
    an equal number of positive and negative charges.  At pH values greater than the 
    pI, a protein will carry a net negative charge.  Similarly, at pH values less 
    than the pI, a protein will carry a net positive charge.
    <br />Knowlede of a protein's pI is important for purification since it is the 
    pH at which the protein will be at its least soluble.<br />
    The pI of a protein can be determined experimentally by isoelectric focussing 
    (IEF) but the value obtained is often inaccurate since it is influenced by post 
    translational modifications of the protein.</dd>
    <dd>The theoretical pI can be calculated from the amino acid sequence of the protein, 
    and there are a number of websites which provide this service.  For example, 
    see ExPASy's <a href="http://ca.expasy.org/tools/pi_tool.html">Compute pI/Mw</a> 
    tool.<br />
    PiMS uses 
    <a href="http://bioinformatics.oxfordjournals.org/cgi/content/abstract/btn397v1?ijkey=jIKd6VUGPrgshbv&amp;keytype=ref">BioJava</a>
    to calculate this value.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>

<dd><a name="J"></a></dd>

<dd><a name="K"></a></dd>

    <dt id="labNotebook"><a name="L"></a>Lab Notebook </dt>
    <dd>All laboratory information recorded in PiMS (<a href="#target">Targets</a>, <a href="#experiment">Experiments</a>, 
    <a href="#sample">Samples</a> etc.) belongs to a specific Lab Notebook.<br />
    This forms the basis for the control of access to data and data sharing in PiMS: the individual <a href="#user">User's</a> 
    ability (<a href="#permission">Permission</a>) to record, edit and view information in PiMS.</dd>
    <dd>Lab Notebooks can be used to organise your laboratory information. e.g. A Lab notebook might correspond to a particular 
    grant or a collaboration.</dd>
    <dd>The PiMS <a href="#administrator">Administrator</a> is able to create new Lab Notebooks and can specify which PiMS Users 
    have access to the information.<br />
    Most items recorded in PiMS can be thought of as pages of a Lab notebook. </dd>

    <dt id="LIMS">LIMS</dt>
    <dd>Laboratory Information Management System</dd>
    
    <dt id="location">Location</dt>
    <dd>See <a href="#container">Container</a></dd>

    <%--NO LONGER USED
    <dt id="molcomp"><a name="M"></a>Molecular Component</dt>
    <dd>PIMS defines a pure component with a known chemical structure, such as 
    'Sodium chloride', as a Molecular Component.<br />
    Restriction enzymes, constructs and primers are also examples of Molecular 
    Components with additional characteristics.<br />
    Molecular Components are distinct from other components classified as 
    'Substance' 'Cell' and 'Composite'.</dd>
   --%>
   
    <dt id="pimsmenu"><a name="M"></a>Menu</dt>
    <dd>Drop down list of navigation links to some of the most commonly used functions of PiMS<br />
    Accessed by mousing over the <a href="#menubar">Menubar</a> at the top of the pages in PiMS.</dd>
    
    <dt id="menubar">Menubar</dt>
    <dd>The horizontal blue bar at the top of all pages in PiMS.<br />
    The Menubar contains a link to the PiMS &quot;Home&quot; page, the &quot;Log in&quot; link, a number of drop-down <a href="#pimsmenu">Menus</a> 
    for navigating around PiMS, a link to the PiMS <a href="#quickSearch">Quick Search</a> and a drop-down list of <a href="#pimsperspective">Perspectives</a>.</dd>
    <dd>The top Menu item in each Menu is a link to the relevant <a href="#functionPages">Functions page</a>. 
    Changing the perspective changes the Menu items available.</dd>
    
    <dt id="milestone">Milestone</dt>
    <dd>In PiMS a Milestone can be set when the success of certain <a href="#experiment">Experiments</a> are recorded.</dd>  
    <dd>Milestones relate to <a href="#targetStatus">Target statuses</a> and when a Milestone is achieved the status of the relevant 
    <a href="#target">Target</a> is updated and it is said to have progressed along the protein production 
    and structure determination process (or pipeline).</dd>
    <dd>The <a href="#diagram">Diagram</a> for a Target or <a href="#construct">Construct</a> displays any Milestones which have been achieved.</dd>
    
    <dt id="molecule">Molecule</dt>
    <dd>In PIMS the term molecule is used to describe any type of pure component with 
    a known chemical structure.  This includes chemicals, DNA, RNA and proteins.</dd>
   
    <dd>A <a href="#target">Target</a> is usually associated with one protein and one DNA Molecule.<br />
    A <a href="#construct">Construct</a> may be associated with several Molecules to represent the <a href="#forwardPrimer">Forward</a> 
    and <a href="#reversePrimer">Reverse</a> primers, the <a href="#insert">Insert</a> and the <a href="#expressedProtein">Expressed</a>
    and <a href="#finalProtein">Final</a> proteins.</dd>
    <dd>Chemicals and <a href="#extension">Extensions</a> are also recorded as Molecules in PiMS.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>

    <dt id="notes"><a name="N"></a>Notes</dt>
    <dd>Most records in PiMS have a <a href="#box">Box</a> labelled &quot;Notes&quot; towards the bottom of the page.<br />
    This allows you to add extra details to the record as you might in a paper lab book. Each Note is dated and the labelled with the 
    author's <a href="#username">Username</a><br />
    A note cannot be removed from a PiMS record and so could be used as an audit trail.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>

    <%--NOT A PIMS TERM YET
    <dt id="optimisation"><a name="O"></a>Optimisation</dt>
    <dd>A further <a href="#screening">screening experiment</a>, after an initial 
    screening has indicated the approximate conditions for success.
    The <a href="#screen">screen</a> used for optimisation will be customised
    in the light of the results of the screening experiment.
    How best to do this is a topic for research.
    Analysis of the records of past experiments (data mining) will facilitate this 
    research.</dd>
    --%>
    
    <dt id="outputSample"><a name="O"></a>Output Sample</dt>
    <dd>An <a href="#experiment">Experiment</a> may or may not produce one or more Output Samples.</dd>
    <dd>A material which is produced by an  in an Experiment.</dd>
    <dd>In PiMS the Output Samples for an Experiment are defined in a <a href="#protocol">Protocol</a> and 
    have one or more <a href="#sampleCategory">Sample categories</a> or Types e.g. Buffer, Cell, Plasmid etc.<br /> 
    Experiments can be linked together if an Output Sample from the first Experiment has the same Sample category 
    as the Input Sample for the next Experiment.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd> 
    
    

    <dt id="parameter"><a name="P"></a>Parameter</dt>
    <dd>Parameters are used in <a href="#protocol">Protcols</a> to describe any experimental details you wish to 
    record in <a href="#experiment">Experiments</a> which are not <a href="#inputSample">Input</a> or <a href="#outputSamples">Output</a> 
    Samples. You can define any number of Parameters in a Protocol</dd>
    <dd>Examples of the use of Parameters might include possible options for the set up of an experiment, e.g. 
    whether or not a culture is to be shaken, or details which describe an experimental result.</dd>      
    <dd>Parameters can be used to describe numerical or text values, a list of possible values or checkbox for True/False values.<br />
    You can define 3 types of Parameters in your Protocol: Set up Parameters e.g. incubation details, Result Parameters e.g. 
    a yield from a purification and Group level Parameters which are used to define an experimental detail for a group of Experiments 
    performed simultaneously e.g. the thermocycling conditions for a PCR performed in a <a href="#plateExperiment">Plate</a> experiment.</dd>
    <dd>A special type of Parmaeter a <a href="#scoreParameter">Score parameter</a> can be used to provide an overview of the success of a 
    Plate Experiment.</dd> 
    
    <dt id="pcrPrduct"></dt>
    <dd>The output from a PCR or Polymerase Chain reaction <a href="#experiment">Experiment.</a></dd>
    <dd>PiMS records the predicted PCR product when you record a <a href="#construct">Construct</a> which includes the <a href="#primerDesign">
    Primer design</a> step.</dd>
    <dd>When you record a PCR <a href="#experiment">Experiment</a> linked to a Construct, the size of the predicted PCR product 
    is displayed in the Experiment record.</dd>
    
    <dt id="permission">Permission</dt>
    <dd>An access permission is the right for the members of a User group to perform a certain operation on the laboratoty
    information belonging to a particular <a href="#labNotebook">Lab notebook</a>.<br />
    The possible operations are to: view, update, delete or create the information.</dd>

    <dt id="pimsperspective">Perspective</dt>
    <dd>At the top right hand corner of most PiMS pages, there is a drop down list of Perspectives.<br />
    By changing the perspective, you can change the menu items available to you, and also change the way 
    some of the records in PiMS are displayed.</dd>
    
    <dt id="plate">Plate (la plaque)</dt>
    <dd>A holder with multiple wells (usually 24, 96, or 384), each capable of containing a sample.</dd>
    
    <dt id="plateExperiment">Plate Experiment</dt>
    <dd>PiMS allows you to record the details for <a href="#experiment">Experiments</a> in <a href="#plate">Plates</a>, up to 
    4 Plates can be recorded as a group.</dd>
    <dd>The display of a Plate Experiment in PiMS includes a &quot;Plate view&quot; representing the wells of the plate arranged in rows
    and columns to simplify data input.<br />
    Each well in the Plate is recorded as an individual Experiment in PiMS but the details for the wells can be updated as a Plate, 
    as a subset of wells in the Plate or as individual wells</dd>
    <dd>You can create a Plate Experiment in PiMS by uploading the details from a spreadsheet and you can export the Plate Experiment 
    to a spreadsheet for use in other applications.</dd>   
    
     

    <dt id="plasmid">Plasmid</dt>
    <dd>Circular DNA, either a <a href="#vector">Vector</a> which does not yet contain <a href="#target">Target</a> DNA, or a <a href="#construct">Construct</a>, which does.</dd>

    <dt id="primerDesign">Primer design</dt>
    <dd>One of the steps in the standard <a href="#constructDesign">Construct design</a> procedure in PiMS is the calculation of suitable Primers 
    based on the user-specified <a href="#tm">T<sub>m</sub></a> and start and end positions.</dd>
    <dd>PiMS uses the following criteria to design Primers for a <a href="#construct">Construct</a>:<br />
     &nbsp; Primers must be within 5<sup>o</sup>C of the user-specified T<sub>m</sub><br />
     &nbsp; 50 nucleotides or less in length<br />
     &nbsp; End with no more than 2 Gs or Cs</dd>
     
    <dt id="primerless">Primerless Construct</dt>
    <dd>PiMS allows you to design a <a href="#construct">Construct</a> which does not include the <a href="#primerDesign">Primer design</a> 
    step.<br />
    This is mechanism to allow you to record the details of Constructs which are made without the use of PCR and synthetic genes.</dd>
    <dd>The Primerless Construct is linked to a <a href="#target">Target</a> and the Construct details are stored as a 
    <a href="#constructDesign">Construct design</a> Experiment.<br />
    Information recorded for Primerless Constructs in PiMS includes details of the <a href="#insert">Insert</a>,  
    the expression product -<a href="#expressedProtein">Expressed protein</a> and its relationship to the <a href="#target">Target</a>
    (domain, full length etc.).<br />
    In addition, PiMS calculates values for relevant DNA and protein sequences: the length and <a href="#percentGC">&#037;GC</a> for the Insert,
    and the length, wieght(Da), <a href="#extinctionCoeff">Extinction coefficient</a> and <a href="#pI">Isoelectric point</a> for proteins.</dd>

    <dt id="proteinTag">Protein tags</dt>
    <dd><a href="#vector">Vector</a> or Primer-encoded Protein sequences which are fused to the N- or C-terminus of an
    expression product.</dd>
    <dd>In PiMS Protein tags can be added to the sequence of the <a href="#expressedProtein">Expressed</a> or <a href="#finalProtein">Final</a> 
    protein sequence during or after <a href="#constructDesign">Construct design</a></dd>
    <dd>A Protein tag sequence can be recorded in association with a <a href="#extension">5'-Extension</a> in PiMS.<br />
    The sequence will then be added automatically to the relevant terminus of the <a href="#expressedProtein">Expressed</a> or 
    <a href="#finalProtein">Final</a> protein when the Extension is selected during Construct design.</dd>
        
    <%--NOT PIMS TERMS
    <dt id="proteinSpecies">Protein species</dt>
    <dd>A term used to describe a protein or proteins of unknown function.  For 
    example a protein(s) identified only as a band(s) on a gel.  Also used to 
    describe the existence of previously undetected variants of a protein, such as 
    alternative forms with different or post-translational modifications.</dd>
--%> 
    <dt id="polycist">Polycistronic</dt>
    <dd>Clusters of bacterial genes encoding proteins which are part of the same 
    metabolic pathway are often arranged in tandem along the chromosome.  They are 
    transcribed as a unit from a single promoter resulting in the production of a 
    single &quot;polycistronic&quot; messenger RNA (mRNA) which is subsequently 
    translated into the proteins.  An example is the E.coli Lac operon which 
    consists of 3 genes required for the metabolism of galactose.<br />
    By contrast the majority of eukaryotic mRNAs are &quot;monocistronic&quot; and 
    are translated only into a single polypeptide chain or protein.  However, the 
    chloroplasts of plants contain polycistronic mRNAs.</dd>

    <dt id="polycistcon">Polycistronic construct</dt>
    <dd>See <a href="#construct">construct</a>.  A construct for the co-expression 
    of multiple DNA fragments or genes.  It can be used to express several 
    recombinant proteins under the control of a single promoter.  Analogous to a 
    bacterial operon.  This approach may be necessary in order to produce a soluble 
    protein complex.</dd>
       
    <dt id="protocol">Protocol (le protocole)</dt>
    <dd>A laboratory method, Standard Operating Procedure, or protocol.</dd>
    <dd>In PIMS, a Protocol is a user-definable, reusable <a href="#experiment">Experiment</a> template.<br />
    A Protocol allows you to define exactly what information you record for your Experiments and how they can be 
    linked together to create a <a href="#workflow">Workflow</a>.
    A Protocol is used to define the <a href="#expType">Experiment type</a> and the number and types of 
    <a href="#inputSample">Input</a> and <a href="#outputSample">Ouput</a> Samples your Experiments will have. 
    A Protocol is also used to define <a href="#parameters">Parameters</a> -experimental details and results you would 
    like to (or need to) record.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd> 

    <dt id ="quickSearch"><a name="Q"></a>Quick Search</dt>
    <dd>You can search for laboratory information which has be recorded in PiMS by clicking a specific Search link in a <a href="#functionPages">Functions page</a> 
    or <a href="#pimsmenu">Menu</a> and entering search terms.<br />
    PiMS also provides a Quick Search facility which removes the need to locate the relevant search link.<br />
    The Quick Search is accessed either on the Home page in the &quot;Quick Search&quot; <a href="#brick">Brick</a> or by clicking &quot;Search&quot; in the 
    <a href="#menubar">Menubar</a>.</dd>

    <%--Not a PiMS term
    <dt id="rack">Rack (l'etagere)</dt>
    <dd>A holder capable of containing several plates.</dd>
    --%>
     <dt id="recipe"><a name="R"></a>Recipe or Reference Sample</dt>
     <dd>In PiMS a Recipe describes the details of a <a href="#sample">Sample</a>.<br />
     It can be used to define a Sample's <a href="#sampleCategory">Category(s)</a>, pH and its components 
     and their concentration.</dd>
     <dd>Supplier details and catalogue references can also be linked to a Recipe.</dd>
     <dd>The same recipe can be used to describe any number of similar Samples or Stocks.</dd>  

     <dt id="refData">Reference data</dt>
     <dd>When PIMS is installed, it contains information about Chemicals, <a href="#dbName">Database names</a>, 
     <a href="#expType">Experiment types</a>, etc.  This Reference data can be read by any 
     PIMS user, but can be updated only by the administrator.</dd>

     <%--Not PiMS TERMS See RECIPE
     <dd>Reagent information which is common to all batches of the same reagent.  
     This can include supplier and safety information and also details for linking to 
     the supplier's data sheet.  
     It also indicates the appropriate <a href="#sampleCategory">sample category</a> 
     or categories.</dd>
     <dt id="refinement">Refinement</dt>
     <dd>See <a href="#optimisation">optimisation</a>.</dd>
      --%>

     <dt id="researchObjective">Research Objective</dt>
     <dd>The term Research Objective is a description of a clearly defined 
     experimental objective i.e. what you plan to work on.</dd>
     <%--TODO NEEDS CLARIFICATION
     <dd>It describes any particular combination of one or more 'wish targets', 
     domains or fragments of 'wish targets', DNA, RNA, poly- or olio-saccharides or 
     any small molecule ligand or co-factor that you intend to study.<br />
     For example, the components of a macromolecular complex, although there will 
     often be only one component.<br />
     Each of these components is defined as a 
     <a href="#blueprintComponent">Blueprint component</a>.</dd>
     <dd>When you record the details for an experiment in PIMS, the experiment is 
     linked to the Target via an Experiment Blueprint.</dd>

     <dd>Examples of an Experiment Blueprint might be:<br />
     a protein, a protein-DNA complex, a multi-protein complex or a protein comlexed 
     with a ligand or co-factor.</dd>

     <dd>In addition, multiple Experiment Blueprints can de defined from a single 
     'wish target' illustrating the fact that different domains or fragments of the 
     'wish target' may be studied in various contexts.<br /><br />
     For example: a given protein may have more than one distinct domains such as a 
     kinase with catalytic and regulatory domains.<br />
     You may intend to study different aspects of the protein and so separate 
     Experiment Blueprints might be:
     <ul>
     <li>the full length protein</li>
     <li>the kinase domain</li>
     <li>the regulatory domain</li>
     <li>a complex between the two domains</li>
     <li>a complex including a substrate</li>
     </ul></dd>
     <dd>In addition, an Experiment Blueprint might contain Blueprint components 
     defined from separate Experiment Blueprints.<br />
     For example: an Experiment Blueprint, called AB1, describes an oligomeric 
     protein AB with two subunits A and B.<br />
     This Experiment Blueprint will, therefore contain two Blueprint components A' 
     and B', defined from thier respective 'wish targets' A and B.<br />
     A different Experiment Blueprint, AB2, describes the AB complex with a 
     co-factor C bound.<br />
     In this case, there might be three Blueprint components A', B' and C' where C' 
     is defined from a separate Experiment Blueprint C.</dd>

     <dt id="researchObjectiveElement">Research Objective Element</dt>
     <dd>A 'wish target', domain or fragment of a 'wish target', DNA, RNA, poly- or 
     olio-saccharides or a small molecule ligand or co-factor.<br />
     Part of an <a href="#expBlueprint">Experiment Blueprint</a></dd>
     --%>
     <%--NOT PIMS TERMS    
     <dt id="researchTarget">Research target</dt>
     <dd>A protein, protein complex or other molecular species whose function and/or 
     structure is under investigation in a research environment.</dd>
    
     <dt id="result">Result</dt>
     <dd>An experimental "result", examples include a value, set of values or a gel 
     image, all of which provide information to the scientist.</dd>
     --%>
    <dt id="reversePrimer">Reverse Primer</dt>
    <dd>a.k.a. the antisense primer. A short single stranded DNA fragment designed to anneal to a larger complementary (sense) 
    sequence to initiate DNA synthesis during PCR.<br /></dd>
    <dd>The sequence of suitable Reverse and <a href="#forwardPrimer">Forward</a> primers are calculated as part of 
    <a href="#constructDesign">Construct design</a> in PiMS.<br />
    These can be modified by the addition of <a href="#extension">Extensions</a> to the 5'-end of the Primer sequence.<br />
    PiMS also creates a Reverse Primer <a href="#sample">Sample</a> which can be used as an <a href="#inputSample">Input Sample</a> in
    a suitable <a href="#experiment">Experiment</a>.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd> 

    <dt id="sample"><a name="S"></a>Sample</dt>
    <dd>A physical sample in a laboratory which may be produced by an experiment, e.g. the contents of an Eppendorf.</dd>
    <dd>A prep.</dd>
    <dd>An aliquot.</dd>
    
    <dd>In PiMS a Sample may conform to a <a href="#recipe">Recipe</a> which can be used to define its 
    <a href="#sampleComponent">Components</a> (des composants) and <a href="#sampleCategory">Category</a> or Type.<br />
    The <a href="#holder">Holder</a>, <a href="#location">Location</a>, amount and &quot;use by date&quot;of a Sample can also be 
    recorded in PiMS.</dd>
    <dd>PiMS <a href="#experiment">Experiments</a> &quot;utilise&quot; <a href="#inputSample">Input Samples</a> and most 
    &quot;create&quot; <a href="#outputSample">Output Samples</a>.</dd>    

    <dt id="sampleCategory">Sample category or Type</dt>
    <dd>Each <a href="#sample">Sample</a> in PiMS can belong to one or more Categories, e.g. buffer, 
    salt, detergent, antibiotic, etc.</dd>
    <dd>Sample categories are important for linking <a href="#experiment">Experiments</a> together to create 
    <a href="#workflow">Workflows</a>.</dd>
    
    <dt id="sampleComponent">Sample component</dt>
    <dd>A <a href="#sample">Sample</a> in PiMS can be made up of one or more Sample components.<br />
    For example, a PCR buffer may contain three sample components: KCl, Tris and MgCl<sub>2</sub>, each at a known, final concentration in the sample.</dd>
    <dd>The components for a Sample are defined in the recipe to which it conforms.</dd>

    <%--??NOT A PIMS TERM
    <dt id="scale-up">Scale-up</dt>
    <dd><i>v. or n.</i> To perform an experiment in bulk, after the optimal 
    conditions have been determined by a <a href="#screening">screening experiment</a>.
    </dd>
    --%>
    <dt id="scientific-goal">Scientific goal</dt>
    <dd>The Scientific goals of PiMS are the areas where PiMS can help scientists manage 
    <a href="#experiment">Experimental</a> and <a href="#target">Target</a> information.   
    For certain scientific goals, the use of PIMS might influence laboratory practice.  For example, the introduction of 
    <a href="#barcode">Barcodes</a> to facilitate <a href="#sample">Sample</a> tracking.</dd>
    <dd>A Scientific goal is what 
    <a href="http://alistair.cockburn.us/crystal/articles/sucwg/structuringucswithgoals.htm">Alistair Cockburn</a> 
    calls a strategic scope, summary goal.</dd>

    <dt id="scoreParameter">Score parameter</dt>
    <dd>PiMS supports scoring of <a href="#plateExperiment">Plate experiments</a> by means of a special type of 
    <a href="#parameters">Parameter</a> in a <a href="#protocol">Protocol</a>.<br />
    Colours and icons are associated with different values for Score parameters and are displayed in the Plate view providing 
    an overview of the success (or otherwise) of the Plate Experiment.<br />
    </dd>
    
    <dt id="screen">Screen (xtalPiMS)</dt>
    <dd>The description of a Crystallisation screen which defines the conditions in each well solution</dd>

    <dt id="sdm">SDM: Site-directed Mutagenesis</dt>
    <dd>PiMS allows you to design and record the details for a &quot;mutated&quot; <a href="#construct">Construct</a>.
    <br />Starting from an existing PiMS Construct you may introduce point mutations, insertions and deletions based on the DNA or 
    protein sequence.<br />
    PiMS will then design suitable PCR primers and record the predicted <a href="#pcrProduct">PCR product</a> or 
    <a href="#insert">Insert</a> and the expression products -<a href="#expressedProtein">Expressed</a> and 
    <a href="#finalProtein">Final</a> proteins.</dd>
    
    <dt id="sequence">Sequence</dt>
    <dd>PiMS <a href="#target">Targets</a> are usually recorded with at least one DNA or Protein sequence.
    Your Target will need a DNA sequence for <a href="#constructDesign">Construct design</a> in PiMS.</dd>
    <dd>A <a href="#construct">Construct</a> may have several Sequences associated with it including the 
    <a href="#insert">Insert</a>, the <a href="#expressedProtein">Expressed</a> and <a href="#finalProtein">Final</a> proteins
    and the <a href="#forwardPrimer">Forward</a> and <a href="#reversePrimer">Reverse</a> primers.</dd>
    <dd>PiMS calculates and displays the length and <a href="#percentGC">&#037;GC</a> for DNA sequences, and 
    and the length, wieght(Da), <a href="#extinctionCoeff">Extinction coefficient</a> and <a href="#pI">Isoelectric point</a> 
    for protein sequences.</dd>
    
    <dt id="simSearch">Similarity search</dt>
    <dd>PiMS provides a local sequence similarity search for any DNA or protein <a href="#sequence">Sequence</a>. 
    These include <a href="#target">Target</a> protein and DNA sequences, a <a href="#construct">Construct's</a>
    <a href="#expressedProtein">Expressed</a> and <a href="#finalProtein">Final</a> proteins, primer and  
    <a href="#insert">Insert</a> sequences.<br />
    The search employs the Smith-Waterman algorithm as implemented by 
    <a href="http://bioinformatics.oxfordjournals.org/cgi/content/abstract/btn397v1?ijkey=jIKd6VUGPrgshbv&amp;keytype=ref">BioJava</a></dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd> 
    
    <%--NOT PIMS TERMS
    <dt id="screening">Screening Experiment</dt>
    <dd>There are situations where the appropriate reagents and/or conditions 
    to use for a particular experiment have not been established. To resolve 
    this problem, it is necessary to perform one or more experiments 
    simultaneously, where the conditions and/or reagents are varied
    systematically in order to define a range of favourable combinations.  
    The outcome of such a primary screen will help to define a narrower range 
    of conditions for either <a href="#optimisation">optimisation</a> or 
    <a href="#scale-up ">scale-up</a>.</dd>
    
    <dt id="stockSolution">Stock solution</dt>
    <dd>A Stock solution is a solution of a laboratory reagent of known, standard concentration.
    It is typically the highest concentration of the reagent kept in the laboratory. 
    A stock solution may be purchased from a supplier or prepared in the laboratory. 
    Typically a more dilute solution, a working dilution, will be used directly in experiments.</dd> 
     
    <dt id="substance">Substance</dt>
    <dd>A substance is a component where no detailed chemical structure is 
    provided such as 'salt' or 'phosphate'. This is also a simple way to describe 
    complex mixtures, such as an extract or serum.</dd>
    --%>

    <dt id="target"><a name="T"></a>Target</dt>
    <dd>Something that is represented by a record in a genomic database.</dd>
    <dd>Or, the wildtype sequence of a gene of interest.</dd>
    <dd>Or, the wildtype product of an open reading frame.</dd>
    <dd>Biological Complexes can be represented in PiMS by linking two (or more) PiMS Targets. PiMS also provides support 
    for DNA Targets: which do not lead to protein production, and Natural Source Targets: for which no protein or 
    DNA sequences are available.</dd>

    <dt id="targetStatus">Target status</dt>
    <dd>The stage a <a href="#target">Target</a> has reached in the protein production and structure determination process (or pipeline).
    This represents the most recent successful <a href="#experiment">Experiment</a>.
    The possible statuses are similar to the &quot;Progression stages&quot; defined by 
    TargetDB:
    <blockquote>
    Selected,  PCR,  Cloned,  Expressed,  Soluble,  Small Scale expression,  Production Scale expression,  Purified,   
     In crystallization,  Crystallized,  Diffraction-quality Crystals,  Diffraction,  Native diffraction-data, 
     Phasing diffraction-data,  HSQC,  NMR Assigned,  NMR NOE,  Crystal Structure,  NMR Structure,  In PDB,  In BMRB, 
     Molecular Function,  Biological Process,  Cellular Component,  Work Stopped,  Other</blockquote></dd>
     <dd>The &quot;status&quot; of a Target is updated when a <a href="#milestone">Milestone</a> is set for a successful Experiment linked to that Target.</dd>

    <dt id="template">Template</dt>
    <dd>One of the <a href="#outputSample">Output Samples</a> from a <a href="#primerDesign">Primer design</a> Experiment.<br />
    Representing the selected region of the Target DNA sequence.</dd>

    <dt id="tm">T<sub>m</sub></dt>
    <dd>PiMS uses the following formula to calculate the T<sub>m</sub> for a DNA sequence:</dd>
    <dd>&nbsp; &nbsp; 64.9 + (41 x (nGC-16.4) / sequence length) - 650 / sequence length<br />
    &nbsp; &nbsp; where nGC = the number of G and C nucleotides in the sequence</dd>
    <dd>This is used in the <a href="#primerDesign">Primer design</a> step of PiMS <a href="#constructDesign">Construct design</a></dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd> 

    <dt id="user"><a name="U"></a>User</dt>
    <dd>Every operation performed with PIMS is part of a session which began by logging on with a specific <a href="#username">Username</a> and password.<br />
    A User can belong to one or more <a href="#userFroup">User groups</a> with specific rights or <a href="#permission">Permissions</a> to view, 
    update, create and delete the laboratory information in PiMS.</dd>
    <dd>When the <a href="#administrator">Administrator</a> creates a new <a href="#labNotebook">Lab notebook</a> he or she can also specify 
    which PiMS User groups (and hence Users) can access the information belonging to it.</dd>

    <dt id="username">Username</dt>
    <dd>Each PiMS <a href="#user">User</a> is issued with a Username and password which are required to log in to the system.</dd>
    <dd>After you have logged in to PiMS your Username will be displayed in the <a href="#menubar">Menubar</a><br />
    Usernames are also used to identify the person who created a particular record. For example, when you record a new <a href="#target">Target</a> in PiMS your 
    Username will be displayed in the &quot;Scientist&quot; field.</dd>
    
    <dt id="userGroup">User group</dt>
    <dd><a href="#user">Users</a> can belong to one or more User groups with different <a href="#permissions">Permisions</a>
    to access the laboratory information belonging to <a href="#labNotebook">Lab notebooks</a>.<br />
    For example at Alice and Bob may both be members of the &quot;Yeast&quot; User group, and are able to record  and update information
    in the &quot;Yeast&quot; Lab notebook. Alice may also be a member of the &quot;Kinase&quot; group,
    and is also able to access all information in the &quot;Kinase&quot; Lab notebook.<br />
    Professor Black is a member of all of the User groups but only has permission to view the items in each Lab notebook. 
    However, he can view all of the information in the laboratory PIMS.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd> 

    <dt id="vector"><a name="V"></a>Vector</dt>
    <dd>A <a href="#plasmid">Plasmid</a>, into which a fragment of foreign DNA can be inserted by a cloning experiment.  The inserted fragment is 
    referred to as the &#39;<a href="#insert">insert</a>&#39;.</dd>
    <dd>Vectors are either <a href="#cloningVector">Cloning Vectors</a> or <a href="#expressionVector">Expression Vectors</a>.</dd>
    <dd>PiMS provides <a href="#refData">Reference data</a> for commonly used Vectors which can be used to record Vector <a href="#sample">Samples</a>.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>

    <dt id="workflow">Workflow<a name="W"></a></dt>
    <dd>In PiMS a Workflow is constructed by linking a series of <a href="#experiment">Experiments</a> together where the 
    <a href="#outputSample">Output Sample</a> from Experiment 1 becomes the <a href="#inputSample">Input Sample</a> for Experiment 2.<br />
    This is achieved by ensuring that the Output <a href="#sampleCategory">Sample category</a> or Type from Experiment 1 matches the Input Sample 
    category for Experiment 2. Sample categories are defined in the <a href="#protocol">Protocol</a>.</dd>
    <dd>PiMS provides a Graphical view or <a href="#diagram">Diagram</a> of a Workflow created in this way which can be viewed by clicking the 
                <pimsWidget:linkWithIcon 
                icon="actions/viewdiagram.gif" 
                url="JavaScript:void(0)"
                text="Diagram" />link on a Sample or Experiment view.</dd>
    <dd><div class="toplink"><a href="#">Back to top</a></div></dd>

<dd><a name="X"></a></dd>

<dd><a name="Y"></a></dd>

<dd><a name="Z"></a></dd>

<dd><a name="Other"></a></dd>

 </dl>
<br /><br />
 </div>
 </div>
<jsp:include page="/JSP/core/Footer.jsp" />
