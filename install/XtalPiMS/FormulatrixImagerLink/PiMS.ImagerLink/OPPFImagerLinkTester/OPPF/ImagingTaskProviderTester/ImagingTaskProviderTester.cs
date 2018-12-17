using System;
using System.Collections;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Windows.Forms;
using Formulatrix.Integrations.ImagerLink;
using Formulatrix.Integrations.ImagerLink.Scheduling;
using OPPF.Integrations.ImagerLink;
using OPPF.Integrations.ImagerLink.Scheduling;

namespace OPPFImagerLinkTester.OPPF.ImagingTaskProviderTester
{
	/// <summary>
	/// Summary description for ImagingTaskProviderTester.
	/// </summary>
	public class ImagingTaskProviderTester : System.Windows.Forms.UserControl
	{
		private System.Windows.Forms.Button btnGetImagingTasks;
		private System.Windows.Forms.TextBox txtPlateBarcode;
		private System.Windows.Forms.DataGrid dgImagingTasks;
		private System.Windows.Forms.Button btnSkippedImaging;
		private System.Windows.Forms.Button btnImagedPlate;
		private System.Windows.Forms.Button btnImagingPlate;
		private System.Windows.Forms.Button btnUpdatePriority;
		private System.Windows.Forms.GroupBox grpActions;
		private System.Windows.Forms.GroupBox grpImagingTasks;
		private System.Windows.Forms.GroupBox grpPlateBarcode;
		private System.Windows.Forms.Label lblNewPriority;
		private System.Windows.Forms.NumericUpDown nudNewPriority;
		private System.Windows.Forms.RadioButton rdbScheduled;
		private System.Windows.Forms.RadioButton rdbUnscheduled;
		private System.Windows.Forms.TextBox txtImagingID;
		private System.Windows.Forms.Label label1;
		/// <summary> 
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;

		/// <summary>
		/// The IImagingTaskProvider
		/// </summary>
		/// TODO: Dynamically load the DLL
		private Formulatrix.Integrations.ImagerLink.Scheduling.IImagingTaskProvider itp;

		/// <summary>
		/// The Robot
		/// </summary>
		private Robot robot;

		/// <summary>
		/// The DataTable
		/// </summary>
		private DataTable dt;

		/// <summary>
		/// The barcode for which imaging tasks were last obtained
		/// </summary>
		private string barcode;
		private System.Windows.Forms.Button btnGetImagingTasksWS;
		private System.Windows.Forms.Button btnRepeat;

		/// <summary>
		/// The array of IImagingTasks last received
		/// </summary>
		//private IImagingTask[] tasks;

		#region IImagingTaskProvider Methods

		// public IImagingTask[] GetImagingTasks(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID)
		// public bool SupportsPriority(Formulatrix.Integrations.ImagerLink.IRobot robot)
		// public void UpdatedPriority(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, DateTime dateToImage, int priority)
		// public string ImagingPlate(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, bool scheduled, DateTime dateToImage, DateTime dateImaged)
		// public void ImagedPlate(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, string imagingID)
		// public void SkippedImaging(Formulatrix.Integrations.ImagerLink.IRobot robot, string plateID, DateTime dateToImage)

		#endregion

