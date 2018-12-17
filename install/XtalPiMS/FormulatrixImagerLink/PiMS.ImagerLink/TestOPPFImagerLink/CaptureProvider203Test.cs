using OPPF.Integrations.ImagerLink.Imaging;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Formulatrix.Integrations.ImagerLink.Imaging;
using Formulatrix.Integrations.ImagerLink;
using System.Collections.Generic;

namespace TestOPPFImagerLink
{
    
    
    /// <summary>
    ///This is a test class for CaptureProvider203Test and is intended
    ///to contain all CaptureProvider203Test Unit Tests
    ///</summary>
    [TestClass()]
    public class CaptureProvider203Test
    {


        private TestContext testContextInstance;

        /// <summary>
        ///Gets or sets the test context which provides
        ///information about and functionality for the current test run.
        ///</summary>
        public TestContext TestContext
        {
            get
            {
                return testContextInstance;
            }
            set
            {
                testContextInstance = value;
            }
        }

        #region Additional test attributes
        // 
        //You can use the following additional attributes as you write your tests:
        //
        //Use ClassInitialize to run code before running the first test in the class
        //[ClassInitialize()]
        //public static void MyClassInitialize(TestContext testContext)
        //{
        //}
        //
        //Use ClassCleanup to run code after all tests in a class have run
        //[ClassCleanup()]
        //public static void MyClassCleanup()
        //{
        //}
        //
        //Use TestInitialize to run code before running each test
        //[TestInitialize()]
        //public void MyTestInitialize()
        //{
        //}
        //
        //Use TestCleanup to run code after each test has run
        //[TestCleanup()]
        //public void MyTestCleanup()
        //{
        //}
        //
        #endregion


        /// <summary>
        ///A test for SetCaptureProfiles
        ///</summary>
        [TestMethod()]
        public void SetCaptureProfilesTest()
        {
            CaptureProvider203 target = new CaptureProvider203(); // TODO: Initialize to an appropriate value
            List<IProperty> captureProfiles = null; // TODO: Initialize to an appropriate value
            target.SetCaptureProfiles(captureProfiles);
            List<IProperty>  actual = ((ICaptureProfiles)target).CaptureProfiles;
            Assert.AreEqual(0, actual.Count);
            //Assert.Inconclusive("A method that does not return a value cannot be verified.");
        }

        /*
         * Not relevant for build against Formulatrix.Integrations.ImagerLink.dll v2.0.1.136
         */ 
        /// <summary>
        ///A test for Formulatrix.Integrations.ImagerLink.Imaging.ICaptureProfiles.CaptureProfiles
        ///</summary>
        [TestMethod()]
        [DeploymentItem("OPPFImagerLink.dll")]
        public void CaptureProfilesTest()
        {
            ICaptureProfiles target = new CaptureProvider203(); // TODO: Initialize to an appropriate value
            List<IProperty> actual;
            actual = target.CaptureProfiles;
            Assert.AreEqual(0, actual.Count);
            //Assert.Inconclusive("Verify the correctness of this test method.");
        }

        /// <summary>
        ///A test for CaptureProvider203 Constructor
        ///</summary>
        [TestMethod()]
        public void CaptureProvider203ConstructorTest()
        {
            CaptureProvider203 target = new CaptureProvider203();
            Assert.IsInstanceOfType(target, typeof(ICaptureProvider));
            Assert.IsInstanceOfType(target, typeof(ICaptureProfiles));
            Assert.IsInstanceOfType(target, typeof(CaptureProvider));
        }
    }
}
