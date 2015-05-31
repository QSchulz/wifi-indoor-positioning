#include "rssi-display.h"

void signal_handler ( int sig )
{
  printf( "Termination or interrupt signal received.\nTelling other threads to close.\n" );
  got_sigint = 1;

  clear_list( device_list );

  printf( "Main thread closed.\n" );
  exit( -1 );
}

int main( int argc, char** argv ) {

  pthread_t pcap_thread;
  char* iface = "wlan0";

  if ( argc == 2 ) {
    iface = argv[1];
  }
  printf( "Using %s as listening interface.\n", iface );

  sem_init( &synchro, 0, 1 );

  printf( "Mapping interrupt and termination signals to custom handler.\n" );
  if ( signal( SIGINT, &signal_handler ) == SIG_ERR || signal( SIGTERM, &signal_handler ) == SIG_ERR ) {

    printf( "Failed to map signals to custom handler.\n" );
    exit( -1 );

  }
  printf( "Mapping of interrupt and termination signals to custom handler done.\n" );

  printf( "Creating pcap_thread.\n" );
  if ( pthread_create( &pcap_thread, NULL, pcap_function, (void *) iface ) != 0 ) {

    printf( "Failed to create pcap_thread.\n" );
    exit( -1 );

  }
  printf( "pcap_thread created.\n" );

  printf( "Launching http server.\n" );
  if ( start_http_server() == -1 ) {
    printf( "Lauching of http server failed.\n" );
    exit( -1 );
  }
  printf( "Successfully launched http server.\n" );

  pthread_join ( pcap_thread, NULL );

  exit( 0 );


}
