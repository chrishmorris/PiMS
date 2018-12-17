<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Help for PIMS Recipes and Stocks' />
</jsp:include>

<div class="help">
<pimsWidget:box initialState="fixed" title="Overview">
PiMS has facilities to enable you to record the details of all reagents used in the laboratory.<br />
This includes a &#39;Recipe&#39;, or Reference data to describe the composition of a reagent, and Samples or 
&#39;Stocks&#39; of the reagent based on the recipe.  PiMS will also record details of any Experiments where the Stock is used.
</pimsWidget:box>
<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#recipeSearch">Search</a> for a Recipe</li>
  <li><a href="#recipeView">View a PiMS Recipe record</a></li>
  <li><a href="#recipeCreate">Record a new Recipe</a></li>
  <li>
   <ul>
    <li><a href="#addCategory">Set sample category</a> of a Recipe</li>
    <li><a href="#addComponents">Add components</a> to a Recipe</li>
   </ul>
  </li>
  <li><a href="#copyRecipe">Copy</a> a Recipe</li>
  <li><a href="#recipeEdit">Edit</a> the details for a Recipe</li>
  <li><a href="#createStock">New Sample or Reagent Stock</a> which conforms to a recipe</li>
  <li>
   <ul>
    <li><a href="#fromMenu">New Sample</a></li>
    <li><a href="#fromRecipe">New Sample</a> from Recipe view</li>
   </ul>
  </li>
  <li><a href="HelpSamples.jsp">Sample management</a> -reports</li>
 </ul>
 <a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS.
