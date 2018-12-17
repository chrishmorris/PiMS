/*     */ package uk.ac.ox.oppf.pims.inserter;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.hibernate.Session;
/*     */ import org.pimslims.dao.WritableVersion;
/*     */ import org.pimslims.exception.AccessException;
/*     */ import org.pimslims.exception.ConstraintException;
/*     */ import org.pimslims.metamodel.ModelObject;
/*     */ import org.pimslims.model.accessControl.User;
/*     */ import org.pimslims.model.people.Organisation;
/*     */ import uk.ac.ox.oppf.optic.model.Laboratory;
/*     */ import uk.ac.ox.oppf.optic.model.dao.PersonDAO;
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
/*     */ public class PersonInserter
/*     */   extends AbstractInserter
/*     */ {
/*  33 */   private static PersonInserter instance = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  38 */   private static Map<String, Integer> osLoginNameMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  44 */     osLoginNameMap.put("adham", new Integer(46));
/*  45 */     osLoginNameMap.put("alderton", new Integer(31));
/*  46 */     osLoginNameMap.put("andreas", new Integer(62));
/*  47 */     osLoginNameMap.put("anil", new Integer(35));
/*  48 */     osLoginNameMap.put("cameron", new Integer(61));
/*  49 */     osLoginNameMap.put("chrism", new Integer(27));
/*  50 */     osLoginNameMap.put("christian", new Integer(6));
/*  51 */     osLoginNameMap.put("clove", new Integer(48));
/*  52 */     osLoginNameMap.put("dave", new Integer(5));
/*  53 */     osLoginNameMap.put("daves", new Integer(25));
/*  54 */     osLoginNameMap.put("erika", new Integer(53));
/*  55 */     osLoginNameMap.put("geoff", new Integer(23));
/*  56 */     osLoginNameMap.put("gilbert", new Integer(63));
/*  57 */     osLoginNameMap.put("ian", new Integer(51));
/*  58 */     osLoginNameMap.put("jack", new Integer(38));
/*  59 */     osLoginNameMap.put("jan", new Integer(54));
/*  60 */     osLoginNameMap.put("joanne", new Integer(29));
/*  61 */     osLoginNameMap.put("joe", new Integer(49));
/*  62 */     osLoginNameMap.put("jon", new Integer(37));
/*  63 */     osLoginNameMap.put("jonathan", new Integer(3));
/*  64 */     osLoginNameMap.put("karl", new Integer(17));
/*  65 */     osLoginNameMap.put("kinfai", new Integer(47));
/*  66 */     osLoginNameMap.put("lester", new Integer(8));
/*  67 */     osLoginNameMap.put("liz", new Integer(11));
/*  68 */     osLoginNameMap.put("louise", new Integer(28));
/*  69 */     osLoginNameMap.put("mayo", new Integer(36));
/*  70 */     osLoginNameMap.put("mohammad", new Integer(2));
/*  71 */     osLoginNameMap.put("nahid", new Integer(33));
/*  72 */     osLoginNameMap.put("nathan", new Integer(10));
/*  73 */     osLoginNameMap.put("nick", new Integer(32));
/*  74 */     osLoginNameMap.put("nicola", new Integer(44));
/*  75 */     osLoginNameMap.put("oleg", new Integer(41));
/*  76 */     osLoginNameMap.put("radu", new Integer(43));
/*  77 */     osLoginNameMap.put("ray", new Integer(18));
/*  78 */     osLoginNameMap.put("rebecca", new Integer(26));
/*  79 */     osLoginNameMap.put("reg", new Integer(66));
/*  80 */     osLoginNameMap.put("ren", new Integer(57));
/*  81 */     osLoginNameMap.put("rene", new Integer(15));
/*  82 */     osLoginNameMap.put("robert", new Integer(1));
/*  83 */     osLoginNameMap.put("sarah", new Integer(30));
/*  84 */     osLoginNameMap.put("stephen", new Integer(64));
/*  85 */     osLoginNameMap.put("stepheng", new Integer(65));
/*  86 */     osLoginNameMap.put("susan", new Integer(50));
/*  87 */     osLoginNameMap.put("toyo", new Integer(59));
/*  88 */     osLoginNameMap.put("vanboxel", new Integer(40));
/*  89 */     osLoginNameMap.put("walter", new Integer(34));
/*  90 */     osLoginNameMap.put("yvonne", new Integer(22));
/*     */     
/*     */ 
/*  93 */     osLoginNameMap.put("rob.h", new Integer(3));
/*     */     
/*     */ 
/*  96 */     osLoginNameMap.put("administrator", new Integer(37));
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
/*     */   public static uk.ac.ox.oppf.optic.model.Person getOpticPersonByOsLogin(Session sess, String login)
/*     */   {
/* 109 */     Integer pid = (Integer)osLoginNameMap.get(login);
/*     */     
/*     */ 
/* 112 */     if (pid == null) {
/* 113 */       pid = new Integer(18);
/*     */     }
/*     */     
/*     */ 
/* 117 */     if (sess == null) {
	// for testing
	uk.ac.ox.oppf.optic.model.Person person = new uk.ac.ox.oppf.optic.model.Person();
	person.setFamilyName(login);
	return person;
/* 118 */       // was throw new RuntimeException("Couldn't map OS User to Optic Person as now connection to Optic is available: " + login);
/*     */     }
/* 120 */     uk.ac.ox.oppf.optic.model.Person p = PersonDAO.getInstance().get(pid, sess);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */     if (p == null) {
/* 128 */       throw new RuntimeException("Didn't find Optic Person for OS User " + login);
/*     */     }
/* 130 */     return p;
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
/*     */   public static org.pimslims.model.people.Person getPersonByOsLogin(WritableVersion version, String login, Session sess)
/*     */   {
/* 143 */     System.out.println("PersonInserter [" + login + "]");
/*     */     
/*     */ 
/* 146 */     uk.ac.ox.oppf.optic.model.Person p = getOpticPersonByOsLogin(sess, login);
/*     */     
/*     */ 
/* 149 */     if (p == null) {
/* 150 */       throw new RuntimeException("Didn't find Optic Person for OS User " + login);
/*     */     }
/* 152 */     System.out.println("PersonInserter Get the Optic Person for this person [" + p.getTitle() + " " + p.getGivenName() + " " + p.getFamilyName() + "]");
/*     */     
/*     */ 
/* 155 */     org.pimslims.model.people.Person pp = null;
/*     */     Throwable t = null;
/* 157 */     try { pp = (org.pimslims.model.people.Person)version.findFirst(org.pimslims.model.people.Person.class, buildAttrMap(p));
/*     */     }
/*     */     catch (Exception e) {
/* 160 */       System.out.println("PersonInserter Exception caught [" + e.getLocalizedMessage() + "]");
/* 161 */       e.printStackTrace();
/* 162 */       t = e.getCause(); }
/* 163 */     while (null != t) {
/* 164 */       System.out.println("PersonInserter Caused By [" + t.getLocalizedMessage() + "]");
/* 165 */       t.printStackTrace();
/* 166 */       t = t.getCause();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 171 */     if (pp == null) {
/* 172 */       throw new RuntimeException("Didn't find PIMS Person for Optic Person " + p.getGivenName() + " " + p.getFamilyName());
/*     */     }
/*     */     
/* 175 */     System.out.println("PersonInserter Got the Optic Person for this person [" + pp.getTitle() + " " + pp.getGivenName() + " " + pp.getFamilyName() + "]");
/*     */     
/* 177 */     return pp;
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
/*     */   public static User getUserByOsLogin(WritableVersion version, String login, Session sess)
/*     */   {
/* 190 */     System.out.println("PersonInserter [" + login + "]");
/*     */     
/*     */ 
/* 193 */     uk.ac.ox.oppf.optic.model.Person p = getOpticPersonByOsLogin(sess, login);
/*     */     
/*     */ 
/* 196 */     if (p == null) {
/* 197 */       throw new RuntimeException("Didn't find Optic Person for OS User " + login);
/*     */     }
/* 199 */     System.out.println("PersonInserter Get the Optic Person for this person [" + p.getTitle() + " " + p.getGivenName() + " " + p.getFamilyName() + "]");
/*     */     
/*     */ 
/* 202 */     org.pimslims.model.people.Person pp = null;
/*     */     Throwable t = null;
/* 204 */     try { pp = (org.pimslims.model.people.Person)version.findFirst(org.pimslims.model.people.Person.class, buildAttrMap(p));
/*     */     }
/*     */     catch (Exception e) {
/* 207 */       System.out.println("PersonInserter Exception caught [" + e.getLocalizedMessage() + "]");
/* 208 */       e.printStackTrace();
/* 209 */       t = e.getCause(); }
/* 210 */     while (null != t) {
/* 211 */       System.out.println("PersonInserter Caused By [" + t.getLocalizedMessage() + "]");
/* 212 */       t.printStackTrace();
/* 213 */       t = t.getCause();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 218 */     if (pp == null) {
/* 219 */       throw new RuntimeException("Didn't find PIMS Person for Optic Person " + p.getGivenName() + " " + p.getFamilyName());
/*     */     }
/*     */     
/* 222 */     System.out.println("PersonInserter Got the Optic Person for this person [" + pp.getTitle() + " " + pp.getGivenName() + " " + pp.getFamilyName() + "]");
/*     */     
/*     */ 
/* 225 */     Collection<User> users = pp.getUsers();
/* 226 */     return (User)users.iterator().next();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PersonInserter getInstance()
/*     */   {
/* 236 */     if (instance == null) {
/* 237 */       instance = new PersonInserter();
/*     */     }
/* 239 */     return instance;
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
/*     */   public static Map<String, Object> buildAttrMap(uk.ac.ox.oppf.optic.model.Person p)
/*     */   {
/* 256 */     HashMap<String, Object> attr = new HashMap();
/*     */     
/* 258 */     attr.put("givenName", p.getGivenName());
/* 259 */     attr.put("familyName", p.getFamilyName());
/* 260 */     if ((null != p.getTitle()) && (!"".equals(p.getTitle()))) {
/* 261 */       attr.put("title", p.getTitle());
/*     */     }
/*     */     
/* 264 */     return attr;
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
/*     */   public ModelObject insertPerson(WritableVersion version, uk.ac.ox.oppf.optic.model.Person p)
/*     */     throws ConstraintException, AccessException
/*     */   {
/* 278 */     return create(version, org.pimslims.model.people.Person.class, buildAttrMap(p));
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
/*     */   public void linkToOrganisation(WritableVersion version, uk.ac.ox.oppf.optic.model.Person p)
/*     */     throws ConstraintException, AccessException
/*     */   {
/* 292 */     Organisation org = findOrganisation(version, p);
/* 293 */     createPersonGroup(version, p, org);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Organisation findOrganisation(WritableVersion version, uk.ac.ox.oppf.optic.model.Person p)
/*     */   {
/* 304 */     Laboratory lab = p.getLaboratory();
/*     */     
/* 306 */     Collection<Organisation> mos = version.findAll(Organisation.class, OrganisationInserter.buildAttrMap(lab));
/*     */     
/* 308 */     Organisation org = null;
/*     */     
/* 310 */     for (Organisation mo : mos) {
/* 311 */       org = mo;
/*     */     }
/*     */     
/* 314 */     return org;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void insertAll(WritableVersion version, Session sess)
/*     */     throws ConstraintException, AccessException
/*     */   {
/* 326 */     List<uk.ac.ox.oppf.optic.model.Person> people = PersonDAO.getInstance().findAll(sess);
/*     */     
/* 328 */     for (uk.ac.ox.oppf.optic.model.Person p : people) {
/* 329 */       org.pimslims.model.people.Person pp = (org.pimslims.model.people.Person)insertPerson(version, p);
/* 330 */       PersonInGroupInserter.getInstance().insertPersonInGroup(version, p, pp);
/* 331 */       UserInserter.getInstance().insertUser(version, p, pp);
/*     */     }
/*     */   }
/*     */   
/*     */   public PersonInserter() {}
/*     */   
/*     */   private void createPersonGroup(WritableVersion version, uk.ac.ox.oppf.optic.model.Person p, Organisation org) {}
/*     */ }

/* Location:           C:\Users\cm65\sow7\oppf\lib\pims-oppf.jar
 * Qualified Name:     uk.ac.ox.oppf.pims.inserter.PersonInserter
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.0.1
 */