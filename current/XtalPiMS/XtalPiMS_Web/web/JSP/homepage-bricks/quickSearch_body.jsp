  <form class="quicksearch" id="quicksearchform" method="get" action="${pageContext.request.contextPath}/Search/" onsubmit='quicksearchSetIsGroupExperiment(this);return warnChange()' target="_top">
  <p style="font-weight:bold;font-size:90%;">Choose what to search for:</p>
    <ul style="">
      <li><label><input checked="checked" type="radio" name="_metaClass" value="<%= org.pimslims.model.target.Target.class.getName() %>"> <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/target.gif" /> Targets</label></li>
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.target.ResearchObjective.class.getName() %>"> <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/construct.gif" /> Constructs</label></li>
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.experiment.Experiment.class.getName() %>"> <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/experiment.gif" /> Experiments</label></li>
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.protocol.Protocol.class.getName() %>"> <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/protocol.gif" /> Protocols</label></li>
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.experiment.ExperimentGroup.class.getName() %>"> <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/plate.gif" /> Plate experiments</label></li>
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.experiment.ExperimentGroup.class.getName() %>"> <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/experimentgroup.gif" /> Experiment groups</label></li>
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.sample.Sample.class.getName() %>"> <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/sample.gif" /> Samples</label></li>
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.people.Organisation.class.getName() %>"> <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/organisation.gif" /> Organisations</label></li>
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.holder.Holder.class.getName() %>"> <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/holder.gif" /> Containers</label></li>
      
<%-- not in use
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.reference.SampleCategory.class.getName() %>">Sample types</label></li>
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.reference.ExperimentType.class.getName() %>">Experiment Types</label></li>
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.molecule.Molecule.class.getName() %>"> <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/molecule.gif" /> Molecules</label></li>
      <li><label><input type="radio" name="_metaClass" value="<%= org.pimslims.model.reference.Organism.class.getName() %>"> <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/organism.gif" /> Organisms</label></li>
--%>     
     </ul> 
    <div id="quicksearchfields">
    <input type="checkbox" name="_only_experiment_groups" id="_only_experiment_groups" value="true" style="position:absolute;left:-5000px" />
    <input style="clear:both;width: 65%; margin-right: 3px;" name="search_all" id="search_all" 
        type="text" class="text" value="Enter search terms here"
        onfocus="document.getElementById('quicksearchsubmit').disabled='';if(!this.used){this.value='';this.used=true};" />
    <input type="submit" name="SUBMIT" value="Search" onclick="dontWarn()" id="quicksearchsubmit" disabled="disabled" />
    </div>
  </form>
  <script type="text/javascript"><!--
  function quicksearchSetIsGroupExperiment(frm){
	    frm=$(frm);
	    var numOptions=frm["_metaClass"].length;
	    for(var i=0;i<numOptions;i++){
	        var rad=$(frm["_metaClass"][i]);
	        if(rad.checked){
	            if(rad.up("li").innerHTML.indexOf("group") > -1){
	                $("_only_experiment_groups").checked="checked";
	            } else {
                    $("_only_experiment_groups").checked="";
	            }
	        }
	    }	    

  }//-->
  </script>
