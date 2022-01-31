package Timer;

import java.util.Date;

public class TimerLogic {

	private int hours;
	private int minutes;
	private int seconds;
	public static long hourInMilisec = 3600000, 
			minuteInMilisec = 60000, 
			secondInMilisecond = 1000,
			waitingTime = 14400000;
	
	public TimerLogic(long startTime) {
		startOver(startTime);
	}
	
	public boolean isTimeOver() {
		if(hours == 0 && minutes == 0 && seconds == 0)
			return true;
		return false;
	}
	
	public String dropSecond() {
		if(seconds > 0) {
			seconds--;
			return toString();
		}else {
			if(minutes > 0) {
				minutes--;
				seconds = 59;
				return toString();
			}else {
				if(hours > 0) {
					hours--;
					minutes = 59;
					seconds = 59;
					return toString();
				}
				else
					return "COLLECT YOUR FREE BONUS";
			}
		}
	}
	
	public String toString() {
		String sHour = "", sMinute = "", sSecond = "";
		if(hours == 0 && minutes == 0 && seconds == 0)
			return "COLLECT YOUR FREE BONUS";
		if(hours < 10)
			sHour = "0";
		if(minutes < 10)
			sMinute = "0";
		if(seconds < 10)
			sSecond = "0";
		return sHour+ hours +":" + sMinute+ minutes + ":"+ sSecond+ + seconds;
	}
	
	public void startOver(long startTime) {
		Date date = new Date();
		long differnce = date.getTime() - startTime;
		if(differnce > waitingTime) {
			hours = 0;
			minutes = 0;
			seconds = 0;
		}else {
			//System.out.println("complete:" + waitingTime / hourInMilisec);
			//System.out.println("partial:" + getNumberOfHours(waitingTime - differnce) );
			hours = (int)getNumberOfHours(waitingTime - differnce);
			minutes = (int) getNumberOfMinutes(waitingTime -differnce);
			seconds = (int) getNumberOfSeconds(waitingTime -differnce);
		}
		
	}
	
	private long  getNumberOfHours(long input) {
		//System.out.println("in function: " + input / hourInMilisec);
		long temp = input / hourInMilisec;
		//System.out.println("in function: " + temp);
		//System.out.println("input: " + input);
		//System.out.println("hour: " + hourInMilisec);
		return temp;
	}
	
	private long getNumberOfMinutes(long input) {
		long temp = input %  hourInMilisec;
		return temp / minuteInMilisec;
	}
	
	private long getNumberOfSeconds(long input) {
		long temp = input %  minuteInMilisec;
		return temp / secondInMilisecond;
	}
	
	public long getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public long getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public long getSeconds() {
		return seconds;
	}
	
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
}
