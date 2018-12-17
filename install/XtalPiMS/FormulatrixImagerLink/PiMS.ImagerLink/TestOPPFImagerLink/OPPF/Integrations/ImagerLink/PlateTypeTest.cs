using OPPF.Integrations.ImagerLink;
using Microsoft.VisualStudio.TestTools.UnitTesting;
namespace TestOPPF.Integrations.ImagerLink
{
    
    
    /// <summary>
    ///This is a test class for PlateTypeTest and is intended
    ///to contain all PlateTypeTest Unit Tests
    ///</summary>
    [TestClass()]
    public class PlateTypeTest
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
        ///A test for WellNumberToString
        ///</summary>
        [TestMethod()]
        public void WellNumberToStringTest()
        {
            PlateType target;
            int wellNumber;
            string expected;
            string actual;
            //Assert.Inconclusive("Verify the correctness of this test method.");

            target = new PlateType("1", "Test 96-Well Plate", 12, 8, 1);
            wellNumber = 1;
            expected = "A01";
            actual = PlateType.WellNumberToString(target, wellNumber);
            Assert.AreEqual(expected, actual);
            wellNumber = 12;
            expected = "A12";
            actual = PlateType.WellNumberToString(target, wellNumber);
            Assert.AreEqual(expected, actual);
            wellNumber = 13;
            expected = "B01";
            actual = PlateType.WellNumberToString(target, wellNumber);
            Assert.AreEqual(expected, actual);
            wellNumber = 96;
            expected = "H12";
            actual = PlateType.WellNumberToString(target, wellNumber);
            Assert.AreEqual(expected, actual);

            target = new PlateType("2", "Test Zurich Plate", 3, 16, 19);
            wellNumber = 1;
            expected = "A01";
            actual = PlateType.WellNumberToString(target, wellNumber);
            Assert.AreEqual(expected, actual);
            wellNumber = 3;
            expected = "A03";
            actual = PlateType.WellNumberToString(target, wellNumber);
            Assert.AreEqual(expected, actual);
            wellNumber = 48;
            wellNumber = 4;
            expected = "B01";
            actual = PlateType.WellNumberToString(target, wellNumber);
            Assert.AreEqual(expected, actual);
            wellNumber = 48;
            expected = "P03";
            actual = PlateType.WellNumberToString(target, wellNumber);
            Assert.AreEqual(expected, actual);

        }
    }
}
