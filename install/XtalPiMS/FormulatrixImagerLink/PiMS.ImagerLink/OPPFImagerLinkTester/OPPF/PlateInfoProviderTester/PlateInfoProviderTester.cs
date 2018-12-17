using System;
using System.Collections;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Windows.Forms;
using Formulatrix.Integrations.ImagerLink;
using OPPF.Integrations.ImagerLink;

namespace OPPFImagerLinkTester.OPPF.PlateInfoProviderTester
{
	/// <summary>
	/// Summary description for PlateInfoProviderTester.
	/// </summary>
	public class PlateInfoProviderTester : System.Windows.Forms.UserControl
	{
		private System.Windows.Forms.GroupBox groupBox2;
		private System.Windows.Forms.TextBox txtOutput;
		private System.Windows.Forms.GroupBox groupBox1;
		private System.Windows.Forms.Label lblRobotID;
		private System.Windows.Forms.CheckBox chkBarcodeNull;
		private System.Windows.Forms.CheckBox chkRobotNull;
		private System.Windows.Forms.CheckBox chkRobotNameNull;
		private System.Windows.Forms.CheckBox chkRobotIDNull;
		private System.Windows.Forms.TextBox txtPlateBarcode;
		private System.Windows.Forms.TextBox txtRobotName;
		private System.Windows.Forms.Label lblPlateBarcode;
		private System.Windows.Forms.Label lblRobotName;
		private System.Windows.Forms.Button btnGetPlateTypes;
		private System.Windows.Forms.Button btnGetPlateType;
		private System.Windows.Forms.Button btnGetPlateInfo;
		private System.Windows.Forms.Button btnGetPlateID;
		private System.Windows.Forms.TextBox txtRobotID;
		/// <summary> 
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;

		/// <summary>
		/// The IPlateInfoProvider
		/// </summary>
		/// TODO: Dynamically load the DLL
		private Formulatrix.Integrations.ImagerLink.IPlateInfoProvider pip;

		#region IPlateInfoProvider Methods

		// public string GetPlateID(IRobot robot, string barcode)
		// public IPlateInfo GetPlateInfo(IRobot robot, string plateID)
		// public IPlateType GetPlateType(IRobot robot, string plateTypeID)
		// public IPlateType[] GetPlateTypes(IRobot robot)
		
		#endregion

		public PlateInfoProviderTester()
		{
			// This call is required by the Windows.Forms Form Designer.
			InitializeComponent();

			// TODO: Add any initialization after the InitializeComponent call

			// Get a PlateInfoProvider
            //pip = new PlateInfoProvider();
            pip = new PlateInfoProviderNew();
        }

