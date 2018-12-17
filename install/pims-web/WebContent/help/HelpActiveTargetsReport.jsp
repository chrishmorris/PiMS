<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Active targets report help' />
</jsp:include>

<div class="help">
<div class="toplink"><a href="#">Back to top</a></div>

<h3>Active targets report - obsolete</h3>

<div class="textNoFloat">
The target progress report shows the list of targets for which experiments has been recently performed.   
</div>
<img class="imageNoFloat"
	src="../images/helpScreenshots/targetProgress/target_progress.png" alt="Target progress report" />
<div class="textNoFloat">
Screen-shot 1. 
<br/>Where: 
<ul>
<li><dfn>Target</dfn> - the one of the PIMS targets
<li><dfn>Milestone</dfn>  - the most recent milestone which has been achieved by this target
<li><dfn>Target creator</dfn>  - the person (scientist on the view target page) who research the target
<li><dfn>Recent Experiment</dfn>  - the most recent experiment recorded for the target
<li><dfn>Experimenter</dfn>  - the person, (scientist on the view experiment page) who record the experiment   
<li><dfn>Days since last progress</dfn>  - the number of days passed from the start date of experiment to the current date
<li><dfn>Experiment lab</dfn>  - the lab which has conducted the experiment (the lab where the person who record the experiment works)
</ul>
Here is a list containing one target for which Peter Troshin (the experimenter) recorded some experiments. 
</div>


<div class="toplink"><a href="#">Back to top</a></div>

<h3>Recording an experiment for a target</h3>
<div class="textNoFloat">

To make target appear on the active target list you need to record a new experiment for it. 
Here is an example how such experiment may look like. 
It is important to set a project field in the drop down list of experiment details page (which is shown on the screenshot)
The start date of experiment is also important, as the "days from the last progress" field will be calculated based on it. 
By default the start date will be set to a current date, which would be a good choice in the majority of cases. 
The status of experiment is not taken into account by active target report.    
</div>
<img class="imageNoFloat"
	src="../images/helpScreenshots/targetProgress/mostRecentExpView.png" alt="Most recent experiment view" />
<div class="textNoFloat">
Screen-shot 2. 
</div>

<div class="toplink"><a href="#">Back to top</a></div>

<h3>Viewing all experiments recorded for the targets</h3>
<div class="textNoFloat">
The tick "Display all experiments" if checked tells the active target report to display all 
recent experiments performed for the targets as opposed to showing only one most recent 
experiment for each target if the box is unchecked. Please note that with such settings 
the same target can be appear in the list several times.   
</div>
<img class="imageNoFloat"
	src="../images/helpScreenshots/targetProgress/target_progress_allExps.png" alt="Target progress all experiments" />
<div class="textNoFloat">
Screen-shot 3. As we can see one more experiment has been recorded 4 days ago for the same targets.  
</div>


<div class="toplink"><a href="#">Back to top</a></div>

<h3>Progressing more targets</h3>
<div class="textNoFloat">
To progress the target further along the experimental pipeline the experiment can also be recorded with the 
construct of the target. In the active targets report you will not see any 
differences whether the experiment was recorded for the target or the construct. If experiment is recorded with the 
construct the target of the construct will be shown.
Also note that experiment can be "in progress", and the end date therefore defined in the future. 
Still such experiment will make a target "active" and appear on the active target report.      
</div>
<img class="imageNoFloat"
	src="../images/helpScreenshots/targetProgress/failed_exp_with_construct.png" alt="Experiment with construct" />
<div class="textNoFloat">
Screen-shot 4. Experiment associated with the construct as opposed to being linked with the target. 
</div>
<br/> Now we have two targets made "active".
<img class="imageNoFloat"
	src="../images/helpScreenshots/targetProgress/target_progress_2targets.png" alt="Target progress 2 targets" />
<div class="textNoFloat">
Screen-shot 5.  
</div>
<div class="textNoFloat">
On the main page the "Active targets" brick will also display these two targets when the same person (Peter Troshin) 
is logged in.
Please note however, that if the other person is logged in, different targets are likely to be shown.
This is due to the fact that "Active targets" brick shows the targets for which the person logged in 
has recorded experiments 30 days before the current date.
</div>    
<img class="imageNoFloat" style="text-align: left"
	src="../images/helpScreenshots/targetProgress/target_progress_brick_2targ.png" alt="Target progress 2 targets list" />
<div class="textNoFloat">
Screen-shot 6. Active target brick from the PIMS main page   
</div>

<div class="toplink"><a href="#">Back to top</a></div>

<h3>Multiple target progress</h3>
<div class="textNoFloat">
The fields at the top of the page helps you to refine the search criteria for an active targets list. 
Namely, 
<ul>
<li><dfn>Active in the last NNN days</dfn> Permits to define how many days ago the experiments linked with the target has been recorded</li> 
<li><dfn>Worked at</dfn> Define the laboratory where the experiments were carried out</li>
<li><dfn>Experimenter</dfn>define the experimenter, the person who did (or recorded) the experiment</li>
</ul>
It is also possible to look at the list of active targets irrespective of laboratory where experiment(s) were carried out 
or the person who carried out the experiments.
Beware that for such queries the list of experiments can contain thousands of them   
</div>
<img class="imageNoFloat"
	src="../images/helpScreenshots/targetProgress/target_progress_mult.png" alt="View Primer" />
<div class="textNoFloat">
Screen-shot 7. An example of the "global" active target list.  
</div>



</div> <!--end div help-->
<jsp:include page="/JSP/core/Footer.jsp" />

