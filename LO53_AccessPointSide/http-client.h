#ifndef _HTTP_CLIENT_
#define _HTTP_CLIENT_

#include <sys/msg.h>
#include <sys/types.h>
#include <signal.h>
#include <curl/curl.h>
#include "pcap-thread.h"
#include "rssi_list.h"

extern volatile sig_atomic_t got_sigint;
extern int msqid;

void *http_client_function( void *arg );

#endif
