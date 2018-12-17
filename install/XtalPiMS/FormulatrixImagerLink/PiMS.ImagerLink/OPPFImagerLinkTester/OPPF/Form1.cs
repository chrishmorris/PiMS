using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using System.Data;
using Formulatrix.Integrations.ImagerLink;
using OPPF.Integrations.ImagerLink;
using OPPFImagerLinkTester.OPPF.ImagingTaskProviderTester;

namespace OPPFImagerLinkTester.OPPF
{
	/// <summary>
	/// Summary description for Form1.
	/// </summary>
	public class Form1 : System.Windows.Forms.Form
	{
		private System.Windows.Forms.TabControl tabProviders;
		private System.Windows.Forms.TabPage tabPagePlateInfoProvider;
		private System.Windows.Forms.TabPage tabPageImagingTaskProvider;
		private System.Windows.Forms.TabPage tabPageCaptureProvider;
		private System.Windows.Forms.TabPage tabPageImageProcessorProvider;

		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;
		private OPPFImagerLinkTester.OPPF.ImagingTaskProviderTester.ImagingTaskProviderTester imagingTaskProviderTester1;
		private OPPFImagerLinkTester.OPPF.PlateInfoProviderTester.PlateInfoProviderTester plateInfoProviderTester1;
		private OPPFImagerLinkTester.OPPF.CaptureProviderTester.CaptureProviderTester captureProviderTester1;
		private OPPFImagerLinkTester.OPPF.ImageProcessorProviderTester.ImageProcessorProviderTester imageProcessorProvider1;

		/// <summary>
		/// The IPlateInfoProvider
		/// </summary>
		/// TODO: Dynamically load the DLL
		private Formulatrix.Integrations.ImagerLink.IPlateInfoProvider pip;

		#region ICaptureProvider Methods

		// public ICapturePointList GetCapturePoints(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, string imagingID, bool includeOverview)
		// public ICaptureProfile GetDefaultCaptureProfile(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID)
		// public void GetFirstDrop(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, ref int wellNumber, ref int dropNumber)

		#endregion

		#region IImageProcessorProvider Methods

		// public IImageProcessor GetImageProcessor(Formulatrix.Integrations.ImagerLink.IRobot robot, IProcessingInfo processingInfo)

		#endregion

		public Form1()
		{
			//
			// Required for Windows Form Designer support
			//
			InitializeComponent();

			//
			// TODO: Add any constructor code after InitializeComponent call
			//

			// Get a PlateInfoProvider
			pip = new PlateInfoProvider();
		}

		/// <summary>
		/// Clean up any resources being used.
		/// </summary>
		protected override void Dispose( bool disposing )
		{
			if( disposing )
			{
				if (components != null) 
				{
					components.Dispose();
				}
			}
			base.Dispose( disposing );
		}

