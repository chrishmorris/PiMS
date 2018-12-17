var submenus = {
		   		'Target': [//list of target submenus and their corresponding pages
		   				   ['Target', '/Search/org.pimslims.model.target.Target'],
		   				   ['New Target', '/JSP/Upload.jsp'],
		   				   ['Constructs', '/Search/org.pimslims.model.target.ResearchObjective'],
		   				 //  ['Reports', '/spot/SpotReports'],
		   				   ['Scoreboard', '/TargetScoreboard'],
		   				   ['Complexes', '/BrowseComplex'],
		   				   ['New Complex', '/NewComplex'],
		   				   ['Target Groups', '/Search/org.pimslims.model.target.TargetGroup'],
		   				   ['Similarity Search', '/LocalSW'],
		   				   ['More...', '/functions/Target.jsp']
		   				],
		   		 'Experiment': [//list of experiment submenus and their corresponding pages
		   		           ['New Sequencing Order', '/update/CreateSequencingOrder'],
		   		           ['Sequencing Orders', '/read/AdvancedSearchSOrders'],
		   		           ['Sequencing Main (Admin)', '/read/AdminHome'],
		   		           ['Single Experiments','/Search/org.pimslims.model.experiment.Experiment'],
		   		           ['New Single Experiment','/Create/org.pimslims.model.experiment.Experiment'],
		   		           ['Plate Experiments','/Search/org.pimslims.model.experiment.ExperimentGroup'],
		   		           ['Experiment Groups','/Search/org.pimslims.model.experiment.ExperimentGroup?_only_experiment_groups=true'],
		   		           ['New Plate Experiment','/CreatePlate'],
		   		           ['New Experiment Group','/Create/org.pimslims.model.experiment.ExperimentGroup'],
		   		           ['Protocols','/Search/org.pimslims.model.protocol.Protocol'],
		   		           ['New Protocol','/Create/org.pimslims.model.protocol.Protocol'],
		   	               ['More...','/functions/Experiment.jsp']
		   		         ],
		   		         
		   		 'Sample' : [ //list of experiment submenus and their corresponding pages
		   		           ['Samples', '/Search/org.pimslims.model.sample.Sample'],
		   		           ['New Sample', '/ChooseForCreate/org.pimslims.model.sample.Sample/refSample'],
		   		           ['Active Samples Report', '/read/SampleProgress?active=true&days_of_no_progress=0&ready=yes&next_exp_type=any&experiment_in_use=no'],
		   		           ['Recipes', '/Search/org.pimslims.model.sample.RefSample'],
		   		           ['Containers', '/Search/org.pimslims.model.holder.Holder'],
		   		           ['More...', '/functions/Sample.jsp']
		   		         ],
		   		         
		   		 'User' : [
		   		           ['Organisations', '/Search/org.pimslims.model.people.Organisation'],
		   		           ['More...', '/functions/User.jsp']
		   		         ],
		   		         
                 'Help' : [
                           ['Guide to Using PiMS', '/functions/Help.jsp'],
                           ['Getting Started', '/help/HelpGettingStarted.jsp'],
                           ['Basic Concepts PiMS', '/help/HelpBasicConcepts.jsp'],
                           ['Target Help', '/help/target/HelpTarget.jsp'],
                           ['Complex Help', '/help/target/HelpComplex.jsp'],
                           ['Sequence Search Help', '/help/HelpLocalSimilaritySearch.jsp'],
                           ['Experiment Help', '/help/experiment/HelpExperiments.jsp'],
                           ['Experiment Group Help', '/help/experiment/ExperimentGroup.jsp'],
                           ['Plate Experiment Help', '/help/experiment/plate/HelpCreatePlateExperiment.jsp'],
                           ['Protocol Help', '/help/experiment/protocol/HelpProtocol.jsp'],
                           ['Sample Help', '/help/HelpSamples.jsp'],
                           ['User Help', '/help/HelpPeoplePlaces.jsp'],
                           ['Access Help', '/help/HelpAccessModel.jsp'],
                           ['Reference Help', '/help/reference/HelpRefData.jsp'],
                           ['History Help', '/help/HelpMRU.jsp'],
                           ['Caliper Robot Help', '/help/robots/HelpCaliper.jsp'],
                           ['Glossary', '/help/glossary.jsp'],
                           ['Report a Problem', 'mailto:pims-defects@dl.ac.uk']
                         ]
                        };         