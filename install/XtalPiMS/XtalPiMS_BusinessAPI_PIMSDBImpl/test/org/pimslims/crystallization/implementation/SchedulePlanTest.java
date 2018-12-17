package org.pimslims.crystallization.implementation;

import java.util.LinkedList;
import java.util.List;

import org.pimslims.business.crystallization.model.SchedulePlan;
import org.pimslims.business.crystallization.model.SchedulePlanOffset;
import org.pimslims.business.crystallization.service.ScheduleService;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;

public class SchedulePlanTest extends AbstractXtalTest {

    public SchedulePlanTest(final String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    static final String UNIQUE = "test" + System.currentTimeMillis();

    public void testSimpleCreate() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            //create schedulePlan
            final SchedulePlan schedulePlan = createPlan();
            final ScheduleService service = this.dataStorage.getScheduleService();
            service.create(schedulePlan);

            //load it 
            final SchedulePlan schedulePlan2 = service.findSchedulePlanByName(schedulePlan.getName());

            //verify 
            assertNotNull(schedulePlan2);
            assertNotNull(schedulePlan2.getId());
            assertEquals(schedulePlan.getName(), schedulePlan2.getName());
            assertEquals(schedulePlan.getDescription(), schedulePlan2.getDescription());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }

    }

    private SchedulePlan createPlan() {
        final SchedulePlan plan = new SchedulePlan();
        plan.setName("name" + UNIQUE);
        plan.setDescription("description" + UNIQUE);
        //add a list of offsets
        final List<SchedulePlanOffset> offsets = new LinkedList<SchedulePlanOffset>();
        for (int i = 0; i < 10; i++) {
            final SchedulePlanOffset offset = new SchedulePlanOffset();
            offset.setOffsetHoursFromTimeZero(i);
            offset.setPriority(i / 2);
            offset.setSchedulePlan(plan);
            offsets.add(offset);
        }
        plan.setOffsets(offsets);
        return plan;
    }

    public void testFullCreate() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            //create schedulePlan
            final SchedulePlan schedulePlan = createPlan();
            final ScheduleService service = this.dataStorage.getScheduleService();
            service.create(schedulePlan);

            //load it 
            final SchedulePlan schedulePlan2 = service.findSchedulePlanByName(schedulePlan.getName());

            //verify 
            assertEquals(schedulePlan.getOffsets().size(), schedulePlan2.getOffsets().size());
            for (int i = 0; i < schedulePlan.getOffsets().size(); i++) {
                final SchedulePlanOffset offset = schedulePlan.getOffsets().get(i);
                final SchedulePlanOffset offset2 = schedulePlan2.getOffsets().get(i);
                assertEquals(offset.getOffsetHoursFromTimeZero(), offset2.getOffsetHoursFromTimeZero());
                assertEquals(offset.getPriority(), offset2.getPriority());
                assertEquals(offset.getSchedulePlan().getName(), offset2.getSchedulePlan().getName());
            }

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }

    }
}
