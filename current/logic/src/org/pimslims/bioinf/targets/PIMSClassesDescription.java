package org.pimslims.bioinf.targets;

import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.target.Target;

/**
 * @author Petr Troshin This is a dictionary of user available attributes and roles of PIMS classes.
 * @deprecated Please use internal PIMS classes static properties instead
 */
@Deprecated
public interface PIMSClassesDescription {

    public interface SampleCategoryAttr {

        String name = "name";

        String details = "details";
    }

    public interface SampleCategoryRoles {
        String abstractSamples = "abstractSamples";

        String experimentTypes = "experimentTypes";

        String materials = "materials";

        String protocol = "protocol";
    }

    public interface MolComponentAttr {
        String molType = "molType";

        //String casNum = "casNum";

        String empiricalFormula = "empiricalFormula";

        String molecularMass = "molecularMass";

        //String seqDetails = "seqDetails";

        //String seqString = "seqString";

        String name = "name";

        String synonyms = "synonyms";

        String details = "details";
    }

    public interface MolComponentRoles {

        String categories = "categories";

        String componentDbRefs = "componentDbRefs";

        // String compositeElements="compositeElements";
        String labels = "labels";

        String molCompFeatures = "molCompFeatures";

        String molecule = "molecule";

        String naturalSource = "naturalSource";

        String refMolCompFeatures = "refMolCompFeatures";

        String specificLabelGroups = "specificLabelGroups";

        String blueprintComponents = "blueprintComponents";

    }

    public interface SampleAttr {
        String rowPosition = "rowPosition";

        String colPosition = "colPosition";

        String subPosition = "subPosition";

        String creationDate = "creationDate";

        String initialAmount = "initialAmount";

        String currentAmountFlag = "currentAmountFlag";

        String batchNum = "batchNum";

        String name = "name";

        String pH = "ph";

        String ionicStrength = "ionicStrength";

        String isHazard = "isHazard";

        String details = "details";
    }

    public interface SampleRoles {

        String hazardPhrases = AbstractSample.PROP_HAZARDPHRASES;

        String holder = "holder";

        String localRiskPhrases = "localRiskPhrases";

        String rPhrases = "rPhrases";

        String sPhrases = "sPhrases";

        String sampleCategories = "sampleCategories";

        String sampleComponents = "sampleComponents";

    }

    public interface TargetGroupAttr {
        String shortName = "shortName";

        String completeName = "completeName";

        String details = "details";

        String groupingType = "groupingType";
    }

    public interface TargetGroupRoles {
        String subTargetGroups = "subTargetGroups";

        String targetGroup = "targetGroup";

        String targets = "targets";

        String dbRefs = "dbRefs";

        String citations = "citations";
    }

    public interface TargetAttr {
        @Deprecated
        String localName = "name";

        String NAME = "name";

        @Deprecated
        String geneName = "name";

        String seqString = "seqString";

        String biologicalProcess = "biologicalProcess";

        String biochemicalFunction = "biochemicalFunction";

        String functionDescription = "functionDescription";

        String topology = "topology";

        String cellLocation = "cellLocation";

        String catalyticActivity = "catalyticActivity";

        String pathway = "pathway";

        String similarityDetails = "similarityDetails";

        String whyChosen = "whyChosen";

        String details = "details";
    }

    public interface TargetRoles {
        String citations = "citations";

        String creator = "creator";

        String protein = "protein";

        String nucleotides = "nucleicAcids";

        String species = "species";

        String dbRefs = "dbRefs";

        String blueprintComponents = "blueprintComponents";

        String targetGroups = Target.PROP_TARGETGROUPS;

    }

    public interface MoleculeAttr {
        String name = "name";

        String longName = "longName";

        String molType = "molType";

        String commonNames = "commonNames";

        String keywords = "keywords";

        String functions = "functions";

        String seqLength = "seqLength";

        String molecularMass = "molecularMass";

        String details = "details";

        String seqDetails = "seqDetails";

        String seqString = "seqString";

        String stdSeqString = "stdSeqString";