</pimsWidget:box>
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

 
  <li><h3 id="recipeSearch">Search for a Recipe</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
  To find a recipe in PiMS select &#39;Search Recipes&#39; from the &#39;Sample menu&#39; in the 
  horizontal menubar, or click on &#39;Samples&#39; in the menubar then click <a href="javascript:void(0)">Search Recipes</a>
  in the &#39;Functionality for Experiments&#39; page.<br />

  <img class="imageNoFloat" src="../images/helpScreenshots/recipe/navToRecipeSearch.png" alt="Recipe in menu" /><br /><br />
  You will see the first page of the search recipe function.
  The page displays the number of Recipe records already recorded in PiMS
  &nbsp; <em>-in the example there are 659</em>
  <br /><br />
  
  The form contains 2 search blocks labelled <strong>Quick Search</strong> and <strong>Search Criteria</strong>
  <br /><br />
  There is also a link labelled <a href="#recipeCreate">Create a new Recipe</a><br />
  &nbsp; <em> -see <a href="#recipeCreate">Record a new Recipe</a></em>
  <br />
  <img class="imageNoFloat" src="../images/helpScreenshots/recipe/searchRecipe.png" alt="Search for a Recipe in PiMS" /><br /><br />
   <ul>
    <li>Enter a value value in the search field(s). <em>-&#39;tris&#39; has been entered in the Name field in the example</em><br />
    and click <input class="button" value="Search" type="submit" /><br /><br /></li>
    <li>You will see a list of all PiMS Recipes which match the search term(s) you entered<br />
    &nbsp; <em>-in the example there are 6 matching Recipe records with &#39;tris&#39; in the Name.</em><br />
    </li>
    </ul>
     <img class="imageNoFloat" src="../images/helpScreenshots/recipe/searchRecipeResult.png" alt="Search Recipe Result" /><br /><br />
   
  </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
 
  <li><h3 id="recipeView">View a Recipe</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
  To see the details recorded for a PiMS recipe:<br />
  &nbsp; click the link in the search results page.<br /><br />
  The example below shows the PiMS Recipe for TRIS.<br />
  
  &nbsp; TRIS is classified as a PiMS &#39;Chemical/Biochemical&#39;<br />
  &nbsp; The Recipe defines a single component: -TRIS and there are currently no &#39;Stocks&#39; of TRIS 
  recorded in the database.<br />
  &nbsp; <strong>note:</strong> <em>-this Recipe is PiMS Reference data and so the details can only be edited by a 
  PiMS administrator user.<br /></em><br />
  <img class="imageNoFloat" src="../images/helpScreenshots/recipe/recipeView.jpg" alt="View of a PiMS Recipe" /><br /><br />
   <ul>
    <li>To see the details recorded for the Recipe component(s), click the name of the component<br />
    &nbsp; <em>-the example shows details recorded for the component called &#39;TRIS&#39;<br />
    &nbsp; -this includes the CAS number, Chemical formula, Mass and any synonyms for the component name</em>
    <br /><br /></li>
   </ul>
   <img class="imageNoFloat" src="../images/helpScreenshots/recipe/componentView.jpg" alt="View of a PiMS Recipe component" />
  </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
  <li><h3 id="recipeCreate">Record a new Recipe</h3>
  <div class="helpcontents">
   <div class="textNoFloat">
   To create a new PiMS Recipe record, navigate to the Recipe search page and click the link labelled 
   <a href="javascript:void(0)">Create a new Recipe</a><br />
   You will see a PiMS &#39;New Recipe&#39; form.<br /><br />
   The new recipe form allows you to record the specific details for the Recipe.<br />
   The example shows values which have been entered to record a recipe for &#39;Tris-EDTA buffer pH8.0&#39;<br />
    <img class="imageNoFloat" src="../images/helpScreenshots/recipe/newRecipe.jpg" alt="New Recipe form" /><br /><br />
   When you have completed the form, click <input class="button" value="Save" type="submit" /> 
   to display the new Recipe details.<br /><br />
   &nbsp; <strong>note:</strong> you must provide a unique name for the new Recipe<br /><br />
    <img class="imageNoFloat" src="../images/helpScreenshots/recipe/newRecipeView.jpg" alt="View of a New Recipe record" /><br /><br />

   <h4 id="addCategory">Set a Sample Category</h4>	
     <ul>
      <li>To define category of the recipe: click <a href="javascript:void(0)">Edit</a> recipe Type<br /><br /></li>
      <li>The Add/Remove sample categories form shows existing categories attributed to the recipe.</li>
      <li>The lower part of the page allows you to search for new catgories to define the recipe.</li> 
      <li>Check the checkbox against the categories you wish to select and click 
      <input class="button" value="Add selected items" type="submit" /><br />
      &nbsp; <em>-In this example Tris-EDTA buffer pH8.0 has been set as a buffer</em><br />
       <img class="imageNoFloat" src="../images/helpScreenshots/recipe/searchRecipeCategory.jpg" alt="Search result for Recipe category" /><br /><br /></li>
     </ul>
     
   <h4 id="addComponents">Add components</h4>	
     <ul>
      <li>To define the Recipe components: click <a href="javascript:void(0)">Add new component</a><br /><br /></li>
      <li>Enter the name of the component you wish to add to the Recipe in the resulting search form and click 
      <input class="button" value="Search" type="submit" /><br />
      &nbsp; -<em>in the example &#39;tris&#39; has been entered in the &#39;Name&#39; field and the search has found 
      7 matching components in the PiMS component Reference data.</em></li>
     
      <img class="imageNoFloat" src="../images/helpScreenshots/recipe/searchRecipeComponentResult.jpg" alt="Search result for Recipe component" /><br /><br />
     
      <li>Check the radio button against the component you wish to select and click 
      <input class="button" value="Next" type="submit" /><br />
      &nbsp; <em>-TRIS has been selected in the example<br />
      &nbsp; <strong>note:</strong> if your search is unsuccessful ask your PiMS administrator to update the reference data.</em><br /><br /></li>
      <li>Repeat these steps for any additional components.<br /><br /></li>
      <li>The selected component(s) will be displayed in the Recipe details page where you may enter a Molar concentration 
      value and any additional details.<br />
      &nbsp; <strong>note:</strong><em> -if you enter a non-Molar concentration you will see a warning</em><br />
       <img class="imageNoFloat" src="../images/helpScreenshots/recipe/concentrationWarning.jpg" alt="Warning for non-M concentration" /><br /><br />
      &nbsp;<em> -the example shows two components have been added to the Recipe</em><br />
       <img class="imageNoFloat" src="../images/helpScreenshots/recipe/newRecipePlusComponentsView.png" alt="View Recipe with components" /><br /><br /></li>
     </ul>
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>

  <li><h3 id="copyRecipe">Copy a Recipe</h3>
  <div class="helpcontents">
   <div class="textNoFloat">
    You can make a copy of an existing recipe.  This will allow you to edit the details recorded for a Reference Recipe<br /><br />
    <ul>
     <li>Navigate to the details page for a PiMS Recipe<br /><br /></li>
     <li> Click <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/copy.gif" alt="Copy icon" /> <a href="JavaScript:void()">Copy</a> near the top of the page
     -you will see a warning:<br /><br />
     <img class="imageNoFloat" src="../images/helpScreenshots/recipe/copyWarning.jpg" alt="Warning before copying recipe" /></li>
     <li>Click &#39;OK&#39; to display an editable copy of the Recipe record (or &#39;Cancel&#39; to abort the copy).<br />
     &nbsp; <em> -the example shows a copy of the PiMS Reference recipe for TRIS</em>
     <img class="imageNoFloat" src="../images/helpScreenshots/recipe/copyOfRecipe.png" alt="Copy of a recipe" /><br />
     &nbsp; <strong>note:</strong> you must provide a unique name for the new Recipe</li>
    </ul>
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
  

  <li><h3 id="recipeEdit">Edit the details for a Recipe</h3>
  <div class="helpcontents">
   <div class="textNoFloat">
   The details of PiMS Recipe records can be edited, provided you are permitted to do so.<br /><br />
    <ul>
     <li>Navigate to the details page for a Recipe in PiMS.<br /><br /></li>
     <li>To edit the &#39;Name&#39;, &#39;pH&#39; or &#39;details&#39; of a Recipe, or the &#39;concentration&#39; or &#39;details&#39; for a Recipe component:<br />
      type in the editable form fields and click <input class="button" value="Save" type="submit" /><br /><br /></li>
     <li>To change the Sample category (Type) for the Recipe: click the <a href="javascript:void(0)">Edit</a> link<br />
     &nbsp; You then have 3 options:</li>
     <li>
      <ul>
       <li>To create a new Sample cateogory for the Recipe record: click the link<br />
        <img class="imageNoFloat" src="../images/helpScreenshots/recipe/sampleCatNew.jpg" alt="Create new Sample category for Recipe" /><br /><br /></li>
       <li>To remove a Sample category from the Recipe record: check the box against the category and click 
        <input class="button" value="Remove selected items" type="submit" /><br />
        <img class="imageNoFloat" src="../images/helpScreenshots/recipe/sampleCatRemove.jpg" alt="Remove Sample category for Recipe" /><br /><br /></li>
       <li>To Add a Sample category (already in PiMS) to the Recipe record: enter values in the search form and click <input class="button" value="Search" type="submit" /><br />
       Then select the cateogories to add from the resulting list.<br />
        <img class="imageNoFloat" src="../images/helpScreenshots/recipe/sampleCatSearch.jpg" alt="Search for Sample categories for Recipe" /><br /><br /></li>
      </ul>
     &nbsp; Any changes will be displayed in the Recipe details.<br /><br />
     </li>
     <li>To add a Sample component click <a href="javascript:void(0)">Add new component</a><br />
     &nbsp; <em> -see <a href="#addComponents">Add components</a> for more details</em><br /><br /></li>
     <li>To remove a Sample component, click the green dustbin 
      <img class="icon" src="../skins/default/images/icons/actions/delete.gif" alt="Delete bin icon" title="Delete this Recipe"/> 
     icon to the right of the component information.<br />
     You will see a warning similar to the example below:<br />
      <img class="imageNoFloat" src="../images/helpScreenshots/recipe/deleteComponentMessage.jpg" alt="Warning when deleting a Recipe component" />
     <br /><br />
     Click <input class="button" value="Delete" type="submit" /> to continue or <a href="javascript:void(0)">Back</a> to abandon.<br />
     A message confirming that the Component has been deleted will be displayed, with a link to the updated Recipe record.</li>
    </ul> 
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>

  <li><h3 id="createStock">Record a New Sample or Reagent Stock which conforms to a PiMS Recipe</h3>
  <div class="helpcontents">
   <div class="textNoFloat">
   PiMS recipes are used to define Sample or Reagent Stocks.
   In this way, the details are only be recorded once in PiMS and any number of Sample/Stock records can be created based on these details.<br /> 
   If the Stock is used in any PiMS Experiments, these details will also be recorded.<br />
   A new Sample or Stock can be recorded in 2 ways:<br />
   <h4 id="fromMenu">1. New Sample from the Sample menu</h4>
   <ul>
    <li>Select New Sample from the Sample menu or click &quot;New&quot; in the Sample box on the Sample Functions page<br />
        <img class="imageNoFloat" src="../images/helpScreenshots/recipe/newSample1.png" alt="Navigate to New Sample" /><br /><br /></li>    

    <li>You must first select a recipe to use<br />
         <img class="imageNoFloat" src="../images/helpScreenshots/recipe/newSample2.png" alt="Select a recipe" /><br /><br /></li>
    <li>Enter a search term in the Quick Search box<br />
        &nbsp; <em>Tris-EDTA buffer pH8.0 has been entered in the example, and one matching Recipe has been found</em><br /><br /></li>
    <li>Select a recipe and click <input type="submit" value="Next&gt;&gt;&gt;" /> to continue<br /><br /></li>
    <li>If there are no suitable recipes in the results, click the link to record a new recipe at the top of the page<br />
        <a href="javaScript:void(0)">Create a new Reagent specification for the new Sample</a>  &nbsp; <em>-see <a href="#recipeCreate">Record a new Recipe</a> for more details</em><br /><br />
        <img class="imageNoFloat" src="../images/helpScreenshots/recipe/newSample3.png" alt="Record sample name and details" /><br /><br /></li>
    <li>The Recipe and Lab Notebook selected in the previous step are displayed.<br />
        Enter a unique &#39;Name&#39; for the Sample <em> -TE Buffer 081208 has been entered in the example</em><br />
        &nbsp; <strong>note:</strong> you may enter a barcode instead of a name<br />
        You may also select a &quot;Unit&quot; for the stock. e.g. the Mass or Volume<br /><br /></li>
    <li>When you are happy with the details of the Sample, click <input type="submit" value="Create"/></li>    
   </ul>
   <h4 id="fromRecipe">2. New Sample from Recipe view</h4>
   <ul>
    <li>Navigate to the details page for a Recipe in PiMS.<br /><br /></li>
    <li>Locate the box headed <strong>Available stocks of this recipe</strong> towards the bottom of the page.<br /> 
    -enter a unique &#39;Name&#39; for the stock <em> -TE Buffer 081208 has been entered in the example</em><br />
    -select a Lab Notebook from the drop-down list and  and click <input class="button" value="Create" type="submit" /><br />
    <img class="imageNoFloat" src="../images/helpScreenshots/recipe/recordStock1.png" alt="Record new stock from recipe" /><br /><br /></li>    
   </ul>
   
    <li>A Sample for your recipe will be created and you will be taken to the Sample/Stock view page.<br />
    <em>see <a href="HelpSamples.jsp">Sample Help</a> for mode details</em><br /></li>

    <img class="imageNoFloat" src="../images/helpScreenshots/recipe/viewStock1.png" alt="View stock" /><br /><br /> 
   </ul>
   
   
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
  <li><h3 id="outputSampleStock">Experiment output samples -stocks created by Experiments</h3>
  <div class="helpcontents">
   <div class="textNoFloat">
   PiMS Sample/Stock records are also created for &#39;Output samples&#39; when you record a new Experiment.<br /><br />
   The Sample/Stock view page is accessed by clicking link to a Sample listed on the the Output Samples tabs in a PiMS Experiment record.<br /><br />
   Sample/Stock records created in this way will include additional details about the relevant Experiment record.<br />
   &nbsp; <em>-the example shows details from a Sample/Stock record for a PCR product the output from a PiMS PCR Experiment<br />
   &nbsp; it includes a link to the Experiment record <a href="javascript:void(0)">PCR2</a> and a list of Experiment parameters.</em><br />
    <img class="imageNoFloat" src="../images/helpScreenshots/recipe/outputSample.jpg" alt="View output sample stock" /><br />
   
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>

 
 

<jsp:include page="/JSP/core/Footer.jsp" />
