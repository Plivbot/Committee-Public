package me.committee.api.util;

/**
 * Makes managing timings easier
 */
public class StopWatch {

    private long instantiationTime = System.currentTimeMillis();

    /**
     * Time passed
     *
     * @return time from the creation of the instance
     */
    public long current() {
        return System.currentTimeMillis() - instantiationTime;
    }

    /**
     * Checks if the time specified has passed since the creation of the instance
     *
     * @param time the amount of time in ms that is required to return true
     * @return true if the time has passed
     */
    public boolean hasPassed(long time) {
        return time <= System.currentTimeMillis() - instantiationTime;
    }

    /**
     * Converts the passed time to seconds
     *
     * @param decimalPlaces how many decimal places we want
     *                      (I would usually keep this as 3 to be consistent)
     * @return {@link StopWatch#current()} converted to seconds
     */
    public double toSeconds(int decimalPlaces) {
        return MathUtil.round((double) current() / 1000, decimalPlaces);
    }

    /**
     * Resets the instantiation time to current time
     */
    public void reset() {
        this.instantiationTime = System.currentTimeMillis();
    }
}