        String isFinalised = "isFinalised";
    }

    public interface MoleculeRoles {
        String citations = "citations";

        String molResLinks = "molResLinks";

        String molResidues = "molResidues";

        String molSeqFragments = "molSeqFragments";

        String moleculeSysNames = "moleculeSysNames";

        String naturalSource = "naturalSource";

        String chains = "chains";

        String molCompFeatures = "molCompFeatures";

        String refMolCompFeatures = "refMolCompFeatures";

        String molComponents = "molComponents";

        String refMolComponents = "refMolComponents";

        String nucTarget = "nucTarget";

        String protTarget = "protTarget";

        String blueprintComps = "blueprintComps";

        String testBlueprintComps = "testBlueprintComps";
    }

    public interface StatusAttr {
        String date = "date";
    }

    public interface StatusRoles {
        String target = "target";

        String code = "code";
    }

    public interface TargetStatusAttr {
        String name = "name";

        String details = "details";
    }

    public interface TargetStatusRoles {
        String status = "status";
    }

    public interface TargetDbRefAttr {
        String alignBegin = "alignBegin";

        String alignEnd = "alignEnd";

        String dbRefAlignBegin = "dbRefAlignBegin";

        String dbRefAlignEnd = "dbRefAlignEnd";

        String threadingScore = "threadingScore";

        String threadingProg = "threadingProg";

        String details = "details";
    }

    public interface TargetDbRefRoles {
        String dbRef = "dbRef";

        String target = "target";
    }

    public interface DbNameAttr {
        String url = "url";

        String name = "name";

        String fullName = "fullName";

        String details = "details";
    }

    public interface DbNameRoles {
        String dbRefs = "dbRefs";
    }

    public interface DbRefAttr {
        String url = "url";

        String release = "release";

        String code = "code";

        String details = "details";
    }

    public interface DbRefRoles {
        String dbName = "dbName";

        String componentDbRefs = "componentDbRefs";

        String targetGroups = "targetGroups";

        String blueprintDbRefs = "blueprintDbRefs";

        String targets = "targets";
    }

    public interface NaturalSourceAttr {
        String organismName = "organismName";

        String ncbiTaxonomyId = "ncbiTaxonomyId";

        String scientificName = "scientificName";

        String genus = "genus";

        String species = "species";

        String organismAcronym = "organismAcronym";

        String atccNumber = "atccNumber";

        String ictvCode = "ictvCode";

        String strain = "strain";

        String fraction = "fraction";

        String cellLine = "cellLine";

        String cellLocation = "cellLocation";

        String cellType = "cellType";

        String geneMnemonic = "geneMnemonic";

        String organ = "organ";

        String organelle = "organelle";

        String tissue = "tissue";

        String variant = "variant";

        String subVariant = "subVariant";

        String plasmidDetails = "plasmidDetails";

        String secretion = "secretion";

        String details = "details";
    }

    public interface NaturalSourceRoles {
        String molSeqFragments = "molSeqFragments";

        String molecules = "molecules";

        String targets = "targets";

        String entries = "entries";
    }

    public interface JournalCitationAttr {
        String journalAbbreviation = "journalAbbreviation";

        String journalFullName = "journalFullName";

        String volume = "volume";

        String issue = "issue";

        String astm = "astm";

        String issn = "issn";

        String csd = "csd";

        String serial = "serial";

        String keywords = "keywords";

        String title = "title";

        String status = "status";

        String casAbstractCode = "casAbstractCode";

        String medlineUiCode = "medlineUiCode";

        String pubMedId = "pubMedId";

        String firstPage = "firstPage";

        String lastPage = "lastPage";

        String year = "year";

        String details = "details";

        String isPrimary = "isPrimary";
    }

    public interface CitationRoles {

        String authors = "authors";

        String editors = "editors";

        String molecules = "molecules";

        String nmrProbes = "nmrProbes";

        String nmrSpectrometers = "nmrSpectrometers";

        String methods = "methods";

        String softwares = "softwares";

        String molSystems = "molSystems";

        String shiftReferences = "shiftReferences";