		public ImagingTaskProviderTester()
		{
			// This call is required by the Windows.Forms Form Designer.
			InitializeComponent();

			// TODO: Add any initialization after the InitializeComponent call

			// Get an ImagingTaskProvider
            //itp = new ImagingTaskProvider();
            itp = new ImagingTaskProviderNew();

			// Get a robot
			Robot r = new Robot();
			r.SetID("1");
			r.SetName("RI1000-0014");
			robot = r;

			// Get a DataTable
			dt = new DataTable();
 
			// Set the columns for the DataTable
			dt.Columns.Add(new DataColumn("Date To Image", typeof(DateTime)));
			dt.Columns.Add(new DataColumn("Priority", typeof(Int32)));
			dt.Columns.Add(new DataColumn("In Queue", typeof(bool)));
			dt.Columns.Add(new DataColumn("State", typeof(Formulatrix.Integrations.ImagerLink.Scheduling.ImagingState)));
            dt.Columns.Add(new DataColumn("Date Imaged UTC", typeof(DateTime)));
            dt.Columns.Add(new DataColumn("Date To Image UTC", typeof(DateTime)));
            dt.Columns.Add(new DataColumn("Date Imaged Local", typeof(DateTime)));
            dt.Columns.Add(new DataColumn("Date To Image Local", typeof(DateTime)));

			// Convert to something the DataGrid can show
			DataView dv = new DataView(dt);
			dv.AllowDelete = false;
			dv.AllowEdit = false;
			dv.AllowNew = false;
			dv.Sort = "Date To Image ASC";

			// Show the table in the DataGrid
			dgImagingTasks.DataSource = dv;

			System.Windows.Forms.CurrencyManager cm = null;
			System.Windows.Forms.DataGridTableStyle ts = null;
			if (dgImagingTasks.TableStyles.Count > 0)
			{
				ts = dgImagingTasks.TableStyles[0];
			}
			else
			{
				cm = (CurrencyManager)BindingContext[dv, dt.TableName];
				ts = new System.Windows.Forms.DataGridTableStyle(cm);
				dgImagingTasks.TableStyles.Add(ts);
			}
			for (Int32 i=0; i<dt.Columns.Count; i++)
			{
				System.Data.DataColumn dc = dt.Columns[i];
				if (dc.DataType == typeof(System.DateTime))
				{
					System.Windows.Forms.DataGridColumnStyle cs =
						dgImagingTasks.TableStyles[0].GridColumnStyles[i];
					if ((cs != null) && (cs.GetType() ==
						typeof(System.Windows.Forms.DataGridTextBoxColumn)))
					{
						((System.Windows.Forms.DataGridTextBoxColumn)cs).Format = "yyyy-MM-dd HH:mm:ss";
						((System.Windows.Forms.DataGridTextBoxColumn)cs).Width = 150;
					}
				}
				else
				{
					System.Windows.Forms.DataGridTextBoxColumn dgcs = new
						System.Windows.Forms.DataGridTextBoxColumn();
					ts.GridColumnStyles.Add(dgcs);
				}
			}
			//EdsDataGrid.TableStyles.Add(ts);
		}

