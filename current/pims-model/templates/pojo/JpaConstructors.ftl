    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
<#-- ************************************************************ -->
<#-- Empty constructor                                            -->
<#-- ************************************************************ -->
    /**
     * Empty constructor
     */
    protected ${pojo.getDeclarationName()}() {
        super();
    }

<#-- ************************************************************ -->
<#-- Constructor for PIMS WritableVersion                         -->
<#-- ************************************************************ -->
    /**
     * Constructor for PIMS WritableVersion
     */
    protected ${pojo.getDeclarationName()}(WritableVersion wVersion) throws org.pimslims.metamodel.ConstraintException {
        super(wVersion);
<#if pojo.shortName = "MemopsBaseClass">
        this.setAccess(this.get_Version().getCurrentUserAccess());
</#if>
    }

<#-- ************************************************************ -->
<#-- Constructor for required arguments without memops project    -->
<#-- ************************************************************ -->
<#assign arguments = getConstructorRequiredArgs(pojo) />
<#assign parentArgument = getConstructorParentArg(pojo) />
<#if arguments != "" || parentArgument != "">
    /**
     * Constructor for required arguments without memops project
     * assigned to default in init()
     */
    public ${pojo.getDeclarationName()}(WritableVersion wVersion${arguments}${parentArgument}) throws org.pimslims.metamodel.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        ${getConstructorRequiredArgsBody(pojo)}
        init(attributes);
    }

</#if>
<#-- ************************************************************ -->
<#-- Constructor with argument [name, value] in a map             -->
<#-- ************************************************************ -->
    /**
     * Constructor with argument [name, value] in a map
     */
    public ${pojo.getDeclarationName()}(WritableVersion wVersion, java.util.Map<String, Object> attributes) throws org.pimslims.metamodel.ConstraintException {
        this(wVersion);
        init(attributes);
    }
