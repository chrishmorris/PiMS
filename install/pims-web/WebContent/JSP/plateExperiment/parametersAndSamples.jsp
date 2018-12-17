<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

    <h4 id="inputshead" onclick="showPanel(this)" title="The samples you put into the wells">Input samples</h4>
    <div id="inputsamples" class="panel current">
        <p class="explanation">The samples you put into the wells</p>
        <c:forEach var="ris" items="${refInputSamples}">
            <div class="param is${ris.dbId}">
            <h5>${ris.name}</h5>
            <form action="#" onsubmit="updateSampleAndAmount(this);return false">
                <c:set var="prettifiedUnit" value="${ris.displayUnit}" />
                <c:if test="${'uL' eq ris.displayUnit}"><c:set var="prettifiedUnit" value="&#181;L" /></c:if>
                <input class="amount" type="text" value="" name="${ris._Hook}:amount" id="${ris._Hook}:amount"/>${prettifiedUnit} <!-- Don't hard-code the unit -->
                <select class="sample" name="${ris._Hook}:sample" id="${ris._Hook}:sample" onchange="sampleSelectOnChange(this)" onmousedown="if('search'!=this.value)this.oldValue=this.value">
                    <option value="(various)" disabled="disabled">(various)</option>
                    <option value="">None</option>
                    <option value="search">Search...</option>
                    <c:forEach items="${recentSamples[ris._Hook]}" var="recentSample">
                        <option value="${recentSample.hook}">${recentSample.name}</option>
                    </c:forEach>
                </select>
                <input type="hidden" name="risHook" class="risHook" value="${ris._Hook}" />
                <input type="hidden" name="${ris._Hook}:displayUnit" class="displayUnit" value="${ris.displayUnit}" />
                <input type="submit" value="Update"/>
                <c:if test="${isPlateExperiment}">
                    <div>or <a href="#" onclick="beginAdvancedFill('${ris._Hook}')">Advanced fill options...</a></div>
                </c:if>
            </form>
            </div>
        </c:forEach>
    </div>
    
    <c:set var="hasResultParameters" value="${false}" />
    <c:set var="hasGroupParameters" value="${false}" />
    <c:forEach var="pd" items="${parameterDefinitions}">
        <c:if test="${pd.isResult}"><c:set var="hasResultParameters" value="${true}" /></c:if>    
        <c:if test="${pd.isGroupLevel}"><c:set var="hasGroupParameters" value="${true}" /></c:if>    
    </c:forEach>
    
    <c:if test="${hasGroupParameters}">
    <h4 onclick="showPanel(this)" title="Parameters with the same value over the whole group">Group-level parameters</h4>
    <div class="panel" id="groupparams">
        <p class="explanation">Parameters with the same value over the whole group</p>
        <jsp:include page="parameters.jsp">
            <jsp:param name="groupLevelParams" value="${true}" />
            <jsp:param name="resultParams" value="${false}" />
        </jsp:include>
    </div>
    </c:if>

    <c:set var="sectionTitle" value="Parameters" />
    <c:if test="${hasGroupParameters || hasResultParameters}"><c:set var="sectionTitle" value="Set-up parameters" /></c:if>
    <h4 onclick="showPanel(this)" title="Experimental conditions like times and temperatures">${sectionTitle}</h4>
    <div class="panel" id="setupparams">
        <p class="explanation">Experimental conditions like times and temperatures</p>
        <jsp:include page="parameters.jsp">
            <jsp:param name="groupLevelParams" value="${false}" />
            <jsp:param name="resultParams" value="${false}" />
        </jsp:include>
    </div>
    
    <c:if test="${hasResultParameters}">
    <h4 onclick="showPanel(this)" title="The results of your experiments">Result parameters</h4>
    <div id="resultparams" class="panel">
        <p class="explanation">The results of your experiments</p>
        <jsp:include page="parameters.jsp">
            <jsp:param name="groupLevelParams" value="${false}" />
            <jsp:param name="resultParams" value="${true}" />
        </jsp:include>
    </div>
    </c:if>
    
    <h4 id="statushead" onclick="showPanel(this)" title="Whether the experiment has been finished, and whether it was completed without problems">Status</h4>
    <div id="statuspanel" class="panel">
        <p class="explanation">Whether the experiment has been finished, and whether it was completed without problems.</p>
        <div class="param status">
        <h5>Status</h5>
        <form action="#" onsubmit="updateExperimentStatuses(this);return false">
            <select class="paramvalue" name="exptstatus" id="exptstatus" onchange="exptStatusOnchange()">
                <option value="(various)" disabled="disabled">(various)</option>
                <option value="To_be_run">To be run</option>
                <option value="In_process">In progress</option>
                <option value="OK">OK</option>
                <option value="Failed">Failed</option>
            </select>
            <c:if test="${!empty milestoneName}">
            <div id="cantsetmilestone" style="display:none;color:#600;font-weight:bold">
                One or more of these experiments doesn't have a construct. You can't set a milestone for experiments with no construct.
            </div>
            <div id="milestone">
                <h5>Milestone "${milestoneName}" achieved:</h5>
                <select class="milestoneAchieved" id="milestoneAchieved" name="milestoneAchieved">
                    <option value="(various)">(various)</option>
                    <option value="true">Yes</option>
                    <option value="false">No</option>
                </select>    
            </div>
            </c:if>
            <input type="submit" value="Update"/>
        </form>
        </div>
    </div>
    
    <c:forEach var="pd" items="${parameterDefinitions}">
    <c:if test="${fn:startsWith(pd.name,'__')}"><%-- There should be only one! --%>

	    <h4 onclick="showPanel(this)" title="Whether the experiment gave a good result">${pd.label}</h4>
	    <div id="score_${fn:substringAfter(pd.name,'__')}" class="score panel">
	        <div class="param pd${pd.dbId}" id="pd${pd.dbId}">
            <form action="#" class="experimentscores ${fn:substringAfter(pd.name,'__')}" onsubmit="updateExperimentScores(this);return false;">
                <label>
                <input type="radio" value="(various)" name="score" checked="checked" /> (various)
                </label>
                <c:forEach var="pv" items="${pd.possibleValues}" varStatus="status">
                <label class="${fn:substringAfter(pd.name,'__')}${status.count-1}"><%-- "score0","score1", etc. --%>
                <input type="radio" value="<c:out value="${pv}"/>" name="score" /> ${pv}
                </label>
                </c:forEach>
                <div>
                    <br/><input type="submit" value="Update" />
                </div>
                
    <%-- JMD SequenceResult link
         TODO Find a better way of special-casing, preferably at the protocol or parameterdefinition level --%>
    <c:if test="${pd.name eq '__SEQUENCE'}">
        <div id="__sequenceLink">&nbsp;</div>
    </c:if>
            </form>
            </div>
	        <script type="text/javascript">
	        $("score_${fn:substringAfter(pd.name,'__')}").pdefName="${fn:substringAfter(pd.name,'__')}"
	        </script>
	    </div>
    </c:if>
    </c:forEach>    

    <c:forEach var="ros" items="${refOutputSamples}">
    <h4 id="os${ros.dbId}" onclick="showPanel(this)" title="The contents of the wells after the experiment">Output: ${ros.name}</h4>
    <div class="panel outputsample">
        <p class="explanation">Click on any of the samples to go to its individual View page.</p>
    </div>
    </c:forEach>
