#include "rssi-display.h"

void signal_handler ( int sig )
{
  printf( "Termination or interrupt signal received.\nTelling other threads to close.\n" );
  got_sigint = 1;

  clear_list( device_list );

  msgctl(msqid, IPC_RMID, NULL);

  printf( "Main thread closed.\n" );
  exit( -1 );
}

int main( int argc, char** argv ) {

  pthread_t pcap_thread, http_client_thread;
  key_t key = 0;
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

  printf( "Creating message queue\n" );

  if ( ( msqid = msgget( ftok( "./http", key ), IPC_CREAT | IPC_EXCL | 0750 ) ) == -1 ) {
    printf( "Error while creating message queue\n" );
    exit( -1 );
  }

  printf( "Successfully created message queue\n" );

  printf( "Creating http_client_thread.\n" );
  if ( pthread_create( &http_client_thread, NULL, http_client_function, (void *) NULL ) != 0 ) {
    printf( "Failed to create http_client_thread.\n" ),
    exit( -1 );
  }
  printf( "http_client_thread created.\n" );

  pthread_join ( http_client_thread, NULL );
  pthread_join ( pcap_thread, NULL );

  msgctl(msqid, IPC_RMID, NULL);

  exit( 0 );


}
