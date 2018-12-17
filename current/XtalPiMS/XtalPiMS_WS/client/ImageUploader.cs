using System;
using System.Collections.Generic;
using System.IO;
using System.Xml.Serialization;
using OPPF.XML;
using OPPF.Proxies2;
using OPPF.Utilities;
using log4net;

namespace OPPF.Integrations.ImageUploader
{
    class ImageUploader
    {
        /// <summary>
        /// The name of the lock file used to ensure only one instance runs at a time
        /// </summary>
        public const string LOCK_FILE_NAME = @".lock";

        /// <summary>
        /// The logger for this class
        /// </summary>
        private static ILog log;

        /// <summary>
        /// The folder to search for ImageInfo xml files
        /// </summary>
        private string imageInfosDir;

        /// <summary>
        /// The maximum number of ImageInfo xml files to process in a single web service call
        /// </summary>
        private int maxChunk = 20;

        /// <summary>
        /// Zero arg constructor. Reads the configuration file to obtain imageInfosDir.
        /// OPPFConfigXML.Configure() must already have been called.
        /// </summary>
        public ImageUploader()
        {
            // TODO Allow %robot% variable to be correctly interpreted
            this.imageInfosDir = OPPFConfigXML.GetXmlDirectory();
        }

        /// <summary>
        /// Find and process the ImageInfo xml files
        /// </summary>
        /// <returns>True if there may be more files to process.</returns>
        public bool processImageInfos() {

            bool moreImageInfos = false;

            DirectoryInfo di = new DirectoryInfo(imageInfosDir);

            DirectoryInfo processed = new DirectoryInfo(Path.Combine(di.FullName, @"Processed"));
            if (!processed.Exists)
            {
                processed.Create();
            }

            DirectoryInfo quarantine = new DirectoryInfo(Path.Combine(di.FullName, @"Quarantine"));
            if (!quarantine.Exists)
            {
                quarantine.Create();
            }

            // TODO Potentially expensive - can we iterate?
            FileInfo[] files = di.GetFiles("*.xml", SearchOption.TopDirectoryOnly);

            List<UploadImage> uploadImageList = new List<UploadImage>();
            //List<FileInfo> filesToProcess = new List<FileInfo>();
            Dictionary<string, FileInfo> urlToFiles = new Dictionary<string, FileInfo>();
            long threshold = DateTime.Now.Ticks - (TimeSpan.TicksPerSecond * 10);

            try
            {
                foreach (FileInfo fi in files)
                {
                    // File at least 10 seconds old
                    if ((threshold > fi.CreationTimeUtc.Ticks) && (threshold > fi.LastWriteTimeUtc.Ticks))
                    {

                        // Read XML
                        XmlSerializer serializer = new XmlSerializer(typeof(ImageInfo));
                        TextReader reader = new StreamReader(fi.FullName);
                        ImageInfo imageInfo = (ImageInfo) serializer.Deserialize(reader);
                        reader.Close();

                        OPPF.Proxies2.UploadImage uploadImage = parseImageInfo(imageInfo);

                        uploadImageList.Add(uploadImage);
                        //filesToProcess.Add(fi);
                        urlToFiles.Add(uploadImage.url, fi);

                        if (maxChunk <= uploadImageList.Count)
                        {
                            moreImageInfos = true;
                            break;
                        }

                    }
                }

                if (0 < uploadImageList.Count)
                {
                    OPPF.Proxies2.uploadImages uploadImagesElement = new uploadImages();
                    uploadImagesElement.wrapper = uploadImageList.ToArray();

                    WSPlate ws = WSPlateFactory.getWSPlate2();
                    OPPF.Proxies2.uploadImagesResponse response = ws.uploadImages(uploadImagesElement);
                    OPPF.Proxies2.UploadImageResponse[] responses = response.wrapper;

                    /*
                    foreach (FileInfo src in filesToProcess)
                    {
                        string dest = Path.Combine(processed.FullName, src.Name);
                        src.MoveTo(dest);
                        log.Info("Processed " + src.FullName);
                    }
                     */

                    for (int i = 0; i < responses.Length; i++)
                    {
                        FileInfo src;
                        urlToFiles.TryGetValue(responses[i].url, out src);

                        if (null == src)
                        {
                            log.Error("Failed to match url " + responses[i].url + " to known image file"); 
                        }

                        else if (responses[i].ok)
                        {
                            string dest = Path.Combine(processed.FullName, src.Name);
                            src.MoveTo(dest);
                            log.Info("Processed " + src.FullName);
                        }
                        else
                        {
                            string dest = Path.Combine(quarantine.FullName, src.Name);
                            src.MoveTo(dest);
                            log.Warn("Quarantined " + src.FullName + ": " + responses[i].reason);
                        }
                    }
                }
                else
                {
                    log.Info("Nothing to do");
                }
            }
            catch (Exception e)
            {
                // Log it
                log.Error(e);

                Dictionary<string, FileInfo>.ValueCollection filesToProcess = urlToFiles.Values;
                foreach (FileInfo src in filesToProcess)
                {
                    try
                    {
                        string dest = Path.Combine(quarantine.FullName, src.Name);
                        src.MoveTo(dest);
                        log.Warn("Quarantined " + src.FullName);
                    }
                    catch (Exception e2)
                    {
                        // Log it
                        log.Error(e2);
                    }
                }
            }

            return moreImageInfos;

        }


        public OPPF.Proxies2.UploadImage parseImageInfo(ImageInfo imageInfo)
        {
            OPPF.Proxies2.UploadImage uploadImage = new OPPF.Proxies2.UploadImage();
            uploadImage.colourDepth = 24;
            uploadImage.dateImaged = imageInfo.ImagedAt.ToUniversalTime();
            OPPF.Proxies2.Size image = new OPPF.Proxies2.Size();
            image.height = imageInfo.SizeInPixels.Height;
            image.width = imageInfo.SizeInPixels.Width;
            uploadImage.image = image;
            uploadImage.imagingID = imageInfo.ImagingId;
            OPPF.Proxies2.Size pixel = new OPPF.Proxies2.Size();
            pixel.height = Convert.ToSingle(imageInfo.SizeInMicrons.Height / image.height);
            pixel.width = Convert.ToSingle(imageInfo.SizeInMicrons.Width / image.width);
            uploadImage.pixel = pixel;
            uploadImage.plateID = imageInfo.PlateId;
            Robot robot = new Robot();
            robot.iD = imageInfo.Imager;
            robot.name = imageInfo.Imager;
            uploadImage.robot = robot;
            if (ImageInfoImageType.C.Equals(imageInfo.ImageType))
            {
                uploadImage.type = ImageType.composite;
            }
            else
            {
                uploadImage.type = ImageType.slice;
            }
            uploadImage.url = imageInfo.Url;
            uploadImage.well = imageInfo.Drop;

            return uploadImage;
        }

        static void Main(string[] args)
        {
            try
            {
                FileStream _lock = File.Open(LOCK_FILE_NAME, FileMode.OpenOrCreate, FileAccess.ReadWrite, FileShare.None);

                // Load configuration
                OPPFConfigXML.Configure();

                log = LogManager.GetLogger(@"OPPF.Integrations.ImageUploader");

                ImageUploader imageUploader = new ImageUploader();
                while (imageUploader.processImageInfos())
                {
                    // Nothing to do
                };

                _lock.Close();
                File.Delete(LOCK_FILE_NAME);
            }
            catch (Exception e)
            {
                log.Error(e);
            }
        }
    }
}
