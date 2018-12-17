using OPPF.Utilities;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Formulatrix.Integrations.ImagerLink;
using OPPF.Proxies;

namespace TestOPPF.Utilities
{
    
    
    /// <summary>
    ///This is a test class for RobotUtilsTest and is intended
    ///to contain all RobotUtilsTest Unit Tests
    ///</summary>
    [TestClass()]
    public class RobotUtilsTest
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
        ///A test for iRobotToString
        ///</summary>
        [TestMethod()]
        public void iRobotToStringTest()
        {
            IRobot robot = null; // TODO: Initialize to an appropriate value
            string expected = string.Empty; // TODO: Initialize to an appropriate value
            string actual;
            actual = RobotUtils.iRobotToString(robot);
            Assert.AreEqual(expected, actual);
            Assert.Inconclusive("Verify the correctness of this test method.");
        }

        /// <summary>
        ///A test for createProxy
        ///</summary>
        [TestMethod()]
        public void createProxyTest()
        {
            IRobot robot = null; // TODO: Initialize to an appropriate value
            Robot expected = null; // TODO: Initialize to an appropriate value
            Robot actual;
            actual = RobotUtils.createProxy(robot);
            Assert.AreEqual(expected, actual);
            Assert.Inconclusive("Verify the correctness of this test method.");
        }

        /// <summary>
        ///A test for RobotUtils Constructor
        ///</summary>
        [TestMethod()]
        public void RobotUtilsConstructorTest()
        {
            RobotUtils target = new RobotUtils();
            Assert.Inconclusive("TODO: Implement code to verify target");
        }
    }
}
