package fr.utbm.LO53_IPS.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class TimerTaskScheduler {

	public void schedule() {
		Timer timer = new Timer();
		timer.schedule(new JobRunner(), new Date(), 1000);
	}
}
