package maze;
/**
* A utility class to measure the time taken to solve a maze in nanoseconds
* and convert it to seconds.
*
* @author Manzel Kiinu + Darrin Bracken
*/
public class MazeTimer {
 private long startTime;
 private long endTime;
 private boolean running;
 /**
  * Starts the timer. Sets the start time to the current time in nanoseconds.
  */
 public void start() {
     startTime = System.nanoTime();
     running = true;
 }
 /**
  * Stops the timer. Sets the end time to the current time in nanoseconds.
  */
 public void stop() {
     endTime = System.nanoTime();
     running = false;
 }
 /**
  * Resets the timer, clearing start and end times.
  */
 public void reset() {
     startTime = 0;
     endTime = 0;
     running = false;
 }
 
 public boolean isRunning() {
	  return running;
 }
 
 /**
  * Returns the elapsed time in seconds from the last start to stop time.
  * If the timer is still running, it will calculate the elapsed time up to
  * the current moment.
  *
  * @return the elapsed time in seconds
  */
 public double getElapsedTimeInSeconds() {
     if (running) {
         return (System.nanoTime() - startTime) / 1e9;
     } else {
         return (endTime - startTime) / 1e9;
     }
 }

}



