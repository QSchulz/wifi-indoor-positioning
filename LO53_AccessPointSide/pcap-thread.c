#include "pcap-thread.h"

void *pcap_function( void *arg ) {

  char *iface = (char *) arg;
  char errbuf[PCAP_ERRBUF_SIZE];
  pcap_t * handle = NULL;
  struct ieee80211_radiotap_header * rtap_head;
  struct ieee80211_header * eh;
  struct pcap_pkthdr header;
  const u_char * packet;
  u_char * mac;
  u_char first_flags;
  int offset = 0, i = 0;
  char rssi;
  Device * dev_info;
  Message msg;

  msg.id = 53;
  device_list = malloc( sizeof( Device* ) );

  // Open pcap handle to sniff traffic
  handle = pcap_open_live( iface, BUFSIZ, 1, 1000, errbuf );

  if ( handle == NULL ) {

    printf( "Could not open pcap on %s\n", iface );
    pthread_exit( (void *) -1 );

  }

  while ( got_sigint == 0 ) {

    packet = pcap_next( handle, &header );

    if ( !packet )
      continue;

    rtap_head = (struct ieee80211_radiotap_header *) packet;
    int len = (int) rtap_head->it_len[0] + 256 * (int) rtap_head->it_len[1];
    eh = (struct ieee80211_header *) ( packet + len );

    if ( ( eh->frame_control & 0x03 ) == 0x01 ) {

      mac = eh->source_addr;
      first_flags = rtap_head->it_present[0];
      offset = 8;
      offset += ( ( first_flags & 0x01 ) == 0x01 ) ? 8 : 0 ;
      offset += ( ( first_flags & 0x02 ) == 0x02 ) ? 1 : 0 ;
      offset += ( ( first_flags & 0x04 ) == 0x04 ) ? 1 : 0 ;
      offset += ( ( first_flags & 0x08 ) == 0x08 ) ? 4 : 0 ;
      offset += ( ( first_flags & 0x10 ) == 0x10 ) ? 2 : 0 ;
      rssi = *( (char *) rtap_head + offset ) - 0x100;
      printf( "%d bytes -- %02X:%02X:%02X:%02X:%02X:%02X -- RSSI: %d dBm\n",
             len, mac[0], mac[1], mac[2], mac[3], mac[4], mac[5], (int) rssi );
      // We got some message issued by a terminal (FromDS=0,ToDS=1)
      sem_wait( &synchro );

      if ( ( dev_info = find_mac( *device_list, mac ) ) == NULL ) {

        printf( "New device (%02X:%02X:%02X:%02X:%02X:%02X) detected, adding it to the list of devices.\n", mac[0], mac[1], mac[2], mac[3], mac[4], mac[5] );
        dev_info = add_Device( device_list, mac );
        printf( "New device (%02X:%02X:%02X:%02X:%02X:%02X) added to the list of devices.\n", mac[0], mac[1], mac[2], mac[3], mac[4], mac[5] );

        for ( i = 0; i < 6; i++ ) {
          msg.mac[i] = mac[i];
        }
        msgsnd( msqid, &msg, sizeof( Message ) - 4, 0 );

      }

      printf( "Adding current measure to the measurements list.\n" );
      add_value( &dev_info->histogram, (int) rssi );
      printf( "Current measure added to the measurements list.\n\n" );
      sem_post( &synchro );

    }

  }

  pcap_close( handle );
  pthread_exit( (void *) 0 );

}
