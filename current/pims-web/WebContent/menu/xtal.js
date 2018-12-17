var submenus = {
		'Search': [//list of target submenus and their corresponding pages
		        ['Plate Experiments', '/ViewPlates.jsp'],
		        ['Plate Inspections', '/ViewInspections.jsp'],
		        ['Samples', '/Search/org.pimslims.model.sample.Sample?sampleCategories=Purified+protein'],
		        ['Constructs', '/ViewConstructs.jsp'],
		        ['Groups', '/ViewGroups.jsp'],
	            ['More...', '/Search/']
		   ],
		
		'Screens' : [
		        ['Screens', '/ViewScreens.jsp'],
		        ['Conditions', '/ViewConditions.jsp']
		],
			        
	   	'Diffraction' : [
	   	        ['Create pins-in-pucks shipment', '/AssembleShipment/?destination=Diamond'],
	   	        ['Create plates shipment', '/AssembleShipment/?destination=Diamond&plateshipment=yes'],
	   	        ['Recent shipments', '/RecentShipments'],
	   	        ['Retrieve ISPyB results', '/IspybResults.jsp']
	   	],
			   	        
        'Reference' : [
                ['Imagers', '/Search/org.pimslims.model.experiment.Instrument'], 
                ['Image Type', '/Search/org.pimslims.model.reference.ImageType'], 
                ['Plate Types', '/Search/org.pimslims.model.reference.CrystalType'], 
                ['Scoring Scheme', '/Search/org.pimslims.model.crystallization.ScoringScheme']
           ],
        
        'Help' : [
                ['Guide to Using PiMS', '/functions/Help.jsp'],
                ['Target Help', '/help/HelpTargets.jsp'],
                ['Complex Help', '/help/HelpComplex.jsp'],
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
                ['Akta Experiment Help', '/help/robots/akta/HelpAktaOverview.jsp'],
                ['Caliper Robot Help', '/help/robots/HelpCaliper.jsp'],
                ['Glossary', '/help/glossary.jsp'],
                ['Report a Problem', 'mailto:pims-defects@dl.ac.uk']
           ],
};
        
