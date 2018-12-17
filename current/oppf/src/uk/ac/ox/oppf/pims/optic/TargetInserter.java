/*     */ package uk.ac.ox.oppf.pims.optic;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import org.pimslims.dao.WritableVersion;
/*     */ import org.pimslims.exception.AccessException;
/*     */ import org.pimslims.exception.ConstraintException;
/*     */ import org.pimslims.metamodel.ModelObject;
/*     */ import org.pimslims.model.accessControl.User;
/*     */ import org.pimslims.model.core.Attachment;
/*     */ import org.pimslims.model.core.ExternalDbLink;
/*     */ import org.pimslims.model.molecule.Molecule;
/*     */ import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.Database;
/*     */ import org.pimslims.model.reference.Organism;
/*     */ import org.pimslims.model.target.Project;
/*     */ import org.pimslims.model.target.Target;
/*     */ import org.pimslims.model.target.TargetGroup;
/*     */ import uk.ac.ox.oppf.optic.model.OpticGroup;
/*     */ import uk.ac.ox.oppf.pims.optic.beans.TargetBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TargetInserter
/*     */ {
/*     */   private TargetBean targetBean;
/*  35 */   private Database opticName = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  40 */   private Database genBankName = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TargetInserter() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TargetInserter(TargetBean targetBean)
/*     */   {
/*  56 */     setTargetBean(targetBean);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TargetBean getTargetBean()
/*     */   {
/*  64 */     return this.targetBean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setTargetBean(TargetBean targetBean)
/*     */   {
/*  73 */     if (targetBean == null) throw new IllegalArgumentException("targetBean must not be null");
/*  74 */     this.targetBean = targetBean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Target create(WritableVersion version)
/*     */     throws AccessException, ConstraintException
/*     */   {
/* 100 */     if (this.targetBean == null) {
/* 101 */       throw new IllegalStateException("targetBean not set yet!");
/*     */     }
/* 103 */     System.out.println("TargetInserter.create [" + this.targetBean.getLocalName() + ":" + this.targetBean.getComments() + "]");
/* 104 */     Target target = null;
/*     */     
/*     */ 
/*     */ 
/* 108 */     User creator = findCreator(version);
/*     */     
/*     */ 
/* 111 */     Collection<Project> projects = findProjects(version);
/*     */     
/*     */ 
/* 114 */     Molecule protein = createProtein(version);
/*     */     
/*     */ 
/* 117 */     Collection<Molecule> dnas = createDnas(version);
/*     */     
/*     */ 
/* 120 */     Collection<TargetGroup> groups = createTargetGroups(version, this.targetBean.getGroups());
/*     */     
/*     */ 
/* 123 */     Organism organism = this.targetBean.getOrganism();
/*     */     
/*     */ 
/* 126 */     HashMap<String, Object> attr = new HashMap();
/* 127 */     attr.put("name", this.targetBean.getName());
/* 128 */     attr.put("creator", creator);
/* 129 */     attr.put("functionDescription", this.targetBean.getFuncDesc());
/* 130 */     attr.put("protein", protein);
/* 131 */     attr.put("whyChosen", this.targetBean.getComments());
/*     */     
/* 133 */     attr.put("nucleicAcids", dnas);
/* 134 */     attr.put("projects", projects);
/* 135 */     attr.put("species", organism);
/* 136 */     attr.put("targetGroups", groups);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */     System.out.println("TargetInserter.create target [" + this.targetBean.getName() + ":" + creator + ":" + organism + "]");
/* 147 */     target = (Target)version.create(Target.class, attr);
/*     */     
/*     */ 
/* 150 */     target.setAliasNames(this.targetBean.getAliases());
/*     */     
/* 152 */     Collection<Attachment> dbRefs = createDbRefs(version, target);
/* 153 */     target.setAttachments(dbRefs);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 162 */     return target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Database findDbName(WritableVersion version, String name)
/*     */   {
/* 175 */     HashMap<String, Object> dbNameAttributes = new HashMap();
/* 176 */     dbNameAttributes.put("name", name);
/*     */     
/* 178 */     Collection<Database> c = version.findAll(Database.class, dbNameAttributes);
/*     */     
/*     */ 
/* 181 */     if (c.isEmpty()) { return null;
/*     */     }
/*     */     
/* 184 */     return (Database)c.iterator().next();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ExternalDbLink createDbRef(WritableVersion version, ModelObject parent, Database dbName, String code, String details)
/*     */     throws AccessException, ConstraintException
/*     */   {
/* 203 */     if (dbName == null) { throw new IllegalArgumentException("dbName must not be null");
/*     */     }
/*     */     
/* 206 */     HashMap<String, Object> attr = new HashMap();
/*     */     
/*     */ 
/* 209 */     attr.put("dbName", dbName);
/* 210 */     attr.put("code", code);
/* 211 */     attr.put("details", details);
/* 212 */     attr.put("parentEntry", parent);
/*     */     
/*     */ 
/*     */ 
/* 216 */     return (ExternalDbLink)version.create(ExternalDbLink.class, attr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Collection<Attachment> createDbRefs(WritableVersion version, ModelObject parent)
/*     */     throws AccessException, ConstraintException
/*     */   {
/* 231 */     if (this.opticName == null) { this.opticName = findDbName(version, "OPTIC");
/*     */     }
/*     */     
/* 234 */     if (this.genBankName == null) { this.genBankName = findDbName(version, "GenBank");
/*     */     }
/*     */     
/* 237 */     ArrayList<Attachment> dbRefList = new ArrayList();
/*     */     
/*     */ 
/* 240 */     if (this.opticName != null)
/*     */     {
/* 242 */       String code = this.targetBean.getLocalName();
/* 243 */       if (code == null) code = "not specified";
/* 244 */       if (code.length() == 0) { code = "not specified";
/*     */       }
/*     */       
/* 247 */       dbRefList.add(createDbRef(version, parent, this.opticName, code, this.targetBean.getHyperlink()));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 252 */     if (this.genBankName != null)
/*     */     {
/*     */ 
/* 255 */       String code = this.targetBean.getGiNumber();
/* 256 */       if (code == null) code = "not specified";
/* 257 */       if (code.length() == 0) { code = "not specified";
/*     */       }
/*     */       
/* 260 */       dbRefList.add(createDbRef(version, parent, this.genBankName, code, this.targetBean.getGiNumber()));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 265 */     return dbRefList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private User findCreator(WritableVersion version)
/*     */   {
/* 283 */     HashMap<String, Object> attr = new HashMap();
/*     */     
/* 285 */     attr.put("person", this.targetBean.getPerson());
/* 286 */     User user = (User)version.findFirst(User.class, attr);
/* 287 */     return user;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Collection<Project> findProjects(WritableVersion version)
/*     */   {
/* 320 */     if (this.targetBean.getProject() != null)
/*     */     {
/*     */ 
/* 323 */       Collection<Project> projects = new ArrayList();
/* 324 */       projects.add(this.targetBean.getProject());
/* 325 */       return projects;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 330 */     HashMap<String, Object> attr = new HashMap();
/*     */     
/*     */ 
/* 333 */     System.out.println("TargetInserter findProjects serial=[" + this.targetBean.getProject() + ":" + this.targetBean.getProjectId() + "]");
/* 334 */     attr.put("serial", new Integer(this.targetBean.getProjectId()));
/*     */     
/*     */ 
/*     */ 
/* 338 */     Collection<Project> c = version.findAll(Project.class, attr);
/*     */     
/*     */ 
/* 341 */     if (c.isEmpty()) { return null;
/*     */     }
/*     */     
/* 344 */     this.targetBean.setProject((Project)c.iterator().next());
/*     */     
/*     */ 
/* 347 */     Collection<Project> projects = new ArrayList();
/* 348 */     projects.add(this.targetBean.getProject());
/* 349 */     return projects;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Molecule createProtein(WritableVersion version)
/*     */     throws AccessException, ConstraintException
/*     */   {
/* 364 */     ModelObject organism = findOrganism(version);
/*     */     
/*     */ 
/* 367 */     ComponentCategory category = findComponentCategory(version, "Protein");
/*     */     
/*     */ 
/* 370 */     HashMap<String, Object> attr = new HashMap();
/*     */     
/*     */ 
/* 373 */     attr.put("molType", "protein");
/* 374 */     attr.put("seqString", this.targetBean.getProtSeq());
/* 375 */     attr.put("name", this.targetBean.getCommonName());
/* 376 */     attr.put("naturalSource", organism);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 381 */     Molecule molComponent = (Molecule)version.create(Molecule.class, attr);
/* 382 */     if (null != category)
/* 383 */       molComponent.addCategory(category);
/* 384 */     return molComponent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Collection<Molecule> createDnas(WritableVersion version)
/*     */     throws AccessException, ConstraintException
/*     */   {
/* 400 */     Organism organism = findOrganism(version);
/*     */     
/*     */ 
/* 403 */     ComponentCategory category = findComponentCategory(version, "Nucleic acid");
/* 404 */     System.out.println("findComponentCategory [" + category + "]");
/*     */     
/*     */ 
/* 407 */     HashMap<String, Object> attr = new HashMap();
/*     */     
/*     */ 
/* 410 */     attr.put("molType", "DNA");
/* 411 */     attr.put("seqString", this.targetBean.getDnaSeq());
/* 412 */     attr.put("name", this.targetBean.getLocalName() + " DNA");
/* 413 */     attr.put("naturalSource", organism);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 418 */     Collection<Molecule> dnas = new ArrayList();
/*     */     
/* 420 */     Molecule molComponent = (Molecule)version.create(Molecule.class, attr);
/* 421 */     if (null != category) {
/* 422 */       molComponent.addCategory(category);
/*     */     }
/*     */     
/*     */ 
/* 426 */     dnas.add(molComponent);
/* 427 */     return dnas;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Organism findOrganism(WritableVersion version)
/*     */   {
/* 441 */     if (this.targetBean.getOrganism() != null) { return this.targetBean.getOrganism();
/*     */     }
/*     */     
/* 444 */     HashMap<String, Object> attr = new HashMap();
/*     */     
/*     */ 
/*     */ 
/* 448 */     attr.put("ncbiTaxonomyId", new Integer(this.targetBean.getOrganismId()));
/*     */     
/*     */ 
/* 451 */     Collection<Organism> c = version.findAll(Organism.class, attr);
/*     */     
/*     */ 
/* 454 */     if (c.isEmpty()) { return null;
/*     */     }
/*     */     
/* 457 */     this.targetBean.setOrganism((Organism)c.iterator().next());
/*     */     
/*     */ 
/* 460 */     return this.targetBean.getOrganism();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ComponentCategory findComponentCategory(WritableVersion version, String name)
/*     */   {
/* 472 */     HashMap<String, Object> attr = new HashMap();
/*     */     
/*     */ 
/* 475 */     attr.put("name", name);
/*     */     
/*     */ 
/* 478 */     Collection<ComponentCategory> c = version.findAll(ComponentCategory.class, attr);
/*     */     
/*     */ 
/* 481 */     if (c.isEmpty()) { return null;
/*     */     }
/*     */     
/* 484 */     return (ComponentCategory)c.iterator().next();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Collection<TargetGroup> createTargetGroups(WritableVersion version, Collection<OpticGroup> groups)
/*     */   {
/* 491 */     Collection<TargetGroup> targetGroups = new ArrayList();
/*     */     
/* 493 */     for (OpticGroup group : groups)
/*     */     {
/*     */ 
/* 496 */       HashMap<String, Object> attr = new HashMap();
/*     */       
/*     */ 
/* 499 */       attr.put("name", group.getName());
/* 500 */       Collection<TargetGroup> c = version.findAll(TargetGroup.class, attr);
/*     */       
/* 502 */       for (TargetGroup mo : c) {
/* 503 */         TargetGroup g = mo;
/* 504 */         targetGroups.add(g);
/*     */       }
/*     */     }
/*     */     
/* 508 */     return targetGroups;
/*     */   }
/*     */ }

/* Location:           C:\Users\cm65\sow7\oppf\lib\pims-oppf.jar
 * Qualified Name:     uk.ac.ox.oppf.pims.optic.TargetInserter
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.0.1
 */