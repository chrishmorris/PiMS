/*     */ package uk.ac.ox.oppf.pims.inserter;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.sql.SQLException;
/*     */ import org.biojava.bio.seq.DNATools;
/*     */ import org.biojava.bio.seq.RNATools;
/*     */ import org.biojava.bio.symbol.IllegalAlphabetException;
/*     */ import org.biojava.bio.symbol.IllegalSymbolException;
/*     */ import org.biojava.bio.symbol.SymbolList;
/*     */ import org.hibernate.CacheMode;
/*     */ import org.hibernate.FlushMode;
/*     */ import org.hibernate.Query;
/*     */ import org.hibernate.ScrollableResults;
/*     */ import org.hibernate.Session;
/*     */ import org.pimslims.dao.WritableVersion;
/*     */ import org.pimslims.exception.AbortedException;
/*     */ import org.pimslims.exception.AccessException;
/*     */ import org.pimslims.exception.ConstraintException;
/*     */ import org.pimslims.metamodel.ModelObject;
/*     */ import org.pimslims.model.target.ResearchObjective;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import uk.ac.ox.oppf.optic.model.Construct;
/*     */ import uk.ac.ox.oppf.optic.model.FwdTag;
/*     */ import uk.ac.ox.oppf.optic.model.GenbankInfo;
/*     */ import uk.ac.ox.oppf.optic.model.RevTag;
/*     */ import uk.ac.ox.oppf.pims.optic.ConstructInserter;
/*     */ import uk.ac.ox.oppf.pims.optic.beans.ConstructBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstructAdaptor
/*     */   extends AbstractInserter
/*     */ {
/*  36 */   public static final Byte AUTHORIZED_BYTE = new Byte((byte)1);
/*  37 */   public static final Byte FAILED_BYTE = new Byte((byte)-1);
/*     */   
/*     */   public static final String AUTHORIZED = "Authorized";
/*     */   
/*     */   public static final String FAILED = "Failed";
/*     */   
/*     */   private static final int STOP_SYMBOL = 42;
/*     */   
/*  45 */   private static final Logger log = LoggerFactory.getLogger(ConstructAdaptor.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Session session;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ConstructInserter inserter;
/*     */   
/*     */ 
/*     */ 
/*     */   private static ConstructAdaptor instance;
/*     */   
/*     */ 
/*     */ 
/*     */   private WritableVersion version;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConstructAdaptor getInstance(WritableVersion version)
/*     */   {
/*  70 */     log.info("getInstance [WritableVersion version]");
/*  71 */     if (instance == null) {
/*  72 */       instance = new ConstructAdaptor(version);
/*     */     }
/*  74 */     return instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ConstructAdaptor(WritableVersion version)
/*     */   {
/*  81 */     log.info("[WritableVersion version]");
/*  82 */     this.version = version;
/*  83 */     this.inserter = new ConstructInserter();
/*  84 */     init();
/*     */   }
/*     */   
/*     */   public ConstructAdaptor(WritableVersion version, Session session) {
/*  88 */     log.info("[WritableVersion version, Session session]");
/*  89 */     this.version = version;
/*  90 */     this.session = session;
/*  91 */     this.inserter = new ConstructInserter();
/*  92 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstructBean createConstructBean(Construct c)
/*     */     throws IllegalSymbolException, IllegalAlphabetException
/*     */   {
/* 114 */     GenbankInfo t = c.getGenbankInfo();
/*     */     
/*     */ 
/* 117 */     SymbolList dna = DNATools.createDNA(t.getDnaSequence().substring(c.getStart().intValue() - 1, c.getStop().intValue()));
/*     */     
/*     */ 
/* 120 */     String fwdPrimerAnneal = dna.subStr(1, c.getFwdAnnealLen().intValue());
/*     */     
/*     */ 
/* 123 */     SymbolList symL = dna.subList(dna.length() - c.getRevAnnealLen().intValue() + 1, dna.length());
/* 124 */     symL = DNATools.reverseComplement(symL);
/* 125 */     String revPrimerAnneal = symL.seqString();
/* 133 */     SymbolList prot = longestORF(dna);
/*     */     
/*     */ 
/* 136 */     Integer[] startStop = findRelativeProteinStartStop(t.getProteinSequence(), prot.seqString());
/*     */     
/*     */ 
/* 139 */     ConstructBean cb = new ConstructBean();
/* 140 */     cb.setTargetLocalName(TargetAdaptor.deriveLocalName(t));
/*     */     
/*     */ 
/* 143 */     cb.setConstructId("OPPF" + c.getConstructId().toString());
/* 144 */     cb.setDescription(c.getDescription());
/*     */     
/* 146 */     cb.setDnaSeq(dna.seqString());
/* 147 */     cb.setProtSeq(prot.seqString());
/*     */     
/* 149 */     System.out.println("Forward Primer [" + c.getFwdTag().getFwdTagSeq() + "." + fwdPrimerAnneal + "]");
/* 150 */     cb.setFwdPrimer(c.getFwdTag().getFwdTagSeq() + fwdPrimerAnneal);
/* 151 */     cb.setRevPrimer(c.getRevTag().getRevTagSeq() + revPrimerAnneal);
/*     */     
/* 153 */     cb.setFwdTag(c.getFwdTag().getFwdTagName());
/* 154 */     cb.setRevTag(c.getRevTag().getRevTagName());
/*     */     
/* 156 */     cb.setFwdOverlapLen(c.getFwdAnnealLen());
/* 157 */     cb.setRevOverlapLen(c.getRevAnnealLen());
/*     */     
/* 159 */     cb.setFwdOverlapSeq(fwdPrimerAnneal);
/* 160 */     cb.setRevOverlapSeq(revPrimerAnneal);
/*     */ 
/* 165 */     cb.setTargetProtEnd(startStop[1]);
/* 166 */     cb.setTargetProtStart(startStop[0]);
/*     */     
/* 168 */     log.info("PersonInserter [" + c.getPickedBy() + ":" + this.session + "]");
/* 169 */     cb.setPickedBy(PersonInserter.getUserByOsLogin(this.version, c.getPickedBy(), this.session));
/* 170 */     cb.setPickedAt(c.getPickedAt());
/* 171 */     cb.setManual(c.isManual());
/*     */     
/* 173 */     if (c.getAuthBy() != null) {
/* 174 */       cb.setAuthBy(PersonInserter.getPersonByOsLogin(this.version, c.getAuthBy(), this.session));
/* 175 */       cb.setAuthAt(c.getAuthAt());
/* 176 */       cb.setAuth(getAuth(c.getAuth()));
/*     */     }
/*     */     
/*     */ 
/* 180 */     return cb;
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
/*     */   public SymbolList longestORF(SymbolList dna)
/*     */     throws IllegalAlphabetException
/*     */   {
/* 201 */     int end = dna.length();
/* 202 */     int overhang = end % 3;
/* 205 */     SymbolList prot3; SymbolList prot1;
SymbolList prot2;
if (overhang == 0) {
/* 206 */       prot1 = trimProtein(RNATools.translate(DNATools.toRNA(dna.subList(1, end))));
/* 207 */       prot2 = trimProtein(RNATools.translate(DNATools.toRNA(dna.subList(2, end - 2))));
/* 208 */       prot3 = trimProtein(RNATools.translate(DNATools.toRNA(dna.subList(3, end - 1))));
/*     */     }
/*     */     else {
/* 212 */       if (overhang == 1) {
/* 213 */         prot1 = trimProtein(RNATools.translate(DNATools.toRNA(dna.subList(1, end - 1))));
/* 214 */         prot2 = trimProtein(RNATools.translate(DNATools.toRNA(dna.subList(2, end))));
/* 215 */         prot3 = trimProtein(RNATools.translate(DNATools.toRNA(dna.subList(3, end - 2))));
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 220 */         prot1 = trimProtein(RNATools.translate(DNATools.toRNA(dna.subList(1, end - 2))));
/* 221 */         prot2 = trimProtein(RNATools.translate(DNATools.toRNA(dna.subList(2, end - 1))));
/* 222 */         prot3 = trimProtein(RNATools.translate(DNATools.toRNA(dna.subList(3, end))));
/*     */       }
/*     */     }
/*     */     
/* 226 */     SymbolList prot = prot1;
/* 227 */     if (prot2.length() > prot.length()) prot = prot2;
/* 228 */     if (prot3.length() > prot.length()) { prot = prot3;
/*     */     }
/*     */     
/* 231 */     return prot;
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
/*     */   private SymbolList trimProtein(SymbolList protein)
/*     */   {
/* 244 */     int stop = protein.seqString().indexOf('*');
/* 245 */     if (stop < 0) return protein;
/* 246 */     if (stop == 0) return SymbolList.EMPTY_LIST;
/* 247 */     return protein.subList(1, stop);
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
/*     */   public Integer[] findRelativeProteinStartStop(String targetSeq, String constructSeq)
/*     */   {
/* 264 */     int start = -1;
/* 265 */     int first = 0;
/* 266 */     int matchLen = constructSeq.length();
/* 267 */     for (first = 0; first < 3; first++)
/*     */     {
/*     */ 
/* 270 */       for (matchLen = constructSeq.length() - first; matchLen >= 10; matchLen--) {
/* 271 */         start = targetSeq.indexOf(constructSeq.substring(first, matchLen));
/* 272 */         if (start > -1) break;
/*     */       }
/* 274 */       if (start > -1) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 279 */     if (start < 0) { return new Integer[] { new Integer(-1), new Integer(-1) };
/*     */     }
/*     */     
/* 282 */     int end = start + matchLen - first;
/*     */     
/*     */ 
/* 285 */     return new Integer[] { new Integer(start), new Integer(end) };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAuth(Byte auth)
/*     */   {
/* 296 */     if (auth == null) return null;
/* 297 */     if (AUTHORIZED_BYTE.equals(auth)) return "Authorized";
/* 298 */     if (FAILED_BYTE.equals(auth)) return "Failed";
/* 299 */     return null;
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
/*     */   public ModelObject insert(Construct c)
/*     */     throws IllegalSymbolException, IllegalAlphabetException, AccessException, ConstraintException, ClassNotFoundException, SQLException
/*     */   {
/* 316 */     log.info("insertConstruct [" + c.getConstructId() + ":" + c.getDescription() + "]");
/* 317 */     ConstructBean b = createConstructBean(c);
/* 318 */     if (b == null) {
/* 319 */       /*log.warn("ConstructAdaptor> Bad construct: OPPF" + c.getConstructId().toString());
      return null; */
throw new IllegalArgumentException("ConstructAdaptor> Bad construct: OPPF" + c.getConstructId().toString());
/*     */     }
/*     */     
/* 323 */     String id = b.getConstructId(); 
assert id.equals( "OPPF" + c.getConstructId());
/* 324 */     ModelObject myConstruct = this.version.findFirst(ResearchObjective.class, "localName", id);
/* 325 */     if (null != myConstruct) {
/*        System.out.println("ConstructAdaptor Exception caught [" + id + ":construct already exists]");
       return null; */
throw new IllegalArgumentException("ConstructAdaptor Exception caught [" + id + ":construct already exists]");

/*     */     }
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
/* 352 */     this.inserter.setConstructBean(b);
/* 353 */     this.inserter.initRefData(this.version);
/* 354 */     return this.inserter.createExpBlueprint(this.version);
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
/*     */   public void insertAll(Session sess)
/*     */     throws IllegalSymbolException, IllegalAlphabetException, AccessException, ClassNotFoundException, ConstraintException, AbortedException, SQLException
/*     */   {
/* 373 */     this.session = sess;
/*     */     
/*     */ 
/* 376 */     int i = 0;
/*     */     
/*     */ 
/* 379 */     log.info("Starting search");
/*     */     
/*     */ 
/* 382 */     ScrollableResults constructs = sess.createQuery("from Construct as construct order by construct_id").setCacheMode(CacheMode.IGNORE).setFlushMode(FlushMode.COMMIT).scroll();
/*     */     
/*     */ 
/* 385 */     log.info("Starting inserts");
/* 386 */     while (constructs.next())
/*     */     {
/*     */ 
/* 389 */       Construct c = (Construct)constructs.get(0);
/*     */       
/*     */ 
/* 392 */       insert(c);
/*     */       
/*     */ 
/* 395 */       i++;
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
/* 412 */       sess.evict(c);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\cm65\sow7\oppf\lib\pims-oppf.jar
 * Qualified Name:     uk.ac.ox.oppf.pims.inserter.ConstructAdaptor
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.0.1
 */