using System;
using System.Drawing;
using System.Drawing.Imaging;
using System.Drawing.Drawing2D;
using System.IO;

namespace OPPF.Utilities.Imaging
{
	/// <summary>
	/// Various image-related utilities.
	/// </summary>
	public abstract class ImageUtils
	{

        /// <summary>
        /// Save img as a jpeg with the specified filename at quality 100L.
        /// </summary>
        /// <param name="img">The image to save</param>
        /// <param name="filename">The filename of the saved image</param>
        public static void saveAsJpeg(Image img, string filename)
        {
            saveAsJpeg(img, filename, 100L);
        }

        /// <summary>
        /// Save img as a jpeg with the specified filename and quality.
        /// </summary>
        /// <param name="img">The image to save</param>
        /// <param name="filename">The filename of the saved image</param>
        /// <param name="quality">The jpeg quality setting</param>
        public static void saveAsJpeg(Image img, string filename, long quality)
        {
            ImageCodecInfo encoder = OPPF.Utilities.Imaging.ImageUtils.GetEncoderInfo("image/jpeg");
            EncoderParameters encoderParams = new EncoderParameters(1);
            encoderParams.Param[0] = new EncoderParameter(Encoder.Quality, quality);
            img.Save(filename + ".jpg", encoder, encoderParams);
        }

        /// <summary>
        /// Save img as a png with the specified filename.
        /// </summary>
        /// <param name="img">The image to save</param>
        /// <param name="filename">The filename of the saved image</param>
        public static void saveAsPng(Image img, string filename)
        {
            //ImageCodecInfo encoder = OPPF.Utilities.ImageFile.GetEncoderInfo("image/png");
            //EncoderParameters encoderParams = new EncoderParameters(1);
            //encoderParams.Param[0] = new EncoderParameter(Encoder.ColorDepth, 16L);
            FileStream fs = new FileStream(filename = ".png", FileMode.Create);
            img.Save(fs, System.Drawing.Imaging.ImageFormat.Png);
            fs.Close();
        }

        /// <summary>
        /// Save img as an LZW-compressed tiff with the specified filename.
        /// </summary>
        /// <param name="img">The image to save</param>
        /// <param name="filename">The filename of the saved image</param>
        public static void saveAsTiff(Image img, string filename)
        {
            ImageCodecInfo encoder = OPPF.Utilities.Imaging.ImageUtils.GetEncoderInfo("image/tiff");
            EncoderParameters encoderParams = new EncoderParameters(1);
            encoderParams.Param[0] = new EncoderParameter(Encoder.Compression, (long)EncoderValue.CompressionLZW);
            //encoderParams.Param[1] = new EncoderParameter(Encoder.ColorDepth, 16L);
            img.Save(filename + ".tif", encoder, encoderParams);
        }

        /// <summary>
        /// Resize img so that it is no larger than max and retains its original aspect ratio.
        /// If img is already smaller than max, or max is Size.Empty, it is simply returned.
        /// </summary>
        /// <param name="img">The image to be resized</param>
        /// <param name="max">The max size of the image after resizing</param>
        /// <returns>The resized image</returns>
        public static Image resizeImage(Image img, Size max)
        {
            // Is resize necessary?
            if (Size.Empty.Equals(max) || ((img.Size.Height <= max.Height) && (img.Size.Width <= max.Width)))
            {
                return img;
            }

            // Figure out new dimensions
            int height = max.Height;
            int width = max.Width;
            double heightRatio = img.Size.Height / max.Height;
            double widthRatio = img.Size.Width / max.Width;

            // If height needs to shrink more than width
            if (heightRatio >= widthRatio)
            {
                width = System.Convert.ToInt32((double)img.Size.Width / heightRatio);
                // Handle rounding error
                if (width > max.Width)
                {
                    width = max.Width;
                }
            }

            // Else width needs to shrink more than height
            else
            {
                height = System.Convert.ToInt32((double)img.Size.Height / widthRatio);
                // Handle rounding error
                if (height > max.Height)
                {
                    height = max.Height;
                }
            }

            // Do resize
            Bitmap newImg = new Bitmap(width, height);
            using (Graphics gr = Graphics.FromImage(newImg))
            {
                gr.SmoothingMode = SmoothingMode.AntiAlias;
                gr.InterpolationMode = InterpolationMode.HighQualityBicubic;
                gr.PixelOffsetMode = PixelOffsetMode.HighQuality;
                gr.DrawImage(img, new Rectangle(0, 0, width, height));
            }

            // Copy metadata

            newImg.SetResolution(img.HorizontalResolution * width / (float)img.Width, img.VerticalResolution * height / (float)img.Height);
            return newImg;
        }

        /// <summary>
        /// Convert the stringified representation of an image format to the relevant
        /// System.Drawing.Imaging.ImageFormat value.
        /// </summary>
        /// <param name="fileFormat"></param>
        /// <returns></returns>
        public static System.Drawing.Imaging.ImageFormat GetImageFormat(string fileFormat)
        {
            string lFileFormat = fileFormat.ToLowerInvariant();
            System.Drawing.Imaging.ImageFormat imageFormat = System.Drawing.Imaging.ImageFormat.Bmp;
            if ("bmp".Equals(lFileFormat))
            {
                imageFormat = System.Drawing.Imaging.ImageFormat.Bmp;
            }
            else if ("emf".Equals(lFileFormat))
            {
                imageFormat = System.Drawing.Imaging.ImageFormat.Emf;
            }
            else if ("exif".Equals(lFileFormat))
            {
                imageFormat = System.Drawing.Imaging.ImageFormat.Exif;
            }
            else if ("gif".Equals(lFileFormat))
            {
                imageFormat = System.Drawing.Imaging.ImageFormat.Gif;
            }
            else if ("jpg".Equals(lFileFormat) || "jpeg".Equals(lFileFormat))
            {
                imageFormat = System.Drawing.Imaging.ImageFormat.Jpeg;
            }
            else if ("png".Equals(lFileFormat))
            {
                imageFormat = System.Drawing.Imaging.ImageFormat.Png;
            }
            else if ("tif".Equals(lFileFormat) || "tiff".Equals(lFileFormat))
            {
                imageFormat = System.Drawing.Imaging.ImageFormat.Tiff;
            }

            return imageFormat;

        }

        /// <summary>
        /// Find the ImageCodecInfo for the specified mime-type.
        /// </summary>
        /// <param name="mimeType">The mime-type to search with</param>
        /// <returns>The ImageCodecInfo that applies to the specified mime-type</returns>
        public static System.Drawing.Imaging.ImageCodecInfo GetEncoderInfo(String mimeType)
        {
            int j;
            System.Drawing.Imaging.ImageCodecInfo[] encoders;
            encoders = System.Drawing.Imaging.ImageCodecInfo.GetImageEncoders();
            for (j = 0; j < encoders.Length; ++j)
            {
                if (encoders[j].MimeType == mimeType)
                    return encoders[j];
            }
            return null;
        }

    }
}
