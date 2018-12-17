var submenus = { 
		  'Target': [//list of target submenus and their corresponding pages
		                 ['Target', '/Search/org.pimslims.model.target.Target'],
	                     ['New Target', '/JSP/Upload.jsp'],
		                 ['Constructs', '/Search/org.pimslims.model.target.ResearchObjective'],
		               //  ['Reports', '/spot/SpotReports'],
		                 ['Scoreboard', '/TargetScoreboard'],
		                 ['Complexes', '/BrowseComplex'],
		                // ['New Complex', '/NewComplex'],
		               //  ['Target Groups', '/Search/org.pimslims.model.target.TargetGroup'],
		                 ['Similarity Search', '/LocalSW'],
		                 ['More...', '/functions/Target.jsp']
		              ],
	      'Experiment': [//list of experiment submenus and their corresponding pages

	                     ['Single Experiments','/Search/org.pimslims.model.experiment.Experiment'],
	                   //  ['New Single Experiment','/Create/org.pimslims.model.experiment.Experiment'],
	                     ['Plate Experiments','/Search/org.pimslims.model.experiment.ExperimentGroup'],
                         ['Experiment Groups','/Search/org.pimslims.model.experiment.ExperimentGroup?_only_experiment_groups=true'],  
	                   //  ['New Plate Experiment','/CreatePlate'],
	                   //  ['New Experiment Group','/Create/org.pimslims.model.experiment.ExperimentGroup'],
	                     ['Protocols','/Search/org.pimslims.model.protocol.Protocol'],

	                     ['New Protocol','/Create/org.pimslims.model.protocol.Protocol'],
	                     ['More...','/functions/Experiment.jsp']
	                  ],
	       'Sample' : [ //list of experiment submenus and their corresponding pages
	                     ['Samples', '/Search/org.pimslims.model.sample.Sample'],
	                     ['New Sample', '/ChooseForCreate/org.pimslims.model.sample.Sample/refSample'],
	                     //['Active Samples Report', '/read/SampleProgress?active=true&days_of_no_progress=0&ready=yes&next_exp_type=any&experiment_in_use=no']                              ['Recipes', '/Search/org.pimslims.model.sample.RefSample'],
	                     ['Containers', '/Search/org.pimslims.model.holder.Holder'],
	                     ['More...', '/functions/Sample.jsp']
	                  ],
	       'User' : [
	                     ['Organisations', '/Search/org.pimslims.model.people.Organisation'],
	                     ['Users', '/Search/org.pimslims.model.accessControl.User'],
	                     ['Access Rights (admin)', '/help/AccessRights.jsp'],
	                     ['More...', '/functions/User.jsp']
	                  ],	
	       'Reference' : [
                         ['Organisation', '/Search/org.pimslims.model.reference.Organism'],
                         ['Target Status', '/Search/org.pimslims.model.reference.TargetStatus'],
                         ['Database Name', 'Search/org.pimslims.model.reference.Database'],
                         ['Experiment Type', '/Search/org.pimslims.model.reference.ExperimentType'],
                         ['Holder Category', '/Search/org.pimslims.model.reference.HolderCategory'],
                         ['Sample Category', '/Search/org.pimslims.model.reference.SampleCategory'],
                         ['Chemical', '/Search/org.pimslims.model.molecule.Molecule?molType=other'],
                         ['Extension', '/ExtensionsList'],
                         ['Molecule Category', '/Search/org.pimslims.model.reference.ComponentCategory'],
                         ['Hazard Phrase', '/Search/org.pimslims.model.reference.HazardPhrase'],
                         ['More...', '/functions/Reference.jsp']
                       ]
          
};          