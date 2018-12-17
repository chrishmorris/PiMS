using OPPF.Utilities;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.IO;

namespace TestOPPF.Utilities
{
    
    
    /// <summary>
    ///This is a test class for OPPFConfigXMLTest and is intended
    ///to contain all OPPFConfigXMLTest Unit Tests
    ///</summary>
    [TestClass()]
    public class OPPFConfigXMLTest
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
        ///A test for XmlDirectory
        ///</summary>
        [TestMethod()]
        [DeploymentItem("OPPFImagerLink.dll")]
        public void XmlDirectoryTest()
        {
            OPPFConfigXML_Accessor target = new OPPFConfigXML_Accessor();
            string expected = OPPFConfigXML_Accessor.DEFAULT_XML_DIRECTORY;
            string actual;
            target.XmlDirectory = expected;
            actual = target.XmlDirectory;
            Assert.AreEqual(expected, actual);
        }

        /// <summary>
        ///A test for LoggerConfig
        ///</summary>
        [TestMethod()]
        [DeploymentItem("OPPFImagerLink.dll")]
        public void LoggerConfigTest()
        {
            OPPFConfigXML_Accessor target = new OPPFConfigXML_Accessor();
            string expected = OPPFConfigXML_Accessor.DEFAULT_LOGGER_CONFIG;
            string actual;
            target.LoggerConfig = expected;
            actual = target.LoggerConfig;
            Assert.AreEqual(expected, actual);
        }

        /// <summary>
        ///A test for ImageFormat
        ///</summary>
        [TestMethod()]
        [DeploymentItem("OPPFImagerLink.dll")]
        public void ImageFormatTest()
        {
            OPPFConfigXML_Accessor target = new OPPFConfigXML_Accessor();
            string expected = OPPFConfigXML_Accessor.DEFAULT_IMAGE_FORMAT;
            string actual;
            target.ImageFormat = expected;
            actual = target.ImageFormat;
            Assert.AreEqual(expected, actual);
        }

        /// <summary>
        ///A test for ImageDirectory
        ///</summary>
        [TestMethod()]
        [DeploymentItem("OPPFImagerLink.dll")]
        public void WebDirectoryTest()
        {
            OPPFConfigXML_Accessor target = new OPPFConfigXML_Accessor();
            string expected = OPPFConfigXML_Accessor.DEFAULT_WEB_DIRECTORY;
            string actual;
            target.WebDirectory = expected;
            actual = target.WebDirectory;
            Assert.AreEqual(expected, actual);
        }

        /// <summary>
        ///A test for ArchiveDirectory
        ///</summary>
        [TestMethod()]
        [DeploymentItem("OPPFImagerLink.dll")]
        public void ArchiveDirectoryTest()
        {
            OPPFConfigXML_Accessor target = new OPPFConfigXML_Accessor();
            string expected = OPPFConfigXML_Accessor.DEFAULT_ARCHIVE_DIRECTORY;
            string actual;
            target.ArchiveDirectory = expected;
            actual = target.ArchiveDirectory;
            Assert.AreEqual(expected, actual);
        }

        /// <summary>
        ///A test for ToString
        ///</summary>
        [TestMethod()]
        public void ToStringTest()
        {
            OPPFConfigXML_Accessor target = new OPPFConfigXML_Accessor(); // TODO: Initialize to an appropriate value
            string expected = string.Empty; // TODO: Initialize to an appropriate value
            string actual;
            actual = target.ToString();
            Assert.AreEqual(expected, actual);
        }

        /// <summary>
        ///A test for getImageFormat
        ///</summary>
        [TestMethod()]
        public void GetImageFormatTest()
        {
            string expected = OPPFConfigXML_Accessor.DEFAULT_IMAGE_FORMAT;
            string actual;
            actual = OPPFConfigXML.GetImageFormat();
            Assert.AreEqual(expected, actual);
        }

        /// <summary>
        ///A test for getDirectory
        ///</summary>
        [TestMethod()]
        public void GetDirectoryTest()
        {
            string expected = OPPFConfigXML_Accessor.DEFAULT_WEB_DIRECTORY;
            string actual;
            actual = OPPFConfigXML.GetWebDirectory();
            Assert.AreEqual(expected, actual);
        }

        /// <summary>
        ///A test for Configure
        ///</summary>
        [TestMethod()]
        public void ConfigureTest()
        {
            OPPFConfigXML.Configure();
        }

    }
}
