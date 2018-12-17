/*     */ package uk.ac.ox.oppf.pims.optic;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.biojava.bio.seq.DNATools;
/*     */ import org.biojava.bio.symbol.IllegalAlphabetException;
/*     */ import org.biojava.bio.symbol.IllegalSymbolException;
/*     */ import org.biojava.bio.symbol.SymbolList;
/*     */ import org.pimslims.dao.WritableVersion;
/*     */ import org.pimslims.exception.AccessException;
/*     */ import org.pimslims.exception.ConstraintException;
/*     */ import org.pimslims.metamodel.ModelObject;
/*     */ import org.pimslims.model.molecule.Molecule;
/*     */ import org.pimslims.model.target.ResearchObjective;
/*     */ import org.pimslims.model.target.ResearchObjectiveElement;
/*     */ import org.pimslims.model.target.Target;
/*     */ import org.pimslims.presentation.PrimerBean;
/*     */ import org.pimslims.presentation.TargetBean;
/*     */ import org.pimslims.presentation.construct.ConstructAnnotator;
/*     */ import org.pimslims.presentation.construct.ConstructBeanWriter;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class ConstructInserter
/*     */ {
/*  46 */   private static final Logger log = LoggerFactory.getLogger(ConstructInserter.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private uk.ac.ox.oppf.pims.optic.beans.ConstructBean construct;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ModelObject expBlueprint;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ModelObject blueprintComponent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstructInserter() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstructInserter(uk.ac.ox.oppf.pims.optic.beans.ConstructBean construct)
/*     */   {
/*  75 */     setConstructBean(construct);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstructInserter(WritableVersion wv)
/*     */     throws AccessException, ConstraintException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstructInserter(uk.ac.ox.oppf.pims.optic.beans.ConstructBean construct, WritableVersion wv)
/*     */     throws AccessException, ConstraintException
/*     */   {
/*  97 */     setConstructBean(construct);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public uk.ac.ox.oppf.pims.optic.beans.ConstructBean getConstructBean()
/*     */   {
/* 105 */     return this.construct;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized void setConstructBean(uk.ac.ox.oppf.pims.optic.beans.ConstructBean construct)
/*     */   {
/* 112 */     if (construct == null) throw new IllegalArgumentException("construct must not be null!");
/* 113 */     this.construct = construct;
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
/*     */   public ModelObject getExpBlueprint()
/*     */   {
/* 137 */     return this.expBlueprint;
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
/*     */   public synchronized void initRefData(WritableVersion version)
/*     */     throws AccessException, ConstraintException
/*     */   {}
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
/*     */   public synchronized ResearchObjective createExpBlueprint(WritableVersion version)
/*     */     throws AccessException, ClassNotFoundException, ConstraintException, SQLException
/*     */   {
/* 173 */     if (this.construct == null) throw new IllegalArgumentException("construct must not be null!");
/* 174 */     if (version == null) { throw new IllegalArgumentException("wv must not be null!");
/*     */     }
/*     */     
/* 177 */     this.expBlueprint = null;
/*     */     
/*     */ 
/* 180 */     if ((null == this.construct.getDescription()) || (0 == this.construct.getDescription().length())) {
/* 181 */       log.warn(this.construct.getConstructId() + " has null or zero-length description! Using 'Not stated' for whyChosen");
/* 182 */       this.construct.setDescription("Not stated");
/*     */     }
/*     */     
/* 185 */     Map<String, Object> m = new HashMap();
/* 186 */     m.put("commonName", this.construct.getConstructId());
/* 187 */     m.put("localName", this.construct.getConstructId());
/* 188 */     m.put("whyChosen", this.construct.getDescription());
/* 189 */     m.put("owner", this.construct.getPickedBy());
/* 190 */     this.expBlueprint = version.create(ResearchObjective.class, m);
/*     */     
/*     */ 
/* 193 */     createBlueprintComponent(version);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 198 */     ConstructBeanWriter.createPrimerDesignExperiment(version, pimsConstructBean(this.construct, (Target)findTarget(version)), (ResearchObjective)this.expBlueprint);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 207 */     return (ResearchObjective)this.expBlueprint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private org.pimslims.presentation.construct.ConstructBean pimsConstructBean(uk.ac.ox.oppf.pims.optic.beans.ConstructBean construct, Target target)
/*     */   {
/* 215 */     PrimerBean fwd = new PrimerBean(PrimerBean.FORWARD.booleanValue());
/* 216 */     PrimerBean rev = new PrimerBean(PrimerBean.REVERSE.booleanValue());
/* 217 */     fwd.setName(construct.getConstructId() + "F");
/* 218 */     rev.setName(construct.getConstructId() + "R");
/*     */     
/* 220 */     org.pimslims.presentation.construct.ConstructBean bean = new org.pimslims.presentation.construct.ConstructBean(new TargetBean(target), fwd, rev);
/*     */     
/*     */ 
/*     */ 
/* 224 */     bean.setConstructId(construct.getConstructId());
/* 225 */     bean.setDescription(construct.getDescription());
/* 226 */     bean.setDateOfEntry(construct.getPickedAt());
/*     */     
/* 228 */     bean.setForwardTag(construct.getFwdTag());
/* 229 */     bean.setFwdOverlapLen(construct.getFwdOverlapLen());
/* 230 */     bean.setFwdPrimer(construct.getFwdPrimer());
/*     */     
/* 232 */     bean.setReverseTag(construct.getRevTag());
/* 233 */     bean.setRevOverlapLen(construct.getRevOverlapLen());
/* 234 */     bean.setRevPrimer(construct.getRevPrimer());
/*     */     
/* 236 */     Collection<PrimerBean> primerBeans = bean.getPrimers();
/* 237 */     for (PrimerBean primerbean : primerBeans) {
/* 238 */       PrimerBean.annotatePrimer(primerbean);
/*     */     }
/*     */     
/* 241 */     return bean;
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
/*     */   private void createBlueprintComponent(WritableVersion version)
/*     */     throws AccessException, ConstraintException
/*     */   {
/* 256 */     if ((null == this.construct.getDescription()) || (0 == this.construct.getDescription().length())) {
/* 257 */       log.warn(this.construct.getConstructId() + " has null or zero-length description! Using 'Not stated' for whyChosen");
/* 258 */       this.construct.setDescription("Not stated");
/*     */     }
/*     */     
/*     */ 
/* 262 */     Molecule template = ConstructBeanWriter.createMolComp(version, "Template", this.construct.getDnaSeq(), "DNA", this.construct.getConstructId());
/*     */     
/*     */ 
/* 265 */     Map<String, Object> m = new HashMap();
/* 266 */     m.put("researchObjective", this.expBlueprint);
/* 267 */     m.put("componentType", "OpticConstruct");
/* 268 */     m.put("whyChosen", this.construct.getDescription());
/* 269 */     m.put("approxBeginSeqId", this.construct.getTargetProtStart());
/* 270 */     m.put("approxEndSeqId", this.construct.getTargetProtEnd());
/* 271 */     m.put("target", findTarget(version));
/* 272 */     m.put("molecule", template);
/* 273 */     this.blueprintComponent = version.create(ResearchObjectiveElement.class, m);
/*     */     
/* 275 */     ResearchObjectiveElement bc = (ResearchObjectiveElement)this.blueprintComponent;
/*     */     
/*     */ 
/* 278 */     bc.addTrialMolComponent(ConstructBeanWriter.createMolComp(version, "Protein", this.construct.getProtSeq(), "protein", this.construct.getConstructId()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 283 */     bc.addTrialMolComponent(ConstructBeanWriter.createMolComp(version, "Expressed Protein", this.construct.getProtSeq(), "protein", this.construct.getConstructId()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 288 */     bc.addTrialMolComponent(ConstructBeanWriter.createMolComp(version, "Final Protein", this.construct.getProtSeq(), "protein", this.construct.getConstructId()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 293 */     bc.addTrialMolComponent(ConstructBeanWriter.createMolComp(version, "PCR Product", getPCRProductSequence(this.construct), "DNA", this.construct.getConstructId()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String getPCRProductSequence(uk.ac.ox.oppf.pims.optic.beans.ConstructBean bean)
/*     */   {
/* 300 */     StringBuffer sb = new StringBuffer();
/* 301 */     sb.append(bean.getFwdPrimer().substring(0, bean.getFwdPrimer().length() - bean.getFwdOverlapLen().intValue()));
/* 302 */     sb.append(bean.getDnaSeq());
/*     */     
/*     */     try
/*     */     {
/* 306 */       SymbolList symL = DNATools.createDNA(bean.getRevPrimer().substring(0, bean.getRevPrimer().length() - bean.getRevOverlapLen().intValue()));
/*     */       
/*     */ 
/* 309 */       symL = DNATools.reverseComplement(symL);
/*     */       
/*     */ 
/* 312 */       sb.append(symL.seqString());
/*     */     }
/*     */     catch (IllegalSymbolException ex)
/*     */     {
/* 316 */       ex.printStackTrace();
/*     */     }
/*     */     catch (IllegalAlphabetException ex)
/*     */     {
/* 320 */       ex.printStackTrace();
/*     */     }
/*     */     
/* 323 */     return sb.toString().toUpperCase();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ModelObject findTarget(WritableVersion version)
/*     */     throws AccessException
/*     */   {
/* 335 */     if (this.construct.getTargetHook() != null)
/*     */     {
/* 337 */       ModelObject t = version.get(this.construct.getTargetHook());
/*     */       
/* 339 */       if (t == null) {
/* 340 */         throw new AccessException("Couldn't find the Target for this Construct! Have you stored this target? " + this.construct.getTargetHook());
/*     */       }
/* 342 */       return t;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 347 */     Map<String, Object> m = new HashMap();
/*     */     
/* 349 */     m.put("name", this.construct.getTargetLocalName());
/*     */     
/* 351 */     Collection<Target> c = version.findAll(Target.class, m);
/*     */     
/*     */ 
/* 354 */     if (c.isEmpty()) {
/* 355 */       throw new AccessException("Couldn't find the Target for this Construct! Have you stored this target? " + this.construct.getTargetLocalName());
/*     */     }
/*     */     
/* 358 */     return (ModelObject)c.iterator().next();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public class OpticPIMSMapping
/*     */   {
/*     */     public static final String primerDesignExpt = "Primer Design";
/*     */     
/*     */ 
/*     */     public static final String fwdTag = "Forward Tag";
/*     */     
/*     */ 
/*     */     public static final String revTag = "Reverse Tag";
/*     */     
/*     */ 
/*     */     public static final String fwdPrimer = "Forward Primer";
/*     */     
/*     */ 
/*     */     public static final String revPrimer = "Reverse Primer";
/*     */     
/*     */ 
/*     */     public static final String fwdPrimerTm = "Forward Primer Tm";
/*     */     
/*     */ 
/*     */     public static final String revPrimerTm = "Reverse Primer Tm";
/*     */     
/*     */ 
/*     */     public static final String fwdOverlap = "Forward Overlap";
/*     */     
/*     */ 
/*     */     public static final String revOverlap = "Reverse Overlap";
/*     */     
/*     */ 
/*     */     public static final String manual = "Manual Design";
/*     */     
/*     */ 
/*     */     public static final String application = "Optic";
/*     */     
/*     */ 
/*     */     public static final String description = "Description";
/*     */     
/*     */ 
/*     */     public static final String componentType = "OpticConstruct";
/*     */     
/*     */ 
/*     */     public static final String construct = "construct";
/*     */     
/*     */ 
/*     */     public static final String template = "Template";
/*     */     
/*     */ 
/*     */     public static final String pcrProduct = "PCRProduct";
/*     */     
/*     */ 
/*     */     public OpticPIMSMapping() {}
/*     */     
/*     */ 
/*     */     public String getCategory(String category)
/*     */     {
/* 418 */       if ("construct".equals(category)) return "construct";
/* 419 */       if ("template".equals(category)) return "Template";
/* 420 */       return "";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\cm65\sow7\oppf\lib\pims-oppf.jar
 * Qualified Name:     uk.ac.ox.oppf.pims.optic.ConstructInserter
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.0.1
 */