package com.avighna.Game;

import com.avighna.Manager.GameManager;
import com.avighna.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

public class HoldingQueue {
    private static final Logger logger = LoggerFactory.getLogger(HoldingQueue.class);
    private static Queue<Pair<Board, MoveStorer>> moveQueueRequests = new LinkedList<>();

    public static synchronized void addRequest(Pair<Board, MoveStorer> p){
        logger.info("request added to the queue\n");
        moveQueueRequests.add(p);
    }

    public static synchronized Pair<Board, MoveStorer> dequeueRequest(){
        logger.info("dequeue request occured");
        return moveQueueRequests.poll();
    }

    public static int getSize(){
        return moveQueueRequests.size();
    }


}
