<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//Tigris//DTD XHTML 1.0 Transitional//EN"
"http://style.tigris.org/tigris_transitional.dtd">
<html>
<head>
 <style type="text/css">
/* <![CDATA[ */
@import "css/readyset.css";
@import "css/inst.css";
/*  ]]> */
 </style>

<link rel="stylesheet" type="text/css" href="css/print.css" media="print" />
 <title>PiMS FAQ</title>
</head>

<body>
<div class="app">
<div class="readyset">
 <h2>PiMS Frequently Asked Questions</h2>

 


<dl>
  <dt id="input">
    I have recorded an new experiment, and I am trying to set an input sample.
    The sample I want is not on the list.
  </dt>
  <dd>
    The list shows only "active" samples. First, please look at your sample
    and check that it is active. 
  </dd><dd>
    Each input to an experiment has a <a href="glossary.jsp#sampleCategory">Sample Category</a> associated with it. 
    The list will show only samples with the appropriate category.
    Please look at your sample and at the protocol.
    If the sample categories do not match, see one of these:<ul>
      <li><a href="output">I did an experiment, and I am looking that the output sample,
    at the list of "Next Experiments".
    The protocol I want to use is not on the list.</a></li>
      <li><a href="stock">I recorded a stock of reagent. Now I want to use it in an experiment, but it is not on the list.</a></li>
    </ul>    
  </dd>
  <dt id="output">
    I did an experiment, and I am looking that the output sample,
    at the list of "Next Experiments".
    The protocol I want to use is not on the list.
  </dt>
  <dd>
    Your output sample has a specific <a href="glossary.jsp#sampleCategory">Sample Category</a> associated with it. 
    This was set in the protocol for the experiment that made the sample.
    The list of "Next Experiments" shows all protocols which expect an input sample belonging to this category.
    To fix this you need to change the protocol you have just used, or the one you want to use next,
    in order to make sure that the sample categories match.
  </dd>
  <dt id="stock">
    I recorded a stock of reagent. Now I want to use it in an experiment, but it is not on the list.
  </dt>
  <dd>    
    Each input to an experiment has a <a href="glossary.jsp#sampleCategory">Sample Category</a> associated with it. 
    The list will show only samples with the appropriate category.
    When you save details of a new stock, you specify the "recipe" for it.
    The sample category is copied from the recipe.
    You need to change either the recipe or the protocol, to make sure
    that the sample categories match.
  </dd>
  <dt id="newCategory">
    I need to add a new <a href="glossary.jsp#sampleCategory">Sample Category</a> to PiMS
  </dt>
  <dd>
    To do this, you must log on as Administrator. 
    Change to the admin perspective, and use the "Reference Data" menu.
    Please send details of your new sample category to 
    <a href="mailto:pims-users@dl.ac.uk">the PiMS team</a>.
    If it is likely to be useful to other PiMS users we will add it
    to a future release.    
    You will then want to make one or more protocols 
    which have an input that is marked with this sample category.
  </dd>
  <dd>
    If your new category is for samples that contain targets 
    (e.g. in the form of protein or DNA)
    then you will need to make one or more protocols whose output
    is of this sample category.
  </dd>
  <dd>
    Alternatively, if your new category is for stocks of reagent,
    then you should make one or more "Recipes" for reagents
    of this sample category.
  </dd>
  
  <dt id="">
  
  </dt>
  <dd>
    
  </dd>
</dl>

</body></html>
