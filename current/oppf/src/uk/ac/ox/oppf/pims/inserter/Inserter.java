/*     */ package uk.ac.ox.oppf.pims.inserter;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.biojava.bio.symbol.IllegalAlphabetException;
/*     */ import org.biojava.bio.symbol.IllegalSymbolException;
/*     */ import org.hibernate.CacheMode;
/*     */ import org.hibernate.FlushMode;
/*     */ import org.hibernate.Query;
/*     */ import org.hibernate.ScrollableResults;
/*     */ import org.hibernate.Session;
/*     */ import org.pimslims.dao.AbstractModel;
/*     */ import org.pimslims.dao.ModelImpl;
/*     */ import org.pimslims.dao.WritableVersion;
/*     */ import org.pimslims.exception.AbortedException;
/*     */ import org.pimslims.exception.AccessException;
/*     */ import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.Target;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import uk.ac.ox.oppf.optic.HibernateUtils;
/*     */ import uk.ac.ox.oppf.optic.model.Construct;
/*     */ import uk.ac.ox.oppf.optic.model.GenbankInfo;
/*     */ import uk.ac.ox.oppf.optic.model.Species;
/*     */ import uk.ac.ox.oppf.optic.model.dao.GenbankInfoDAO;
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
/*     */ public class Inserter
/*     */   extends AbstractInserter
/*     */ {
/*  43 */   private static final Logger log = LoggerFactory.getLogger(Inserter.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  48 */   private static Inserter instance = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   private Session session = null;
/*     */   
/*     */ 
/*     */   public Inserter() {}
/*     */   
/*     */ 
/*     */   public static Inserter getInstance()
/*     */   {
/*  61 */     if (instance == null) {
/*  62 */       instance = new Inserter();
/*     */     }
/*  64 */     return instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Session getSession()
/*     */   {
/*  73 */     if ((this.session == null) || (!this.session.isOpen())) {
/*  74 */       this.session = GenbankInfoDAO.getInstance().getSession();
/*     */     }
/*  76 */     return this.session;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void closeSession()
/*     */   {
/*  83 */     if ((this.session != null) && (this.session.isOpen())) {
/*  84 */       this.session.close();
/*  85 */       this.session = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void insertConstructs(WritableVersion version, Collection<String> constructs)
/*     */     throws SQLException
/*     */   {
/*  92 */     HibernateUtils.initialize();
/*  93 */     Session sess = getSession();
/*     */     try
/*     */     {
/*  96 */       log.info("ConstructAdaptor");
/*     */       
/*  98 */       for (Iterator<String> it = constructs.iterator(); it.hasNext();) {
/*  99 */         String s = (String)it.next();
/*     */         
/*     */ 
/* 102 */         ScrollableResults results = sess.createQuery("from Construct as construct where construct_id = " + s).setCacheMode(CacheMode.IGNORE).setFlushMode(FlushMode.COMMIT).scroll();
/* 103 */         while (results.next())
/*     */         {
/* 105 */           Construct construct = (Construct)results.get(0);
/*     */           
/*     */ 
/* 108 */           log.info("insertConstruct [" + construct.getConstructId() + ":" + construct.getDescription() + "]");
/*     */         }
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/*     */       Iterator<String> it;
/* 115 */       if (sess.isOpen()) {
/* 116 */         sess.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void insertTargets(WritableVersion version, Collection<String> targets
		) throws SQLException, AccessException, ClassNotFoundException, IllegalAlphabetException, IllegalSymbolException
/*     */   {
/* 123 */     HibernateUtils.initialize();
/* 124 */     Session sess = getSession();
/*     */     
/* 126 */     log.info("TargetAdaptor>");
/*     */     try
/*     */     {
/* 129 */       for (String s : targets)
/*     */       {
/*     */ 
/* 132 */         List<GenbankInfo> results = sess.createQuery("from GenbankInfo as genbankInfo where id = " + s).list();
/* 133 */         log.info("ScrollableResults [" + results.size() + "]");
/* 134 */         for (GenbankInfo target : results)
/*     */         {
/*     */           try
/*     */           {
/* 138 */             insertTarget(version, target, null ); //TODO needs ConstructWriter
/*     */           }
/*     */           catch (ConstraintException e)
/*     */           {
/* 162 */             System.out.println("TargetInserter ConstraintException caught [" + target.getId() + ":" + e.getLocalizedMessage() + "]");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 169 */       if (sess.isOpen())
/* 170 */         sess.close();
/*     */     }
/* 172 */     HibernateUtils.close();
/*     */   }
/**
 * @param version
 * @param target
 * @param pimsWriter TODO
 * @throws AccessException
 * @throws ConstraintException
 * @throws IllegalSymbolException
 * @throws IllegalAlphabetException
 * @throws ClassNotFoundException
 * @throws SQLException
 */
public org.pimslims.model.target.Target insertTarget(WritableVersion version, GenbankInfo target, ConstructWriterI pimsWriter) throws AccessException, ConstraintException,
		IllegalSymbolException, IllegalAlphabetException,
		ClassNotFoundException, SQLException {
	log.info("insertTarget [" + target.getId() + ":" + target.getDescription() + "]");
	/* 139 */             TargetAdaptor targetAdaptor = new TargetAdaptor(version);
	/* 140 */             Target target2 = targetAdaptor.insert(target);
	/*     */             
	/*     */ 
	/* 143 */             log.info("number of constructs [" + target.getConstructs().size() + "]");
	/* 144 */             Collection<Construct> constructs = target.getConstructs();
	/* 145 */             for (Iterator<Construct> iter = constructs.iterator(); iter.hasNext();) {
	/* 146 */               Construct construct = (Construct)iter.next();
	/* 147 */               log.info("insertConstruct [" + construct.getAuth() + ":" + construct.getConstructId() + ":" + construct.getDescription() + "]");
	/* 148 */               if (construct.getAuth().byteValue() != 255) {
	/* 149 */                 ConstructAdaptor constructAdaptor = new ConstructAdaptor(version);
	/* 150 */                 constructAdaptor.insert(construct);
	/*     */               }
	/*     */             }
	/*     */             
	/*     */ 
	/* 155 */             Species species = target.getSpecies();
	/* 156 */             log.info("insertSpecies [" + species.getSpeciesId() + ":" + species.getCommon() + "]");
	/* 157 */             SpeciesInserter speciesInserter = new SpeciesInserter(version);
	/* 158 */             speciesInserter.insert(species);
	return target2;
}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void insertAll(WritableVersion version)
/*     */   {
/* 184 */     Session sess = getSession();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 192 */       log.info("RefDataInserter>");
/* 193 */       RefDataInserter.getInstance().insertAll(version);
/* 194 */       version.getSession().flush();
/*     */       
/*     */ 
/* 197 */       log.info("OrganisationInserter>");
/* 198 */       OrganisationInserter.getInstance().insertAll(version, sess);
/* 199 */       version.getSession().flush();
/*     */       
/*     */ 
/* 202 */       log.info("PersonInserter>");
/* 203 */       PersonInserter.getInstance().insertAll(version, sess);
/* 204 */       version.getSession().flush();
/*     */       
/*     */ 
/* 207 */       log.info("SpeciesInserter>");
/* 208 */       SpeciesInserter.getInstance(version).insertAll(sess);
/* 209 */       version.getSession().flush();
/*     */       
/*     */ 
/*     */ 
/* 213 */       log.info("TargetAdaptor>");
/* 214 */       TargetAdaptor.getInstance(version).insertAll(sess);
/*     */       
/* 216 */       log.info("TargetGroupInserter>");
/* 217 */       TargetGroupInserter.getInstance().insertAll(version, sess);
/* 218 */       version.commit();
/*     */       
/* 220 */       log.info("ConstructAdaptor>");
/* 221 */       ConstructAdaptor.getInstance(version).insertAll(sess);
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 226 */       log.error("Caught exception!", e);
/*     */       
/*     */ 
/* 229 */       throw new RuntimeException(e);
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 234 */       if (sess.isOpen()) { sess.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void insertRefData(WritableVersion version)
/*     */   {
/* 243 */     HibernateUtils.initialize();
/* 244 */     Session sess = getSession();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 252 */       log.info("RefDataInserter>");
/* 253 */       RefDataInserter.getInstance().insertAll(version);
/* 254 */       version.getSession().flush();
/*     */       
/*     */ 
/* 257 */       log.info("OrganisationInserter>");
/* 258 */       OrganisationInserter.getInstance().insertAll(version, sess);
/* 259 */       version.getSession().flush();
/*     */       
/*     */ 
/* 262 */       log.info("PersonInserter>");
/* 263 */       PersonInserter.getInstance().insertAll(version, sess);
/* 264 */       version.getSession().flush();
/*     */       
/*     */ 
/* 267 */       log.info("SpeciesInserter>");
/* 268 */       SpeciesInserter.getInstance(version).insertAll(sess);
/* 269 */       version.getSession().flush();
/*     */       
/*     */ 
/*     */ 
/* 273 */       log.info("TargetGroupInserter>");
/* 274 */       TargetGroupInserter.getInstance().insertAll(version, sess);
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 279 */       log.error("Caught exception!", e);
/*     */       
/*     */ 
/* 282 */       throw new RuntimeException(e);
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 287 */       if (sess.isOpen()) { sess.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 301 */     if (args.length == 0) {
/* 302 */       System.out.println("Usage: Inserter [-a] [-r] [-t target] [-c construct]");
/*     */     }
/*     */     
/* 305 */     boolean allFlag = false;
/* 306 */     boolean refFlag = false;
/* 307 */     Collection<String> targets = new ArrayList();
/* 308 */     Collection<String> constructs = new ArrayList();
/* 309 */     for (int i = 0; i < args.length; i++)
/*     */     {
/* 311 */       String s = args[i];
/* 312 */       if (s.charAt(0) == '-') {
/* 313 */         char ch = s.charAt(1);
/* 314 */         switch (ch)
/*     */         {
/*     */         case 'a': 
/* 317 */           allFlag = true;
/* 318 */           break;
/*     */         
/*     */         case 'c': 
/* 321 */           allFlag = false;
/* 322 */           constructs.add(args[(++i)]);
/* 323 */           break;
/*     */         
/*     */         case 'r': 
/* 326 */           refFlag = true;
/* 327 */           break;
/*     */         
/*     */         case 't': 
/* 330 */           allFlag = false;
/* 331 */           targets.add(args[(++i)]);
/* 332 */           break;
/*     */         
/*     */         default: 
/* 335 */           i++;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/* 341 */     AbstractModel model = ModelImpl.getModel();
/* 342 */     WritableVersion version = model.getWritableVersion("administrator");
/*     */     
/*     */     try
/*     */     {
/* 346 */       if (allFlag) {
/* 347 */         getInstance().insertAll(version);
/*     */       }
/* 349 */       else if (refFlag) {
/* 350 */         getInstance().insertRefData(version);
/*     */       }
/*     */       else
/*     */       {
/* 354 */         Inserter inserter = new Inserter();
/* 355 */         if (!targets.isEmpty()) {
/* 356 */           inserter.insertTargets(version, targets);
/*     */         }
/* 358 */         if (!constructs.isEmpty()) {
/* 359 */           inserter.insertConstructs(version, constructs);
/*     */         }
/*     */       }
/* 362 */       version.commit();
/*     */     }
/*     */     catch (AbortedException e) {
/* 365 */       e.printStackTrace();
/*     */     }
/*     */     catch (ConstraintException e) {
/* 368 */       e.printStackTrace();
/*     */     }
/*     */     catch (SQLException e) {
/* 371 */       e.printStackTrace();
/*     */     }
/*     */     catch (Exception e) {
/* 374 */       e.printStackTrace();
/*     */     }
/*     */     finally {
/* 377 */       if (!version.isCompleted())
/*     */       {
/* 379 */         version.abort();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\cm65\sow7\oppf\lib\pims-oppf.jar
 * Qualified Name:     uk.ac.ox.oppf.pims.inserter.Inserter
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.0.1
 */