        String ratioShiftReferences = "ratioShiftReferences";

        String dataLists = "dataLists";

        String experiments = "experiments";

        String sampleConditionSets = "sampleConditionSets";

        String targets = "targets";

        String protocols = "protocols";
    }

    public interface AnnotationAttr {
        String name = "name";

        String description = "description";

        String path = "path";

        String date = "date";

        String details = "details";
    }

    public interface AnnotationRoles {
        String author = "author";

        String experiments = "experiments";

        String targets = "targets";

        String url = "url";
    }

    public interface UrlAttr {
        String name = "name";

        String user = "user";

        String password = "password";

        String host = "host";

        String path = "path";

        String protocol = "protocol";
    }

    public interface PersonAttr {
        String familyName = "familyName";

        String givenName = "givenName";

        String middleInitials = "middleInitials";

        String familyTitle = "familyTitle";

        String title = "title";
    }

    public interface PersonRoles {
        String currentGroup = "currentGroup";

        String personInGroups = "personInGroups";

        String authorCitations = "authorCitations";

        String editorCitations = "editorCitations";

        String targets = "targets";

        String authoredEntries = "authoredEntries";

        String contactEntries = "contactEntries";

        String createdProtocols = "createdProtocols";

        String editedProtocols = "editedProtocols";

        String createdExps = "createdExps";

        String editedExps = "editedExps";
    }

    public interface ThesisCitationAttr {
        String institution = "institution";

        String city = "city";

        String stateProvince = "stateProvince";

        String country = "country";

        String keywords = "keywords";

        String title = "title";

        String status = "status";

        String casAbstractCode = "casAbstractCode";

        String medlineUiCode = "medlineUiCode";

        String pubMedId = "pubMedId";

        String firstPage = "firstPage";

        String lastPage = "lastPage";

        String year = "year";

        String details = "details";

        String isPrimary = "isPrimary";
    }

    public interface BookCitationAttr {
        String bookTitle = "bookTitle";

        String volume = "volume";

        String bookSeries = "bookSeries";

        String publisher = "publisher";

        String publisherCity = "publisherCity";

        String isbn = "isbn";

        String keywords = "keywords";

        String title = "title";

        String status = "status";

        String casAbstractCode = "casAbstractCode";

        String medlineUiCode = "medlineUiCode";

        String pubMedId = "pubMedId";

        String firstPage = "firstPage";

        String lastPage = "lastPage";

        String year = "year";

        String details = "details";

        String isPrimary = "isPrimary";

    }

    public interface LocationAttr {
        String location = "location";

        String room = "room";

        String temperature = "temperature";

        String tempDisplayUnit = "tempDisplayUnit";

        String pressure = "pressure";

        String pressureDisplayUnit = "pressureDisplayUnit";

    }

    public interface LocationRoles {
        String organisation = "organisation";

        String holderLocations = "holderLocations";

        String sampleLocations = "sampleLocations";
    }

    public interface OrganisationRoles {
        String name = "name";

        String address = "address";

        String city = "city";

        String country = "country";

        String emailAddress = "emailAddress";

        String url = "url";
    }

    public interface OrganisationAttr {
        String groups = "groups";

        String entries = "entries";
    }

    public interface UserAttr {
        String name = "name";
    }

    public interface UserRoles {

        String ledGroups = "ledGroups"; // (0-*)

        String userGroups = "userGroups"; // (0-*)

        String person = "person"; // (0-1)
    }

    public interface UserGroupAttr {
        String name = "name";
    }

    public interface UserGroupRoles {

        String permissions = "permissions"; // (0-*)
    }

    public interface AccessObjectAttr {
        String name = "name";

        String description = "description";
    }

    public interface PermissionAttr {
        String permissionClass = "permissionClass";

        String opType = "opType"; // one_of(create,read,update,delete)

        String roleName = "roleName";

        String permission = "permission"; // Boolean
    }

    public interface PermissionRoles {

        String accessObject = "accessObject"; // (1-1)

        String userGroup = "userGroup"; // (1-1)
    }

}
