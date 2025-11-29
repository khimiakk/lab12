package ucu.task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Group<T> extends Task<T> {
    public String groupUuid;
    private List<Task<T>> tasks;

    public Group<T> addTask(Task<T> task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
        return this;
    }

    @Override
    public void freeze() {
        super.freeze();
        if (groupUuid == null) {
            groupUuid = UUID.randomUUID().toString();
        }
        setHeader("GroupID:", groupUuid);

        if (tasks != null) {
            for (Task<T> task : tasks) {
                task.freeze();
                task.setHeader("GroupID:", groupUuid);
            }
        }
    }

    @Override
    public void apply(T arg) {
        this.freeze();

        if (tasks != null) {
            tasks = Collections.unmodifiableList(tasks);

            for (Task<T> task : tasks) {
                task.apply(arg);
            }
        }
    }


}
