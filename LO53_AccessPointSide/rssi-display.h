#ifndef _RSSI_DISPLAY_
#define _RSSI_DISPLAY

#include <pthread.h>
#include <signal.h>
#include "pcap-thread.h"
#include "rssi_list.h"
#include "http-server.h"

void signal_handler ( int sig );

volatile sig_atomic_t got_sigint = 0;
Device ** device_list;
sem_t synchro;

#endif
