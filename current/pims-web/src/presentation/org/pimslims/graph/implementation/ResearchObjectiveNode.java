/**
 * pims-web org.pimslims.utils.graph.implementation ConstructNode.java
 * 
 * @author Marc Savitsky
 * @date 15 Jan 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.graph.implementation;

import org.pimslims.metamodel.ModelObject;

/**
 * ConstructNode
 * 
 */
public class ResearchObjectiveNode extends ModelObjectNode {

    public ResearchObjectiveNode(final ModelObject object) {
        super(object);
    }

    /*  @Override
     public final String getId() {
         final ResearchObjective researchObjective = (ResearchObjective) this.getObject();
         final StringBuffer sb = new StringBuffer();

         sb.append("Construct: name ");
         sb.append(researchObjective.getCommonName());
         sb.append(";");

         if (ServletUtil.validString(researchObjective.getLocalName())) {
             sb.append(" other name ");
             sb.append(researchObjective.getLocalName());
             sb.append(";");
         }

         for (final ResearchObjectiveElement element : researchObjective.getResearchObjectiveElements()) {
             if (TargetGraphModel.isConstruct(element.getComponentType())) {
                 final Integer beginId = element.getApproxBeginSeqId();
                 final Integer endId = element.getApproxEndSeqId();
                 if (beginId != null && endId != null) {
                     sb.append(" Res " + beginId.toString() + "-" + endId.toString() + ";");
                 }
             }
         }

         //sb.append(" whychosen ");
         //sb.append(this.limitAttribute(researchObjective.getWhyChosen()));
         //sb.append(";");

         return sb.toString();
     } */

}
