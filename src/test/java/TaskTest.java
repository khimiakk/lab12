import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ucu.task2.Group;
import ucu.task2.Signature;
import ucu.task2.Task;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Group<Integer> group;
    private AtomicInteger counter;

    @BeforeEach
    void setUp() {
        group = new Group<>();
        counter = new AtomicInteger(0);
    }

    @Test
    void testTaskFreeze() {
        Task<Integer> task = new Signature<>(counter::addAndGet);
        assertNull(task.getId());
        task.freeze();
        assertNotNull(task.getId());
    }

    @Test
    void testTaskHeaders() {
        Task<Integer> task = new Signature<>(counter::addAndGet);
        task.setHeader("TestHeader", "TestValue");
        assertEquals("TestValue", task.getHeader("TestHeader"));
    }

    @Test
    void testSignatureApply() {
        Task<Integer> task = new Signature<>(counter::addAndGet);
        assertEquals(0, counter.get());
        task.apply(5);
        assertEquals(5, counter.get());
    }

    @Test
    void testGroupApply() {
        Task<Integer> task1 = new Signature<>(counter::addAndGet);
        Task<Integer> task2 = new Signature<>(val -> counter.addAndGet(val * 2));
        group.addTask(task1).addTask(task2);

        group.apply(3);
        assertEquals(9, counter.get());
    }

    @Test
    void testGroupFreeze() {
        Task<Integer> task1 = new Signature<>(counter::addAndGet);
        Task<Integer> task2 = new Signature<>(val -> counter.addAndGet(val * 2));
        group.addTask(task1).addTask(task2);

        group.freeze();
        String groupUuid = group.groupUuid;
        assertNotNull(groupUuid);

        assertEquals(groupUuid, task1.getHeader("GroupID:"));
        assertEquals(groupUuid, task2.getHeader("GroupID:"));

        assertThrows(UnsupportedOperationException.class,
                () -> group.addTask(new Signature<>(counter::addAndGet))
        );
    }

    @Test
    void testMultipleGroups() {
        Group<Integer> group1 = new Group<>();
        Group<Integer> group2 = new Group<>();

        Task<Integer> task1 = new Signature<>(counter::addAndGet);
        Task<Integer> task2 = new Signature<>(val -> counter.addAndGet(val * 2));
        group1.addTask(task1);
        group2.addTask(task2);

        group1.freeze();
        group2.freeze();

        assertNotEquals(group1.groupUuid, group2.groupUuid);
    }

    @Test
    void testTaskReuseInDifferentGroups() {
        Task<Integer> task = new Signature<>(counter::addAndGet);

        Group<Integer> group1 = new Group<>();
        group1.addTask(task);
        group1.freeze();

        Group<Integer> group2 = new Group<>();
        group2.addTask(task);
        group2.freeze();

        assertNotEquals(group1.groupUuid, group2.groupUuid);
        assertEquals(group2.groupUuid, task.getHeader("GroupID:"));
    }
}