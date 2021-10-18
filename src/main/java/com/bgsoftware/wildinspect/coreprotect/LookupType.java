package com.bgsoftware.wildinspect.coreprotect;

import java.util.List;

public enum LookupType {

    BLOCK_LOOKUP(List.of(0, 1)), // BLOCK
    CLICK_LOOKUP(List.of(2)), // CLICK
    KILL_LOOKUP(List.of(3)),
    CONTAINER_LOOKUP(List.of(4)),
    SIGN_LOOKUP(List.of(10));

    public final List<Integer> actionID;

    public List<Integer> getActionId() {
        return actionID;
    }

    LookupType(List<Integer> actionID) {
        this.actionID = actionID;
    }
}
