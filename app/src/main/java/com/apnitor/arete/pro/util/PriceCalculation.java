package com.apnitor.arete.pro.util;

import android.util.Log;

public class PriceCalculation {
    static String LOG_TAG = "PriceCalculation";


    /**
     * Average between:
     * <p>
     * 1- 15 minutes per bedroom/other rooms + 20 minutes per bath + 30 minutes per kitchen.
     * 2 - SQFT/700= hours
     * <p>
     * That will give us the estimated time and then we multiply by the recommended price per hour of each city. Iowa will be $25.
     */
    public static String getHousePrice() {

        String housePrice = "";
        return housePrice;

    }

    /**
     * First room $55
     * Every room after that $35
     * Every stair $35
     * Hallway $15
     * Bath $15
     * <p>
     * Deodorize: + $15 per room
     * Protect: + $15 per room
     * <p>
     * MINIMUM CHARGE $150
     */
    public static String getCarpetPrice(int mCleanRoom, int mProtectRoom, int mDeoRoom, int mCleanBath, int mProtectBath, int mDeoBath, int mCleanEntryHall, int mProtectEntryHall,
                                        int mDeoEntryHall, int mCleanStaircase, int mProtectStaircase, int mDeoStaircase) {

        String carpetPrice = "";

        int mRoomsPrice = 0;
        for (int i = 0; i < mCleanRoom; i++) {
            if (i == 0) {
                mRoomsPrice += 55;
            } else {
                mRoomsPrice += 35;
            }
        }
        int roomProtect = mProtectRoom * 15;
        int roomDeodorize = mDeoRoom * 15;
        mRoomsPrice = mRoomsPrice + roomProtect + roomDeodorize;

        //BATH
        int mBathPrice = 0;
        int bathClean = mCleanBath * 15;
        int bathProtect = mProtectBath * 15;
        int bathDeo = mDeoBath * 15;
        mBathPrice = bathClean + bathProtect + bathDeo;

        //HALLWAY
        int mHallwayPrice = 0;
        int hallwayClean = mCleanEntryHall * 15;
        int hallwayProtect = mProtectEntryHall * 15;
        int hallwayDeo = mDeoEntryHall * 15;
        mHallwayPrice = hallwayClean + hallwayProtect + hallwayDeo;


        // STAIRS
        int mStairsPrice = 0;
        int stairClean = mCleanStaircase * 35;
        int stairProtect = mProtectStaircase * 15;
        int stairDeodorize = mCleanStaircase * 15;
        mStairsPrice = stairClean + stairProtect + stairDeodorize;
        Log.d(LOG_TAG, "Carpet Price mRoomsPrice " + mRoomsPrice + " mBathPrice " + mBathPrice + " mHallwayPrice " + mHallwayPrice + " mStairsPrice " + mStairsPrice);
        int taskPrice = mRoomsPrice + mBathPrice + mHallwayPrice + mStairsPrice;
        if (taskPrice < 151) {
            taskPrice = 150;
        }
        Log.d(LOG_TAG, " Carpet Price : " + taskPrice);
        carpetPrice = taskPrice + "";
        return carpetPrice;
    }

    /**
     * Window Cleaning Implemented price
     * <p>
     * Minimum charge $125.00
     * <p>
     * Windows-In/out/sills/tracks
     * First floor from $3.50 per pane
     * Second floor $5.00 per pane
     * <p>
     * Screens-Remove/mark/scrub/rinse/reinstall
     * $1.00 to $1.50 per screen
     * <p>
     * French Doors/Sliding Glass Doors
     * From $5-$10 per door
     * Mirrors Cleaned
     * From $4 per mirror
     * Hard Water Deposit Removal (NOT IN UI)
     * From $19.00 per pane
     * Ceiling Fans-Blades cleaned/Base cleaned and oiled (NOT IN UI)
     * $10-$15 per fan
     * <p>
     * Garden Windows (EXTRA FIELD IN UI)
     * $3.50 per pane
     * <p>
     * Skylight (EXTRA FIELD IN UI)
     * Price not calculated
     * <p>
     * Stories (EXTRA FIELD IN UI)
     * Price not calculated
     */
    public static String getWindowPrice(int mFirstFloor, int mSecondFloor, int mScreens, int mFrenchWindows, int mPatioDoor, int mWardrobeMirror) {
        String windowPrice = "";
        // WINDOW FIRST FLOOR
        float mWindowFFPrice = 0.0f;
        for (int i = 0; i < mFirstFloor; i++) {
            mWindowFFPrice += 3.50;
        }

        // WINDOW SECOND FLOOR
        float mWindoSFPrice = 0.0f;
        for (int i = 0; i < mSecondFloor; i++) {
            mWindoSFPrice += 5.00;
        }

        // SCREENS
        float mScreensPrice = 0.0f;
        mScreensPrice = mScreens * 1.0f;

        // FRENCH DOOR
        float mFrenchPrice = 0.0f;
        mFrenchPrice = mFrenchWindows * 5 * 1.0f;

        // SLIDING DOOR
        float mSlidingPrice = 0.0f;
        mSlidingPrice = mPatioDoor * 10 * 1.0f;

        // MIRRORS
        float mMirrorsPrice = 0.0f;
        mMirrorsPrice = mWardrobeMirror * 4 * 1.0f;

        float taskPrice = mWindowFFPrice + mWindoSFPrice + mScreensPrice + mFrenchPrice + mSlidingPrice + mMirrorsPrice;
        Log.d(LOG_TAG, "Window Price mWindowFFPrice " + mWindowFFPrice + " mWindoSFPrice " + mWindoSFPrice + " mScreensPrice " + mScreensPrice + " mFrenchPrice " + mFrenchPrice + " mSlidingPrice " + mSlidingPrice + " mMirrorsPrice " + mMirrorsPrice);

        if (taskPrice < 126) {
            taskPrice = 125;
        }
        windowPrice = taskPrice + "";
        return windowPrice;
    }
}