		#region Windows Form Designer generated code
		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.tabProviders = new System.Windows.Forms.TabControl();
			this.tabPagePlateInfoProvider = new System.Windows.Forms.TabPage();
			this.plateInfoProviderTester1 = new OPPFImagerLinkTester.OPPF.PlateInfoProviderTester.PlateInfoProviderTester();
			this.tabPageImagingTaskProvider = new System.Windows.Forms.TabPage();
			this.imagingTaskProviderTester1 = new OPPFImagerLinkTester.OPPF.ImagingTaskProviderTester.ImagingTaskProviderTester();
			this.tabPageCaptureProvider = new System.Windows.Forms.TabPage();
			this.tabPageImageProcessorProvider = new System.Windows.Forms.TabPage();
			this.captureProviderTester1 = new OPPFImagerLinkTester.OPPF.CaptureProviderTester.CaptureProviderTester();
			this.imageProcessorProvider1 = new OPPFImagerLinkTester.OPPF.ImageProcessorProviderTester.ImageProcessorProviderTester();
			this.tabProviders.SuspendLayout();
			this.tabPagePlateInfoProvider.SuspendLayout();
			this.tabPageImagingTaskProvider.SuspendLayout();
			this.tabPageCaptureProvider.SuspendLayout();
			this.tabPageImageProcessorProvider.SuspendLayout();
			this.SuspendLayout();
			// 
			// tabProviders
			// 
			this.tabProviders.Controls.Add(this.tabPagePlateInfoProvider);
			this.tabProviders.Controls.Add(this.tabPageImagingTaskProvider);
			this.tabProviders.Controls.Add(this.tabPageCaptureProvider);
			this.tabProviders.Controls.Add(this.tabPageImageProcessorProvider);
			this.tabProviders.Location = new System.Drawing.Point(0, 0);
			this.tabProviders.Name = "tabProviders";
			this.tabProviders.SelectedIndex = 0;
			this.tabProviders.Size = new System.Drawing.Size(800, 544);
			this.tabProviders.TabIndex = 0;
			// 
			// tabPagePlateInfoProvider
			// 
			this.tabPagePlateInfoProvider.Controls.Add(this.plateInfoProviderTester1);
			this.tabPagePlateInfoProvider.Location = new System.Drawing.Point(4, 25);
			this.tabPagePlateInfoProvider.Name = "tabPagePlateInfoProvider";
			this.tabPagePlateInfoProvider.Size = new System.Drawing.Size(792, 515);
			this.tabPagePlateInfoProvider.TabIndex = 0;
			this.tabPagePlateInfoProvider.Text = "PlateInfoProvider";
			// 
			// plateInfoProviderTester1
			// 
			this.plateInfoProviderTester1.Location = new System.Drawing.Point(0, 0);
			this.plateInfoProviderTester1.Name = "plateInfoProviderTester1";
			this.plateInfoProviderTester1.Size = new System.Drawing.Size(792, 515);
			this.plateInfoProviderTester1.TabIndex = 0;
			// 
			// tabPageImagingTaskProvider
			// 
			this.tabPageImagingTaskProvider.Controls.Add(this.imagingTaskProviderTester1);
			this.tabPageImagingTaskProvider.Location = new System.Drawing.Point(4, 25);
			this.tabPageImagingTaskProvider.Name = "tabPageImagingTaskProvider";
			this.tabPageImagingTaskProvider.Size = new System.Drawing.Size(792, 515);
			this.tabPageImagingTaskProvider.TabIndex = 1;
			this.tabPageImagingTaskProvider.Text = "ImagingTaskProvider";
			// 
			// imagingTaskProviderTester1
			// 
			this.imagingTaskProviderTester1.Location = new System.Drawing.Point(0, 0);
			this.imagingTaskProviderTester1.Name = "imagingTaskProviderTester1";
			this.imagingTaskProviderTester1.Size = new System.Drawing.Size(792, 515);
			this.imagingTaskProviderTester1.TabIndex = 0;
			// 
			// tabPageCaptureProvider
			// 
			this.tabPageCaptureProvider.Controls.Add(this.captureProviderTester1);
			this.tabPageCaptureProvider.Location = new System.Drawing.Point(4, 25);
			this.tabPageCaptureProvider.Name = "tabPageCaptureProvider";
			this.tabPageCaptureProvider.Size = new System.Drawing.Size(792, 515);
			this.tabPageCaptureProvider.TabIndex = 2;
			this.tabPageCaptureProvider.Text = "CaptureProvider";
			// 
			// tabPageImageProcessorProvider
			// 
			this.tabPageImageProcessorProvider.Controls.Add(this.imageProcessorProvider1);
			this.tabPageImageProcessorProvider.Location = new System.Drawing.Point(4, 25);
			this.tabPageImageProcessorProvider.Name = "tabPageImageProcessorProvider";
			this.tabPageImageProcessorProvider.Size = new System.Drawing.Size(792, 515);
			this.tabPageImageProcessorProvider.TabIndex = 3;
			this.tabPageImageProcessorProvider.Text = "ImageProcessorProvider";
			// 
			// captureProviderTester1
			// 
			this.captureProviderTester1.Location = new System.Drawing.Point(0, 0);
			this.captureProviderTester1.Name = "captureProviderTester1";
			this.captureProviderTester1.Size = new System.Drawing.Size(792, 515);
			this.captureProviderTester1.TabIndex = 0;
			// 
			// imageProcessorProvider1
			// 
			this.imageProcessorProvider1.Location = new System.Drawing.Point(0, 0);
			this.imageProcessorProvider1.Name = "imageProcessorProvider1";
			this.imageProcessorProvider1.Size = new System.Drawing.Size(792, 515);
			this.imageProcessorProvider1.TabIndex = 0;
			// 
			// Form1
			// 
			this.AutoScaleBaseSize = new System.Drawing.Size(6, 15);
			this.ClientSize = new System.Drawing.Size(800, 544);
			this.Controls.Add(this.tabProviders);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.Fixed3D;
			this.Name = "Form1";
			this.Text = "OPPFImagerLinkTester";
			this.tabProviders.ResumeLayout(false);
			this.tabPagePlateInfoProvider.ResumeLayout(false);
			this.tabPageImagingTaskProvider.ResumeLayout(false);
			this.tabPageCaptureProvider.ResumeLayout(false);
			this.tabPageImageProcessorProvider.ResumeLayout(false);
			this.ResumeLayout(false);

		}
		#endregion

		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main() 
		{
			Application.Run(new Form1());
		}

	}
}