<%--
    <h4 onclick="showPanel(this)" title="The contents of the wells after the experiment">Output samples</h4>
    <%- - TODO For groups, show each OutputSample separately if there's more than one - -%>
    <div id="outputsamples" class="panel"><p class="explanation">Click on any of the samples to go to its individual View page.</p>
    </div>
--%>    
    <h4 onclick="showPanel(this)" title="Links to the individual experiments within this group">Experiments</h4>
    <div id="expts" class="panel"><p class="explanation">Click on any of the experiments to go to its individual View page.</p>
    </div>

    <h4 onclick="showPanel(this)" title="Links to the targets and constructs">Targets and constructs</h4>
    <div id="targets" class="panel">
     
            <div class="param researchObjective blueprintHook">
            <form action="#" onsubmit="updateConstructOnSelectedExperiments(this);return false;">
                <select name="researchObjective" class="paramvalue researchObjective" onchange="targetSelectOnChange(this)">
                    <option value="(various)" disabled="disabled">(various)</option>
                    <option value="">None</option>
                    <option value="search">Search...</option>
                    <c:forEach items="${recentTargets}" var="targetHook">
                        <option value="${targetHook.hook}"><c:out value="${targetHook.name}" /></option>
                    </c:forEach>
                </select>
                <input type="submit" value="Update" />
            </form>
            </div>
   
    </div>

