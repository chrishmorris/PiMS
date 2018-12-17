/*    */ package uk.ac.ox.oppf.pims.inserter;
/*    */ 
/*    */ import org.pimslims.dao.AbstractModel;
/*    */ import org.pimslims.dao.ModelImpl;
/*    */ import org.pimslims.dao.WritableVersion;
/*    */ import org.pimslims.exception.AbortedException;
/*    */ import org.pimslims.exception.AccessException;
/*    */ import org.pimslims.exception.ConstraintException;
/*    */ import org.pimslims.metamodel.ModelObject;
/*    */ import org.pimslims.model.target.Project;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TestRefDataInserter
/*    */   extends TestAbstractInserter
/*    */ {
/*    */   public TestRefDataInserter() {}
/*    */   
/*    */   public void testCreateProject()
/*    */     throws ConstraintException, AccessException, AbortedException
/*    */   {
/* 26 */     AbstractModel model = ModelImpl.getModel();
/* 27 */     WritableVersion version = model.getWritableVersion("administrator");
/*    */     
      RefDataInserter rdi = new RefDataInserter();
     ModelObject mo = rdi.insertOPPFProject(version);
     assertNotNull(mo);
     Project p = (Project)mo;
     assertEquals(p.getShortName(), "OPPF");     
/*    */ 
/* 38 */     version.abort();
/*    */   }
/*    */ }

/* Location:           C:\Users\cm65\sow7\oppf\lib\pims-oppf.jar
 * Qualified Name:     uk.ac.ox.oppf.pims.inserter.TestRefDataInserter
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.0.1
 */