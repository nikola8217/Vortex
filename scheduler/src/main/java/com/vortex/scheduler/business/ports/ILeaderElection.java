package com.vortex.scheduler.business.ports;

public interface ILeaderElection {
    boolean tryAcquireLock();
    void releaseLock();
    boolean isLeader();
}