		/// <summary> 
		/// Clean up any resources being used.
		/// </summary>
		protected override void Dispose( bool disposing )
		{
			// Release pip
			pip = null;

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
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.txtOutput = new System.Windows.Forms.TextBox();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.lblRobotID = new System.Windows.Forms.Label();
            this.chkBarcodeNull = new System.Windows.Forms.CheckBox();
            this.chkRobotNull = new System.Windows.Forms.CheckBox();
            this.chkRobotNameNull = new System.Windows.Forms.CheckBox();
            this.chkRobotIDNull = new System.Windows.Forms.CheckBox();
            this.txtPlateBarcode = new System.Windows.Forms.TextBox();
            this.txtRobotName = new System.Windows.Forms.TextBox();
            this.lblPlateBarcode = new System.Windows.Forms.Label();
            this.lblRobotName = new System.Windows.Forms.Label();
            this.btnGetPlateTypes = new System.Windows.Forms.Button();
            this.btnGetPlateType = new System.Windows.Forms.Button();
            this.btnGetPlateInfo = new System.Windows.Forms.Button();
            this.btnGetPlateID = new System.Windows.Forms.Button();
            this.txtRobotID = new System.Windows.Forms.TextBox();
            this.groupBox2.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.txtOutput);
            this.groupBox2.Location = new System.Drawing.Point(0, 144);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(792, 372);
            this.groupBox2.TabIndex = 7;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Output";
            // 
            // txtOutput
            // 
            this.txtOutput.Location = new System.Drawing.Point(8, 24);
            this.txtOutput.Multiline = true;
            this.txtOutput.Name = "txtOutput";
            this.txtOutput.ReadOnly = true;
            this.txtOutput.Size = new System.Drawing.Size(776, 336);
            this.txtOutput.TabIndex = 12;
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.lblRobotID);
            this.groupBox1.Controls.Add(this.chkBarcodeNull);
            this.groupBox1.Controls.Add(this.chkRobotNull);
            this.groupBox1.Controls.Add(this.chkRobotNameNull);
            this.groupBox1.Controls.Add(this.chkRobotIDNull);
            this.groupBox1.Controls.Add(this.txtPlateBarcode);
            this.groupBox1.Controls.Add(this.txtRobotName);
            this.groupBox1.Controls.Add(this.lblPlateBarcode);
            this.groupBox1.Controls.Add(this.lblRobotName);
            this.groupBox1.Controls.Add(this.btnGetPlateTypes);
            this.groupBox1.Controls.Add(this.btnGetPlateType);
            this.groupBox1.Controls.Add(this.btnGetPlateInfo);
            this.groupBox1.Controls.Add(this.btnGetPlateID);
            this.groupBox1.Controls.Add(this.txtRobotID);
            this.groupBox1.Location = new System.Drawing.Point(0, 0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(792, 144);
            this.groupBox1.TabIndex = 6;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Input";
            // 
            // lblRobotID
            // 
            this.lblRobotID.Location = new System.Drawing.Point(56, 24);
            this.lblRobotID.Name = "lblRobotID";
            this.lblRobotID.Size = new System.Drawing.Size(216, 16);
            this.lblRobotID.TabIndex = 17;
            this.lblRobotID.Text = "Robot ID : ";
            this.lblRobotID.TextAlign = System.Drawing.ContentAlignment.TopRight;
            // 
            // chkBarcodeNull
            // 
            this.chkBarcodeNull.Location = new System.Drawing.Point(552, 72);
            this.chkBarcodeNull.Name = "chkBarcodeNull";
            this.chkBarcodeNull.Size = new System.Drawing.Size(48, 14);
            this.chkBarcodeNull.TabIndex = 11;
            this.chkBarcodeNull.Text = "Null";
            this.chkBarcodeNull.CheckedChanged += new System.EventHandler(this.chkBarcodeNull_CheckedChanged);
            // 
            // chkRobotNull
            // 
            this.chkRobotNull.Location = new System.Drawing.Point(608, 40);
            this.chkRobotNull.Name = "chkRobotNull";
            this.chkRobotNull.Size = new System.Drawing.Size(104, 14);
            this.chkRobotNull.TabIndex = 10;
            this.chkRobotNull.Text = "Robot Null";
            this.chkRobotNull.CheckedChanged += new System.EventHandler(this.chkRobotNull_CheckedChanged);
            // 
            // chkRobotNameNull
            // 
            this.chkRobotNameNull.Location = new System.Drawing.Point(552, 48);
            this.chkRobotNameNull.Name = "chkRobotNameNull";
            this.chkRobotNameNull.Size = new System.Drawing.Size(48, 14);
            this.chkRobotNameNull.TabIndex = 9;
            this.chkRobotNameNull.Text = "Null";
            this.chkRobotNameNull.CheckedChanged += new System.EventHandler(this.chkRobotNameNull_CheckedChanged);
            // 
            // chkRobotIDNull
            // 
            this.chkRobotIDNull.Location = new System.Drawing.Point(552, 24);
            this.chkRobotIDNull.Name = "chkRobotIDNull";
            this.chkRobotIDNull.Size = new System.Drawing.Size(48, 16);
            this.chkRobotIDNull.TabIndex = 8;
            this.chkRobotIDNull.Text = "Null";
            this.chkRobotIDNull.CheckedChanged += new System.EventHandler(this.chkRobotIDNull_CheckedChanged);
            // 
            // txtPlateBarcode
            // 
            this.txtPlateBarcode.Location = new System.Drawing.Point(272, 64);
            this.txtPlateBarcode.Name = "txtPlateBarcode";
            this.txtPlateBarcode.Size = new System.Drawing.Size(256, 20);
            this.txtPlateBarcode.TabIndex = 3;
            // 
            // txtRobotName
            // 
            this.txtRobotName.Location = new System.Drawing.Point(272, 40);
            this.txtRobotName.Name = "txtRobotName";
            this.txtRobotName.Size = new System.Drawing.Size(256, 20);
            this.txtRobotName.TabIndex = 2;
            // 
            // lblPlateBarcode
            // 
            this.lblPlateBarcode.Location = new System.Drawing.Point(56, 72);
            this.lblPlateBarcode.Name = "lblPlateBarcode";
            this.lblPlateBarcode.Size = new System.Drawing.Size(216, 16);
            this.lblPlateBarcode.TabIndex = 10;
            this.lblPlateBarcode.Text = "Barcode / Plate ID / Plate Type ID : ";
            this.lblPlateBarcode.TextAlign = System.Drawing.ContentAlignment.TopRight;
            // 
            // lblRobotName
            // 
            this.lblRobotName.Location = new System.Drawing.Point(56, 48);
            this.lblRobotName.Name = "lblRobotName";
            this.lblRobotName.Size = new System.Drawing.Size(216, 16);
            this.lblRobotName.TabIndex = 9;
            this.lblRobotName.Text = "Robot Name : ";
            this.lblRobotName.TextAlign = System.Drawing.ContentAlignment.TopRight;
            // 
            // btnGetPlateTypes
            // 
            this.btnGetPlateTypes.Location = new System.Drawing.Point(616, 104);
            this.btnGetPlateTypes.Name = "btnGetPlateTypes";
            this.btnGetPlateTypes.Size = new System.Drawing.Size(112, 24);
            this.btnGetPlateTypes.TabIndex = 7;
            this.btnGetPlateTypes.Text = "GetPlateTypes";
            this.btnGetPlateTypes.Click += new System.EventHandler(this.btnGetPlateTypes_Click);
            // 
            // btnGetPlateType
            // 
            this.btnGetPlateType.Location = new System.Drawing.Point(432, 104);
            this.btnGetPlateType.Name = "btnGetPlateType";
            this.btnGetPlateType.Size = new System.Drawing.Size(112, 24);
            this.btnGetPlateType.TabIndex = 6;
            this.btnGetPlateType.Text = "GetPlateType";
            this.btnGetPlateType.Click += new System.EventHandler(this.btnGetPlateType_Click);
            // 
            // btnGetPlateInfo
            // 
            this.btnGetPlateInfo.Location = new System.Drawing.Point(248, 104);
            this.btnGetPlateInfo.Name = "btnGetPlateInfo";
            this.btnGetPlateInfo.Size = new System.Drawing.Size(112, 24);
            this.btnGetPlateInfo.TabIndex = 5;
            this.btnGetPlateInfo.Text = "GetPlateInfo";
            this.btnGetPlateInfo.Click += new System.EventHandler(this.btnGetPlateInfo_Click);
            // 
            // btnGetPlateID
            // 
            this.btnGetPlateID.Location = new System.Drawing.Point(72, 104);
            this.btnGetPlateID.Name = "btnGetPlateID";
            this.btnGetPlateID.Size = new System.Drawing.Size(112, 24);
            this.btnGetPlateID.TabIndex = 4;
            this.btnGetPlateID.Text = "GetPlateID";
            this.btnGetPlateID.Click += new System.EventHandler(this.btnGetPlateID_Click);
            // 
            // txtRobotID
            // 
            this.txtRobotID.Location = new System.Drawing.Point(272, 16);
            this.txtRobotID.Name = "txtRobotID";
            this.txtRobotID.Size = new System.Drawing.Size(256, 20);
            this.txtRobotID.TabIndex = 1;
            // 
            // PlateInfoProviderTester
            // 
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.Name = "PlateInfoProviderTester";
            this.Size = new System.Drawing.Size(792, 515);
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);

		}
		#endregion

		#region User Interface

		private void chkRobotIDNull_CheckedChanged(object sender, System.EventArgs e)
		{
			if (chkRobotIDNull.Checked) 
			{
				txtRobotID.Enabled = false;
			}
			else
			{
				txtRobotID.Enabled = true;
			}
		}

		private void chkRobotNameNull_CheckedChanged(object sender, System.EventArgs e)
		{
			if (chkRobotNameNull.Checked) 
			{
				txtRobotName.Enabled = false;
			}
			else
			{
				txtRobotName.Enabled = true;
			}
		}

		private void chkRobotNull_CheckedChanged(object sender, System.EventArgs e)
		{
			if (chkRobotNull.Checked) 
			{
				txtRobotID.Enabled = false;
				txtRobotName.Enabled = false;
				chkRobotIDNull.Enabled = false;
				chkRobotNameNull.Enabled = false;
			}
			else
			{
				chkRobotIDNull.Enabled = true;
				chkRobotNameNull.Enabled = true;
				chkRobotIDNull_CheckedChanged(chkRobotNull, e);
				chkRobotNameNull_CheckedChanged(chkRobotNull, e);
			}
		}

		private void chkBarcodeNull_CheckedChanged(object sender, System.EventArgs e)
		{
			if (chkBarcodeNull.Checked) 
			{
				txtPlateBarcode.Enabled = false;
			}
			else
			{
				txtPlateBarcode.Enabled = true;
			}
		}

		private void btnGetPlateID_Click(object sender, System.EventArgs e)
		{
			// Cursor to wait
			this.Cursor = Cursors.WaitCursor;

			string plateID = pip.GetPlateID(getRobot(), getBarcode());
			
			txtOutput.Text = "";

			if (plateID == null) 
			{
				txtOutput.AppendText("GetPlateID(robot, barcode) returned null");
			}
			else
			{
				txtOutput.AppendText("GetPlateID(robot, barcode) returned \"" + plateID + "\"");
			}

			// Cursor to default
			this.Cursor = Cursors.Default;
		}

		private void btnGetPlateInfo_Click(object sender, System.EventArgs e)
		{
			// Cursor to wait
			this.Cursor = Cursors.WaitCursor;

			IPlateInfo pi = pip.GetPlateInfo(getRobot(), getBarcode());
			
			txtOutput.Text = "";

			if (pi == null) 
			{
				txtOutput.AppendText("GetPlateInfo(robot, plateID) returned null");
			}
			else
			{
				txtOutput.AppendText("DateDispensed  = " + pi.DateDispensed.ToString() + "\r\n");
				txtOutput.AppendText("ExperimentName = " + pi.ExperimentName + "\r\n");
				txtOutput.AppendText("PlateNumber    = " + pi.PlateNumber + "\r\n");
				txtOutput.AppendText("PlateTypeID    = " + pi.PlateTypeID + "\r\n");
				txtOutput.AppendText("ProjectName    = " + pi.ProjectName + "\r\n");
				txtOutput.AppendText("UserEmail      = " + pi.UserEmail + "\r\n");
				txtOutput.AppendText("UserName       = " + pi.UserName + "\r\n");
			}

			// Cursor to deafult
			this.Cursor = Cursors.Default;
		}

		private void btnGetPlateType_Click(object sender, System.EventArgs e)
		{
			// Cursor to wait
			this.Cursor = Cursors.WaitCursor;

			IPlateType pt = pip.GetPlateType(getRobot(), getBarcode());
			txtOutput.Text = "";

			if (pt == null)
			{
				txtOutput.AppendText("GetPlateType(robot, plateTypeID) returned null");
			}
			else
			{
				txtOutput.AppendText("ID         = " + pt.ID + "\r\n");
				txtOutput.AppendText("Name       = " + pt.Name + "\r\n");
				txtOutput.AppendText("NumColumns = " + pt.NumColumns + "\r\n");
				txtOutput.AppendText("NumDrops   = " + pt.NumDrops + "\r\n");
				txtOutput.AppendText("NumRows    = " + pt.NumRows + "\r\n");
			}

			// Cursor to default
			this.Cursor = Cursors.Default;
		}

		private void btnGetPlateTypes_Click(object sender, System.EventArgs e)
		{
			// Cursor to wait
			this.Cursor = Cursors.WaitCursor;

			IPlateType[] plateTypes = pip.GetPlateTypes(getRobot());

			txtOutput.Text = "";

			if (plateTypes == null)
			{
				txtOutput.AppendText("GetPlateTypes(robot) returned null");
			}
			else
			{

				txtOutput.AppendText("Got " + plateTypes.GetLength(0) + " IPlateTypes:\r\n");
				foreach(IPlateType pt in plateTypes)
				{
					txtOutput.AppendText("\tID         = " + pt.ID + "\r\n");
					txtOutput.AppendText("\tName       = " + pt.Name + "\r\n");
					txtOutput.AppendText("\tNumColumns = " + pt.NumColumns + "\r\n");
					txtOutput.AppendText("\tNumDrops   = " + pt.NumDrops + "\r\n");
					txtOutput.AppendText("\tNumRows    = " + pt.NumRows + "\r\n");
					txtOutput.AppendText("\t\r\n");
				}
			}

			// Cursor to default
			this.Cursor = Cursors.Default;
		}

		#endregion

		/// <summary>
		/// Method to get the plate barcode/id
		/// </summary>
		/// <remarks>Takes into account the setting of chkBarcodeNull to allow
		/// a null barcode/id to be used on demand</remarks>
		/// <returns>The plate barcode/id or null, as appropriate</returns>
		private string getBarcode()
		{
			if (chkBarcodeNull.Checked)
			{
				return null;
			}
			else
			{
				return txtPlateBarcode.Text;
			}
		}

		/// <summary>
		/// Method to get the plate barcode/id
		/// </summary>
		/// <remarks>Takes into account the setting of chkRobotIDNull, chkRobotNameNull
		/// and chkRobotNull to allow null attributes or a null robot to be used on demand</remarks>
		/// <returns>The IRobot or null, as appropriate</returns>
		private IRobot getRobot()
		{
			Robot robot = null;
			if (! chkRobotNull.Checked)
			{
				robot = new Robot(null, null);

				if (! chkRobotIDNull.Checked)
				{
					robot.SetID(txtRobotID.Text);
				}

				if (! chkRobotNameNull.Checked)
				{
					robot.SetName(txtRobotName.Text);
				}

			}
			return robot;
		}

	}

}
