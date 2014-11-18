package org.starnub.server.cache.wrappers;

import io.netty.channel.ChannelHandlerContext;
import org.starnub.server.StarNub;
import org.starnub.server.eventsrouter.events.PlayerEvent;
import org.starnub.server.eventsrouter.events.StarNubEvent;
import org.starnub.server.eventsrouter.handlers.StarNubEventHandler;

import java.util.concurrent.TimeUnit;

public class PlayerCtxCacheWrapper extends CacheWrapper<ChannelHandlerContext> {

    /**
     * Basic constructor. RECOMMENDED.
     *
     * @param CACHE_OWNER       String representing the owner of this cache, should be set to the plugins exact name
     * @param CACHE_NAME        String representing the name for this specific cache implementation, to be used to task thread purging
     * @param AUTO_CACHE_PURGER boolean you must create a auto cache purger implementation if once that you need does
     */
    public PlayerCtxCacheWrapper(String CACHE_OWNER, String CACHE_NAME, boolean AUTO_CACHE_PURGER) {
        super(CACHE_OWNER, CACHE_NAME, AUTO_CACHE_PURGER);
    }

    /**
     * Time specific constructor. RECOMMENDED.
     *
     * @param CACHE_OWNER           String representing the owner of this cache, should be set to the plugins exact name
     * @param CACHE_NAME            String representing the name for this specific cache implementation, to be used to task thread purging
     * @param AUTO_CACHE_PURGER     boolean you must create a auto cache purger implementation if once that you need does
     *                              not exist already, if you will not be using this which is not recommended, use null in its place.
     * @param TIME_UNIT             TimeUnit representing the time units to set the auto prune and purge to set 0 for off (Not recommended)
     * @param CACHE_PRUNE_TASK_TIME int representing the time units to to automatically remove cache of this age at the set interval of this time unit
     * @param CACHE_PURGE_TAKE_TIME int representing the time to purge all cache entirely
     */
    public PlayerCtxCacheWrapper(String CACHE_OWNER, String CACHE_NAME, boolean AUTO_CACHE_PURGER, TimeUnit TIME_UNIT, int CACHE_PRUNE_TASK_TIME, int CACHE_PURGE_TAKE_TIME) {
        super(CACHE_OWNER, CACHE_NAME, AUTO_CACHE_PURGER, TIME_UNIT, CACHE_PRUNE_TASK_TIME, CACHE_PURGE_TAKE_TIME);
    }

    /**
     * Cache size specific constructor, NOT RECOMMENDED FOR USE UNLESS YOU NEED MORE THEN PLAYER COUNT WORTH OF ELEMENTS.
     *
     * @param CACHE_OWNER       String representing the owner of this cache, should be set to the plugins exact name
     * @param CACHE_NAME        String representing the name for this specific cache implementation, to be used to task thread purging
     * @param AUTO_CACHE_PURGER boolean you must create a auto cache purger implementation if once that you need does
     *                          not exist already, if you will not be using this which is not recommended, use null in its place.
     * @param elementMultiple   int representing the multiple of elements this will = the total player count, multiplied by this number
     */
    public PlayerCtxCacheWrapper(String CACHE_OWNER, String CACHE_NAME, boolean AUTO_CACHE_PURGER, int elementMultiple) {
        super(CACHE_OWNER, CACHE_NAME, AUTO_CACHE_PURGER, elementMultiple);
    }

    /**
     * Cache size specific constructor, NOT RECOMMENDED FOR USE UNLESS YOU NEED MORE THEN PLAYER COUNT WORTH OF ELEMENTS.
     *
     * @param CACHE_OWNER       String representing the owner of this cache, should be set to the plugins exact name
     * @param CACHE_NAME        String representing the name for this specific cache implementation, to be used to task thread purging
     * @param AUTO_CACHE_PURGER boolean you must create a auto cache purger implementation if once that you need does
     *                          not exist already, if you will not be using this which is not recommended, use null in its place.
     * @param elements          int representing the multiple of elements this will = the total player count, multiplied by this number
     * @param dummyBoolean      boolean that represents nothing and used to create a new signature //TODO
     */
    public PlayerCtxCacheWrapper(String CACHE_OWNER, String CACHE_NAME, boolean AUTO_CACHE_PURGER, int elements, boolean dummyBoolean) {
        super(CACHE_OWNER, CACHE_NAME, AUTO_CACHE_PURGER, elements, dummyBoolean);
    }

    /**
     * This will purge ChannelHandlerContext from the list when players disconnect
     */
    @Override
    public void registerEvents() {
        StarNub.getStarNubEventRouter().registerEventSubscription("StarNub", "Player_Disconnected", new StarNubEventHandler() {
            @Override
            public void onEvent(StarNubEvent eventData) {
                getCacheMap().remove(((PlayerEvent) eventData).getPLAYER_SESSION().getSENDER_CTX());
            }
        });
    }
}
