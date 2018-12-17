package org.pimslims.diamond.plateimporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.pimslims.access.Access;
import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.RefHolderOffset;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.schedule.SchedulePlan;
import org.pimslims.report.Report;
import org.pimslims.search.Paging;
import org.pimslims.search.Paging.Order;

public class IspybImporter {

	private static ArrayList<Map<String,String>> newPlates;
	
	public static void main(String[] args) throws BusinessException {
		
		System.out.println("-------------------------------------");
		System.out.println("xtalPiMS ISPyB Plate Importer (author:ejd53)");
		System.out.println("Beginning plate import from ISPyB");
	
    	File file=new File("conf/Properties");
    	try {
            Properties properties = new Properties();
            InputStream in = new FileInputStream(file);
            properties.load(in);
            //don't need any beyond existing PiMS DB connection details
		} catch (IOException e) {
			if (e instanceof FileNotFoundException) {
                System.err.println("Could not find properties file at: " + file.getAbsolutePath());
            }
            throw new RuntimeException(e);
	    }		

    	Calendar newestXtalpimsPlateDate;
    	
		WritableVersion version = null;
		try {
			version=ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);
			newestXtalpimsPlateDate=getNewestXtalpimsPlateDate(version);
			if(null==newestXtalpimsPlateDate){
				throw new RuntimeException("No plates found in xtalPIMS, can't get creation date of newest");
			}

			IspybImporter.newPlates=getIspybPlatesSince(newestXtalpimsPlateDate);

	    	Iterator i=newPlates.iterator();
			while(i.hasNext()){

				HashMap<String,String> ispybPlate=(HashMap<String,String>) i.next();
				String created=(String) ispybPlate.get("created");
				String barcode=(String) ispybPlate.get("barcode");
				String proposal=(String) ispybPlate.get("proposal");
				String proteinAcronym=(String) ispybPlate.get("acronym");
				String user=(String) ispybPlate.get("owner");
				String plateTypeName=(String) ispybPlate.get("pt_name");
				int plateTypeRows=Integer.parseInt(ispybPlate.get("pt_rows"));
				int plateTypeCols=Integer.parseInt(ispybPlate.get("pt_cols"));
				int plateTypeSubpositions=Integer.parseInt(ispybPlate.get("pt_drops"));

				Protocol protocol=version.findFirst(Protocol.class, Protocol.PROP_NAME, "CrystalTrial");
				SchedulePlan defaultSchedule=version.findFirst(SchedulePlan.class, SchedulePlan.PROP_NAME, "Default Schedule");
				if(null==protocol){
					//Can't proceed without the protocol, so we stop
					System.out.println("No protocol called 'CrystalTrial' exists in xtalPiMS, so aborting. Has reference data been loaded?");
					version.abort();
				} else if(null==defaultSchedule){
					//Can't proceed without the schedule, so we stop
					System.out.println("No schedule called 'Default Schedule' exists in xtalPiMS, so aborting. You need to create it.");
					version.abort();
				} else {
				
					System.out.println("Checking prerequisites for ISPyB plate '"+barcode+"'");
					//parse the creation date
					
					//Check for pre-requisites and abort if missing
					String labNotebookName=proposal+"."+proteinAcronym;
					LabNotebook labNotebook=version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, labNotebookName);
					UserGroup grp=version.findFirst(UserGroup.class, UserGroup.PROP_NAME, proposal);
					User plateOwner=version.findFirst(User.class, User.PROP_NAME, user);
					if(null==labNotebook){
						//Can't proceed without the lab notebook, so we stop
						throw new RuntimeException("No lab notebook called '"+labNotebookName+"' exists in xtalPiMS, so aborting. User rights sync should create the lab notebook.");
					} else if(null==grp){
						//Can't proceed without the user's usergroup, so we stop
						throw new RuntimeException("No usergroup called '"+proposal+"' exists in xtalPiMS, so aborting. User rights sync should create this usergroup and put the user in it.");
					} else if(null==plateOwner){
						//Can't proceed without the user, so we stop
						throw new RuntimeException("No user called '"+user+"' exists in xtalPiMS, so aborting. User rights sync should create the user.");
					} else {
						HolderType plateType=version.findFirst(HolderType.class, HolderType.PROP_NAME, plateTypeName);
						if(null==plateType){
							//The plate type doesn't exist in xtalpims, create it and set its default schedule to the default default schedule(!)
							System.out.println("No plate type called '"+plateTypeName+"' exists in xtalPiMS. Trying to create it...");
							Integer reservoirPosition=new Integer(plateTypeSubpositions+1);
							plateType=new CrystalType(version, reservoirPosition, plateTypeName);
							plateType.setMaxRow(plateTypeRows);
							plateType.setMaxColumn(plateTypeCols);
							plateType.setMaxSubPosition(plateTypeSubpositions+1);
							plateType.setDefaultSchedulePlan(defaultSchedule);
							System.out.println("...done.");
						} else {
							//The plate type exists in xtalpims. Check that its dimensions match those in ISPyB.
							//TODO Check that rows, cols, subs match those in ISPyB
						}
						SchedulePlan plateTypeSchedule=plateType.getDefaultSchedulePlan();
						if(null==plateTypeSchedule){
							//pre-existing type with no schedule, set it to the default
							System.out.println("Plate type '"+plateTypeName+"' exists in xtalPiMS but has no default schedule. Setting a default schedule for this plate type.");
							plateType.setDefaultSchedulePlan(defaultSchedule);
							plateTypeSchedule=defaultSchedule;
						}

					}
					System.out.println("Finished checking prerequisites for ISPyB plate '"+barcode+"'");
				}
			}
			if(!version.isCompleted()){
				version.commit();
			}
		} catch (RuntimeException e) {
			System.out.println("Aborting due to missing pre-requisites: ");
			System.out.println(e.getMessage());
		} catch (AbortedException e) {
			e.printStackTrace();
		} catch (ConstraintException e) {
			e.printStackTrace();
		} finally {
			if(null!=version && !version.isCompleted()){
				version.abort();
				System.exit(1);
			}
		}
		
		Iterator i=IspybImporter.newPlates.iterator();
		while(i.hasNext()){
			DataStorageImpl dataStorage = null;
			HashMap ispybPlate=(HashMap) i.next();
			
			try {
				
				String created=(String) ispybPlate.get("created");
				String barcode=(String) ispybPlate.get("barcode");
				String proposal=(String) ispybPlate.get("proposal");
				String proteinAcronym=(String) ispybPlate.get("acronym");
				String user=(String) ispybPlate.get("owner");
				String plateTypeName=(String) ispybPlate.get("pt_name");
				//int plateTypeRows=(int) ispybPlate.get("pt_rows");
				//int plateTypeCols=(int) ispybPlate.get("pt_cols");
				//int plateTypeSubpositions=(int) ispybPlate.get("pt_drops");

				dataStorage=new DataStorageImpl(ModelImpl.getModel());
				//dataStorage.openResources(user);
				dataStorage.openResources(Access.ADMINISTRATOR);
				WritableVersion wv=dataStorage.getWritableVersion();

				//ISPyB doesn't enforce unique container names, better hope VMXi's processes do.
				//In the meantime, if container already exists try appending _2, _3, etc., until
				//we find something that works.
				System.out.println("Checking for pre-existing");
				String rootName=barcode;
				int offset=1;
				Holder existing=wv.findFirst(Holder.class, Holder.PROP_NAME, barcode);
				while(null!=existing){
					System.out.println("Plate with barcode "+barcode+" exists!");
					offset++;
					barcode=rootName+"_"+offset;
					System.out.println("Checking for plate with barcode "+barcode);
					existing=wv.findFirst(Holder.class, Holder.PROP_NAME, barcode);
				}
				System.out.println("Creating plate with barcode "+barcode);
								
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    Date date = formatter.parse(created);
				Calendar creationDate=new GregorianCalendar();
				creationDate.setTime(date);
				
				String labNotebookName=proposal+"."+proteinAcronym;

				String sampleName=proposal+"_"+proteinAcronym;
				Sample s=wv.findFirst(Sample.class,Sample.PROP_NAME, sampleName);
				if(null==s){
					s=new Sample(wv, sampleName);
					s.setAmountUnit("L");
					s.setCurrentAmount((float) 1);
				}
				
				DiamondPlateImporter dpi=new DiamondPlateImporter();
				dpi.createdFilledTrialPlate(dataStorage, barcode, plateTypeName, sampleName, "Blank", 0.000001d, 0.000001d, 0.000002d, creationDate, user, labNotebookName);

				Collection<RefHolderOffset> nullOffsets=wv.findAll(RefHolderOffset.class, RefHolderOffset.PROP_COLOFFSET, null);
				Iterator nos=nullOffsets.iterator();
				while(nos.hasNext()){
					RefHolderOffset rho=(RefHolderOffset)nos.next();
					System.out.println("Found null RefHolderOffset: "+rho.get_Name()+", attempting to delete");
					rho.delete();
					System.out.println("Deleted null RefHolderOffset: "+rho.get_Name());
				}
				
				dataStorage.commit();
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ConstraintException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				IspybImporter.closeResources(dataStorage);
			}
		}
		System.out.println("Finished plate import from ISPyB");
		System.out.println("-------------------------------------");
		System.out.println(" ");
	}
	
	private static Calendar getNewestXtalpimsPlateDate(ReadableVersion version){
		Calendar now=new GregorianCalendar();
		Calendar lastMonth=new GregorianCalendar();
		lastMonth.add(Calendar.MONTH, -1);
		Report r=new Report(version, Holder.class, new HashMap<String,Object>(), null, lastMonth, now);
		Paging p=new Paging(0,10,Holder.PROP_CREATIONDATE,Order.DESC);
		ArrayList<Holder> plates=(ArrayList<Holder>) r.getResults(p);
		System.out.println("Found "+plates.size()+" recent plates in xtalPIMS ordering by newest first");
		if(0==plates.size()){
			return null;
		}
		Holder newest=(Holder) plates.iterator().next();
		return newest.getCreationDate();
	}
	
	private static ArrayList<Map<String,String>> getIspybPlatesSince(Calendar since){
		
        ArrayList<Map<String,String>> plates=new ArrayList<Map<String,String>>();
		try {
			/*
			//DUMMY DATA FOR TESTING
						String data="EJD2015216A           2015-12-25 19:34:06  mx4025  9098       CrystalQuickX             192  ejd53"+
						"\n					EJD20151216B        2015-12-26 19:44:08  mx4025  9098         CrystalQuickX             192  ejd53";
						String[] rows=data.split("\n");
						for(int i=0;i<rows.length;i++){
							String row=rows[i].trim();
							String[] parts=row.split(" +");
							Map<String, String> plate=new HashMap<String, String>();
							plate.put("barcode", parts[0]);
							plate.put("created", parts[1]+" "+parts[2]);
							plate.put("owner", "ejd53");
							plate.put("proposal", parts[3]);
							plate.put("acronym", parts[4]);
							plate.put("pt_name", parts[5]);
							plate.put("pt_rows", "8");
							plate.put("pt_cols", "12");
							plate.put("pt_drops", ""+Integer.parseInt(parts[6])/(12*8));
							System.out.println("|"+plate.get("barcode")+"|"+plate.get("created")+"|"+plate.get("proposal")+"|"+plate.get("acronym")+
									"|"+plate.get("pt_name")+"|"+plate.get("pt_rows")+"|"+plate.get("pt_cols")+"|"+plate.get("pt_drops")+"|");
							plates.add(plate);
						}
			/**/
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = since.getTime();
			String dateString = formatter.format(d);
			String path="/dls_sw/dasc/bin/ispybscripts/newPlates";
            System.out.println("About to call 'ISPyB latest plates' script at "+path+" with date string "+dateString);
            ProcessBuilder pb = new ProcessBuilder(path, dateString);
            final Process process = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            PrintWriter pw = new PrintWriter(process.getOutputStream());
            String line;

            while ((line = br.readLine()) != null) {
            	System.out.println("Found line:");
            	System.out.println(line);
				String[] parts=line.split("\\s+");
				if(5>parts.length || "barcode".equals(parts[0]) || parts[0].startsWith("-----")){ 
					System.out.println("...ignoring.");
					continue;
				}
				Map<String, String> plate=new HashMap();
				plate.put("barcode", parts[0]);
				plate.put("created", parts[1]+" "+parts[2]);
				plate.put("proposal", parts[3]);
				plate.put("acronym", parts[4]);
				plate.put("pt_name", parts[5]);
				plate.put("pt_rows", "8");
				plate.put("pt_cols", "12");
				plate.put("pt_drops", ""+Integer.parseInt(parts[6])/(12*8));
				plate.put("owner", parts[7]);
			
				System.out.println("Found plate "+plate.get("barcode")+"|"+plate.get("created")+"|"+plate.get("proposal")+"|"+plate.get("acronym")+
						"|"+plate.get("pt_name")+"|"+plate.get("owner")+"|"+plate.get("pt_rows")+"|"+plate.get("pt_cols")+"|"+plate.get("pt_drops")+"|");
				plates.add(plate);
                pw.flush();
            }
            System.out.println("Finished parsing output from 'ISPyB latest plates script");
			/**/		
            
        } catch(Exception e) {
            e.printStackTrace();
        }
		return plates;
	}
	
	private static void closeResources(DataStorage dataStorage) {
        try {
            if (dataStorage != null) {
                dataStorage.closeResources();
                //System.out.println("Close resources in  " + this);
            }
        } catch (BusinessException ex) {
            ex.printStackTrace(); // can we report this error?
        }
    }
	
}