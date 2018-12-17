<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName"
        value='Caliper Robot Help' />
</jsp:include>

<div class="help">

<p>
PiMS can process the Peaktable results files from a Caliper robot to make a new characterisation plate.
</p><p>
Create a new folder on the computer supporting the Caliper robot.<br />
Copy the programs file fileImport_fat.jar into this folder.<br />
Make a Properties file (a sample file can be found in the PiMS project).  This will need to be edited to specify your PiMS database.<br />  
Next make a simple windows batch file in this folder.<br />
Open the file and enter the command:
</p><p>
<i>java -jar fileImport_fat.jar -r -f "C:/Program Files/Caliper/Results" > caliper.log</i>
</p><p>
the parameters are:<br />
-r 		recursive<br />
-f		the folder containing the results<br /> 
</p><p>
When the batch file is run, the program will search folders within the C:/Program Files/Caliper/Results folder (such as C:/Program Files/Caliper/Results/2010-06-18).
</p><p>
Files that are found will be matched against the pattern:
</p><p>
<i>Caliper_GX_PCR139_PCR1_2010-03-02_09-52-33_PeakTable.csv</i>
</p><p>
Caliper		is a prefix string<br />
GX		is the computer name<br />
PCR139_PCR1	is the plate barcode<br />
2010-03-02	the date<br />
09-52-33	the time<br />
PeakTable	the filetype<br />
</p><p>
files that do not conform to this rule are rejected.
</p><p>
For each file that conforms to the pattern, the Caliper results program will look for a plate experiment in PiMS with a name that matches the barcode from the results file name.  
The program needs to find this plate experiment so that it can find information about the contents of each well.
</p><p>
A PiMS plate experiment will now be created from the Caliper Characterisation protocol.  
This protocol has a result parameter (Caliper result) which will contain the score of the well.

</p><p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/caliper_characterisation_basic.jpg" alt="JUnicorn browse" /><br />
</p>

<p>
The Caliper Characterisation plate results page
</p>

<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/caliper_characterisation_result.jpg" alt="JUnicorn browse" /><br />
</p>

<p>
The scoring can be tuned using parameter in the properties file:<br />
Caliper.Result.Maybe = 10<br />
Caliper.Result.Yes = 20<br />
to score as a maybe, the pcr product length found by the caliper robot must be within the range
pcrSize +- pcrSize * Math.log10(pcrSize) / Caliper.Result.Maybe;<br />

to score as a yes, the pcr product length found by the caliper robot must be within the range
pcrSize +- pcrSize * Math.log10(pcrSize) / Caliper.Result.Yes;
</p>

<p>
Files that have been successfully processed are copied into a folder called processed so that they will not be processed again on subsequent runs of the caliper program.
</p>

<p>
You may like to run the batch command file as a scheduled task to automate the process.
</p>

</div>

<jsp:include page="/JSP/core/Footer.jsp" />
