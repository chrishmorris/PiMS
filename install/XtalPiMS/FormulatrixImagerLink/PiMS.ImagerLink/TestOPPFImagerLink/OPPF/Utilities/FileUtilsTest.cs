using OPPF.Utilities;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Formulatrix.Integrations.ImagerLink.Imaging.Processing;
using System.Drawing.Imaging;
using Formulatrix.Integrations.ImagerLink;

namespace TestOPPF.Utilities
{
    
    
    /// <summary>
    ///This is a test class for FileUtilsTest and is intended
    ///to contain all FileUtilsTest Unit Tests
    ///</summary>
    [TestClass()]
    public class FileUtilsTest
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
        ///A test for GetFilename
        ///</summary>
        [TestMethod()]
        public void GetFilenameTest1()
        {
            IImageInfo imageInfo = null; // TODO: Initialize to an appropriate value
            IProcessingInfo processingInfo = null; // TODO: Initialize to an appropriate value
            IPlateInfo plateInfo = null; // TODO: Initialize to an appropriate value
            IPlateType plateType = null; // TODO: Initialize to an appropriate value
            string expected = string.Empty; // TODO: Initialize to an appropriate value
            string actual;
            actual = FileUtils.GetFilename(imageInfo, processingInfo, plateInfo, plateType);
            Assert.AreEqual(expected, actual);
            Assert.Inconclusive("Verify the correctness of this test method.");
        }

        /// <summary>
        ///A test for GetFilename
        ///</summary>
        [TestMethod()]
        public void GetFilenameTest()
        {
            IImageInfo imageInfo = null; // TODO: Initialize to an appropriate value
            IProcessingInfo iProcessingInfo = null; // TODO: Initialize to an appropriate value
            ImageFormat imageFormat = null; // TODO: Initialize to an appropriate value
            string expected = string.Empty; // TODO: Initialize to an appropriate value
            string actual;
            actual = FileUtils.GetFilename(imageInfo, iProcessingInfo, imageFormat);
            Assert.AreEqual(expected, actual);
            Assert.Inconclusive("Verify the correctness of this test method.");
        }

        /// <summary>
        ///A test for GetDirectory
        ///</summary>
        [TestMethod()]
        public void GetDirectoryTest1()
        {
            string directoryTemplate = string.Empty; // TODO: Initialize to an appropriate value
            IImageInfo imageInfo = null; // TODO: Initialize to an appropriate value
            IProcessingInfo processingInfo = null; // TODO: Initialize to an appropriate value
            IRobot robot = null; // TODO: Initialize to an appropriate value
            string expected = string.Empty; // TODO: Initialize to an appropriate value
            string actual;
            actual = FileUtils.GetDirectory(directoryTemplate, imageInfo, processingInfo, robot);
            Assert.AreEqual(expected, actual);
            Assert.Inconclusive("Verify the correctness of this test method.");
        }

        /// <summary>
        ///A test for GetDirectory
        ///</summary>
        [TestMethod()]
        public void GetDirectoryTest()
        {
            IImageInfo imageInfo = null; // TODO: Initialize to an appropriate value
            IProcessingInfo processingInfo = null; // TODO: Initialize to an appropriate value
            IRobot robot = null; // TODO: Initialize to an appropriate value
            string expected = string.Empty; // TODO: Initialize to an appropriate value
            string actual;
            actual = FileUtils.GetDirectory(imageInfo, processingInfo, robot);
            Assert.AreEqual(expected, actual);
            Assert.Inconclusive("Verify the correctness of this test method.");
        }
    }
}
