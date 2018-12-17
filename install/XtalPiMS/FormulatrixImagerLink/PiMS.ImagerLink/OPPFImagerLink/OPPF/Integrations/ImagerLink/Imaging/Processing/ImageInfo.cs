using System;

using Formulatrix.Integrations.ImagerLink.Imaging.Processing;

namespace OPPF.Integrations.ImagerLink.Imaging.Processing
{
	/// <summary>
	/// Class to enable the description of an image.
	/// </summary>
	/// <remarks>The interface is completely undocumented in the ImagerLink code.</remarks>
	public class ImageInfo : IImageInfo
	{
		private System.Drawing.Image _image;
		private int _imageIndex;
		private ImageType _imageType;
		private bool _isZNull;
		private double _z;
		
		public ImageInfo()
		{
			SetImage(null);
			SetImageIndex(0);
			SetImageType(ImageType.FocusLevel);
			SetIsZNull(false);
			SetZ(0D);
		}

		#region IImageInfo Members

        System.Drawing.Image IImageInfo.Image
		{
			get
			{
				return _image;
			}
		}

        int IImageInfo.ImageIndex
		{
			get
			{
				return _imageIndex;
			}
		}

        ImageType IImageInfo.ImageType
		{
			get
			{
				return _imageType;
			}
		}

        bool IImageInfo.IsZNull
		{
			get
			{
				return _isZNull;
			}
		}

        double IImageInfo.Z
		{
			get
			{
				return _z;
			}
		}

		#endregion

        #region Set methods for interface properties

        public void SetImage(System.Drawing.Image image)
        {
            _image = image;
        }

        public void SetImageIndex(int imageIndex)
        {
            _imageIndex = imageIndex;
        }

        public void SetImageType(ImageType imageType)
        {
            _imageType = imageType;
        }

        public void SetIsZNull(bool isZNull)
        {
            _isZNull = isZNull;
        }

        public void SetZ(double z)
        {
            _z = z;
        }

        #endregion



        public static string ToString(IImageInfo imageInfo)
        {
            if (null == imageInfo)
            {
                return "null";
            }
            string msg = "[Image=" + ImageToString(imageInfo.Image) +
               ";ImageIndex=" + imageInfo.ImageIndex +
               ";ImageType=";
            if (null != imageInfo.ImageType)
            {
                msg = msg + imageInfo.ImageType.ToString();
            }
            else
            {
                msg = msg + "null";
            }
            msg = msg +
                ";IsZNull=" + imageInfo.IsZNull +
                ";Z=" + imageInfo.Z + "]";
            return msg;
        }

        public static string ImageToString(System.Drawing.Image img)
        {
            if (null == img)
            {
                return "null";
            }
            return
            "[Flags=" + img.Flags + " => " + ((System.Drawing.Imaging.ImageFlags)img.Flags).ToString() +
            ";FrameDimensionsList=" + img.FrameDimensionsList + // show list
            ";Height=" + img.Height +
            ";HorizontalResolution=" + img.HorizontalResolution +  // This could be set to the correct value
            ";Palette=" + img.Palette +
            ";PhysicalDimension=" + img.PhysicalDimension +
            ";PixelFormat=" + img.PixelFormat +
            ";PropertyIdList=" + img.PropertyIdList + // show list
            ";PropertyItems=" + img.PropertyItems + // show list
            ";RawFormat=" + ImageFormatToString(img.RawFormat) +
            ";Size=" + img.Size +
            ";Tag=" + img.Tag + // check for null
            ";VerticalResolution=" + img.VerticalResolution + // This could be set to the correct value
            ";Width=" + img.Width + "]";
        }

        public static string ImageFormatToString(System.Drawing.Imaging.ImageFormat imgFormat)
        {
            if (null == imgFormat) { return "null"; }
            if (System.Drawing.Imaging.ImageFormat.Bmp.Equals(imgFormat)) { return "Bmp"; }
            if (System.Drawing.Imaging.ImageFormat.Emf.Equals(imgFormat)) { return "Emf"; }
            if (System.Drawing.Imaging.ImageFormat.Exif.Equals(imgFormat)) { return "Exif"; }
            if (System.Drawing.Imaging.ImageFormat.Gif.Equals(imgFormat)) { return "Gif"; }
            if (System.Drawing.Imaging.ImageFormat.Icon.Equals(imgFormat)) { return "Icon"; }
            if (System.Drawing.Imaging.ImageFormat.Jpeg.Equals(imgFormat)) { return "Jpeg"; }
            if (System.Drawing.Imaging.ImageFormat.MemoryBmp.Equals(imgFormat)) { return "MemoryBmp"; }
            if (System.Drawing.Imaging.ImageFormat.Png.Equals(imgFormat)) { return "Png"; }
            if (System.Drawing.Imaging.ImageFormat.Tiff.Equals(imgFormat)) { return "Tiff"; }
            if (System.Drawing.Imaging.ImageFormat.Wmf.Equals(imgFormat)) { return "Wmf"; }
            return imgFormat.ToString();
        }

    }
}
