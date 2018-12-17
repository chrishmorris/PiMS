/**
 * 
 */
package uk.ac.ox.oppf.www.wsplate.util;

import org.apache.axis2.databinding.utils.ConverterUtil;

import uk.ac.ox.oppf.www.wsplate.GetImagingTasks;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfo;
import uk.ac.ox.oppf.www.wsplate.GetPlateType;
import uk.ac.ox.oppf.www.wsplate.ImagedPlate;
import uk.ac.ox.oppf.www.wsplate.ImagingPlate;
import uk.ac.ox.oppf.www.wsplate.Robot;
import uk.ac.ox.oppf.www.wsplate.SkippedImaging;
import uk.ac.ox.oppf.www.wsplate.SupportsPriority;
import uk.ac.ox.oppf.www.wsplate.UpdatedPriority;

/**
 * Utility methods to assist with debugging
 * 
 * @author Jon Diprose
 */
public class DebugUtil {
	
	/**
	 * Stringify the specified Robot
	 * 
	 * @param r - the Robot to stringify
	 * @return A string representation of r
	 */
	public static String convertToString(Robot r) {
        return "[Robot:id=\"" + r.getID()+ "\",name=\"" + r.getName() + "\"]";
	}

	/*
	 * PlateInfoProvider types
	 */
	
	/**
	 * Stringify the specified GetPlateInfo
	 * 
	 * @param gpi - the GetPlateInfo to stringify
	 * @return A string representation of gpi
	 */
	public static String convertToString(GetPlateInfo gpi) {
        return "[GetPlateInfo:robot=" + convertToString(gpi.getRobot()) + ",plateID=\"" + gpi.getPlateID() + "\"]";
	}
	
	/**
	 * Stringify the specified GetPlateType
	 * 
	 * @param gpt - the GetPlateType to stringify
	 * @return A string representation of gpt
	 */
	public static String convertToString(GetPlateType gpt) {
        return "[GetPlateType:robot=" + convertToString(gpt.getRobot()) + ",plateTypeID=\"" + gpt.getPlateTypeID()+"\"]";
	}
	
	/*
	 * ImagingTaskProvider types
	 */
	
	/**
	 * Stringify the specified GetImagingTasks
	 * 
	 * @param git - the GetImagingTasks to stringify
	 * @return A string representation of git
	 */
	public static String convertToString(GetImagingTasks git) {
        return "[GetImagingTasks:robot=" + convertToString(git.getRobot()) + ",plateID=\"" + git.getPlateID() + "\"]";
	}
	
	/**
	 * Stringify the specified SupportsPriority
	 * 
	 * @param sp - the SupportsPriority to stringify
	 * @return A string representation of sp
	 */
	public static String convertToString(SupportsPriority sp) {
        return "[SupportsPriority:robot=" + convertToString(sp.getRobot()) + "]";
	}
	
	/**
	 * Stringify the specified UpdatedPriority
	 * 
	 * @param up - the UpdatedPriority to stringify
	 * @return A string representation of up
	 */
	public static String convertToString(UpdatedPriority up) {
		return "[UpdatedPriority:robot=" + convertToString(up.getRobot()) + ",plateID=\"" + up.getPlateID() + "\",dateToImage=" + ConverterUtil.convertToString(up.getDateToImage()) + ",priority=" + up.getPriority() + "]";
	}
	
	/**
	 * Stringify the specified SkippedImaging
	 * 
	 * @param si - the SkippedImaging to stringify
	 * @return A string representation of si
	 */
	public static String convertToString(SkippedImaging si) {
        return "[SkippedImaging:robot=" + convertToString(si.getRobot()) + ",plateID=\"" + si.getPlateID() + "\",dateToImage=" + ConverterUtil.convertToString(si.getDateToImage())+"]";
	}
	
	/**
	 * Stringify the specified ImagingPlate
	 * 
	 * @param ip - the ImagingPlate to stringify
	 * @return A string representation of ip
	 */
	public static String convertToString(ImagingPlate ip) {
		return "[ImagingPlate:robot=" + convertToString(ip.getRobot()) + ",plateID=\"" + ip.getPlateID() + "\",dateToImage=" + ConverterUtil.convertToString(ip.getDateToImage()) + ",dateImaged=" + ConverterUtil.convertToString(ip.getDateImaged()) + ",scheduled=" + ip.getScheduled() + "]";
	}

	/**
	 * Stringify the specified ImagedPlate
	 * 
	 * @param ip - the ImagedPlate to stringify
	 * @return A string representation of ip
	 */
	public static String convertToString(ImagedPlate ip) {
		return "[ImagedPlate:robot=" + convertToString(ip.getRobot()) + ",plateID=\"" + ip.getPlateID() + "\",imagingID=\"" + ip.getImagingID() + "\"]";
	}

}
