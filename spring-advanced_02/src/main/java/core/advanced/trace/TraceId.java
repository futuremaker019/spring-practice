package core.advanced.trace;

import java.util.UUID;

public class TraceId {


    /**
     * [796bccd9] OrderController.request()
     * [796bccd9] |-->OrderService.orderItem()
     */
    private String id;          // [796bccd9]을 표시함
    private int level;          // 깊이를 표시함

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    public TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8); // 앞 8자리리만 사용한다.
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }
    public TraceId cratePreviousId() {
        return new TraceId(id, level - 1);
    }
    public boolean isFirstLevel() {
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
