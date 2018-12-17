package org.pimslims.graph.implementation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pimslims.graph.IGraph;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.protocol.Workflow;
import org.pimslims.model.reference.SampleCategory;

public class ProtocolGraphModel extends AbstractGraphModel {

    private Workflow workflow;

    public ProtocolGraphModel(final ModelObject centre) {
        super(centre);
    }

    @Override
    public IGraph createGraphModel(final ModelObject object) {
        if (object instanceof Workflow) {
            this.workflow = (Workflow) object;
            final Set<Protocol> protocols = ((Workflow) object).getProtocols();

            for (final Iterator iterator = protocols.iterator(); iterator.hasNext();) {
                final Protocol protocol = (Protocol) iterator.next();
                this.addProtocol(protocol);
            }
            return this;
        }
        this.addProtocol((Protocol) object);
        return this;
    }

    private ModelObjectNode addProtocol(final Protocol protocol) {

        final ModelObjectNode node = this.addNode(protocol);
        //this.dbIds.add(protocol.getDbId());

        // add inputs
        final Collection<RefInputSample> riss = protocol.getRefInputSamples();
        for (final Iterator iterator = riss.iterator(); iterator.hasNext();) {
            final RefInputSample ris = (RefInputSample) iterator.next();
            final SampleCategory sampleCategory = ris.getSampleCategory();
            final ModelObjectNode sample = this.createOrAddSample(sampleCategory);
            this.addEdge(sample, node);
        }

        // add outputs
        final Set<RefOutputSample> ross = protocol.getRefOutputSamples();
        for (final Iterator iterator = ross.iterator(); iterator.hasNext();) {
            final RefOutputSample ros = (RefOutputSample) iterator.next();
            final SampleCategory sampleCategory = ros.getSampleCategory();
            final ModelObjectNode sample = this.createOrAddSample(sampleCategory);
            this.addEdge(node, sample);
        }

        return node;
    }

    private ModelObjectNode createOrAddSample(final SampleCategory sample) {
        final Long dbId = sample.getDbId();
        if (null != this.getNode(dbId)) {
            return this.getNode(dbId);
        }
        final ModelObjectNode ret = this.addNode(sample);
        // add consumers 
        final Collection<RefInputSample> riss = sample.getRefInputSamples();
        for (final Iterator iterator = riss.iterator(); iterator.hasNext();) {
            final RefInputSample ris = (RefInputSample) iterator.next();
            final Protocol protocol = ris.getProtocol();
            if (!protocol.getIsForUse() || null != this.getNode(protocol.getDbId())) {
                continue;
            }
            if (this.isInDiagram(protocol)) {
                final ModelObjectNode pNode = this.addProtocol(protocol);
            }
        }
        //TODO add sources
        return ret;
    }

    /**
     * ProtocolGraphModel.isInDiagram
     * 
     * @param protocol
     * @return
     */
    private boolean isInDiagram(final Protocol protocol) {
        if (null == this.workflow) {
            return true;
        }
        return this.workflow.getProtocols().contains(protocol);
    }

    private static Collection<RefInputSample> getNext(final Protocol protocol) {

        // find possible next experiments
        final Collection<RefInputSample> refInputSamplesForNextExpt = new LinkedHashSet<RefInputSample>();
        final RefOutputSample out = protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES);
        if (null != out) {
            refInputSamplesForNextExpt.addAll(out.getSampleCategory().getRefInputSamples());
        }

        return refInputSamplesForNextExpt;
    }

}
