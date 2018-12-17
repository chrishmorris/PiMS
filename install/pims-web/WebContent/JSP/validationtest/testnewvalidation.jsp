<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Validation test' />
</jsp:include>

<!-- OLD -->

This is a validation test page.

<form onsubmit="return validateFormFields(this)" action="${pageContext.request.contextPath}/" name="testform" id="testform">
  Required field:<br/><input name="requiredBox" id="requiredBox" type="text" value=""/><br/>
  Number field:<br/><input name="numberBox" id="numberBox" type="text" value=""/><br/>
  Integer 3-10:<br/><input name="integerBox" id="integerBox" type="text" value=""/><br/>
  DNA sequence:<br/><input name="dnaSeq" id="dnaSeq" type="text" value=""/><br/>
  Protein sequence:<br/><input name="protSeq" id="protSeq" type="text" value=""/><br/>
  Holder position:<br/><input name="holderPosition" id="holderPosition" type="text" value=""/><br/>
  Require A if B:<br/>A<input name="reqA" id="reqA" type="text" value=""/> 
  <br/>B<input name="reqB" id="reqB" type="text" value=""/><br/>
  Mutual require C and D:<br/>C<input name="reqC" id="reqC" type="text" value=""/> 
  <br/>D<input name="reqD" id="reqD" type="text" value=""/><br/>
  Mutual require E-F and F-G:<br/>E<input name="reqE" id="reqE" type="text" value=""/> 
  <br/>F<input name="reqF" id="reqF" type="text" value=""/><br/>
  G<input name="reqG" id="reqG" type="text" value=""/><br/><br/>
  Required select: <select id="myselect" name="myselect"><option value="">None</option><option value="some" selected="selected">Some</option></select><br/><br/>
  
  Test "Same as":<br/>
  Box 1: <input name="sameAs1" id="sameAs1" type="text" value=""/><br/>
  Box 2: <input name="sameAs2" id="sameAs2" type="text" value=""/><br/><br/>
  
  
  <input type="submit" value="Submit"/>
</form>


<script type="text/javascript">

  attachValidation("requiredBox",{required:true, alias:"Required field"});
  attachValidation("numberBox",{alias:"Number field",numeric:true});
  attachValidation("integerBox",{alias:"Integer 3-10",wholeNumber:true,minimum:3,maximum:10});
  attachValidation("dnaSeq",{dnaSequence:true,alias:"DNA sequence"});
  attachValidation("protSeq",{proteinSequence:true,alias:"Protein sequence"});
  attachValidation("reqA",{ alias:"A", requireIfOtherNotEmpty:"reqB", otherAlias:"B"});
  attachValidation("reqC",{ alias:"C", mutuallyRequire:"reqD", otherAlias:"D"});

  attachValidation("reqE",{ alias:"E", mutuallyRequire:"reqF", otherAlias:"F"});
  attachValidation("reqG",{ alias:"G", mutuallyRequire:"reqF", otherAlias:"F"});
  attachValidation("myselect",{ alias:"Select", required:true });

  attachValidation("holderPosition", { holderPosition:true, alias:"Holder position"});

  attachValidation("sameAs1", { alias:"'Same as' box 1", sameAs:"sameAs2" });
  attachValidation("sameAs2", { alias:"'Same as' box 2", required:true });

</script>

<jsp:include page="/JSP/core/Footer.jsp" />