		/// <summary> 
		/// Clean up any resources being used.
		/// </summary>
		protected override void Dispose( bool disposing )
		{
			if( disposing )
			{
				// Clear up the class objects
				dt.Dispose();
				robot = null;
				itp = null;

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
			this.grpImagingTasks = new System.Windows.Forms.GroupBox();
			this.rdbUnscheduled = new System.Windows.Forms.RadioButton();
			this.rdbScheduled = new System.Windows.Forms.RadioButton();
			this.dgImagingTasks = new System.Windows.Forms.DataGrid();
			this.grpPlateBarcode = new System.Windows.Forms.GroupBox();
			this.btnRepeat = new System.Windows.Forms.Button();
			this.btnGetImagingTasks = new System.Windows.Forms.Button();
			this.txtPlateBarcode = new System.Windows.Forms.TextBox();
			this.grpActions = new System.Windows.Forms.GroupBox();
			this.label1 = new System.Windows.Forms.Label();
			this.txtImagingID = new System.Windows.Forms.TextBox();
			this.nudNewPriority = new System.Windows.Forms.NumericUpDown();
			this.lblNewPriority = new System.Windows.Forms.Label();
			this.btnSkippedImaging = new System.Windows.Forms.Button();
			this.btnImagedPlate = new System.Windows.Forms.Button();
			this.btnImagingPlate = new System.Windows.Forms.Button();
			this.btnUpdatePriority = new System.Windows.Forms.Button();
			this.btnGetImagingTasksWS = new System.Windows.Forms.Button();
			this.grpImagingTasks.SuspendLayout();
			((System.ComponentModel.ISupportInitialize)(this.dgImagingTasks)).BeginInit();
			this.grpPlateBarcode.SuspendLayout();
			this.grpActions.SuspendLayout();
			((System.ComponentModel.ISupportInitialize)(this.nudNewPriority)).BeginInit();
			this.SuspendLayout();
			// 
			// grpImagingTasks
			// 
			this.grpImagingTasks.Controls.Add(this.rdbUnscheduled);
			this.grpImagingTasks.Controls.Add(this.rdbScheduled);
			this.grpImagingTasks.Controls.Add(this.dgImagingTasks);
			this.grpImagingTasks.Location = new System.Drawing.Point(0, 64);
			this.grpImagingTasks.Name = "grpImagingTasks";
			this.grpImagingTasks.Size = new System.Drawing.Size(792, 280);
			this.grpImagingTasks.TabIndex = 4;
			this.grpImagingTasks.TabStop = false;
			this.grpImagingTasks.Text = "Imaging Tasks";
			// 
			// rdbUnscheduled
			// 
			this.rdbUnscheduled.Location = new System.Drawing.Point(8, 240);
			this.rdbUnscheduled.Name = "rdbUnscheduled";
			this.rdbUnscheduled.Size = new System.Drawing.Size(400, 24);
			this.rdbUnscheduled.TabIndex = 5;
			this.rdbUnscheduled.Text = "Unscheduled";
			this.rdbUnscheduled.CheckedChanged += new System.EventHandler(this.rdbUnscheduled_CheckedChanged);
			// 
			// rdbScheduled
			// 
			this.rdbScheduled.Location = new System.Drawing.Point(8, 24);
			this.rdbScheduled.Name = "rdbScheduled";
			this.rdbScheduled.Size = new System.Drawing.Size(400, 24);
			this.rdbScheduled.TabIndex = 4;
			this.rdbScheduled.Text = "Scheduled";
			this.rdbScheduled.CheckedChanged += new System.EventHandler(this.rdbScheduled_CheckedChanged);
			// 
			// dgImagingTasks
			// 
			this.dgImagingTasks.DataMember = "";
			this.dgImagingTasks.HeaderForeColor = System.Drawing.SystemColors.ControlText;
			this.dgImagingTasks.Location = new System.Drawing.Point(40, 56);
			this.dgImagingTasks.Name = "dgImagingTasks";
			this.dgImagingTasks.Size = new System.Drawing.Size(736, 160);
			this.dgImagingTasks.TabIndex = 0;
			// 
			// grpPlateBarcode
			// 
			this.grpPlateBarcode.Controls.Add(this.btnGetImagingTasksWS);
			this.grpPlateBarcode.Controls.Add(this.btnRepeat);
			this.grpPlateBarcode.Controls.Add(this.btnGetImagingTasks);
			this.grpPlateBarcode.Controls.Add(this.txtPlateBarcode);
			this.grpPlateBarcode.Location = new System.Drawing.Point(0, 0);
			this.grpPlateBarcode.Name = "grpPlateBarcode";
			this.grpPlateBarcode.Size = new System.Drawing.Size(792, 64);
			this.grpPlateBarcode.TabIndex = 3;
			this.grpPlateBarcode.TabStop = false;
			this.grpPlateBarcode.Text = "Plate Barcode";
			// 
			// btnRepeat
			// 
			this.btnRepeat.Location = new System.Drawing.Point(456, 24);
			this.btnRepeat.Name = "btnRepeat";
			this.btnRepeat.Size = new System.Drawing.Size(96, 24);
			this.btnRepeat.TabIndex = 2;
			this.btnRepeat.Text = "Repeat";
			this.btnRepeat.Click += new System.EventHandler(this.btnRepeat_Click);
			// 
			// btnGetImagingTasks
			// 
			this.btnGetImagingTasks.Enabled = false;
			this.btnGetImagingTasks.Location = new System.Drawing.Point(272, 24);
			this.btnGetImagingTasks.Name = "btnGetImagingTasks";
			this.btnGetImagingTasks.Size = new System.Drawing.Size(160, 24);
			this.btnGetImagingTasks.TabIndex = 1;
			this.btnGetImagingTasks.Text = "Get Imaging Tasks";
			this.btnGetImagingTasks.Click += new System.EventHandler(this.btnGetImagingTasks_Click);
			// 
			// txtPlateBarcode
			// 
			this.txtPlateBarcode.Location = new System.Drawing.Point(48, 24);
			this.txtPlateBarcode.Name = "txtPlateBarcode";
			this.txtPlateBarcode.Size = new System.Drawing.Size(208, 22);
			this.txtPlateBarcode.TabIndex = 0;
			this.txtPlateBarcode.Text = "";
			this.txtPlateBarcode.TextChanged += new System.EventHandler(this.txtPlateBarcode_TextChanged);
			this.txtPlateBarcode.KeyUp += new System.Windows.Forms.KeyEventHandler(this.txtPlateBarcode_KeyUp);
			// 
			// grpActions
			// 
			this.grpActions.Controls.Add(this.label1);
			this.grpActions.Controls.Add(this.txtImagingID);
			this.grpActions.Controls.Add(this.nudNewPriority);
			this.grpActions.Controls.Add(this.lblNewPriority);
			this.grpActions.Controls.Add(this.btnSkippedImaging);
			this.grpActions.Controls.Add(this.btnImagedPlate);
			this.grpActions.Controls.Add(this.btnImagingPlate);
			this.grpActions.Controls.Add(this.btnUpdatePriority);
			this.grpActions.Location = new System.Drawing.Point(0, 344);
			this.grpActions.Name = "grpActions";
			this.grpActions.Size = new System.Drawing.Size(792, 172);
			this.grpActions.TabIndex = 5;
			this.grpActions.TabStop = false;
			this.grpActions.Text = "Actions";
			// 
			// label1
			// 
			this.label1.Location = new System.Drawing.Point(392, 96);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(88, 24);
			this.label1.TabIndex = 7;
			this.label1.Text = "Imaging ID : ";
			this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// txtImagingID
			// 
			this.txtImagingID.Location = new System.Drawing.Point(480, 96);
			this.txtImagingID.Name = "txtImagingID";
			this.txtImagingID.Size = new System.Drawing.Size(296, 22);
			this.txtImagingID.TabIndex = 6;
			this.txtImagingID.Text = "";
			this.txtImagingID.TextChanged += new System.EventHandler(this.txtImagingID_TextChanged);
			// 
			// nudNewPriority
			// 
			this.nudNewPriority.Location = new System.Drawing.Point(144, 24);
			this.nudNewPriority.Name = "nudNewPriority";
			this.nudNewPriority.Size = new System.Drawing.Size(48, 22);
			this.nudNewPriority.TabIndex = 5;
			// 
			// lblNewPriority
			// 
			this.lblNewPriority.Location = new System.Drawing.Point(8, 24);
			this.lblNewPriority.Name = "lblNewPriority";
			this.lblNewPriority.Size = new System.Drawing.Size(128, 24);
			this.lblNewPriority.TabIndex = 4;
			this.lblNewPriority.Text = "New Priority : ";
			this.lblNewPriority.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// btnSkippedImaging
			// 
			this.btnSkippedImaging.Enabled = false;
			this.btnSkippedImaging.Location = new System.Drawing.Point(224, 56);
			this.btnSkippedImaging.Name = "btnSkippedImaging";
			this.btnSkippedImaging.Size = new System.Drawing.Size(152, 24);
			this.btnSkippedImaging.TabIndex = 3;
			this.btnSkippedImaging.Text = "Skipped Imaging";
			this.btnSkippedImaging.Click += new System.EventHandler(this.btnSkippedImaging_Click);
			// 
			// btnImagedPlate
			// 
			this.btnImagedPlate.Enabled = false;
			this.btnImagedPlate.Location = new System.Drawing.Point(224, 128);
			this.btnImagedPlate.Name = "btnImagedPlate";
			this.btnImagedPlate.Size = new System.Drawing.Size(152, 24);
			this.btnImagedPlate.TabIndex = 2;
			this.btnImagedPlate.Text = "Imaged Plate";
			this.btnImagedPlate.Click += new System.EventHandler(this.btnImagedPlate_Click);
			// 
			// btnImagingPlate
			// 
			this.btnImagingPlate.Enabled = false;
			this.btnImagingPlate.Location = new System.Drawing.Point(224, 96);
			this.btnImagingPlate.Name = "btnImagingPlate";
			this.btnImagingPlate.Size = new System.Drawing.Size(152, 24);
			this.btnImagingPlate.TabIndex = 1;
			this.btnImagingPlate.Text = "Imaging Plate";
			this.btnImagingPlate.Click += new System.EventHandler(this.btnImagingPlate_Click);
			// 
			// btnUpdatePriority
			// 
			this.btnUpdatePriority.Enabled = false;
			this.btnUpdatePriority.Location = new System.Drawing.Point(224, 24);
			this.btnUpdatePriority.Name = "btnUpdatePriority";
			this.btnUpdatePriority.Size = new System.Drawing.Size(152, 24);
			this.btnUpdatePriority.TabIndex = 0;
			this.btnUpdatePriority.Text = "Update Priority";
			this.btnUpdatePriority.Click += new System.EventHandler(this.btnUpdatePriority_Click);
			// 
			// btnGetImagingTasksWS
			// 
			this.btnGetImagingTasksWS.Enabled = false;
			this.btnGetImagingTasksWS.Location = new System.Drawing.Point(584, 24);
			this.btnGetImagingTasksWS.Name = "btnGetImagingTasksWS";
			this.btnGetImagingTasksWS.Size = new System.Drawing.Size(160, 24);
			this.btnGetImagingTasksWS.TabIndex = 3;
			this.btnGetImagingTasksWS.Text = "Get Imaging Tasks (WS)";
			this.btnGetImagingTasksWS.Click += new System.EventHandler(this.btnGetImagingTasksWS_Click);
			// 
			// ImagingTaskProviderTester
			// 
			this.Controls.Add(this.grpImagingTasks);
			this.Controls.Add(this.grpPlateBarcode);
			this.Controls.Add(this.grpActions);
			this.Name = "ImagingTaskProviderTester";
			this.Size = new System.Drawing.Size(792, 515);
			this.grpImagingTasks.ResumeLayout(false);
			((System.ComponentModel.ISupportInitialize)(this.dgImagingTasks)).EndInit();
			this.grpPlateBarcode.ResumeLayout(false);
			this.grpActions.ResumeLayout(false);
			((System.ComponentModel.ISupportInitialize)(this.nudNewPriority)).EndInit();
			this.ResumeLayout(false);

		}
		#endregion

		#region User interface actions

		private void btnGetImagingTasks_Click(object sender, System.EventArgs e)
		{
			// Cursor to wait
			this.Cursor = Cursors.WaitCursor;

			// Store the barcode
			barcode = txtPlateBarcode.Text;

			// Get the tasks for this plate
			try {
				getImagingTasks(robot, DateTime.MinValue);
			}
			catch (Exception ex) 
			{
				MessageBox.Show(this, ex.Message, ex.GetType().Name);
			}


			// Cursor back to normal
			this.Cursor = Cursors.Default;
		}

		/// <summary>
		/// Called when state of rdbScheduled radio button changes
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void rdbScheduled_CheckedChanged(object sender, System.EventArgs e)
		{
			// Update action button state
			updateActionButtonState();
		}

		/// <summary>
		/// Called when state of rdbUnscheduled radio button changes
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void rdbUnscheduled_CheckedChanged(object sender, System.EventArgs e)
		{
			// Update action button state
			updateActionButtonState();
		}

		/// <summary>
		/// Called when text in txtPlateBacode changes
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void txtPlateBarcode_TextChanged(object sender, System.EventArgs e)
		{
			// Disable btnGetImagingTasks if no text
			btnGetImagingTasks.Enabled = (txtPlateBarcode.Text.Length != 0);
			btnGetImagingTasksWS.Enabled = (txtPlateBarcode.Text.Length != 0);
		}

		/// <summary>
		/// Enter has same effect as clicking btnGetImagingTasks
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void txtPlateBarcode_KeyUp(object sender, System.Windows.Forms.KeyEventArgs e)
		{
			if ((e.KeyCode == Keys.Enter) && btnGetImagingTasks.Enabled) btnGetImagingTasks_Click(sender, e);
		}

		private void txtImagingID_TextChanged(object sender, System.EventArgs e)
		{
			// Disable btnGetImagingTasks if no text
			btnImagedPlate.Enabled = (txtImagingID.Text.Length != 0);
		}

		private void btnUpdatePriority_Click(object sender, System.EventArgs e)
		{
			// Cursor to wait
			this.Cursor = Cursors.WaitCursor;

			// Get the identifying dateToImage DateTime
			DateTime toImage = (DateTime) getCurrentRow().ItemArray[0];

			// Request the priority update
			try
			{
				itp.UpdatedPriority(robot, barcode, toImage, (int) nudNewPriority.Value);
			}
			catch (Exception ex) 
			{
				MessageBox.Show(this, ex.Message, ex.GetType().Name);
			}

			// Refresh imaging tasks
			getImagingTasks(robot, toImage);

			// Cursor back to normal
			this.Cursor = Cursors.Default;
		}

		private void btnSkippedImaging_Click(object sender, System.EventArgs e)
		{
			// Cursor to wait
			this.Cursor = Cursors.WaitCursor;

			// Get the identifying dateToImage DateTime
			DateTime toImage = (DateTime) getCurrentRow().ItemArray[0];

			// Notify skipped
			try
			{
				itp.SkippedImaging(robot, barcode, toImage);
			}
			catch (Exception ex) 
			{
				MessageBox.Show(this, ex.Message, ex.GetType().Name);
			}

			// Refresh imaging tasks
			getImagingTasks(robot, toImage);

			// Cursor back to normal
			this.Cursor = Cursors.Default;
		}

		private void btnImagingPlate_Click(object sender, System.EventArgs e)
		{
			// Cursor to wait
			this.Cursor = Cursors.WaitCursor;

			// Are we scheduled or unscheduled?
			bool scheduled = rdbScheduled.Checked;
			
			// Get the identifying dateToImage DateTime
			DateTime toImage;
			if (scheduled)
			{
				toImage = (DateTime) getCurrentRow().ItemArray[0];
			}
			else
			{
				toImage = roundDateTimeToJavaPrecision(DateTime.Now);
			}

			// Notify imaging
			try
			{
				txtImagingID.Text = itp.ImagingPlate(robot, barcode, scheduled, toImage, toImage);
			}
			catch (Exception ex) 
			{
				MessageBox.Show(this, ex.Message, ex.GetType().Name);
			}

			// Refresh imaging tasks
			getImagingTasks(robot, toImage);

			// Cursor back to normal
			this.Cursor = Cursors.Default;
		}

		private void btnImagedPlate_Click(object sender, System.EventArgs e)
		{
			// Cursor to wait
			this.Cursor = Cursors.WaitCursor;

			// Notify imaged
			try
			{
				itp.ImagedPlate(robot, barcode, txtImagingID.Text);
			}
			catch (Exception ex) 
			{
				MessageBox.Show(this, ex.Message, ex.GetType().Name);
			}

			// Refresh imaging tasks
			getImagingTasks(robot, DateTime.MinValue);

			// Cursor back to normal
			this.Cursor = Cursors.Default;
		}

		private void btnGetImagingTasksWS_Click(object sender, System.EventArgs e)
		{
			// Cursor to wait
			this.Cursor = Cursors.WaitCursor;

			// Store the barcode
			barcode = txtPlateBarcode.Text;

			// Get the tasks for this plate
			try 
			{
				getImagingTasksWS(robot, DateTime.MinValue);
			}
			catch (Exception ex) 
			{
				MessageBox.Show(this, ex.Message, ex.GetType().Name);
			}


			// Cursor back to normal
			this.Cursor = Cursors.Default;
		
		}

		private void btnRepeat_Click(object sender, System.EventArgs e)
		{
			int runLength = 1024;
			if (barcode.Length > 0)
			{
				this.Cursor = Cursors.WaitCursor;
				DateTime start = DateTime.Now;
				for (int i = 0; i < runLength; i++)
				{
					itp.GetImagingTasks(robot, barcode);
				}
				DateTime end = DateTime.Now;
				System.TimeSpan interval = end.Subtract(start);
				this.Cursor = Cursors.Default;
				DialogResult result = MessageBox.Show(runLength + " calls to GetImagingTasks() took " + interval.ToString(), 
					"Repeat Result", MessageBoxButtons.OK, MessageBoxIcon.Information);
			}
			else
			{
				DialogResult result = MessageBox.Show("Don't yet have a barcode - must click 'Get Imaging Tasks' first", 
					"Repeat Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
			}
		}

		#endregion

		#region Class private methods

		/// <summary>
		/// Update the state of the action buttons depending on how much
		/// information we have available
		/// </summary>
		private void updateActionButtonState()
		{
			// If we are trying to act on a scheduled imaging
			// and we have selected a scheduled imaging and we have a barcode
			if ((rdbScheduled.Checked) && (dgImagingTasks.CurrentRowIndex > -1) && (barcode.Length > 0))
			{
				// Activate update, imaging and skipped
				btnUpdatePriority.Enabled = true;
				btnImagingPlate.Enabled = true;
				btnSkippedImaging.Enabled = true;
			}

			// Else if we are trying to act on an unscheduled imaging
			else if (rdbUnscheduled.Checked && (barcode.Length > 0))
			{
				// Activate imaging but deactivate update and skipped
				btnUpdatePriority.Enabled = false;
				btnImagingPlate.Enabled = true;
				btnSkippedImaging.Enabled = false;
			}

			// Else disable everything
			else
			{
				// Deactivate update, imaging and skipped
				btnUpdatePriority.Enabled = false;
				btnImagingPlate.Enabled = false;
				btnSkippedImaging.Enabled = false;
			}

			// Activate imaged if we have an imaging id
			btnImagedPlate.Enabled = (txtImagingID.Text.Length != 0);

		}

		/// <summary>
		/// Get the current row from dgImagingTasks
		/// </summary>
		/// <returns></returns>
		private DataRow getCurrentRow()
		{
			CurrencyManager cm = (CurrencyManager) dgImagingTasks.BindingContext[dgImagingTasks.DataSource, dgImagingTasks.DataMember];
			DataRowView drv = (DataRowView) cm.Current;
			return drv.Row;
		}

		/// <summary>
		/// Get the imaging tasks for the plate identified by the class variable
		/// barcode
		/// </summary>
		/// <param name="robot"></param>
		/// <param name="currentDateToImage"></param>
		private void getImagingTasks(IRobot robot, DateTime currentDateToImage)
		{
			// Get the tasks for this plate
			IImagingTask[] tasks = itp.GetImagingTasks(robot, barcode);

			// Display the imaging tasks
			displayImagingTasks(tasks, currentDateToImage);

		}

		/// <summary>
		/// Get the imaging tasks for the plate identified by the class variable
		/// barcode using the web service call
		/// </summary>
		/// <param name="robot"></param>
		/// <param name="currentDateToImage"></param>
		private void getImagingTasksWS(IRobot robot, DateTime currentDateToImage)
		{
			// Get the tasks for this plate
			IImagingTask[] tasks = ((ImagingTaskProvider) itp).GetImagingTasksFromWebService(robot, barcode);

			// Display the imaging tasks
			displayImagingTasks(tasks, currentDateToImage);

		}

		/// <summary>
		/// Display the supplied imaging tasks
		/// </summary>
		/// <param name="tasks"></param>
		private void displayImagingTasks(IImagingTask[] tasks, DateTime currentDateToImage)
		{

			// Clear rows
			dt.Rows.Clear();

			// If we got some tasks
			if (tasks != null)
			{
				// Declare a DataRow
				DataRow dr;
 
				// Get an enumerator
				System.Collections.IEnumerator enumerator = tasks.GetEnumerator();

				// While we have more tasks
				while (enumerator.MoveNext())
				{
					// Get this task
					IImagingTask task = (IImagingTask) enumerator.Current;
					
					// Get a new row
					dr = dt.NewRow();
 
					// Populate the row
					dr[0] = task.DateToImage;
					dr[1] = task.Priority;
					dr[2] = task.InQueue;
					dr[3] = task.State;
                    dr[4] = task.DateImaged.ToUniversalTime();
                    dr[5] = task.DateToImage.ToUniversalTime();
                    dr[6] = task.DateImaged.ToLocalTime();
                    dr[7] = task.DateToImage.ToLocalTime();
 
					// Add the row to the table
					dt.Rows.Add(dr);
				}

			}

			// Use currentDateToImage to reselect current row
			if (DateTime.MinValue.Equals(currentDateToImage))
			{
				dgImagingTasks.CurrentRowIndex = 0;
			}
			else
			{
				DataView dv = (DataView) dgImagingTasks.DataSource;
				System.Collections.IEnumerator enumerator = dv.GetEnumerator();
				int i = 0;
				while (enumerator.MoveNext())
				{
					DataRowView drv = (DataRowView) enumerator.Current;
					if (currentDateToImage.Equals(drv.Row.ItemArray[0]))
					{
						dgImagingTasks.CurrentRowIndex = i;
						break;
					}
					i++;
				}
			}

			// Update radio button labels
			rdbScheduled.Text = "Scheduled imaging tasks for " + barcode;
			rdbUnscheduled.Text = "Unscheduled imaging task for " + barcode;

			if (dgImagingTasks.CurrentRowIndex > -1)
			{
				rdbUnscheduled.Checked = false;
				rdbScheduled.Checked = true;
			}
			else
			{
				rdbScheduled.Checked = false;
				rdbUnscheduled.Checked = true;
			}

			updateActionButtonState();

		}

		/// <summary>
		/// Round C# DateTime's to the same precision as java - basically
		/// means rounding the the number of ticks to the nearest multiple
		/// of 10000
		/// </summary>
		/// <param name="toRound">The DateTime to be rounded</param>
		/// <returns>The result of rounding</returns>
		private DateTime roundDateTimeToJavaPrecision(DateTime toRound)
		{
			long rounding = toRound.Ticks % 10000;
			if (rounding >= 5000)
			{
				rounding = 10000 - rounding;
				return toRound.AddTicks(rounding);
			}
			else
			{
				return toRound.Subtract(System.TimeSpan.FromTicks(rounding));
			}
		}

		#endregion

	}
}
