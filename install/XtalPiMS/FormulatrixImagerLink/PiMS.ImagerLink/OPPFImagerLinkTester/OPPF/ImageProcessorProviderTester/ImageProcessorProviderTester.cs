using System;
using System.Collections;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Windows.Forms;
using Formulatrix.Integrations.ImagerLink.Imaging.Processing;
using OPPF.Integrations.ImagerLink;
using OPPF.Integrations.ImagerLink.Imaging.Processing;

namespace OPPFImagerLinkTester.OPPF.ImageProcessorProviderTester
{
	/// <summary>
	/// Summary description for ImageProcessorProvider.
	/// </summary>
	public class ImageProcessorProviderTester : System.Windows.Forms.UserControl
	{
		private System.Windows.Forms.TextBox textBox1;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.OpenFileDialog openImageFile;
		private System.Windows.Forms.Button openButton; 

        private System.Drawing.Image image;
		private Robot _robot;
		private ProcessingInfo _processingInfo;

		/// <summary> 
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;

		public ImageProcessorProviderTester()
		{
			// This call is required by the Windows.Forms Form Designer.
			InitializeComponent();

			// TODO: Add any initialization after the InitializeComponent call
			_robot = new Robot();
			_robot.SetID("RI1000-0014");
			_robot.SetName("RI1000-0014");
			
			_processingInfo = new ProcessingInfo();
			_processingInfo.SetPlateID("441300240121");
            _processingInfo.SetImagingID("441300240121-071706-124312");
            _processingInfo.SetProfileID("profile1");
            _processingInfo.SetRegionID("region1");
            _processingInfo.SetRegionType(Formulatrix.Integrations.ImagerLink.Imaging.RegionType.Overview);
            _processingInfo.SetWellNumber(1);
            _processingInfo.SetDropNumber(2);

		}

		/// <summary> 
		/// Clean up any resources being used.
		/// </summary>
		protected override void Dispose( bool disposing )
		{
			if( disposing )
			{
				if(components != null)
				{
					components.Dispose();
				}
			}
			base.Dispose( disposing );
		}

		#region Component Designer generated code
		/// <summary> 
		/// Required method for Designer support - do not modify 
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.openImageFile = new System.Windows.Forms.OpenFileDialog();
			this.textBox1 = new System.Windows.Forms.TextBox();
			this.label1 = new System.Windows.Forms.Label();
			this.openButton = new System.Windows.Forms.Button();
			this.SuspendLayout();
			// 
			// openImageFile
			// 
			this.openImageFile.Filter = "Bitmap files (*.bmp)|*.bmp";
			this.openImageFile.Title = "Image File Chooser";
			// 
			// textBox1
			// 
			this.textBox1.Location = new System.Drawing.Point(344, 96);
			this.textBox1.Name = "textBox1";
			this.textBox1.ReadOnly = true;
			this.textBox1.Size = new System.Drawing.Size(208, 22);
			this.textBox1.TabIndex = 0;
			this.textBox1.Text = "textBox1";
			// 
			// label1
			// 
			this.label1.Location = new System.Drawing.Point(144, 96);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(128, 23);
			this.label1.TabIndex = 1;
			this.label1.Text = "Open Image File";
			this.label1.Click += new System.EventHandler(this.label1_Click);
			// 
			// openButton
			// 
			this.openButton.DialogResult = System.Windows.Forms.DialogResult.OK;
			this.openButton.Location = new System.Drawing.Point(256, 96);
			this.openButton.Name = "openButton";
			this.openButton.TabIndex = 2;
			this.openButton.Text = "Open";
			this.openButton.Click += new System.EventHandler(this.button1_Click_1);
			// 
			// ImageProcessorProvider
			// 
			this.Controls.Add(this.openButton);
			this.Controls.Add(this.label1);
			this.Controls.Add(this.textBox1);
			this.Name = "ImageProcessorProvider";
			this.Size = new System.Drawing.Size(792, 515);
			this.ResumeLayout(false);

		}
		#endregion

		private void label1_Click(object sender, System.EventArgs e)
		{
		}
		private void button1_Click_1(object sender, System.EventArgs e)
		{
			if(openImageFile.ShowDialog() == DialogResult.OK)
			{
				textBox1.Text = openImageFile.FileName;
				System.IO.StreamReader sr = new 
					System.IO.StreamReader(openImageFile.FileName);
				
				image = Image.FromStream(sr.BaseStream);
				
				//sr.Close();
                IImageProcessorProvider imageProcessorProvider = new ImageProcessorProvider();
					
				IImageProcessor imageProcessor = imageProcessorProvider.GetImageProcessor(_robot, _processingInfo);

                CaptureInfo captureInfo = new CaptureInfo();
                imageProcessor.SetCaptureInfo(captureInfo);
				
				ImageInfo imageInfo = new ImageInfo();
                imageInfo.SetImage(image);
                imageInfo.SetImageType(ImageType.FocusLevel);
                imageInfo.SetIsZNull(false);

                imageInfo.SetImageIndex(1);
                imageInfo.SetZ(0D);
				imageProcessor.SetImageInfo(imageInfo);

                // And second slice
                //imageInfo.SetImageIndex(2);
                //imageInfo.SetZ(20D);
                //imageProcessor.SetImageInfo(imageInfo);

                // And EFI
                //imageInfo.SetImageType(ImageType.ExtendedFocus);
                //imageInfo.SetImageIndex(3);
                //imageInfo.SetZ(20D);
                //imageProcessor.SetImageInfo(imageInfo);

                imageProcessor.Dispose();
                imageProcessor = null;

			}
		
		}
	}
}
