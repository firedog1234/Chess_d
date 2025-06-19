package com.avighna.Game;

import com.avighna.Pair;

import java.util.LinkedList;
import java.util.Queue;

public class HoldingQueue {
    static Queue<Pair<Board, MoveStorer>> moveQueueRequests = new LinkedList<>();

    static synchronized void addRequest(Pair<Board, MoveStorer> p){
        moveQueueRequests.add(p);
    }

    static synchronized void dequeueRequest(){
        moveQueueRequests.poll();
    }


}
